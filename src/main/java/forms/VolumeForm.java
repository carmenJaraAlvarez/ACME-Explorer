
package forms;

import java.util.ArrayList;
import java.util.Collection;

import domain.Newspaper;
import domain.Volume;

public class VolumeForm {

	private Integer				id;
	private Integer				version;
	private String				title;
	private String				description;
	private Collection<String>	newspapers;


	// Form

	public VolumeForm() {
		this.id = 0;
		this.version = 0;
	}

	public VolumeForm(final Volume volume) {
		this.id = volume.getId();
		this.version = volume.getVersion();
		this.title = volume.getTitle();
		this.description = volume.getDescription();
		this.newspapers = new ArrayList<String>();
		for (Newspaper newspaper : volume.getNewspapers()) {
			this.newspapers.add(String.valueOf(newspaper.getId()));
		}
	}

	public int getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final Integer version) {
		this.version = version;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Collection<String> getNewspapers() {
		return this.newspapers;
	}

	public void setNewspapers(final Collection<String> newspapers) {
		this.newspapers = newspapers;
	}

}
