
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.NewspaperRepository;
import security.Authority;
import domain.Advertisement;
import domain.Article;
import domain.Newspaper;
import domain.User;

@Service
@Transactional
public class NewspaperService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private NewspaperRepository		newspaperRepository;

	// Services---------------------------------------------------------------

	@Autowired
	private AdminService			adminService;
	@Autowired
	private UserService				userService;
	@Autowired
	private SubscriptionService		subscriptionService;
	@Autowired
	private AdvertisementService	advertisementService;
	@Autowired
	private GlobalService			globalService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private VolumeService			volumeService;
	@Autowired
	private Validator				validator;


	// Constructors -----------------------------------------------------------

	public NewspaperService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Newspaper create() {
		final Newspaper res = new Newspaper();
		final User creator = this.userService.findByPrincipal();

		res.setPublisher(creator);
		res.setArticles(new ArrayList<Article>());
		res.setAdvertisements(new ArrayList<Advertisement>());
		res.setDraft(true);

		return res;
	}

	public Newspaper save(final Newspaper newspaper) {
		Assert.notNull(this.userService.findByPrincipal());
		final Newspaper res = this.newspaperRepository.save(newspaper);
		return res;
	}
	public void publishNewspaper(final Newspaper newspaper) {
		final User userLogged = this.userService.findByPrincipal();
		Assert.isTrue(userLogged.equals(newspaper.getPublisher()), "You are not the publisher");
		Assert.isTrue(newspaper.getDraft(), "This newspaper is already published");
		Assert.isTrue(this.allArticlesPublished(newspaper), "All articles are not published yet");
		newspaper.setPubDate(new Date(System.currentTimeMillis() - 1000));
		newspaper.setDraft(false);
		this.save(newspaper);
	}

	public Newspaper findOne(final int newspaperId) {
		return this.newspaperRepository.findOne(newspaperId);
	}

	public Newspaper findOneToDisplay(final int newspaperId) {
		final Newspaper newspaper = this.newspaperRepository.findOne(newspaperId);
		if (this.actorService.checkAuthenticate() && newspaper != null && !this.actorService.findByPrincipal().equals(newspaper.getPublisher())) {
			final Authority auAdmin = new Authority();
			auAdmin.setAuthority(Authority.ADMIN);
			if (!this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(auAdmin))
				Assert.isTrue(!newspaper.getDraft(), "This newspaper is already published");
		} else if (!this.actorService.checkAuthenticate() && newspaper != null)
			Assert.isTrue(!newspaper.getDraft(), "This newspaper is already published");
		return newspaper;
	}

	public Collection<Newspaper> findAll() {
		return this.newspaperRepository.findAll();
	}

	public void deleteAdmin(final Newspaper newspaper) {
		Assert.notNull(this.adminService.findByPrincipal());
		this.volumeService.deleteNewspaper(newspaper);
		this.subscriptionService.deleteNewspaper(newspaper);
		this.advertisementService.deleteNewspaper(newspaper);
		this.newspaperRepository.delete(newspaper);
	}

	// Other business methods -------------------------------------------------

	// Reconstruct for newspaper
	public Newspaper reconstruct(final Newspaper newspaper, final BindingResult binding) {
		Newspaper result = this.create();
		if (newspaper.getId() != 0)
			result = this.findOne(result.getId());
		result.setTitle(newspaper.getTitle());
		result.setDescription(newspaper.getDescription());
		result.setPicture(newspaper.getPicture());
		result.setIsPrivate(newspaper.getIsPrivate());
		this.validator.validate(result, binding);

		return result;

	}

	//Colección con los newspaper que se pueden mostrar a todos los usuarios
	public Collection<Newspaper> getPublishedNewspapers() {
		final Collection<Newspaper> res = this.newspaperRepository.publishedNewspapers();
		return res;
	}
	public Collection<Newspaper> findByKeyWord(final String keyWord) {
		final Collection<Newspaper> newspapers = this.newspaperRepository.findByKeyWord(keyWord);
		return newspapers;
	}

	public Collection<Newspaper> getTabooNewspapers() {
		Assert.notNull(this.adminService.findByPrincipal());
		Collection<String> tabooWords = this.globalService.getTaboos();
		Collection<Newspaper> newspapersTaboo = new HashSet<Newspaper>();
		for (String tabooWord : tabooWords) {
			newspapersTaboo.addAll(this.newspaperRepository.tabooNewspapers(tabooWord));
		}

		return newspapersTaboo;
	}

	// asda,'%') or n.title LIKE concat ('%',asdad,'%') or n.title LIKE concat ('%',sadas

	public Collection<Newspaper> getMyNewspapers() {
		final Integer userLoggedId = this.userService.findByPrincipal().getId();
		return this.newspaperRepository.myNewspapers(userLoggedId);
	}

	private boolean allArticlesPublished(final Newspaper newspaper) {
		boolean res = true;
		for (final Article ar : newspaper.getArticles())
			if (ar.isDraft()) {
				res = false;
				break;
			}
		return res;
	}

	public Collection<Newspaper> findNotPublished() {
		Collection<Newspaper> res;
		res = this.newspaperRepository.findNotPublished();
		return res;
	}

	public Collection<Newspaper> findNewspapersForVolume() {
		Collection<Newspaper> res;
		res = this.newspaperRepository.findNewspapersForVolume(this.userService.findByPrincipal().getId());
		return res;
	}

	public void flush() {
		this.newspaperRepository.flush();
	}

	//Queries 7.1

	public Double getAverageNewspapersPerUser() {
		this.checkAdmin();
		return this.newspaperRepository.averageNewspapersPerUser();
	}
	private void checkAdmin() {
		Assert.isTrue(this.adminService.findByPrincipal() != null);
	}

	public Double getStandardDeviationNewspapersPerUser() {
		return this.newspaperRepository.standardDesviationNewspapersPerUser();
	}
	public Collection<Newspaper> getNewspaperWithMoreArticlesThanAvgPlus10() {
		return this.newspaperRepository.newspaperWithMoreArticlesThanAveragePlus10percent();
	}
	public Collection<Newspaper> getNewspaperWithLessArticlesThanAvgMinus10() {
		return this.newspaperRepository.newspaperWithLessArticlesThanAverageMinus10percent();
	}

	//Queries 24.1

	public Double getRatioPublicVsPrivateNewspaper() {
		return this.newspaperRepository.ratioPublicVsPrivateNewspaper();
	}
	public Double getAverageArticlesPerPublicNewspaper() {
		return this.newspaperRepository.averageArticlesPerPublicNewspaper();
	}
	public Double getAverageArticlesPerPrivateNewspaper() {
		return this.newspaperRepository.averageArticlesPerPrivateNewspaper();
	}
	public Double getRatioSuscribersPerPrivateNewspapersVsTotalCustomers() {
		return this.newspaperRepository.ratioSuscribersPerPrivateNewspaperVsTotalCostumers();
	}
	public Double getRatioPrivateVsPublicNewspapersPerPublisher() {
		return this.newspaperRepository.ratioPrivateVsPublicNewspapersPerPublisher();
	}

	public Collection<Newspaper> findNewspapersWithAdvertisement(final int id) {
		Collection<Newspaper> res = new HashSet<>();
		res = this.newspaperRepository.findNewspapersWithAdvertisement(id);
		return res;
	}

	public Collection<Newspaper> findNewspapersWithoutAdvertisement(final int id) {
		final Collection<Newspaper> with = new HashSet<>(this.newspaperRepository.findNewspapersWithAdvertisement(id));
		final Collection<Newspaper> res = new HashSet<>(this.newspaperRepository.findNewspapersPublished());
		res.removeAll(with);
		return res;
	}

	public Collection<Newspaper> findPublished() {
		final Collection<Newspaper> res = new HashSet<>(this.newspaperRepository.findNewspapersPublished());

		return res;
	}

	//Querys 2.5.3
	public Double ratioNewspapersWithAdvertisementsVsWithout() {
		return this.newspaperRepository.ratioNewspapersWithAdvertisementsVsWithout();
	}

	//Querys 2.11.1
	public Double averageNewspapersPerVolume() {
		return this.newspaperRepository.averageNewspapersPerVolume();
	}

	public Double ratioSubscriptionsToNewspapersVsVolumes() {
		return this.newspaperRepository.ratioSubscriptionsToNewspapersVsVolumes();
	}
}
