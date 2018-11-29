/*
 * NewspaperController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.CustomerService;
import services.SubscriptionService;
import services.UserService;
import services.VolumeService;
import domain.Newspaper;
import domain.Volume;

@Controller
@RequestMapping("/volume")
public class VolumeController extends AbstractController {

	// ------------------------------------------------------------------------
	@Autowired
	private VolumeService		volumeService;

	@Autowired
	private ActorService		actorService;
	@Autowired
	private SubscriptionService	creditcardService;
	@Autowired
	private CustomerService		customerService;
	@Autowired
	private UserService			userService;


	// Constructors -----------------------------------------------------------

	public VolumeController() {
		super();
	}

	// ---------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Volume> volumes = this.volumeService.findAll();
		result = new ModelAndView("volume/list");
		result.addObject("uri", "volume/list.do");
		result.addObject("volumes", volumes);
		if (this.actorService.checkAuthenticate()) {
			Authority auUser = new Authority();
			auUser.setAuthority(Authority.USER);
			if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(auUser)) {
				result.addObject("user", this.userService.findByPrincipal());
			}
		}
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final Integer volumeId) {
		ModelAndView result = new ModelAndView("volume/display");
		final Volume volume = this.volumeService.findOne(volumeId);
		boolean controlSubscription = false;
		if (volume != null) {
			if (this.actorService.checkAuthenticate()) {
				Authority auCustomer = new Authority();
				auCustomer.setAuthority(Authority.CUSTOMER);
				if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(auCustomer)) {
					int customerId = this.customerService.findByPrincipal().getId();
					if (this.creditcardService.subscriptionVolumeExists(customerId, volumeId)) {
						controlSubscription = true;
					}
				}
			}
		} else {
			controlSubscription = false;
			result.addObject("notExists", true);
		}
		final Collection<Newspaper> newspapers = this.volumeService.findNewspapers(volumeId);
		result.addObject("volume", volume);
		result.addObject("newspapers", newspapers);
		result.addObject("controlSubscription", controlSubscription);
		return result;
	}
}
