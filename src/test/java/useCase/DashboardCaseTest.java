
package useCase;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ArticleService;
import services.ChirpService;
import services.NewspaperService;
import services.UserService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class DashboardCaseTest extends AbstractTest {

	@Autowired
	private NewspaperService	newspaperService;
	@Autowired
	private ChirpService		chirpService;
	@Autowired
	private ArticleService		articleService;
	@Autowired
	private UserService			userService;


	//--------------------------------------------------------------------------
	//Use Case 8: An admin goes to the dashboard and can see results
	protected void templateDashboard(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			this.newspaperService.getAverageNewspapersPerUser();
			this.newspaperService.getStandardDeviationNewspapersPerUser();
			this.articleService.getAverageArticlesPerNewspaper();
			this.articleService.getStandardDeviationArticlesPerNewspaper();
			this.articleService.getAverageArticlesPerUser();
			this.articleService.getStandardDeviationArticlesPerUser();

			this.newspaperService.getNewspaperWithLessArticlesThanAvgMinus10();
			this.newspaperService.getNewspaperWithMoreArticlesThanAvgPlus10();
			this.userService.getRatioCreatorsNewspapers();
			this.userService.getRatioCreatorsArticles();

			// 17.1
			this.articleService.getAverageFollowUpsPerArticle();
			this.articleService.getAverageFollowUpsPerArticle1Week();
			this.articleService.getAverageFollowUpsPerArticle2Week();

			this.chirpService.getAverageChirpsPerUser();
			this.chirpService.getStandardDeviationChirpsPerUser();
			this.userService.getUsersChirpsAbove75Average();

			// 24.1

			this.newspaperService.getRatioPublicVsPrivateNewspaper();
			this.newspaperService.getAverageArticlesPerPrivateNewspaper();
			this.newspaperService.getAverageArticlesPerPublicNewspaper();
			this.newspaperService.getRatioPrivateVsPublicNewspapersPerPublisher();
			this.newspaperService.getRatioSuscribersPerPrivateNewspapersVsTotalCustomers();
			this.authenticate(null);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.authenticate(null);
		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverDashboard() {
		final Object testingData[][] = {
			//POSITIVE
			{
				"admin", null
			},
			//NEGATIVE
			{
				"user1", IllegalArgumentException.class
			},
			//NEGATIVE
			{
				null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDashboard((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}
}
