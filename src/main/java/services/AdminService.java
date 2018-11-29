
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdminRepository;
import security.LoginService;
import security.UserAccount;
import domain.Admin;
import forms.ActorForm;

@Service
@Transactional
public class AdminService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private AdminRepository	adminRepository;
	@Autowired
	private Validator validator;


	// Services ---------------------------------------------------------------

	// Constructors -----------------------------------------------------------

	public AdminService() {
		super();
	}
	// Simple CRUD methods ----------------------------------------------------

	public Admin findOne(final int adminId) {
		return this.adminRepository.findOne(adminId);
	}

	public Admin saveEdit(final Admin admin) {
		this.checkPrincipal(admin);
		this.checkConcurrency(admin);
		final Admin result = this.adminRepository.save(admin);

		return result;
	}

	
	// Other business methods -------------------------------------------------

	public Admin findByPrincipal() {
		Admin result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public Admin findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Admin result;
		result = this.adminRepository.findByUserAccount(userAccount.getId());
		return result;
	}
	
	private void checkPrincipal(final Admin admin) {
		if (admin.getId() != 0) {
			final Admin a = this.findByPrincipal();
			Assert.isTrue(admin.equals(a));
		}
	}
	private void checkConcurrency(final Admin admin) {
		if (admin.getId() != 0) {
			final Admin a = this.findOne(admin.getId());
			Assert.isTrue(admin.getVersion() == a.getVersion());
		}
	}
	
	// ---- Reconstruct de ActorForm para editar customers -------
			public Admin reconstructAdmin(final ActorForm actorForm, final BindingResult binding) {
				final Admin adminBBDD = this.findOne(actorForm.getId());
				final Admin result = actorForm.getAdmin();
				result.setUserAccount(adminBBDD.getUserAccount());
				result.setMessagesReceived(adminBBDD.getMessagesReceived());
				result.setMessagesSent(adminBBDD.getMessagesSent());
				result.setFolders(adminBBDD.getFolders());
				this.validator.validate(result, binding);

				return result;

			}

}
