
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Folder;
import forms.CreateActorForm;

@Service
@Transactional
public class ActorService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ActorRepository	actorRepository;

	@Autowired
	private FolderService	folderService;


	// Constructors -----------------------------------------------------------

	public ActorService() {
		super();
	}
	// Simple CRUD methods ----------------------------------------------------

	public Actor findOne(final int actorId) {
		return this.actorRepository.findOne(actorId);
	}

	public Collection<Actor> findAll() {
		return this.actorRepository.findAll();
	}

	// Other business methods -------------------------------------------------

	public Actor findByPrincipal() {
		Actor result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public boolean checkAuthenticate() {
		final boolean res = true;
		try {
			this.findByPrincipal();
		} catch (final Throwable oops) {
			return false;
		}
		return res;
	}

	public boolean checkPassword(final CreateActorForm createActorForm) {
		boolean result = false;
		if (createActorForm.getPassword().equals(createActorForm.getPassword2()))
			result = true;
		return result;
	}

	public Actor findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		return this.actorRepository.findByUserAccount(userAccount.getId());
	}

	public Actor findAdmin() {
		final List<Actor> actores = this.actorRepository.findAll();
		Actor res = null;
		final Authority compara = new Authority();
		compara.setAuthority(Authority.ADMIN);
		for (final Actor a : actores)
			if (a.getUserAccount().getAuthorities().contains(compara)) {
				res = a;
				break;
			}
		return res;
	}

	public Actor save(final Actor a) {
		final Actor actor = this.actorRepository.save(a);
		return actor;
	}

	public Collection<Folder> saveNewFolders(final Actor a) {
		Assert.notNull(a, "Actor Required");
		final Collection<Folder> folders = new ArrayList<Folder>();
		final Folder folderIn = this.folderService.create(a, "in box");
		final Folder folderOu = this.folderService.create(a, "out box");
		final Folder folderNo = this.folderService.create(a, "notification box");
		final Folder folderTr = this.folderService.create(a, "trash box");
		final Folder folderSp = this.folderService.create(a, "spam box");
		folderIn.setOfTheSystem(true);
		folderOu.setOfTheSystem(true);
		folderNo.setOfTheSystem(true);
		folderTr.setOfTheSystem(true);
		folderSp.setOfTheSystem(true);
		folders.add(folderIn);
		folders.add(folderOu);
		folders.add(folderNo);
		folders.add(folderTr);
		folders.add(folderSp);
		a.setFolders(folders);
		final Collection<Folder> folders2 = new ArrayList<Folder>();
		Assert.notEmpty(a.getFolders(), "System folders required");
		for (final Folder f : a.getFolders()) {
			f.setActor(a);
			folders2.add(this.folderService.save(f));
		}
		return folders2;
	}
}
