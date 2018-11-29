/*
 * AbstractController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.GlobalService;

@Controller
@RequestMapping("/legal")
public class LegalController extends AbstractController {

	@Autowired
	GlobalService	globalService;


	// Panic handler ----------------------------------------------------------

	public LegalController() {
		super();
	}

	// Action-1 ---------------------------------------------------------------		

	@RequestMapping("/terms")
	public ModelAndView terms() {
		ModelAndView result;

		result = new ModelAndView("legal/terms");
		result.addObject("tabooWords", this.globalService.getGlobal().getSpamWords());

		return result;
	}
	@RequestMapping("/cookies")
	public ModelAndView cookies() {
		ModelAndView result;

		result = new ModelAndView("legal/cookies");

		return result;
	}

}
