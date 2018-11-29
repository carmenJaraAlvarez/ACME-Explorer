
package useCase;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.ChirpService;
import services.UserService;
import utilities.AbstractTest;
import domain.User;
import forms.CreateActorForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class UserUseCaseTest extends AbstractTest {

	@Autowired
	private UserService		userService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private ChirpService	chirpService;


	//--------------------------------------------------------------------------

	//Use case 1: An unauthenticated person sign in as User
	protected void templateSignIn(final String username, final String password, final String name, final String surname, final String email, final boolean accept, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final CreateActorForm reg = new CreateActorForm();
			reg.setUsername(username);
			//simulation binding
			Assert.isTrue(username != "");
			reg.setPassword(password);
			reg.setEmail(email);
			reg.setName(name);
			reg.setSurname(surname);
			reg.setValida(accept);
			User user = null;
			user = this.userService.reconstruct(reg, null);

			this.userService.flush();
			this.userService.save(user);
			this.userService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	@Test
	public void driverSignInUser() {
		final Object testingData[][] = {
			//POSITIVE
			{
				"user200", "user200", "user200", "user200 surname", "user200@gmail.com", true, null
			},
			//NEGATIVE
			{
				"user201", "user201", "user201", "surname", "", true, javax.validation.ConstraintViolationException.class
			},
			//NEGATIVE //simulation binding in form
			{
				"", "user201", "user201", "surname", "user@mail.com", true, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSignIn((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Boolean) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	//Use case 9
	//An actor who is authenticated as a user must be able to follow another user
	//An actor who is authenticated as a user must be able to list the users who he or she follows
	//An actor who is authenticated as a user must be able to list the users who follow him or her.
	//An actor who is authenticated as a user must be able to display a stream with the chirps posted by all of the users that he or she follows.
	protected void templateFollow(final String username, final String username2, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		this.authenticate(username);
		try {
			this.userService.findFollowers(this.getEntityId(username));
			Assert.isTrue(this.userService.follow(this.getEntityId(username2)).equals("true"));
			this.userService.findFollowing(this.getEntityId(username));
			this.chirpService.getChirpsPeopleFollowing();
			this.userService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.authenticate(null);
		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverFollow() {
		final Object testingData[][] = {
			//Positive test
			{
				"user1", "user3", null
			},
			//Negative test: Following someone you already follow
			{
				"user1", "user2", IllegalArgumentException.class
			},
			//Negative test: Admin trying to follow some user.
			{
				"admin", "user2", AssertionError.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFollow((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	//Use case 10
	//An actor who is authenticated as a user must be able to unfollow another user
	//An actor who is authenticated as a user must be able to list the users who he or she follows
	protected void templateUnfollow(final String username, final String username2, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		this.authenticate(username);
		try {
			this.userService.findFollowing(this.getEntityId(username));
			Assert.isTrue(this.userService.unfollow(this.getEntityId(username2)).equals("true"));
			this.userService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.authenticate(null);
		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverUnfollow() {
		final Object testingData[][] = {
			//Positive test
			{
				"user1", "user2", null
			},
			//Negative test: Following someone you already follow
			{
				"user1", "user3", IllegalArgumentException.class
			},
			//Negative test: Admin trying unfollow someone
			{
				"admin", "user3", AssertionError.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateUnfollow((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

}
