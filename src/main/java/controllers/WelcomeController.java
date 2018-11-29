/*
 * WelcomeController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {

	// ------------------------------------------------------------------------

	@Autowired
	private ActorService	actorService;


	// Constructors -----------------------------------------------------------

	public WelcomeController() {
		super();
	}

	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/index")
	public ModelAndView index(@RequestParam(required = false, defaultValue = "John Doe") String name) {
		ModelAndView result;
		SimpleDateFormat formatter;
		Date moment;

		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		moment = new Date();

		result = new ModelAndView("welcome/index");
		if (this.actorService.checkAuthenticate()) {
			name = this.actorService.findByPrincipal().getName();
		}
		result.addObject("name", name);
		result.addObject("moment", moment);

		return result;
	}
}
