
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "title,body,newspaper_id")
})
public class Article extends DomainEntity {

	@SafeHtml
	private String				title;
	private Date				pubMoment;
	@SafeHtml
	private String				summary;
	@SafeHtml
	private String				body;
	private Collection<String>	pictures;
	private boolean				draft;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@DateTimeFormat(pattern = "MM/dd/yyyy HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPubMoment() {
		return this.pubMoment;
	}

	public void setPubMoment(final Date pubMoment) {
		this.pubMoment = pubMoment;
	}

	@NotBlank
	public String getSummary() {
		return this.summary;
	}

	public void setSummary(final String summary) {
		this.summary = summary;
	}

	@NotBlank
	public String getBody() {
		return this.body;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	@NotNull
	@ElementCollection
	public Collection<String> getPictures() {
		return this.pictures;
	}

	public void setPictures(final Collection<String> pictures) {
		this.pictures = pictures;
	}

	public boolean isDraft() {
		return this.draft;
	}

	public void setDraft(final boolean draft) {
		this.draft = draft;
	}


	/*-- Relaciones --*/

	private User				writer;
	private Article				father;
	private Collection<Article>	followUps;
	private Newspaper			newspaper;


	@Valid
	@ManyToOne(optional = false)
	@NotNull
	public User getWriter() {
		return this.writer;
	}

	public void setWriter(final User writer) {
		this.writer = writer;
	}

	@Valid
	@ManyToOne(optional = true)
	public Article getFather() {
		return this.father;
	}

	public void setFather(final Article father) {
		this.father = father;
	}

	@NotNull
	@Valid
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "father")
	public Collection<Article> getFollowUps() {
		return this.followUps;
	}

	public void setFollowUps(final Collection<Article> followUps) {
		this.followUps = followUps;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Newspaper getNewspaper() {
		return this.newspaper;
	}

	public void setNewspaper(final Newspaper newspaper) {
		this.newspaper = newspaper;
	}

}
