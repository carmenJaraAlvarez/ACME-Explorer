
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CustomerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Customer;
import domain.Folder;
import domain.Subscription;
import forms.ActorForm;
import forms.CreateActorForm;

@Service
@Transactional
public class CustomerService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CustomerRepository	customerRepository;
	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;


	// Services ---------------------------------------------------------------

	// Constructors -----------------------------------------------------------

	public CustomerService() {
		super();
	}
	// Simple CRUD methods ----------------------------------------------------

	public Customer save(final Customer customer) {
		this.checkAuthenticate();
		this.checkPrincipal(customer);
		this.checkConcurrency(customer);
		final Customer result = this.customerRepository.save(customer);
		result.setFolders(this.actorService.saveNewFolders(result));

		return this.customerRepository.save(result);
	}

	public Customer saveEdit(final Customer customer) {
		this.checkPrincipal(customer);
		this.checkConcurrency(customer);
		final Customer result = this.customerRepository.save(customer);

		return result;
	}

	public Customer findOne(final int creditcardId) {
		return this.customerRepository.findOne(creditcardId);
	}

	public Collection<Customer> findAll() {
		return this.customerRepository.findAll();
	}

	public Customer findByPrincipal() {
		Customer result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}
	public Customer findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		return this.customerRepository.findByUserAccount(userAccount.getId());
	}

	// Other business methods -------------------------------------------------

	private void checkPrincipal(final Customer customer) {
		if (customer.getId() != 0) {
			final Customer c = this.findByPrincipal();
			Assert.isTrue(customer.equals(c));
		}
	}
	private void checkConcurrency(final Customer customer) {
		if (customer.getId() != 0) {
			final Customer c = this.findOne(customer.getId());
			Assert.isTrue(customer.getVersion() == c.getVersion());
		}
	}

	private void checkAuthenticate() {
		Assert.isTrue(!this.actorService.checkAuthenticate());
	}

	//Form de Customer (Se utiliza para la creacion de un nuevo customer)
	public Customer reconstruct(final CreateActorForm createActorForm, final BindingResult binding) {

		final Customer result = createActorForm.getCustomer();
		final UserAccount userAccount = createActorForm.getUserAccount();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.CUSTOMER);
		userAccount.getAuthorities().add(authority);
		result.setUserAccount(userAccount);
		result.setSubscriptions(new ArrayList<Subscription>());
		result.setFolders(new ArrayList<Folder>());
		if (binding != null) {
			this.validator.validate(userAccount, binding);
			this.validator.validate(result, binding);

		}

		return result;

	}
	// ---- Reconstruct de ActorForm para editar customers -------
	public Customer reconstructCustomer(final ActorForm actorForm, final BindingResult binding) {
		final Customer customerBBDD = this.findOne(actorForm.getId());
		final Customer result = actorForm.getCustomer();
		result.setSubscriptions(customerBBDD.getSubscriptions());
		result.setUserAccount(customerBBDD.getUserAccount());
		result.setFolders(customerBBDD.getFolders());
		result.setMessagesSent(customerBBDD.getMessagesSent());
		result.setMessagesReceived(customerBBDD.getMessagesReceived());
		this.validator.validate(result, binding);

		return result;

	}
	public void flush() {
		this.customerRepository.flush();
	}

}
