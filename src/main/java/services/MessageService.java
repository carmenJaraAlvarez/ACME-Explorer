
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MessageRepository;
import domain.Actor;
import domain.Folder;
import domain.Message;

@Service
@Transactional
public class MessageService {

	@Autowired
	private MessageRepository	messageRepository;

	//´-----------------------------------
	@Autowired
	private ActorService		actorService;
	@Autowired
	private GlobalService		globalService;
	@Autowired
	private FolderService		folderService;


	public MessageService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Message create() {
		final Message res = new Message();
		final Date ahora = new Date();
		res.setSendMoment(ahora);
		final List<Actor> receivers = new ArrayList<>();
		res.setActorReceivers(receivers);
		final Actor sender = this.actorService.findByPrincipal();
		res.setActorSender(sender);
		final List<Folder> folders = new ArrayList<>();
		res.setFolders(folders);
		final List<Folder> senderFolders = new ArrayList<>(sender.getFolders());
		Folder f = null;
		for (final Folder fol : senderFolders)
			//System.out.print(fol.getName());
			if (fol.getName().equals("out box"))
				f = fol;

		//	System.out.print("*******************"+f.getName());
		res.getFolders().add(f);
		return res;
	}
	public Message createNotificationChanged() {
		final Message res = new Message();
		final Date ahora = new Date();
		res.setSendMoment(ahora);
		final List<Actor> receivers = new ArrayList<>();
		res.setActorReceivers(receivers);
		final List<Folder> folders = new ArrayList<>();
		res.setFolders(folders);
		final Actor administrador = this.actorService.findAdmin();
		res.setActorSender(administrador);
		return res;
	}

	public Collection<Message> findAll() {
		return this.messageRepository.findAll();
	}

	public Message findOne(final int id) {
		return this.messageRepository.findOne(id);
	}

	public void changeFolder(final Message m, final Folder a, final Folder b) {
		Assert.isTrue(this.actorService.findByPrincipal().equals(a.getActor()) && this.actorService.findByPrincipal().equals(b.getActor()));
		Assert.isTrue(a.getMessages().contains(m), "mensaje no en carpeta de origen");
		b.getMessages().add(m);
		a.getMessages().remove(m);
		m.getFolders().add(b);
		m.getFolders().remove(a);
	}

	public Message newMessageControl(final Message e) {
		assert e != null;

		final List<String> spamwords = new ArrayList<String>(this.globalService.getTaboos());
		boolean isSpam = false;
		for (final String s : spamwords)
			if (e.getBody().contains(s)) {
				isSpam = true;
				for (final Actor actor : e.getActorReceivers())
					if (actor.getId() != e.getActorSender().getId()) {

						final Message msgActualizado = this.messageRepository.findOne(e.getId());
						for (final Folder f : actor.getFolders())
							if (f.getName().equals("spam box"))
								msgActualizado.getFolders().add(f);
					}
				break;
			}
		if (isSpam == false)
			for (final Actor actor : e.getActorReceivers()) {
				if (actor.getId() != e.getActorSender().getId()) {

					final Folder inbox = this.folderService.findInboxByActor(actor);
					e.getFolders().add(inbox);
				}
				this.actorService.findOne(e.getActorSender().getId());
			}

		return this.messageRepository.findOne(e.getId());
	}
	public Message newMessageControl2(final Message e) {
		assert e != null;

		//		Actor actualizado = null;
		final List<String> spamwords = new ArrayList<String>(this.globalService.getTaboos());
		boolean isSpam = false;
		for (final String s : spamwords)
			if (e.getBody().contains(s)) {
				isSpam = true;
				//				actualizado = this.actorService.findOne(e.getActorSender().getId());
				//				actualizado.setSuspicious(true);
				for (final Actor actor : e.getActorReceivers())
					if (actor.getId() != e.getActorSender().getId())
						actor.getMessagesReceived().add(e);
				break;
			}
		if (isSpam == false)
			for (final Actor actor : e.getActorReceivers())
				if (actor.getId() != e.getActorSender().getId())
					actor.getMessagesReceived().add(e);

		return this.messageRepository.findOne(e.getId());
	}
	public Message saveNewMessage(final Message e) {
		assert e != null;
		Assert.isTrue(this.actorService.findByPrincipal().equals(e.getActorSender()), "No autorizado");
		Assert.notNull(e.getSubject(), "Subject is null");
		Assert.notNull(e.getBody(), "Body is null");
		Assert.isTrue(e.getPriority().equals("HIGH") || e.getPriority().equals("LOW") || e.getPriority().equals("NEUTRAL"), "Error priority");
		Assert.notEmpty(e.getActorReceivers(), "Receivers is empty");
		Assert.notNull(e.getActorSender(), "Sender is null");
		Assert.notNull(e.getFolders(), "Folders is null");
		Assert.notNull(e.getSendMoment(), "Error date");
		Assert.isTrue(this.actorService.findByPrincipal().equals(e.getActorSender()), "No autorizado");

		return this.messageRepository.saveAndFlush(e);
	}
	public Message newNotification(final Message e) {
		Assert.isTrue(this.actorService.findByPrincipal().equals(e.getActorSender()), "No autorizado");
		assert e != null;
		Assert.notNull(e.getSubject(), "Subject is null");
		Assert.notNull(e.getBody(), "Body is null");
		Assert.isTrue(e.getPriority().equals("HIGH") || e.getPriority().equals("LOW") || e.getPriority().equals("NEUTRAL"), "Error priority");
		Assert.notEmpty(e.getActorReceivers(), "Receivers is empty");
		Assert.notNull(e.getActorSender(), "Sender is null");
		Assert.notNull(e.getFolders(), "Folders is null");
		final Date now = new Date();
		e.setSendMoment(now);
		Assert.notNull(e.getSendMoment(), "Error date");
		Assert.isTrue(this.actorService.findByPrincipal().equals(e.getActorSender()));

		final List<String> spamwords = new ArrayList<String>(this.globalService.getTaboos());

		for (final String s : spamwords)
			if (e.getBody().contains(s))
				for (final Actor actor : e.getActorReceivers()) {
					actor.getMessagesReceived().add(e);
					for (final Folder f : actor.getFolders())
						if (f.getName().equals("spam box")) {
							e.getFolders().add(f);
							break;
						}
				}
			else
				for (final Actor actor : e.getActorReceivers()) {
					actor.getMessagesReceived().add(e);
					final Folder nBox = this.folderService.findNotificationBoxByActor(actor);
					e.getFolders().add(nBox);
				}
		return this.messageRepository.saveAndFlush(e);
	}

	public void delete(final Message e, final Folder f) {
		assert e.getId() != 0;

		final Actor log = this.actorService.findByPrincipal();

		final Actor fol = f.getActor();
		Assert.isTrue(fol.equals(log), "No autorizado");
		Assert.isTrue(f.getName().equals("trash box"));
		Assert.isTrue(this.actorService.findByPrincipal().equals(f.getActor()));
		f.getMessages().remove(e);
		e.getFolders().remove(f);
		//elimina si nadie más lo tiene 
		if (e.getFolders().isEmpty()) {
			this.messageRepository.delete(e.getId());
			Assert.isTrue(!this.messageRepository.findAll().contains(e));
		}

		Assert.isTrue(!f.getMessages().contains(e));
		Assert.isTrue(!e.getFolders().contains(f));
	}

	public Collection<Message> findByFolder(final Integer folderId) {
		Collection<Message> res;
		final Folder folder = this.folderService.findOne(folderId);
		final Actor logeado = this.actorService.findByPrincipal();
		Assert.isTrue(folder.getActor() == logeado);
		res = folder.getMessages();
		return res;
	}

	public Message notificationChanged2(final Message e) {//introduce manager come receptor
		//System.out.print(e.getActorReceivers().size());
		for (final Actor actor : e.getActorReceivers()) {
			actor.getMessagesReceived().add(e);
			final Folder nBox = this.folderService.findNotificationBoxByActor(actor);
			//System.out.print(nBox.getActor().getName());
			e.getFolders().add(nBox);
		}
		return this.messageRepository.saveAndFlush(e);
	}
	public String receiversToString(final Message msg) {
		Assert.notNull(msg);
		final List<Actor> l = new ArrayList<>(msg.getActorReceivers());
		String s = l.get(0).getName();
		l.remove(0);
		if (!l.isEmpty())
			for (final Actor a : l)
				s = s + ", " + a.getName();
		return s;
	}

	public void flush() {
		this.messageRepository.flush();

	}

	//	public Message notificationChangedManager(final String subject, final String body, final Application application) {
	//		final Message m = this.notificationChangedManager1(subject, body, application);
	//		final Message m2 = this.notificationChanged2(m);
	//
	//		return m2;
	//	}
	//	public Message notificationChangedManager1(final String subject, final String body, final Application application) {//crea mensaje con sender manager y receptor explorer
	//		final Message e = this.createNotificationChanged();
	//		e.setSubject(subject);
	//		e.setBody(body);
	//		final Actor receptor1 = this.actorService.findByPrincipal();
	//		final Actor receptor2 = application.getExplorer();
	//
	//		e.getActorReceivers().add(receptor1);
	//		e.getActorReceivers().add(receptor2);
	//		//System.out.print(e.getActorReceivers().size());
	//		return this.messageRepository.saveAndFlush(e);
	//	}
	//
	//	//para secuenciar
	//	public Message notificationChangedExplorer(final String subject, final String body, final Application application) {
	//		final Message m = this.notificationChangedExplorer1(subject, body, application);
	//		final Message m2 = this.notificationChanged2(m);
	//
	//		return m2;
	//	}
	//	public Message notificationChangedExplorer1(final String subject, final String body, final Application application) {//crea mensaje con sender manager y receptor explorer
	//		final Message e = this.createNotificationChanged();
	//		e.setSubject(subject);
	//		e.setBody(body);
	//		final Actor receptor1 = this.actorService.findByPrincipal();
	//		final Actor receptor2 = application.getManager();
	//		e.getActorReceivers().add(receptor1);
	//		e.getActorReceivers().add(receptor2);
	//		return this.messageRepository.saveAndFlush(e);
	//	}

}
