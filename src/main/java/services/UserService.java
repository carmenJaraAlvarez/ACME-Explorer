
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.UserRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Article;
import domain.Chirp;
import domain.Folder;
import domain.Newspaper;
import domain.User;
import domain.Volume;
import forms.ActorForm;
import forms.CreateActorForm;

@Service
@Transactional
public class UserService {

	//managed repository----------------------------------------
	@Autowired
	private UserRepository	userRepository;
	@Autowired
	private ActorService	actorService;

	//Supporting services-----------------------------------------
	@Autowired
	private Validator		validator;


	//Crear usuario
	public User create() {

		final User result = new User();

		result.setFollowers(new ArrayList<User>());
		result.setFollowing(new ArrayList<User>());
		result.setNewspapers(new ArrayList<Newspaper>());
		result.setArticles(new ArrayList<Article>());
		result.setChirps(new ArrayList<Chirp>());
		result.setVolumes(new ArrayList<Volume>());

		return result;
	}

	public Collection<User> findAll() {
		return this.userRepository.findAll();
	}

	public User findOne(final int userId) {
		return this.userRepository.findOne(userId);
	}

	public User save(final User user) {
		this.checkAuthenticate();
		this.checkPrincipal(user);
		this.checkConcurrency(user);
		final User result = this.userRepository.save(user);

		result.setFolders(this.actorService.saveNewFolders(result));

		return this.userRepository.save(result);
	}

	public User saveEdit(final User user) {
		this.checkPrincipal(user);
		this.checkConcurrency(user);

		final User result = this.userRepository.save(user);

		return result;
	}

	// Other business methods -------------------------------------------------

	public String follow(final int userId) {
		String result;
		final User userFollow = this.findOne(userId);
		final User userPrincipal = this.findByPrincipal();
		final Collection<User> users = this.findFollowing(userPrincipal.getId());
		if (users.contains(userFollow))
			result = "false";
		else {
			final Collection<User> followingList = userPrincipal.getFollowing();
			followingList.add(userFollow);
			userPrincipal.setFollowing(followingList);
			this.saveEdit(userPrincipal);
			result = "true";
		}
		return result;
	}

	public String unfollow(final int userId) {
		String result;
		final User userUnfollow = this.findOne(userId);
		final User userPrincipal = this.findByPrincipal();
		final Collection<User> users = this.findFollowing(userPrincipal.getId());
		if (users.contains(userUnfollow)) {
			final Collection<User> followingList = userPrincipal.getFollowing();
			followingList.remove(userUnfollow);
			userPrincipal.setFollowing(followingList);
			this.saveEdit(userPrincipal);
			result = "true";
		} else
			result = "false";
		return result;
	}

	public Collection<Article> articlesPublished(final int userId) {
		return this.userRepository.articlesPublished(userId);
	}

	public Collection<User> findFollowing(final int userId) {
		return this.userRepository.findFollowing(userId);
	}

	public Collection<User> findFollowers(final int userId) {
		return this.userRepository.findFollowers(userId);
	}

	private void checkPrincipal(final User user) {
		if (user.getId() != 0) {
			final User a = this.findByPrincipal();
			Assert.isTrue(user.equals(a));
		}
	}
	private void checkConcurrency(final User user) {
		if (user.getId() != 0) {
			final User a = this.findOne(user.getId());
			Assert.isTrue(user.getVersion() == a.getVersion());
		}
	}

	private void checkAuthenticate() {
		Assert.isTrue(!this.actorService.checkAuthenticate());
	}

	public User findByPrincipal() {
		User result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public User findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		User result;

		result = this.userRepository.findByUserAccount(userAccount.getId());

		return result;
	}
	// Form de User (Se utiliza para la creaci�n de un nuevo usuario) 
	public User reconstruct(final CreateActorForm createActorForm, final BindingResult binding) {
		final User result = createActorForm.getUser();
		final UserAccount userAccount = createActorForm.getUserAccount();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.USER);
		userAccount.getAuthorities().add(authority);
		result.setUserAccount(userAccount);
		result.setFollowers(new ArrayList<User>());
		result.setFollowing(new ArrayList<User>());
		result.setNewspapers(new ArrayList<Newspaper>());
		result.setArticles(new ArrayList<Article>());
		result.setChirps(new ArrayList<Chirp>());
		result.setVolumes(new ArrayList<Volume>());
		result.setFolders(new ArrayList<Folder>());
		if (binding != null) {
			this.validator.validate(userAccount, binding);
			this.validator.validate(result, binding);
		}

		return result;

	}
	// ---- Reconstruct de ActorForm para editar usuarios -------
	public User reconstructUser(final ActorForm actorForm, final BindingResult binding) {
		final User userBBDD = this.findOne(actorForm.getId());
		final User result = actorForm.getUser();
		result.setFollowers(userBBDD.getFollowers());
		result.setFollowing(userBBDD.getFollowing());
		result.setNewspapers(userBBDD.getNewspapers());
		result.setArticles(userBBDD.getArticles());
		result.setChirps(userBBDD.getChirps());
		result.setUserAccount(userBBDD.getUserAccount());
		result.setFolders(userBBDD.getFolders());
		result.setMessagesSent(userBBDD.getMessagesSent());
		result.setMessagesReceived(userBBDD.getMessagesReceived());
		result.setVolumes(userBBDD.getVolumes());
		this.validator.validate(result, binding);

		return result;

	}
	public boolean isMine(final Actor actor) {
		boolean res = false;
		if (this.findByPrincipal().getId() == actor.getId())
			res = true;

		return res;
	}

	public void flush() {
		this.userRepository.flush();
	}

	//Queries 7.1

	public Double getRatioCreatorsNewspapers() {
		return this.userRepository.ratioCreatorsNewspaper();
	}
	public Double getRatioCreatorsArticles() {
		return this.userRepository.ratioCreatorsArticle();
	}

	// Queries 17.1

	public Double getUsersChirpsAbove75Average() {
		return this.userRepository.ratioUsersChirpsAbove75Average();
	}
}
