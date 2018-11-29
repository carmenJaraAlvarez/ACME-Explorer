
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	private Collection<User>		followers;
	private Collection<User>		following;
	private Collection<Newspaper>	newspapers;
	private Collection<Article>		articles;
	private Collection<Chirp>		chirps;
	private Collection<Volume>		volumes;


	@Valid
	@NotNull
	@ManyToMany(mappedBy = "following")
	public Collection<User> getFollowers() {
		return this.followers;
	}

	public void setFollowers(final Collection<User> followers) {
		this.followers = followers;
	}

	@Valid
	@NotNull
	@ManyToMany
	public Collection<User> getFollowing() {
		return this.following;
	}

	public void setFollowing(final Collection<User> following) {
		this.following = following;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "publisher")
	public Collection<Newspaper> getNewspapers() {
		return this.newspapers;
	}

	public void setNewspapers(final Collection<Newspaper> newspapers) {
		this.newspapers = newspapers;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "publisher")
	public Collection<Volume> getVolumes() {
		return this.volumes;
	}

	public void setVolumes(final Collection<Volume> volumes) {
		this.volumes = volumes;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "writer")
	public Collection<Article> getArticles() {
		return this.articles;
	}

	public void setArticles(final Collection<Article> articles) {
		this.articles = articles;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "user")
	public Collection<Chirp> getChirps() {
		return this.chirps;
	}

	public void setChirps(final Collection<Chirp> chirps) {
		this.chirps = chirps;
	}

}
