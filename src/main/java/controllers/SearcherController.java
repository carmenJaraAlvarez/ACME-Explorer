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

import services.ArticleService;
import services.NewspaperService;
import domain.Article;
import domain.Newspaper;

@Controller
@RequestMapping("/searcher")
public class SearcherController extends AbstractController {

	// ------------------------------------------------------------------------
	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private ArticleService		articleService;


	// Constructors -----------------------------------------------------------

	public SearcherController() {
		super();
	}

	// ---------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result = createDisplayModelAndView();
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET, params = "search")
	public ModelAndView searcherList(@RequestParam String keyWord, @RequestParam String search) {
		ModelAndView result;
		if (search.equals("newspaper")) {
			result = createNewspapersModelAndView(keyWord, search);
		} else if (search.equals("article")) {
			result = createArticlesModelAndView(keyWord, search);
		} else {
			result = createDisplayModelAndView();
		}
		return result;
	}

	private ModelAndView createNewspapersModelAndView(final String keyWord, final String search) {
		ModelAndView result = new ModelAndView("search/newspapers");
		final Collection<Newspaper> newspapers = this.newspaperService.findByKeyWord(keyWord);
		result.addObject("uri", "searcher/display.do");
		result.addObject("newspapers", newspapers);
		result.addObject("search", "newspaper");
		result.addObject("keyWord", keyWord);
		return result;
	}
	private ModelAndView createArticlesModelAndView(final String keyWord, final String search) {
		ModelAndView result = new ModelAndView("search/articles");
		final Collection<Article> articles = this.articleService.findByKeyWord(keyWord);
		result.addObject("uri", "searcher/display.do");
		result.addObject("articles", articles);
		result.addObject("search", "article");
		result.addObject("keyWord", keyWord);

		return result;
	}
	private ModelAndView createDisplayModelAndView() {
		ModelAndView result = new ModelAndView("search/display");
		result.addObject("uri", "searcher/display.do");
		result.addObject("search", "display");

		return result;
	}

}
