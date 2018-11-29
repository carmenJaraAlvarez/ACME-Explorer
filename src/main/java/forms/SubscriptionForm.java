
package forms;

import domain.CreditCard;

public class SubscriptionForm {

	private Integer		id;
	private Integer		version;
	private Integer		objectId;
	private CreditCard	creditCard;


	// Form

	public SubscriptionForm() {
	}

	public SubscriptionForm(Integer objectId) {
		this();
		this.id = 0;
		this.version = 0;
		this.objectId = objectId;
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

	//Este es el ID del periódico o del volumen al que se va a subscribir el cliente.
	public int getObjectId() {
		return this.objectId;
	}

	public void setObjectId(final Integer objectId) {
		this.objectId = objectId;
	}

	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

}
