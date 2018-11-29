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
import services.AdvertisementService;
import services.ArticleService;
import services.CustomerService;
import services.SubscriptionService;
import domain.Advertisement;
import domain.Article;

@Controller
@RequestMapping("/article")
public class ArticleController extends AbstractController {

	// ------------------------------------------------------------------------
	@Autowired
	private ArticleService			articleService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private SubscriptionService		creditcardService;
	@Autowired
	private CustomerService			customerService;
	@Autowired
	private AdvertisementService	advertisementService;


	// Constructors -----------------------------------------------------------

	public ArticleController() {
		super();
	}

	// ---------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final Integer articleId) {
		ModelAndView result = new ModelAndView("article/display");
		boolean controlSubscription = false;
		try {
			final Article article = this.articleService.findOneToDisplay(articleId);
			if (article != null) {
				if (article.getNewspaper().getIsPrivate()) {
					if (this.actorService.checkAuthenticate()) {
						Authority auCustomer = new Authority();
						auCustomer.setAuthority(Authority.CUSTOMER);
						Authority auUser = new Authority();
						auUser.setAuthority(Authority.USER);
						Authority auAdmin = new Authority();
						auAdmin.setAuthority(Authority.ADMIN);
						if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(auCustomer)) {
							int customerId = this.customerService.findByPrincipal().getId();
							if (this.creditcardService.subscriptionExists(customerId, article.getNewspaper().getId())) {
								controlSubscription = true;
							}
						} else if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(auUser)) {
							if (article.getWriter().equals(this.actorService.findByPrincipal())) {
								controlSubscription = true;
							}
						} else if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(auAdmin)) {
							controlSubscription = true;
						}
					}
				} else {
					controlSubscription = true;
				}
				result.addObject("article", article);
				//String summary1 = article.getSummary().substring(0, 6);
				//result.addObject("summary1", summary1);
				result.addObject("pictures", article.getPictures());
				result.addObject("controlSubscription", controlSubscription);
				Collection<Article> filterFollowUps = this.articleService.filterFollowUps(article);
				result.addObject("filterFollowUps", filterFollowUps);
				Advertisement randomAdvertisement = this.advertisementService.random(article.getNewspaper());
				result.addObject("randomAdvertisement", randomAdvertisement);
			} else {
				controlSubscription = false;
				result.addObject("deleted", true);
			}

		} catch (Throwable oops) {
			controlSubscription = false;
			result.addObject("deleted", true);
		}
		return result;
	}
}
