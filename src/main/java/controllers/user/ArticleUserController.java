
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.NewspaperService;
import services.UserService;
import controllers.AbstractController;
import domain.Article;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/article/user")
public class ArticleUserController extends AbstractController {

	// Services

	@Autowired
	private ArticleService		articleService;
	@Autowired
	private NewspaperService	newspaperService;
	@Autowired
	private UserService			userService;


	// Constructors -----------------------------------------------------------

	public ArticleUserController() {
		super();
	}

	// ---------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final User log = this.userService.findByPrincipal();
		ModelAndView result;
		final Collection<Article> articles = log.getArticles();
		result = new ModelAndView("article/list");
		result.addObject("uri", "article/user/list.do");
		result.addObject("articles", articles);
		return result;
	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result = new ModelAndView("article/edit");
		final Article article = this.articleService.create();
		//	Write an article and attach it to any newspaper that has not been published, yet
		final Collection<Newspaper> all = this.newspaperService.findNotPublished();
		//	 Follow-ups can be written only after an article is saved in final mode and the corresponding newspaper is published
		final Collection<Article> articles = this.articleService.findFinalAndPublished();//published article when newspaper

		result.addObject("articles", articles);
		result.addObject("article", article);
		result.addObject("newspapers", all);

		return result;
	}
	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int articleId) {
		ModelAndView result;
		Article article;
		try {
			article = this.articleService.findOneToEdit(articleId);
			result = this.createEditModelAndView(article);
		} catch (final Throwable oops) {
			result = new ModelAndView("article/edit");
			result.addObject("notAuthorized", true);
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Article article, final BindingResult binding) {
		ModelAndView result;
		Article save;
		if (article.getId() == 0)
			save = this.articleService.reconstruct(article, binding);
		else
			save = this.articleService.reconstructArticle(article, binding);
		//		Note that articles may be saved in draft mode, which allows to modify them later, or
		//		final model, which freezes them forever.
		if (binding.hasErrors())
			result = this.createEditModelAndView(article);
		else if (this.newspaperService.findOne(save.getNewspaper().getId()) == null)
			result = this.createEditModelAndView(article, "article.newspaper.deleted");
		else if (save.getId() != 0 && this.articleService.findOne(save.getId()) == null)
			result = this.createEditModelAndView(article, "article.article.deleted");
		else if (save.getId() != 0 && !this.articleService.findOne(save.getId()).isDraft())
			result = this.createEditModelAndView(article, "article.nodraft.error");
		else if (save.getNewspaper().getDraft() == false)
			result = this.createEditModelAndView(article, "article.newspaper.published.error");
		else
			try {
				final String s = this.articleService.checkConcurrence(save);
				if (s == null) {
					this.articleService.save(save);
					result = new ModelAndView("redirect:/article/user/list.do");
				} else
					result = this.createEditModelAndView(article, s);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(article, "article.commit.error");
			}
		return result;
	}
	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Article article) {
		ModelAndView result;

		result = this.createEditModelAndView(article, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Article article, final String message) {
		final ModelAndView result = new ModelAndView("article/edit");
		//		 Write an article and attach it to any newspaper that has not been published, yet.
		final Collection<Newspaper> newspapers = this.newspaperService.findNotPublished();
		//		The writer of an article may write follow-ups on it. Follow-ups can be written only after an
		//		article is saved in final mode and the corresponding newspaper is published.
		final Collection<Article> articles = this.articleService.findFinalAndPublished();

		result.addObject("newspapers", newspapers);
		result.addObject("article", article);
		result.addObject("message", message);
		result.addObject("articles", articles);

		return result;
	}
}
