
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.VolumeRepository;
import domain.Newspaper;
import domain.User;
import domain.Volume;
import forms.VolumeForm;

@Service
@Transactional
public class VolumeService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private VolumeRepository	volumeRepository;

	// Services---------------------------------------------------------------

	@Autowired
	private UserService			userService;
	@Autowired
	private Validator			validator;
	@Autowired
	private SubscriptionService	subscriptionService;
	@Autowired
	private NewspaperService	newspaperService;


	// Constructors -----------------------------------------------------------

	public VolumeService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Volume create() {
		final Volume res = new Volume();
		final User creator = this.userService.findByPrincipal();

		res.setPublisher(creator);

		return res;
	}

	public Volume save(final Volume volume) {
		this.checkUser(volume);
		this.checkConcurrency(volume);
		this.checkNewsPublicated(volume);
		final Volume res = this.volumeRepository.save(volume);
		return res;
	}

	public void flush() {
		this.volumeRepository.flush();
	}

	public Volume saveAdmin(final Volume volume) {
		return this.volumeRepository.save(volume);
	}

	public Volume findOne(final int volumeId) {
		return this.volumeRepository.findOne(volumeId);
	}

	public Collection<Volume> findAll() {
		return this.volumeRepository.findAll();
	}

	public void deleteNewspaper(final Newspaper newspaper) {
		final Collection<Volume> volumes = this.findAll();
		for (final Volume volume : volumes) {
			final Collection<Newspaper> newspapers = volume.getNewspapers();
			newspapers.remove(newspaper);
			this.saveAdmin(volume);
		}
	}

	// Other business methods -------------------------------------------------

	public Collection<Newspaper> findNewspapers(final int volumeId) {
		return this.volumeRepository.findNewspapers(volumeId);
	}

	// Reconstruct for volume
	public Volume reconstruct(final VolumeForm volumeForm, final BindingResult binding) {
		final Volume result = this.create();
		result.setId(volumeForm.getId());
		result.setVersion(volumeForm.getVersion());
		result.setTitle(volumeForm.getTitle());
		result.setDescription(volumeForm.getDescription());

		final Date now = new Date();
		final Integer year = this.subscriptionService.obtenerAnyo(now);
		result.setYear(year);
		if (volumeForm.getId() == 0)
			result.setPublisher(this.userService.findByPrincipal());
		else {
			final Volume volumeBBDD = this.volumeRepository.findOne(volumeForm.getId());
			result.setPublisher(volumeBBDD.getPublisher());
		}

		final Collection<Newspaper> newspapers = new ArrayList<Newspaper>();
		if (volumeForm.getNewspapers() != null)
			for (final String newspaperString : volumeForm.getNewspapers()) {
				final Integer newspaperId = new Integer(newspaperString);
				newspapers.add(this.newspaperService.findOne(newspaperId));
			}
		result.setNewspapers(newspapers);

		this.validator.validate(result, binding);

		return result;

	}

	// Private methods

	private void checkUser(final Volume volume) {
		final User logged = this.userService.findByPrincipal();
		Assert.isTrue(volume.getPublisher().equals(logged));
	}
	private void checkConcurrency(final Volume volume) {
		if (volume.getId() != 0) {
			final Volume volumeBBDD = this.findOne(volume.getId());
			Assert.isTrue(volumeBBDD.getVersion() == volume.getVersion());
		}
	}

	private void checkNewsPublicated(final Volume volume) {
		for (final Newspaper n : volume.getNewspapers())
			Assert.isTrue(n.getDraft() == false);
	}

}
