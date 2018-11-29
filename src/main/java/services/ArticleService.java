
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ArticleRepository;
import security.Authority;
import domain.Actor;
import domain.Admin;
import domain.Article;
import domain.User;

@Service
@Transactional
public class ArticleService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ArticleRepository	articleRepository;

	// Services ---------------------------------------------------------------
	@Autowired
	private AdminService		adminService;
	@Autowired
	private UserService			userService;
	@Autowired
	private ActorService		actorService;

	@Autowired
	private GlobalService		globalService;
	@Autowired
	private Validator			validator;


	// Constructors -----------------------------------------------------------

	public ArticleService() {
		super();
	}
	// Simple CRUD methods ----------------------------------------------------

	public Article findOne(final int articleId) {
		return this.articleRepository.findOne(articleId);
	}

	public Collection<Article> findAll() {
		return this.articleRepository.findAll();
	}

	public Article save(final Article a) {
		this.checkUser(a);
		Date now = new Date();
		if (a.getId() != 0) {
			final Article old = this.findOne(a.getId());
			if (old.isDraft() && !a.isDraft()) {
				a.setPubMoment(now);
			}
			Assert.isTrue(old.isDraft(), "Final mode");
		} else if (!a.isDraft()) {
			a.setPubMoment(now);
		}
		final Article article = this.articleRepository.save(a);
		return article;
	}
	// Other business methods -------------------------------------------------

	public Article findOneToDisplay(final int articleId) {
		Article article = this.articleRepository.findOne(articleId);
		if (article != null && article.getNewspaper().getDraft()) {
			Actor actorPrincipal = this.actorService.findByPrincipal();
			Authority auAdmin = new Authority();
			auAdmin.setAuthority(Authority.ADMIN);
			Assert.isTrue(article.getWriter().getId() == actorPrincipal.getId() || actorPrincipal.getUserAccount().getAuthorities().contains(auAdmin) || article.getNewspaper().getPublisher().getId() == actorPrincipal.getId());
		}
		return article;
	}

	public void deleteAdmin(final Article article) {
		this.checkAdmin();
		this.articleRepository.delete(article);
	}
	private void checkAdmin() {
		final Admin admin = this.adminService.findByPrincipal();
		Assert.notNull(admin, "No admin");
	}

	// Reconstruct for article
	public Article reconstruct(final Article article, final BindingResult binding) {
		Article result = this.create();
		result.setTitle(article.getTitle());
		if (article.getSummary() != null && article.getSummary() != "")
			result.setSummary(article.getSummary());
		if (article.getBody() != null && article.getBody() != "")
			result.setBody(article.getBody());
		if (article.getPictures() != null)
			result.setPictures(article.getPictures());
		result.setFather(article.getFather());
		result.setDraft(article.isDraft());
		result.setNewspaper(article.getNewspaper());
		if (binding != null) {
			this.validator.validate(result, binding);
		}
		return result;

	}

	// ---- Reconstruct for article edit -------
	public Article reconstructArticle(final Article article, final BindingResult binding) {
		final Article articleBBDD = this.findOne(article.getId());
		final Article result = article;
		result.setPubMoment(articleBBDD.getPubMoment());
		result.setWriter(article.getWriter());
		result.setFollowUps(article.getFollowUps());
		if (binding != null) {
			this.validator.validate(result, binding);
		}
		return result;

	}

	public Collection<Article> getArticlesPublishedOfNewspaperId(final Integer newspaperId) {
		final Collection<Article> res = this.articleRepository.articlesPublishedOfNewspaperId(newspaperId);
		return res;
	}

	public Collection<Article> getTabooArticles() {
		Assert.notNull(this.adminService.findByPrincipal());
		Collection<String> tabooWords = this.globalService.getTaboos();
		Collection<Article> articlesTaboo = new HashSet<Article>();
		for (String tabooWord : tabooWords) {
			articlesTaboo.addAll(this.articleRepository.tabooArticle(tabooWord));
		}

		return articlesTaboo;
	}

	public Collection<Article> findByKeyWord(final String keyWord) {
		return this.articleRepository.findByKeyWord(keyWord);
	}

	public Article create() {
		final Article res = new Article();
		final User writer = this.userService.findByPrincipal();
		res.setWriter(writer);
		final Collection<Article> followUps = new ArrayList<>();
		res.setFollowUps(followUps);
		res.setDraft(true);
		final Collection<String> pictures = new ArrayList<>();
		res.setPictures(pictures);

		return res;
	}

	public Article findOneToEdit(final int articleId) {
		Article res;
		res = this.articleRepository.findOne(articleId);
		Assert.isTrue(res.isDraft());
		this.checkUser(res);
		return res;
	}
	private void checkUser(final Article res) {
		final User log = this.userService.findByPrincipal();
		Assert.isTrue(res.getWriter().equals(log));
	}

	public Collection<Article> findFinalAndPublished() {
		Collection<Article> res;

		final User log = this.userService.findByPrincipal();
		final int id = log.getId();
		res = this.articleRepository.findFinalAndPublished(id);
		return res;
	}
	public String checkConcurrence(final Article article) {
		String s = null;
		if (article.getId() != 0) {
			final Article articleBD = this.articleRepository.findOne(article.getId());
			if (article.getVersion() != articleBD.getVersion())
				s = "article.concurrency.error";
		}
		return s;
	}

	//Queries 7.1

	public Double getAverageArticlesPerUser() {
		return this.articleRepository.averageArticlesPerUser();
	}
	public Double getStandardDeviationArticlesPerUser() {
		return this.articleRepository.standardDeviationArticlesPerUser();
	}
	public Double getAverageArticlesPerNewspaper() {
		return this.articleRepository.averageArticlesPerNewspaper();
	}
	public Double getStandardDeviationArticlesPerNewspaper() {
		return this.articleRepository.standardDeviationArticlesPerNewspaper();
	}

	//Queries 17.1

	public Double getAverageFollowUpsPerArticle() {
		return this.articleRepository.averageFollowUpsPerArticle();
	}
	public Double getAverageFollowUpsPerArticle1Week() {
		return this.articleRepository.averageFollowUpsPerArticle1WeekAfter();
	}
	public Double getAverageFollowUpsPerArticle2Week() {
		return this.articleRepository.averageFollowUpsPerArticle2WeekAfter();
	}

	public void flush() {
		this.articleRepository.flush();
	}

	public Collection<Article> filterFollowUps(Article article) {
		Collection<Article> res = this.articleRepository.filterFollowUps(article.getId());

		return res;
	}

}
