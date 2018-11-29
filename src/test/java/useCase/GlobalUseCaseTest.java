
package useCase;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ArticleService;
import services.GlobalService;
import utilities.AbstractTest;
import domain.Global;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class GlobalUseCaseTest extends AbstractTest {

	@Autowired
	private GlobalService	globalService;
	@Autowired
	private ArticleService	articleService;


	// Use Case 7: An admin, logs and edit taboo words in the System
	protected void templateTabooArticle(final String username, final String taboo, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final Global global = this.globalService.getGlobal();
			final Collection<String> spamwords = this.globalService.getTaboos();
			spamwords.add(taboo);
			global.setSpamWords(spamwords);
			this.globalService.save(global);
			this.globalService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.authenticate(null);
		this.checkExceptions(expected, caught);
	}
	@Test
	public void driverTabooWordsEdit() {
		final Object testingData[][] = {
			//POSITIVE
			{
				"admin", "tabooWord", null
			},
			//NEGATIVE
			{
				"user1", "newspaper", IllegalArgumentException.class
			},
			//NEGATIVE
			{
				null, "newspaper", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateTabooArticle((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

}
