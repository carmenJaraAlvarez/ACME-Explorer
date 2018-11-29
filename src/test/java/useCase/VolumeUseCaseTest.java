
package useCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.NewspaperService;
import services.UserService;
import services.VolumeService;
import utilities.AbstractTest;
import domain.Newspaper;
import domain.User;
import domain.Volume;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class VolumeUseCaseTest extends AbstractTest {

	//Services used
	@Autowired
	private VolumeService		volumeService;
	@Autowired
	private NewspaperService	newspaperService;
	@Autowired
	private UserService			userService;


	protected void templateCreatesandSaveVolume(final String username, final String title, final String description, final String year, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final User user = this.userService.findByPrincipal();
			final Collection<Volume> volumes = user.getVolumes();

			final Volume volume = this.volumeService.create();

			volume.setTitle(title);
			volume.setDescription(description);
			volume.setNewspapers(new ArrayList<Newspaper>());
			volume.setYear(new Integer(year));

			this.volumeService.save(volume);
			this.volumeService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		this.authenticate(null);
	}
	@Test
	public void driverCreatesandSaveVolume() {
		final Object testingData[][] = {
			//POSITIVE
			{
				"user2", "title", "description", "50", null
			},
			//NEGATIVE: non user
			{
				"customer1", "title", "description", "50", IllegalArgumentException.class
			},
			//NEGATIVE: incorrect year
			{
				"user1", "title", "description", "10", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreatesandSaveVolume((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	//Use case 3
	//An actor who is authenticated as an user must be able to add newspapers to one of his volumes.

	protected void templateAddNewspaper(final String username, final String title, final String description, final String year, final String newspaper, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(username);
			final User user = this.userService.findByPrincipal();
			final Collection<Volume> volumes = user.getVolumes();
			final Newspaper news = this.newspaperService.findOne(this.getEntityId(newspaper));
			final Volume volume = this.volumeService.create();

			final List<Newspaper> newspapers = new ArrayList<Newspaper>();
			newspapers.add(news);

			volume.setTitle(title);
			volume.setDescription(description);
			volume.setNewspapers(newspapers);
			volume.setYear(new Integer(year));

			this.volumeService.save(volume);
			this.volumeService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.authenticate(null);
		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverAddNewspaper() {
		final Object testingData[][] = {
			//POSITIVE
			{
				"user2", "title", "description", "50", "newspaper1", null
			},
			//NEGATIVE: adding non published newspaper
			{
				"user2", "title", "description", "50", "newspaper2", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateAddNewspaper((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}
}
