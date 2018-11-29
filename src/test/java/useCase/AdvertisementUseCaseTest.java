
package useCase;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.AdvertisementService;
import services.AgentService;
import services.NewspaperService;
import utilities.AbstractTest;
import domain.Advertisement;
import domain.Agent;
import domain.CreditCard;
import domain.Newspaper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdvertisementUseCaseTest extends AbstractTest {

	@Autowired
	private AdvertisementService	advertisementService;
	@Autowired
	private AgentService			agentService;
	@Autowired
	private NewspaperService		newspaperService;


	//--------------------------------------------------------------------------

	// Use Case 21: Crear un anuncio
	// Agents register advertisements that are placed in newspapers. For each advertisement, the
	// system must store a title, a URL to a banner, a URL to a target page, and a valid credit card
	// (which must not expire during the current month)
	// An agent logs, lists his/her advertiments and link to create one. He/she gives dates and save it.

	protected void templateCreatesandSaveAdvertisement(final String agentname, final String newspaperName, final String title, final String banner, final String targetPage,
	//creditcard
		final String num, final String holderName, final String brand, final Integer CVV, final Integer expirationMonth, final Integer expirationYear, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(agentname);
			final Agent loged = this.agentService.findByPrincipal();
			final Collection<Advertisement> advertisements = loged.getAdvertisements();
			final Collection<Newspaper> newspapers = this.newspaperService.findPublished();
			final Newspaper newspaper = this.newspaperService.findOne(this.getEntityId(newspaperName));
			Assert.isTrue(newspapers.contains(newspaper));
			final Advertisement advertisement = this.advertisementService.create();
			advertisement.setNewspaper(newspaper);
			advertisement.setTitle(title);
			advertisement.setBanner(banner);
			advertisement.setTargetPage(targetPage);
			//creditcard
			final CreditCard c = new CreditCard();
			c.setBrand(brand);
			c.setCVV(CVV);
			c.setExpirationMonth(expirationMonth);
			c.setExpirationYear(expirationYear);
			c.setHolderName(holderName);
			c.setNum(num);

			advertisement.setCreditCard(c);

			this.advertisementService.save(advertisement);
			Assert.isTrue(loged.getAdvertisements().size() == advertisements.size());
			this.advertisementService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		this.authenticate(null);
	}
	@Test
	public void driverCreatesandSaveAdvertisement() {
		final Object testingData[][] = {
			//POSITIVE
			{
				"agent2", "newspaper7", "title", "http://banner.com", "http://target.com", "1111222233334444", "pepe", "VISA", 555, 12, 20, null
			},

			//NEGATIVE (not authenticated and not URL targetPage)
			{
				null, "newspaper7", "title", "http://banner.com", "http://target.com", "1111222233334444", "pepe", "VISA", 555, 12, 20, IllegalArgumentException.class
			}, {
				"agent2", "newspaper7", "title", "http://banner.com", null, "1111222233334444", "pepe", "VISA", 555, 12, 20, ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreatesandSaveAdvertisement((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Integer) testingData[i][8], (Integer) testingData[i][9], (Integer) testingData[i][10], (Class<?>) testingData[i][11]);
	}

	// Use Case 22 y 23: Listar periódicos con anuncios y sin anuncios.
	// An actor who is authenticated as an agent must be able to:
	// List the newspapers in which they have placed an advertisement.
	// List the newspapers in which they have not placed any advertisements
	//An agent logs and lists newspaper witch advertisements and then, lists without them.
	protected void templateListNewspapers(final String agentname, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(agentname);
			final Agent loged = this.agentService.findByPrincipal();
			final Collection<Newspaper> newspapersWith = this.newspaperService.findNewspapersWithAdvertisement(loged.getId());
			final int size = newspapersWith.size();
			final Collection<Newspaper> newspapersWithout = this.newspaperService.findNewspapersWithoutAdvertisement(loged.getId());
			newspapersWith.removeAll(newspapersWithout);
			final int size2 = newspapersWith.size();
			Assert.isTrue(size == size2);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		this.authenticate(null);
	}
	@Test
	public void driverListNewspapers() {
		final Object testingData[][] = {
			//POSITIVE
			{
				"agent2", null
			},

			//NEGATIVE (not authenticated and an user)
			{
				null, IllegalArgumentException.class
			}, {
				"user2", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListNewspapers((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	// An actor who is authenticated as an administrator must be able to:
	// List the advertisements that contain taboo words in its title.
	// Remove an advertisement that he or she thinks is inappropriate.
	// An admin logs, lists advertisements with taboo words and delete one of then.
	protected void templateListAndDeleteAdvertisement(final String adminName, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(adminName);
			final ArrayList<Advertisement> taboo = new ArrayList<>(this.advertisementService.getTabooAdvertisements());
			final Advertisement toDelete = taboo.get(0);
			this.advertisementService.deleteAdmin(toDelete);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		this.authenticate(null);
	}
	@Test
	public void driverListAndDeleteAdvertisement() {
		final Object testingData[][] = {
			//POSITIVE

			//NEGATIVE (not authenticated and an user)
			{
				null, IllegalArgumentException.class
			}, {
				"user2", IllegalArgumentException.class
			}, {
				"admin", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListAndDeleteAdvertisement((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

}
