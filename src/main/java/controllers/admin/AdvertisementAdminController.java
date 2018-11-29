/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertisementService;
import controllers.AbstractController;
import domain.Advertisement;

@Controller
@RequestMapping("/advertisement/administrator")
public class AdvertisementAdminController extends AbstractController {

	// Services

	@Autowired
	private AdvertisementService	advertisementService;


	// Constructors -----------------------------------------------------------

	public AdvertisementAdminController() {
		super();
	}

	// ---------------------------------------------------------------

	@RequestMapping(value = "/taboo", method = RequestMethod.GET)
	public ModelAndView taboo() {
		ModelAndView result;
		final Collection<Advertisement> advertisements = this.advertisementService.getTabooAdvertisements();
		result = new ModelAndView("advertisement/taboo");
		result.addObject("uri", "advertisement/administrator/taboo.do");
		result.addObject("advertisements", advertisements);
		result.addObject("fromTabooList", true);
		return result;
	}
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Advertisement> advertisements = this.advertisementService.findAll();
		result = new ModelAndView("advertisement/list");
		result.addObject("uri", "advertisement/administrator/list.do");
		result.addObject("advertisements", advertisements);
		result.addObject("fromTabooList", false);
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteGeneral(@RequestParam final int advertisementId) {
		ModelAndView result;
		final Advertisement advertisement = this.advertisementService.findOne(advertisementId);
		try {
			this.advertisementService.deleteAdmin(advertisement);
			result = new ModelAndView("redirect:/advertisement/administrator/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/advertisement/administrator/list.do");
		}
		return result;
	}

	@RequestMapping(value = "/taboo/delete", method = RequestMethod.GET)
	public ModelAndView deleteTaboo(@RequestParam final int advertisementId) {
		ModelAndView result;
		final Advertisement advertisement = this.advertisementService.findOne(advertisementId);
		try {
			this.advertisementService.deleteAdmin(advertisement);
			result = new ModelAndView("redirect:/advertisement/administrator/taboo.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/advertisement/administrator/taboo.do");
		}
		return result;
	}

}
