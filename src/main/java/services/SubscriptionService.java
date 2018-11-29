
package services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SubscriptionRepository;
import domain.CreditCard;
import domain.Customer;
import domain.Newspaper;
import domain.Subscription;
import domain.Volume;
import forms.SubscriptionForm;

@Service
@Transactional
public class SubscriptionService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private SubscriptionRepository	subscriptionRepository;

	// Services ---------------------------------------------------------------
	@Autowired
	private CustomerService			customerService;
	@Autowired
	private NewspaperService		newspaperService;
	@Autowired
	private VolumeService			volumeService;
	@Autowired
	private Validator				validator;


	// Constructors -----------------------------------------------------------
	public SubscriptionService() {
		super();
	}
	// Simple CRUD methods ----------------------------------------------------

	public Subscription findOne(final int creditcardId) {
		return this.subscriptionRepository.findOne(creditcardId);
	}

	public Collection<Subscription> findAll() {
		return this.subscriptionRepository.findAll();
	}

	public Subscription save(final Subscription s) {
		return this.subscriptionRepository.save(s);
	}

	public void deleteNewspaper(final Newspaper newspaper) {
		Integer newspaperId = newspaper.getId();
		Collection<Subscription> subscriptions = this.subscriptionRepository.findSubscriptionByNewspaper(newspaperId);
		for (Subscription subscription : subscriptions) {
			this.subscriptionRepository.delete(subscription);
		}
	}
	// Other business methods -------------------------------------------------

	public Subscription create() {
		Subscription res;
		Customer customer = this.customerService.findByPrincipal();
		res = new Subscription();
		res.setCustomer(customer);
		return res;
	}

	public boolean checkTarjeta(CreditCard creditCard) {
		Date hoy = new Date();
		final Integer mesHoy = this.obtenerMes(hoy);
		final Integer añoHoy = this.obtenerAnyo(hoy);
		Boolean res = true;
		if (creditCard.getExpirationYear() < añoHoy) {
			res = false;
		} else if (creditCard.getExpirationYear() == añoHoy && creditCard.getExpirationMonth() < mesHoy) {
			res = false;
		}
		return res;
	}
	//-----------------------------------------------------------------
	public Integer obtenerAnyo(final Date date) {

		if (null == date)
			return 0;
		else {

			final String formato = "yy";
			final SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
			return Integer.parseInt(dateFormat.format(date));

		}

	}
	public int obtenerMes(final Date date) {

		if (null == date)
			return 0;
		else {

			final String formato = "MM";
			final SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
			return Integer.parseInt(dateFormat.format(date));

		}
	}

	// Reconstruct for newspaper subscription
	public Subscription reconstructForNewspaper(final SubscriptionForm subscriptionForm, final BindingResult binding) {
		Subscription result = this.create();
		if (subscriptionForm.getId() != 0) {
			throw new IllegalArgumentException();
		}

		result.setCreditCard(subscriptionForm.getCreditCard());
		Newspaper newspaper = this.newspaperService.findOne(subscriptionForm.getObjectId());
		result.setNewspaper(newspaper);
		result.setVolume(null);
		if (binding != null) {
			this.validator.validate(result, binding);
		}
		return result;

	}

	// Reconstruct for newspaper subscription
	public Subscription reconstructForVolume(final SubscriptionForm subscriptionForm, final BindingResult binding) {
		Subscription result = this.create();
		if (subscriptionForm.getId() != 0) {
			throw new IllegalArgumentException();
		}

		result.setCreditCard(subscriptionForm.getCreditCard());
		Volume volume = this.volumeService.findOne(subscriptionForm.getObjectId());
		result.setNewspaper(null);
		result.setVolume(volume);
		if (binding != null) {
			this.validator.validate(result, binding);
		}
		return result;

	}

	//Save para subscripción al periódico --------------------------------
	public void controlAndSave(Subscription subscription) {

		this.checkPrincipal(subscription);
		this.checkSubscription(subscription);

		this.save(subscription);
	}
	private void checkSubscription(Subscription subscription) {
		boolean b = this.subscriptionExists(subscription.getCustomer().getId(), subscription.getNewspaper().getId());
		Assert.isTrue(!b, "Subscription exists");

	}
	//control de que no existe suscripción previa al periódico
	public boolean subscriptionExists(int customerId, int newspaperId) {
		boolean res = true;

		Collection<Newspaper> newspapers = new HashSet<Newspaper>();
		newspapers.addAll(this.subscriptionRepository.findNewspaperEq(customerId, newspaperId));

		if (newspapers.isEmpty()) {
			newspapers.addAll(this.subscriptionRepository.findAllNewspapersOfVolumeByCustomer(customerId));
			if (!newspapers.contains(this.newspaperService.findOne(newspaperId))) {
				res = false;
			}
		}

		return res;

	}

	//Save para subscripción al volumen ---------------------------------
	public void controlAndSaveForVolume(Subscription subscription) {

		this.checkPrincipal(subscription);
		this.checkSubscriptionForVolume(subscription);

		this.save(subscription);
	}

	private void checkSubscriptionForVolume(Subscription subscription) {
		boolean b = this.subscriptionVolumeExists(subscription.getCustomer().getId(), subscription.getVolume().getId());
		Assert.isTrue(!b, "Subscription exists");

	}

	//control de que no existe suscripción previa al volumen
	public boolean subscriptionVolumeExists(int customerId, int volumeId) {
		boolean res = true;
		Collection<Subscription> eq = new ArrayList<>();
		eq = this.subscriptionRepository.findVolumeEq(customerId, volumeId);
		if (eq.isEmpty()) {
			res = false;
		}
		return res;

	}

	//Private methods
	private void checkPrincipal(Subscription subscription) {
		Assert.isTrue(this.customerService.findByPrincipal().equals(subscription.getCustomer()), "Not authorized");

	}
	public void flush() {
		this.subscriptionRepository.flush();
	}
}
