
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Folder extends DomainEntity {

	private String	name;
	private boolean	ofTheSystem;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean getOfTheSystem() {
		return this.ofTheSystem;
	}

	public void setOfTheSystem(final boolean ofTheSystem) {
		this.ofTheSystem = ofTheSystem;
	}


	// Relationships ----------------------------------------------------------

	private Collection<Message>	messages;
	private Collection<Folder>	childFolders;
	private Folder				fatherFolder;
	private Actor				actor;


	@ManyToMany(mappedBy = "folders")
	@Valid
	@NotNull
	public Collection<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(final Collection<Message> messages) {
		this.messages = messages;
	}

	@OneToMany(mappedBy = "fatherFolder")
	@Valid
	@NotNull
	public Collection<Folder> getChildFolders() {
		return this.childFolders;
	}

	public void setChildFolders(final Collection<Folder> childFolders) {
		this.childFolders = childFolders;
	}

	@ManyToOne(optional = true)
	@Valid
	public Folder getFatherFolder() {
		return this.fatherFolder;
	}

	public void setFatherFolder(final Folder fatherFolder) {
		this.fatherFolder = fatherFolder;
	}

	@ManyToOne(optional = false)
	@Valid
	@NotNull
	public Actor getActor() {
		return this.actor;
	}

	public void setActor(final Actor actor) {
		this.actor = actor;
	}

}
