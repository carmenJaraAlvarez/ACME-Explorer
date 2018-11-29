
package useCase;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ArticleService;
import services.NewspaperService;
import services.UserService;
import utilities.AbstractTest;
import domain.Article;
import domain.Newspaper;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ArticleUseCaseTest extends AbstractTest {

	@Autowired
	private ArticleService		articleService;
	@Autowired
	private UserService			userService;
	@Autowired
	private NewspaperService	newspaperService;


	//--------------------------------------------------------------------------

	//UC14: An unauthenticated person search for an published article by a keyword

	protected void templateSearchArticle(final String keyword, final String type, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			Assert.isTrue(type == "article");
			final Collection<Article> articles = this.articleService.findByKeyWord(keyword);
			Assert.isTrue(!articles.isEmpty());
			this.articleService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	@Test
	public void driverSearchArticle() {
		final Object testingData[][] = {
			//POSITIVE
			{
				"red", "article", null
			},
			//NEGATIVE
			{
				"red", "newspaper", IllegalArgumentException.class
			},
			//NEGATIVE
			{
				"red", null, IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSearchArticle((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	//	UC4: A logged user lists his/her articles and creates a new one in an newspaper in draft mode, and saves it 

	protected void templateCreatesandSaveArticle(final String username, final String newspaperName, final String title, final String body, final String summary, final String pictures, final boolean draft, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final User user = this.userService.findByPrincipal();
			final Collection<Article> articles = user.getArticles();
			final Collection<Newspaper> newspapers = this.newspaperService.findNotPublished();
			final Newspaper newspaper = this.newspaperService.findOne(this.getEntityId(newspaperName));
			Assert.isTrue(newspapers.contains(newspaper));
			final Article article = this.articleService.create();
			article.setNewspaper(newspaper);
			article.setTitle(title);
			article.setBody(body);
			article.setSummary(summary);
			final Collection<String> imgs = article.getPictures();
			imgs.add("pictures");
			article.setPictures(imgs);
			article.setDraft(draft);

			this.articleService.save(article);
			this.articleService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		this.authenticate(null);
	}
	@Test
	public void driverCreatesandSaveArticle() {
		final Object testingData[][] = {
			//POSITIVE
			{
				"user2", "newspaper2", "title", "body body", "summary", "http://img.com", true, null
			},
			//NEGATIVE: published newspaper
			{
				"user2", "newspaper1", "title", "body body", "summary", "http://img.com", true, IllegalArgumentException.class
			},
			//NEGATIVE
			{
				null, "newspaper1", "title", "body body", "summary", "http://img.com", true, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreatesandSaveArticle((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Boolean) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}
	//Use case 6
	//An actor who is authenticated as an administrator must be able to remove an article that he or she thinks is inappropiate.
	//An actor who is authenticated as an administrator must be able to list the artuckes that contain taboo words
	protected void templateDeleteAdminArticle(final String username, final String article, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(username);
			this.articleService.getTabooArticles();
			this.articleService.deleteAdmin(this.articleService.findOne((this.getEntityId(article))));

			this.articleService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		this.authenticate(null);
	}

	@Test
	public void driverDeleteAdminArticle() {
		final Object testingData[][] = {
			//Positive test: Deleting a newspaper correctly
			{
				"admin", "article4", null
			},
			//Positive test: Deleting a newspaper correctly
			{
				"user1", "article4", IllegalArgumentException.class
			},
			//NEGATIVE
			{
				null, "article4", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteAdminArticle((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	//	UC5: An logged User list his/her articles, links to create one, 
	//	writes an followup on  one of his/her published articles and saves it
	protected void templateCreatesFollowUp(final String username, final String newspaperName, final String title, final String body, final String summary, final String pictures, final String father, final boolean draft, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final User user = this.userService.findByPrincipal();
			final Collection<Article> articles = user.getArticles();
			final Collection<Newspaper> newspapers = this.newspaperService.findNotPublished();
			final Newspaper newspaper = this.newspaperService.findOne(this.getEntityId(newspaperName));
			Assert.isTrue(newspapers.contains(newspaper));
			final Article article = this.articleService.create();
			article.setNewspaper(newspaper);
			article.setTitle(title);
			article.setBody(body);
			article.setSummary(summary);
			final Collection<String> imgs = article.getPictures();
			imgs.add("pictures");
			article.setPictures(imgs);
			article.setDraft(draft);
			final Article fatherArticle = this.articleService.findOne(this.getEntityId(father));
			article.setFather(fatherArticle);
			this.articleService.save(article);
			this.articleService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		this.authenticate(null);
	}
	@Test
	public void driverCreatesFollowUp() {
		final Object testingData[][] = {
			//POSITIVE
			{
				"user2", "newspaper2", "title", "body body", "summary", "http://img.com", "article2", true, null
			},
			//NEGATIVE(published newspaper
			{
				"user2", "newspaper1", "title", "body body", "summary", "http://img.com", "article2", true, IllegalArgumentException.class
			},
			//NEGATIVE
			{
				null, "newspaper1", "title", "body body", "summary", "http://img.com", "article2", true, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreatesFollowUp((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Boolean) testingData[i][7], (Class<?>) testingData[i][8]);
	}
}
