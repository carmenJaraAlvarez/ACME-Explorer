
package controllers.agent;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertisementService;
import services.AgentService;
import services.NewspaperService;
import services.SubscriptionService;
import controllers.AbstractController;
import domain.Advertisement;
import domain.Agent;
import domain.Newspaper;

@Controller
@RequestMapping("/advertisement/agent")
public class AdvertisementAgentController extends AbstractController {

	// Services

	@Autowired
	private AdvertisementService	advertisementService;
	@Autowired
	private AgentService			agentService;
	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private SubscriptionService		subscriptionService;


	// ---------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final Agent log = this.agentService.findByPrincipal();
		ModelAndView result;
		final Collection<Advertisement> advertisements = log.getAdvertisements();
		result = new ModelAndView("advertisement/list");
		result.addObject("uri", "advertisement/agent/list.do");
		result.addObject("advertisements", advertisements);
		return result;
	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result = new ModelAndView("advertisement/create");
		final Advertisement advertisement = this.advertisementService.create();
		Collection<Newspaper> newspapers;
		newspapers = this.newspaperService.findPublished();
		result.addObject("advertisement", advertisement);
		result.addObject("newspapers", newspapers);
		result.addObject("uri", "advertisement/agent/edit.do");

		return result;
	}
	// Edition ----------------------------------------------------------------

	//	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	//	public ModelAndView edit(@RequestParam final int advertisementId) {
	//		ModelAndView result;
	//		Advertisement advertisement;
	//
	//		advertisement = this.advertisementService.findOneToEdit(advertisementId);
	//		result = this.createEditModelAndView(advertisement);
	//
	//		return result;
	//	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Advertisement advertisement, final BindingResult binding) {
		ModelAndView result;
		Advertisement toSave = this.advertisementService.reconstruct(advertisement, binding);
		String s = null;
		if (binding.hasErrors())
			result = this.createEditModelAndView(advertisement);
		else {
			s = this.advertisementService.checkConcurrence(advertisement);
			if (s != null) {
				result = this.createEditModelAndView(advertisement, "advertisement.concurrency.error");
			} else {
				try {
					if (this.subscriptionService.checkTarjeta(advertisement.getCreditCard())) {
						this.advertisementService.save(toSave);
						result = new ModelAndView("redirect:/advertisement/agent/list.do");
					} else {
						result = this.createEditModelAndView(advertisement, "advertisement.date.error");
					}
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(advertisement, "advertisement.commit.error");
				}
			}
		}
		return result;

	}
	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Advertisement advertisement) {
		ModelAndView result;

		result = this.createEditModelAndView(advertisement, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Advertisement advertisement, final String message) {
		final ModelAndView result = new ModelAndView("advertisement/edit");
		Collection<Newspaper> newspapers;
		newspapers = this.newspaperService.findPublished();
		result.addObject("newspapers", newspapers);
		result.addObject("advertisement", advertisement);
		result.addObject("message", message);

		return result;
	}

}
