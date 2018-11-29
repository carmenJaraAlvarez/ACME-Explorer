
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdvertisementRepository;
import domain.Admin;
import domain.Advertisement;
import domain.Agent;
import domain.Newspaper;

@Service
@Transactional
public class AdvertisementService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private AdvertisementRepository	advertisementRepository;

	// Services ---------------------------------------------------------------
	@Autowired
	private AgentService			agentService;
	@Autowired
	private Validator				validator;
	@Autowired
	private GlobalService			globalService;
	@Autowired
	private AdminService			adminService;


	// Constructors -----------------------------------------------------------

	public AdvertisementService() {
		super();
	}
	// Simple CRUD methods ----------------------------------------------------

	public Advertisement findOne(final int advertisementId) {
		return this.advertisementRepository.findOne(advertisementId);
	}

	public Collection<Advertisement> findAll() {
		return this.advertisementRepository.findAll();
	}

	// Other business methods -------------------------------------------------

	public void flush() {
		this.advertisementRepository.flush();
	}

	public Advertisement create() {
		Advertisement res;
		res = new Advertisement();
		final Agent agent = this.agentService.findByPrincipal();
		res.setAgent(agent);
		return res;
	}

	public Advertisement findOneToEdit(final int advertisementId) {
		Advertisement res;
		res = this.findOne(advertisementId);
		this.agentService.checkPrincipal(res.getAgent());
		return res;
	}

	public void save(final Advertisement advertisement) {
		this.agentService.checkPrincipal(advertisement.getAgent());
		this.advertisementRepository.save(advertisement);

	}
	public String checkConcurrence(final Advertisement advertisement) {
		String s = null;
		if (advertisement.getId() != 0) {
			final Advertisement advertisementBD = this.advertisementRepository.findOne(advertisement.getId());
			if (advertisement.getVersion() != advertisementBD.getVersion())
				s = "advertisement.concurrency.error";
		}
		return s;
	}

	public Collection<Advertisement> findByKeyWord(final String keyWord) {
		final Collection<Advertisement> advertisements = this.advertisementRepository.findByKeyWord(keyWord);
		return advertisements;
	}

	public Advertisement reconstruct(final Advertisement advertisement, final BindingResult binding) {
		Advertisement result;
		if (advertisement.getId() == 0) {
			result = this.create();
			result.setTitle(advertisement.getTitle());
			result.setBanner(advertisement.getBanner());
			result.setTargetPage(advertisement.getTargetPage());
			//validar creditcard
			result.setCreditCard(advertisement.getCreditCard());
			result.setNewspaper(advertisement.getNewspaper());

		} else {
			final Advertisement origin = this.findOneToEdit(advertisement.getId());
			result = advertisement;
			advertisement.setAgent(origin.getAgent());
		}

		this.validator.validate(result, binding);
		return result;
	}

	public Collection<Advertisement> getTabooAdvertisements() {
		Assert.notNull(this.adminService.findByPrincipal());
		final Collection<String> tabooWords = this.globalService.getTaboos();
		final Collection<Advertisement> advertisementTaboo = new HashSet<Advertisement>();
		for (final String tabooWord : tabooWords)
			advertisementTaboo.addAll(this.advertisementRepository.tabooAdvertisement(tabooWord));

		return advertisementTaboo;
	}

	public void deleteNewspaper(final Newspaper newspaper) {
		for (final Advertisement a : newspaper.getAdvertisements())
			this.deleteAdmin(a);
	}

	public void deleteAdmin(final Advertisement advertisement) {
		this.checkAdmin();
		this.advertisementRepository.delete(advertisement);

	}

	private void checkAdmin() {
		final Admin admin = this.adminService.findByPrincipal();
		Assert.notNull(admin, "No admin");
	}

	//Query 2.5.3
	public Double ratioAdvertisementsTabooVsTotal() {
		Double res = 0.;
		final Double all = (double) this.findAll().size();
		final Double taboos = (double) this.getTabooAdvertisements().size();
		if (all > 0)
			res = taboos / all;

		return res * 100.;

	}

	public Advertisement random(final Newspaper newspaper) {
		Advertisement res = null;
		final ArrayList<Advertisement> all = new ArrayList<>(newspaper.getAdvertisements());
		final int tam = all.size();
		if (tam > 0) {
			final Random rn = new Random();
			final int i = rn.nextInt(tam);
			//int i = (int) Math.floor(Math.random() * (all.size()));
			res = all.get(i);
		}
		return res;
	}

}
