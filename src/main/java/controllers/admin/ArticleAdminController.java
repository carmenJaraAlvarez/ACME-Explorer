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

import services.ArticleService;
import controllers.AbstractController;
import domain.Article;

@Controller
@RequestMapping("/article/administrator")
public class ArticleAdminController extends AbstractController {

	// Services

	@Autowired
	private ArticleService	articleService;


	// Constructors -----------------------------------------------------------

	public ArticleAdminController() {
		super();
	}

	// ---------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Article> articles = this.articleService.findAll();
		result = new ModelAndView("article/list");
		result.addObject("uri", "article/administrator/list.do");
		result.addObject("articles", articles);
		result.addObject("fromGeneralList", true);
		return result;
	}

	@RequestMapping(value = "/taboo", method = RequestMethod.GET)
	public ModelAndView taboo() {
		ModelAndView result;
		final Collection<Article> articles = this.articleService.getTabooArticles();
		result = new ModelAndView("article/taboo");
		result.addObject("uri", "article/administrator/taboo.do");
		result.addObject("articles", articles);
		result.addObject("fromTabooList", true);
		return result;
	}

	@RequestMapping(value = "/general/delete", method = RequestMethod.GET)
	public ModelAndView deleteGeneral(@RequestParam final int articleId) {
		ModelAndView result;
		final Article article = this.articleService.findOne(articleId);
		try {
			this.articleService.deleteAdmin(article);
			result = new ModelAndView("redirect:/article/administrator/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/article/administrator/list.do");
		}
		return result;
	}

	@RequestMapping(value = "/taboo/delete", method = RequestMethod.GET)
	public ModelAndView deleteTaboo(@RequestParam final int articleId) {
		ModelAndView result;
		final Article article = this.articleService.findOne(articleId);
		try {
			this.articleService.deleteAdmin(article);
			result = new ModelAndView("redirect:/article/administrator/taboo.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/article/administrator/taboo.do");
		}
		return result;
	}

}
