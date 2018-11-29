
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.FolderRepository;
import domain.Actor;
import domain.Folder;
import domain.Message;

@Service
@Transactional
public class FolderService {

	//Managed respository -------------------------------------

	@Autowired
	private FolderRepository	folderRepository;

	//Supporting services ------------------------------------
	@Autowired
	private ActorService		actorService;
	@Autowired
	private MessageService		messageService;


	//Constructors
	public FolderService() {
		super();
	}

	//Simple CRUD methods ------------------------------------

	public Folder create(final Folder father) {
		//father puede ser null
		final Collection<Folder> childFolders = new ArrayList<Folder>();
		final Collection<Message> messages = new ArrayList<Message>();
		final Actor propietario = this.actorService.findByPrincipal();
		final Folder res = new Folder();

		res.setActor(propietario);
		res.setChildFolders(childFolders);
		res.setMessages(messages);
		if (father != null) {
			Assert.isTrue(father.getActor() == propietario, "carpeta padre no pertenece");
			res.setFatherFolder(father);
		}
		res.setOfTheSystem(false);
		return res;
	}

	public Folder create(final Actor actor, final String name) {

		final Collection<Folder> childFolders = new ArrayList<Folder>();
		final Collection<Message> messages = new ArrayList<Message>();

		final Folder res = new Folder();
		res.setActor(actor);
		res.setName(name);
		res.setChildFolders(childFolders);
		res.setMessages(messages);

		return res;
	}

	public Folder findOne(final int folderId) {
		return this.folderRepository.findOne(folderId);
	}

	public Folder findOneToEdit(final int folderId) {
		final Folder folder = this.folderRepository.findOne(folderId);
		this.checkPrincipal(folder);
		return folder;
	}

	public Folder save(final Folder folder) {

		Assert.notNull(folder, "Folder required");
		Assert.notNull(folder.getName(), "Name of Folder required");
		Assert.notNull(folder.getOfTheSystem(), "getOfTheSystem of Folder required");
		Assert.notNull(folder.getChildFolders(), "ChildFolders of Folder required");
		Assert.notNull(folder.getMessages(), "Messages of Folder required");
		Assert.notNull(folder.getActor(), "Actor of Folder required");

		Folder resFolder;
		resFolder = this.folderRepository.save(folder);
		return resFolder;

	}
	public void delete(final Folder folder) {
		Assert.notNull(folder);
		Assert.isTrue(!folder.getOfTheSystem(), "carpeta del sistema");
		Assert.isTrue(folder.getActor() == this.actorService.findByPrincipal(), "No es propietario");
		//mensajes a papelera
		Folder papelera = null;
		papelera = this.folderRepository.trashBox(folder.getActor().getId());
		Assert.isTrue(papelera != null, "no localizada papelera");
		if (!folder.getMessages().isEmpty())
			for (final Message msg : folder.getMessages())
				this.messageService.changeFolder(msg, folder, papelera);
		Collection<Folder> hijas = folder.getChildFolders();
		//comprobación carpetas hijas
		if (hijas.size() > 0) {
			for (Folder f : hijas) {
				delete(f);
			}
		}

		this.folderRepository.delete(folder);
	}

	//Complex methods ----------------------------------------

	public Collection<Folder> findByActor(final Actor actor) {
		return this.folderRepository.findByActorId(actor.getId());
	}

	public Collection<Folder> findByPrincipal() {
		Collection<Folder> res;
		final Actor a = this.actorService.findByPrincipal();
		res = this.folderRepository.findByActorId(a.getId());
		return res;
	}

	public Folder create() {
		final Actor actor = this.actorService.findByPrincipal();
		final Collection<Folder> childFolders = new ArrayList<Folder>();
		final Collection<Message> messages = new ArrayList<Message>();
		final Folder res = new Folder();
		res.setActor(actor);
		res.setChildFolders(childFolders);
		res.setMessages(messages);

		return res;
	}
	public Collection<Folder> primerNivel() {
		Collection<Folder> res;
		final Actor actor = this.actorService.findByPrincipal();
		res = this.folderRepository.primerNivel(actor.getId());
		return res;
	}
	public Folder inbox() {
		final Actor actor = this.actorService.findByPrincipal();
		Folder res;
		res = this.folderRepository.inbox(actor.getId());
		return res;
	}
	public Folder findInboxByActor(final Actor actor) {
		Folder res;
		res = this.folderRepository.inbox(actor.getId());
		return res;
	}

	public Folder findNotificationBoxByActor(final Actor actor) {
		Folder res;
		res = this.folderRepository.notification(actor.getId());
		return res;
	}

	public Folder findTrashBoxByActor(final Actor actor) {
		Folder res;
		res = this.folderRepository.trashBox(actor.getId());
		return res;
	}

	public Folder findActual(final Message messageToEdit) {
		Folder res = null;
		final Collection<Folder> folthers = this.actorService.findByPrincipal().getFolders();
		for (final Folder f : folthers)
			if (messageToEdit.getFolders().contains(f)) {
				res = f;
				break;
			}
		return res;
	}

	public List<Folder> findAll() {
		return this.folderRepository.findAll();
	}
	public String ubicacion(final Folder f) {
		String res = "/";
		final Folder aux = f;
		while (aux.getFatherFolder() != null)
			res = "/" + aux.getFatherFolder().getName() + res;
		return res;

	}
	public List<Folder> findRest(final Folder folder) {
		final Collection<Folder> all = this.findByActor(folder.getActor());
		all.remove(folder);
		final List<Folder> res = new ArrayList<>(all);
		return res;

	}
	public boolean validName(final Folder folder) {
		boolean res = true;
		Collection<Folder> all = null;
		if (folder.getFatherFolder() == null) {
			all = this.primerNivel();
			for (final Folder f : all)
				if (f.getName().equals(folder.getName())) {
					res = false;
					break;
				}

		} else {
			all = this.findAll();
			for (final Folder f : all)
				if (f.getName().equals(folder.getName()) && f.getFatherFolder().getId() == folder.getFatherFolder().getId()) {
					res = false;
					break;
				}
		}
		return res;
	}

	public String checkConcurrence(final Folder folder) {
		String s = null;
		if (folder.getId() != 0) {
			final Folder folderBD = this.folderRepository.findOne(folder.getId());
			if (folder.getVersion() != folderBD.getVersion())
				s = "folder.concurrency.error";
		}
		return s;
	}

	protected void checkPrincipal(final Folder folder) {
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(folder.getActor().equals(actor));
	}

	public void flush() {
		this.folderRepository.flush();
	}
}
