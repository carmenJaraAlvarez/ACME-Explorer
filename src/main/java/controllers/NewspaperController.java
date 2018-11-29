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
import services.ArticleService;
import services.CustomerService;
import services.NewspaperService;
import services.SubscriptionService;
import domain.Article;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper")
public class NewspaperController extends AbstractController {

	// ------------------------------------------------------------------------
	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private ArticleService		articleService;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private SubscriptionService	creditcardService;
	@Autowired
	private CustomerService		customerService;


	// Constructors -----------------------------------------------------------

	public NewspaperController() {
		super();
	}

	// ---------------------------------------------------------------

	@RequestMapping(value = "/publicList", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Newspaper> newspapers = this.newspaperService.getPublishedNewspapers();
		result = new ModelAndView("newspaper/publicList");
		result.addObject("uri", "newspaper/publicList.do");
		result.addObject("newspapers", newspapers);
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final Integer newspaperId) {
		ModelAndView result = new ModelAndView("newspaper/display");
		final Newspaper newspaper = this.newspaperService.findOneToDisplay(newspaperId);
		boolean controlSubscription = false;
		if (newspaper != null) {
			if (newspaper.getIsPrivate()) {
				if (this.actorService.checkAuthenticate()) {
					Authority auCustomer = new Authority();
					auCustomer.setAuthority(Authority.CUSTOMER);
					Authority auUser = new Authority();
					auUser.setAuthority(Authority.USER);
					Authority auAdmin = new Authority();
					auAdmin.setAuthority(Authority.ADMIN);
					if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(auCustomer)) {
						int customerId = this.customerService.findByPrincipal().getId();
						if (this.creditcardService.subscriptionExists(customerId, newspaperId)) {
							controlSubscription = true;
						}
					} else if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(auUser)) {
						if (newspaper.getPublisher().equals(this.actorService.findByPrincipal())) {
							controlSubscription = true;
						}
					} else if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(auAdmin)) {
						controlSubscription = true;
					}
				}
			} else {
				controlSubscription = true;
			}
		} else {
			controlSubscription = false;
			result.addObject("deleted", true);
		}
		final Collection<Article> articles = this.articleService.getArticlesPublishedOfNewspaperId(newspaperId);
		result.addObject("newspaper", newspaper);
		result.addObject("articles", articles);
		result.addObject("controlSubscription", controlSubscription);
		return result;
	}
}
