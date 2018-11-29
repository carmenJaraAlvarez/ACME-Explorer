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

import services.ChirpService;
import controllers.AbstractController;
import domain.Chirp;

@Controller
@RequestMapping("/chirp/administrator")
public class ChirpAdminController extends AbstractController {

	// Services

	@Autowired
	private ChirpService	chirpService;


	// Constructors -----------------------------------------------------------

	public ChirpAdminController() {
		super();
	}

	// ---------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Chirp> chirps = this.chirpService.findAll();
		result = new ModelAndView("chirp/all");
		result.addObject("uri", "chirp/administrator/list.do");
		result.addObject("chirps", chirps);
		result.addObject("fromGeneralList", true);
		return result;
	}

	@RequestMapping(value = "/taboo", method = RequestMethod.GET)
	public ModelAndView taboo() {
		ModelAndView result;
		final Collection<Chirp> chirps = this.chirpService.getTabooChirps();
		result = new ModelAndView("chirp/taboo");
		result.addObject("uri", "chirp/administrator/taboo.do");
		result.addObject("chirps", chirps);
		result.addObject("fromTabooList", true);
		return result;
	}

	@RequestMapping(value = "/taboo/delete", method = RequestMethod.GET)
	public ModelAndView deleteTaboo(@RequestParam final int chirpId) {
		ModelAndView result;
		final Chirp chirp = this.chirpService.findOne(chirpId);
		try {
			this.chirpService.deleteAdmin(chirp);
			result = new ModelAndView("redirect:/chirp/administrator/taboo.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/chirp/administrator/taboo.do");
		}

		return result;
	}

	@RequestMapping(value = "/general/delete", method = RequestMethod.GET)
	public ModelAndView deleteGeneral(@RequestParam final int chirpId) {
		ModelAndView result;
		final Chirp chirp = this.chirpService.findOne(chirpId);
		try {
			this.chirpService.deleteAdmin(chirp);
			result = new ModelAndView("redirect:/chirp/administrator/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/chirp/administrator/list.do");
		}

		return result;
	}

}
