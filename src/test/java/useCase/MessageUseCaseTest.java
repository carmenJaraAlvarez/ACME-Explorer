
package useCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.AdminService;
import services.AgentService;
import services.FolderService;
import services.MessageService;
import services.UserService;
import utilities.AbstractTest;
import domain.Actor;
import domain.Admin;
import domain.Agent;
import domain.Folder;
import domain.Message;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MessageUseCaseTest extends AbstractTest {

	@Autowired
	private MessageService	messageService;
	@Autowired
	private FolderService	folderService;
	@Autowired
	private AgentService	agentService;
	@Autowired
	private UserService		userService;
	@Autowired
	private AdminService	adminService;
	@Autowired
	private ActorService	actorService;


	//--------------------------------------------------------------------------

	// Use Case 17
	// An actor who is authenticated must be able to:
	// Exchange messages with other actors 
	// An user logs, lists his/her inbox messages, creates a new one to an agent.
	protected void templateCreatesandSaveMessage(final String username, final String agentname, final String subject, final String body, final String priority, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final User loged = this.userService.findByPrincipal();
			final Collection<Message> messages = this.folderService.findInboxByActor(loged).getMessages();
			final Message message = this.messageService.create();
			final Agent a = this.agentService.findOne(this.getEntityId(agentname));
			final Collection<Actor> actorReceivers = new ArrayList<>();
			actorReceivers.add(a);
			message.setActorReceivers(actorReceivers);
			message.setSubject(subject);
			message.setBody(body);
			message.setPriority(priority);

			this.messageService.saveNewMessage(message);

			this.messageService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		this.authenticate(null);
	}
	@Test
	public void driverCreatesandSaveMessage() {
		final Object testingData[][] = {
			//POSITIVE
			{
				"user2", "agent1", "subject", "body", "LOW", null
			},

			//NEGATIVE (not authenticated )
			{
				null, "agent1", "subject", "body", "LOW", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreatesandSaveMessage((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	// Use Case 18
	// An actor who is authenticated must be able to:
	// Manage his or her message folders, except for the system folders.
	// An user go to his/her inbox messages, creates a new folder in a existing folder and changes where it is.
	protected void templateCreateSaveChangeFolder(final String username, final String foldername, final String fatherfoldername1, final String fatherfoldername2, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final User loged = this.userService.findByPrincipal();
			Collection<Message> messages = this.folderService.findInboxByActor(loged).getMessages();
			final Folder a = this.folderService.findOne(this.getEntityId(fatherfoldername1));
			messages = a.getMessages();
			final Folder newfolder = this.folderService.create();
			newfolder.setFatherFolder(a);
			newfolder.setName(foldername);
			Assert.isTrue(this.folderService.validName(newfolder));
			final Folder saved = this.folderService.save(newfolder);
			this.folderService.flush();
			Assert.isTrue(this.folderService.findAll().contains(saved));
			saved.setFatherFolder(this.folderService.findOne(this.getEntityId(fatherfoldername2)));
			this.folderService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		this.authenticate(null);
	}
	@Test
	public void driverCreateSaveChangeFolder() {
		final Object testingData[][] = {
			//POSITIVE
			{
				"user2", "new folder", "inBoxUser2", "trashBoxUser2", null
			},

			//NEGATIVE (not authenticated)
			{
				null, "new folder", "inBoxUser2", "trashBoxUser2", IllegalArgumentException.class
			},
			//NEGATIVE (existing name)
			{
				"user2", "inBoxUser2", null, "trashBoxUser2", AssertionError.class
			//control en controlador, simulado aqui con assert
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSaveChangeFolder((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	// Use Case 19
	// An actor who is authenticated as an administrator must be able to:
	// Broadcast a message to the actors of the system.
	// An admin logs, list his/her inbox messages, links to new notification and send a message to everybody

	protected void templateCreatesandSaveBroadcast(final String username, final String subject, final String body, final String priority, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final Admin loged = this.adminService.findByPrincipal();
			final Collection<Message> messages = this.folderService.findInboxByActor(loged).getMessages();
			final Message message = this.messageService.create();
			final List<Actor> allActors = new ArrayList<>(this.actorService.findAll());
			message.getActorReceivers().addAll(allActors);
			message.setSubject(subject);
			message.setBody(body);
			message.setPriority(priority);

			this.messageService.newNotification(message);

			this.messageService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		this.authenticate(null);
	}
	@Test
	public void driverCreatesandSaveBroadcast() {
		final Object testingData[][] = {
			//POSITIVE
			{
				"admin", "subject", "body", "LOW", null
			},

			//NEGATIVE (not authenticated )
			{
				null, "subject", "body", "LOW", IllegalArgumentException.class
			},
			//NEGATIVE (not admin)
			{
				"user1", "subject", "body", "LOW", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreatesandSaveBroadcast((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	// Use Case 
	// An actor who is authenticated must be able to:
	// Exchange messages with other actors and manage them, which includes deleting and
	// moving them from one folder to another folder.
	// An user logs lists his/her inbox messages, moves one to trashbox and deletes it.
	protected void templateListMoveDeleteMessage(final String username, final String msg, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final User loged = this.userService.findByPrincipal();
			final ArrayList<Message> messages = new ArrayList<>(this.folderService.findInboxByActor(loged).getMessages());
			final Message message = this.messageService.findOne(this.getEntityId(msg));
			final Folder a = this.folderService.findInboxByActor(loged);
			final Folder f = this.folderService.findTrashBoxByActor(loged);
			this.messageService.changeFolder(message, a, f);
			final Collection<Message> messages2 = f.getMessages();
			this.messageService.flush();
			this.messageService.delete(message, f);
			Assert.isTrue(!f.getMessages().contains(message));
			this.messageService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		this.authenticate(null);
	}
	@Test
	public void driverListMoveDeleteMessage() {
		final Object testingData[][] = {

			//NEGATIVE (not authenticated )
			{
				null, "message1", IllegalArgumentException.class
			},
			//NEGATIVE (not his/her message )
			{
				"user1", "message1", IllegalArgumentException.class
			},
			//POSITIVE
			{
				"user3", "message1", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListMoveDeleteMessage((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// An actor who is authenticated must be able to:
	// Manage his or her message folders, except for the system folders
	// An user logs, lists his/her inbox message, browses the folders and selects one to delete.
	protected void templateListDeleteFolder(final String username, final String foldername, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final User loged = this.userService.findByPrincipal();
			final Collection<Message> messages = this.folderService.findInboxByActor(loged).getMessages();
			final Folder f = this.folderService.findOne(this.getEntityId(foldername));
			this.folderService.delete(f);
			this.folderService.flush();
			Assert.isTrue(!loged.getFolders().contains(f));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		this.authenticate(null);
	}
	@Test
	public void driverListDeleteFolder() {
		final Object testingData[][] = {

			//NEGATIVE (not authenticated)
			{
				null, "customFolderUser1", IllegalArgumentException.class
			},
			//NEGATIVE (system folder)
			{
				"user1", "inBoxUser2", IllegalArgumentException.class

			},
			//POSITIVE
			{
				"user1", "customFolderUser1", null
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListDeleteFolder((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
}
