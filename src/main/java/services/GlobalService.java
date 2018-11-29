
package services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.GlobalRepository;
import domain.Global;

@Service
@Transactional
public class GlobalService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private GlobalRepository	globalRepository;

	@Autowired
	private AdminService		adminService;


	// Constructors -----------------------------------------------------------

	public GlobalService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public Collection<Global> findAll() {
		Collection<Global> result;

		result = this.globalRepository.findAll();

		return result;
	}

	// Other business methods ---------------------------------------------------
	public Global getGlobal() {
		return (Global) this.findAll().toArray()[0];
	}

	public Collection<String> getTaboos() {
		Collection<String> spam;
		spam = this.getGlobal().getSpamWords();
		return spam;
	}

	public Global findOne(final int globalId) {
		this.checkAdmin();
		return this.globalRepository.findOne(globalId);
	}

	private void checkAdmin() {
		Assert.isTrue(this.adminService.findByPrincipal() != null);
	}

	public Global save(final Global global) {
		Assert.notNull(global);
		Assert.notNull(global.getSpamWords(), "spamWords");
		this.checkAdmin();
		final Set<String> taboo = new HashSet<String>();
		for (final String spam : global.getSpamWords())
			if (spam.trim() != "")
				taboo.add(spam.toLowerCase());

		global.setSpamWords(taboo);

		if (global.getId() != 0)
			Assert.isTrue(this.findOne(global.getId()).getVersion() == (global.getVersion()), "No se puede modificar porque la versión ha cambiado");

		final Global result = this.globalRepository.save(global);
		return result;
	}

	public void flush() {
		this.globalRepository.flush();
	}
}
