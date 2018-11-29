
package controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CustomerService;
import services.SubscriptionService;
import services.VolumeService;
import controllers.AbstractController;
import domain.Subscription;
import domain.Volume;
import forms.SubscriptionForm;

@Controller
@RequestMapping("/volume/customer")
public class VolumeCustomerController extends AbstractController {

	// Services

	@Autowired
	private VolumeService		volumeService;

	@Autowired
	private SubscriptionService	subscriptionService;
	@Autowired
	private CustomerService		customerService;


	// Constructors -----------------------------------------------------------

	public VolumeCustomerController() {
		super();
	}

	// ---------------------------------------------------------------

	@RequestMapping(value = "/subscribe", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final Integer objectId) {
		ModelAndView result;
		try {
			if (!this.subscriptionService.subscriptionVolumeExists(this.customerService.findByPrincipal().getId(), objectId)) {
				result = new ModelAndView("subscription/volume");
				final Volume volume = this.volumeService.findOne(objectId);
				if (volume == null) {
					result.addObject("notExists", true);
				}
				final SubscriptionForm subscriptionForm = new SubscriptionForm(objectId);
				result.addObject("subscriptionForm", subscriptionForm);
				result.addObject("objectId", objectId);
				result.addObject("title", volume.getTitle());
				result.addObject("formURL", "volume/customer/subscribe.do?objectId=" + objectId);
				result.addObject("cancelURL", "volume/display.do?volumeId=" + objectId);
			} else {
				result = new ModelAndView("redirect:/volume/display.do?volumeId=" + objectId);
			}
		} catch (final Throwable oops) {
			result = this.panic(oops);
		}
		return result;
	}

	@RequestMapping(value = "/subscribe", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final SubscriptionForm subscriptionForm, final BindingResult binding) {
		ModelAndView result;
		Subscription subscription = this.subscriptionService.reconstructForVolume(subscriptionForm, binding);
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(subscriptionForm);
		} else {
			try {
				if (this.subscriptionService.checkTarjeta(subscription.getCreditCard())) {
					this.subscriptionService.controlAndSaveForVolume(subscription);
					result = new ModelAndView("redirect:/volume/display.do?volumeId=" + subscription.getVolume().getId());
				} else {
					result = this.createEditModelAndView(subscriptionForm, "subscription.date.error");
				}
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(subscriptionForm, "subscription.commit.error");
			}
		}

		return result;

	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final SubscriptionForm subscriptionForm) {
		ModelAndView result;

		result = this.createEditModelAndView(subscriptionForm, null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final SubscriptionForm subscriptionForm, final String message) {

		final ModelAndView result = new ModelAndView("subscription/volume");
		Volume volume = this.volumeService.findOne(subscriptionForm.getObjectId());
		result.addObject("message", message);
		result.addObject("subscriptionForm", subscriptionForm);
		result.addObject("objectId", subscriptionForm.getObjectId());
		result.addObject("title", volume.getTitle());
		result.addObject("formURL", "volume/customer/subscribe.do?objectId=" + subscriptionForm.getObjectId());
		result.addObject("cancelURL", "volume/display.do?volumeId=" + subscriptionForm.getObjectId());
		return result;

	}
}
