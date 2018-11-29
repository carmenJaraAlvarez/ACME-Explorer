
package controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.NewspaperService;
import services.SubscriptionService;
import controllers.AbstractController;
import domain.Newspaper;
import domain.Subscription;
import forms.SubscriptionForm;

@Controller
@RequestMapping("/newspaper/customer")
public class NewspaperCustomerController extends AbstractController {

	// Services

	@Autowired
	private NewspaperService	newspaperService;
	@Autowired
	private SubscriptionService	subscriptionService;


	// Constructors -----------------------------------------------------------

	public NewspaperCustomerController() {
		super();
	}

	// ---------------------------------------------------------------

	@RequestMapping(value = "/subscribe", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final Integer objectId) {
		ModelAndView result = new ModelAndView("subscription/newspaper");
		final Newspaper newspaper = this.newspaperService.findOne(objectId);
		if (newspaper == null) {
			result.addObject("notExists", true);
		} else if (!newspaper.getIsPrivate()) {
			result = new ModelAndView("redirect:/newspaper/display.do?newspaperId=" + objectId);
		}

		final SubscriptionForm subscriptionForm = new SubscriptionForm(newspaper.getId());
		result.addObject("subscriptionForm", subscriptionForm);
		result.addObject("objectId", objectId);
		result.addObject("title", newspaper.getTitle());
		result.addObject("formURL", "newspaper/customer/subscribe.do?objectId=" + objectId);
		result.addObject("cancelURL", "newspaper/display.do?newspaperId=" + objectId);
		return result;
	}

	@RequestMapping(value = "/subscribe", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final SubscriptionForm subscriptionForm, final BindingResult binding) {
		ModelAndView result;
		Subscription subscription = this.subscriptionService.reconstructForNewspaper(subscriptionForm, binding);
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(subscriptionForm);
		} else {
			try {
				if (this.subscriptionService.checkTarjeta(subscription.getCreditCard())) {
					this.subscriptionService.controlAndSave(subscription);
					result = new ModelAndView("redirect:/newspaper/display.do?newspaperId=" + subscription.getNewspaper().getId());
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

		final ModelAndView result = new ModelAndView("subscription/newspaper");
		Newspaper newspaper = this.newspaperService.findOne(subscriptionForm.getObjectId());
		result.addObject("message", message);
		result.addObject("subscriptionForm", subscriptionForm);
		result.addObject("objectId", subscriptionForm.getObjectId());
		result.addObject("title", newspaper.getTitle());
		result.addObject("formURL", "newspaper/customer/subscribe.do?objectId=" + subscriptionForm.getObjectId());
		result.addObject("cancelURL", "newspaper/display.do?newspaperId=" + subscriptionForm.getObjectId());
		return result;

	}
}
