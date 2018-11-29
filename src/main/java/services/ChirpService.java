
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ChirpRepository;
import domain.Chirp;
import domain.User;

@Service
@Transactional
public class ChirpService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ChirpRepository	chirpRepository;

	// Services

	@Autowired
	private UserService		userService;

	@Autowired
	private AdminService	adminService;

	@Autowired
	private GlobalService	globalService;

	//Supporting services-----------------------------------------
	@Autowired
	private Validator		validator;


	// Constructors -----------------------------------------------------------

	public ChirpService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public Chirp create() {
		final Chirp res = new Chirp();
		res.setPubMoment(new Date(System.currentTimeMillis() - 1000));
		res.setUser(this.userService.findByPrincipal());
		return res;
	}

	public Chirp save(final Chirp c) {
		this.checkPrincipal(c);
		c.setPubMoment(new Date(System.currentTimeMillis() - 1000));
		final Chirp res = this.chirpRepository.save(c);
		return res;
	}

	public Chirp findOne(final int chirpId) {
		return this.chirpRepository.findOne(chirpId);
	}

	public Collection<Chirp> findAll() {
		return this.chirpRepository.findAll();
	}

	public void delete(final Chirp c) {
		this.chirpRepository.delete(c);
	}

	public Collection<Chirp> findByKeyWord(final String keyWord) {
		final Collection<Chirp> chirps = this.chirpRepository.findByKeyWord(keyWord);
		return chirps;
	}

	// Other business methods -------------------------------------------------

	private void checkPrincipal(final Chirp c) {
		User logged = null;
		logged = this.userService.findByPrincipal();
		Assert.isTrue(c.getUser().equals(logged));
	}

	// Reconstruct for chirp
	public Chirp reconstruct(final Chirp chirp, final BindingResult binding) {
		final Chirp result = this.create();
		result.setTitle(chirp.getTitle());
		result.setDescription(chirp.getDescription());
		this.validator.validate(result, binding);

		return result;

	}

	//Chirps from people I followed (people following)
	public Collection<Chirp> getChirpsPeopleFollowing() {
		final User logged = this.userService.findByPrincipal();
		final Collection<Chirp> res = new HashSet<Chirp>();
		for (final User follow : logged.getFollowing())
			res.addAll(follow.getChirps());
		return res;
	}

	public Collection<Chirp> getTabooChirps() {
		Assert.notNull(this.adminService.findByPrincipal());
		Collection<String> tabooWords = this.globalService.getTaboos();
		Collection<Chirp> chirpsTaboo = new HashSet<Chirp>();
		for (String tabooWord : tabooWords) {
			chirpsTaboo.addAll(this.chirpRepository.tabooChirps(tabooWord));
		}

		return chirpsTaboo;
	}

	public void deleteAdmin(final Chirp chirp) {
		Assert.notNull(this.adminService.findByPrincipal());
		this.delete(chirp);
	}

	public void flush() {
		this.chirpRepository.flush();
	}

	//Queries 17.1

	public Double getAverageChirpsPerUser() {
		return this.chirpRepository.averageChirpsPerUser();
	}
	public Double getStandardDeviationChirpsPerUser() {
		return this.chirpRepository.standardDeviationChirpsPerUser();
	}

}
