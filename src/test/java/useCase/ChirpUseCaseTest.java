
package useCase;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ChirpService;
import services.UserService;
import utilities.AbstractTest;
import domain.Chirp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChirpUseCaseTest extends AbstractTest {

	//Services used
	@Autowired
	private ChirpService	chirpService;

	@Autowired
	private UserService		userService;


	//Use case 7
	//A user may post a chirp.
	protected void templateCreateSaveChirp(final String username, final String title, final String description, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(username);
			this.userService.findByPrincipal().getChirps();
			final Chirp c = this.chirpService.create();
			c.setTitle(title);
			c.setDescription(description);
			this.chirpService.save(c);
			this.userService.findByPrincipal().getChirps();
			this.chirpService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverCreateSaveChirp() {
		final Object testingData[][] = {
			//(1) Positive test: Creating and saving a chirp correctly
			{
				"user1", "title33", "description33", null
			},
			//(2) Negative test: Creating and saving a chirp whith blank fields
			{
				"user1", "", "", ConstraintViolationException.class
			},
			//NEGATIVE
			{
				null, "title", "description", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSaveChirp((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	//Use case 8
	//An actor who is authenticated as an administrator must be able to remove a chirp that he or she thinks is inappropiate.
	//An actor who is authenticated as an administrator must be able to list the chirps that contain taboo words
	protected void templateDeleteAdminChirp(final String username, final String chirp, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(username);
			this.chirpService.getTabooChirps();
			this.chirpService.deleteAdmin(this.chirpService.findOne((this.getEntityId(chirp))));
			this.authenticate(null);

			this.chirpService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverDeleteAdminChirp() {
		final Object testingData[][] = {
			//(1) Positive test: Deleting a chirp correctly
			{
				"admin", "chirp1", null
			},
			//(1) Positive test: Deleting a chirp correctly
			{
				"user1", "chirp1", IllegalArgumentException.class
			},
			//NEGATIVE
			{
				null, "chirp1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteAdminChirp((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
}
