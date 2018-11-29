
package useCase;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.NewspaperService;
import services.UserService;
import utilities.AbstractTest;
import domain.Newspaper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class NewspaperUseCaseTest extends AbstractTest {

	//Services used
	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private UserService			userService;


	//Use case 2
	//An actor who is authenticated as a user must be able to create a newspaper.
	//An actor who is authenticated as a user must be able to publish a newspaper that he or she's created..

	protected void templatePublishNewspaper(final String username, final String username2, final String title, final String description, final String picture, final boolean isPrivate, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(username);
			this.newspaperService.getMyNewspapers();
			final Newspaper n = this.newspaperService.create();
			n.setPublisher(this.userService.findOne(this.getEntityId(username)));
			n.setTitle(title);
			n.setDescription(description);
			n.setPicture(picture);
			n.setIsPrivate(isPrivate);
			final Newspaper n2 = this.newspaperService.save(n);
			this.authenticate(null);
			if (username2.isEmpty()) {
				this.authenticate(username);
				this.newspaperService.getMyNewspapers();
				this.newspaperService.publishNewspaper(n2);
			} else {
				this.authenticate(username2);
				this.newspaperService.publishNewspaper(n2);
			}

			this.newspaperService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverPublishNewspaper() {
		final Object testingData[][] = {
			//(1) Positive test: publishing a newspaper correctly (publich)
			{
				"user1", "", "newspaper33", "description of newspaper33", "http://www.picture.com", false, null
			},
			//(2) Positive test: publishing a newspaper correctly (private)
			{
				"user1", "", "newspaper33", "description of newspaper33", "http://www.picture.com", true, null
			},
			//(3) Positive test: publishing a newspaper with blank fields (picture)
			{
				"user1", "", "title33", "description of newspaper33", "", false, null
			},
			//(4) Negative test: publishing a newspaper with blank fields (title)
			{
				"user1", "", "", "description of newspaper33", "http://www.picture.com", false, ConstraintViolationException.class
			},
			//(5) Negative test: publishing a newspaper with blank fields (description)
			{
				"user1", "", "title33", "", "http://www.picture.com", false, ConstraintViolationException.class
			},
			//(6) Negative test: publishing a newspaper that is not of the publisher
			{
				"user1", "user2", "newspaper33", "description of newspaper33", "http://www.picture.com", false, ConstraintViolationException.class
			},
			//(7) Negative test: publishing a newspaper by a customer
			{
				"user1", "customer1", "newspaper33", "description of newspaper33", "http://www.picture.com", false, ConstraintViolationException.class
			},
			//(8) Negative test: publishing a newspaper not being authenticated
			{
				null, "", "newspaper33", "description of newspaper33", "http://www.picture.com", false, IllegalArgumentException.class
			},
			//(9) Negative test: publishing a newspaper not being an user (admin)
			{
				"admin1", "", "newspaper33", "description of newspaper33", "http://www.picture.com", false, IllegalArgumentException.class
			},
			//(10) Negative test: publishing a newspaper not being an user (customer)
			{
				"customer1", "", "newspaper33", "description of newspaper33", "http://www.picture.com", false, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templatePublishNewspaper((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Boolean) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	//Use case 3
	//An actor who is authenticated as an administrator must be able to remove a newspaper that he or she thinks is inappropiate.
	//An actor who is authenticated as an administrator must be able to list the newspapers that contain taboo words
	protected void templateDeleteAdminNewspaper(final String username, final String newspaper, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(username);
			this.newspaperService.getTabooNewspapers();
			this.newspaperService.findPublished();
			this.newspaperService.deleteAdmin(this.newspaperService.findOne((this.getEntityId(newspaper))));
			this.newspaperService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.authenticate(null);
		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverDeleteAdminNewspaper() {
		final Object testingData[][] = {
			//NEGATIVE
			{
				"user1", "newspaper3", IllegalArgumentException.class
			},
			//NEGATIVE
			{
				null, "newspaper3", IllegalArgumentException.class
			},
			//(1) Positive test: Deleting a newspaper correctly
			//TODO caso positivo
			{
				"admin", "newspaper3", null
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteAdminNewspaper((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
}
