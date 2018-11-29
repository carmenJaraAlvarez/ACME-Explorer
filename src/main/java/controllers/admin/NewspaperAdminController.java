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

import services.NewspaperService;
import controllers.AbstractController;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper/administrator")
public class NewspaperAdminController extends AbstractController {

	// Services

	@Autowired
	private NewspaperService	newspaperService;


	// Constructors -----------------------------------------------------------

	public NewspaperAdminController() {
		super();
	}

	// ---------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Newspaper> newspapers = this.newspaperService.findAll();
		result = new ModelAndView("newspaper/adminList");
		result.addObject("uri", "newspaper/administrator/list.do");
		result.addObject("newspapers", newspapers);
		result.addObject("delete", "delete");
		return result;
	}

	@RequestMapping(value = "/taboo", method = RequestMethod.GET)
	public ModelAndView taboo() {
		ModelAndView result;
		final Collection<Newspaper> newspapers = this.newspaperService.getTabooNewspapers();
		result = new ModelAndView("newspaper/taboo");
		result.addObject("uri", "newspaper/administrator/taboo.do");
		result.addObject("newspapers", newspapers);
		result.addObject("delete", "deleteTaboo");
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int newspaperId) {
		ModelAndView result;
		final Newspaper newspaper = this.newspaperService.findOne(newspaperId);
		try {
			this.newspaperService.deleteAdmin(newspaper);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("newspaper/error");
		}
		return result;
	}
	
	@RequestMapping(value = "/deleteTaboo", method = RequestMethod.GET)
	public ModelAndView deleteTaboo(@RequestParam final int newspaperId) {
		ModelAndView result;
		final Newspaper newspaper = this.newspaperService.findOne(newspaperId);
		try {
			this.newspaperService.deleteAdmin(newspaper);
			result = new ModelAndView("redirect:taboo.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("newspaper/error");
		}
		return result;
	}

}
