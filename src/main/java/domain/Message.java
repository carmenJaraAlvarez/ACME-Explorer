
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Message extends DomainEntity {

	private Date	sendMoment;
	private String	subject;
	private String	body;
	private String	priority;


	@Past
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "MM/dd/yyyy HH:mm")
	public Date getSendMoment() {
		return this.sendMoment;
	}

	public void setSendMoment(final Date sendMoment) {
		this.sendMoment = sendMoment;
	}
	@NotBlank
	public String getSubject() {
		return this.subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}
	@NotBlank
	public String getBody() {
		return this.body;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	@Pattern(regexp = "HIGH|NEUTRAL|LOW")
	public String getPriority() {
		return this.priority;
	}

	public void setPriority(final String priority) {
		this.priority = priority;
	}


	// Relationships ----------------------------------------------------------

	private Actor				actorSender;
	private Collection<Actor>	actorReceivers;
	private Collection<Folder>	folders;


	@ManyToOne(optional = false)
	@Valid
	@NotNull
	public Actor getActorSender() {
		return this.actorSender;
	}

	public void setActorSender(final Actor actorSender) {
		this.actorSender = actorSender;
	}

	@ManyToMany
	@Valid
	@NotEmpty
	public Collection<Actor> getActorReceivers() {
		return this.actorReceivers;
	}

	public void setActorReceivers(final Collection<Actor> actorReceivers) {
		this.actorReceivers = actorReceivers;
	}

	@ManyToMany
	@Valid
	@NotNull
	public Collection<Folder> getFolders() {
		return this.folders;
	}

	public void setFolders(final Collection<Folder> folders) {
		this.folders = folders;
	}

}
