
package forms;

import domain.Admin;
import domain.Agent;
import domain.Customer;
import domain.User;

public class ActorForm {

	private Integer	id;
	private Integer	version;
	private String	name;
	private String	surname;
	private String	address;
	private String	tlfn;
	private String	email;


	// Form

	public ActorForm() {

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

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public String getTlfn() {
		return this.tlfn;
	}

	public void setTlfn(final String tlfn) {
		this.tlfn = tlfn;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public ActorForm(final User user) {
		this();
		this.id = user.getId();
		this.version = user.getVersion();
		this.name = user.getName();
		this.surname = user.getSurname();
		this.address = user.getAddress();
		this.email = user.getEmail();
		this.tlfn = user.getTlfn();

	}

	public ActorForm(final Customer customer) {
		this();
		this.id = customer.getId();
		this.version = customer.getVersion();
		this.name = customer.getName();
		this.surname = customer.getSurname();
		this.address = customer.getAddress();
		this.email = customer.getEmail();
		this.tlfn = customer.getTlfn();

	}

	public ActorForm(final Admin admin) {
		this();
		this.id = admin.getId();
		this.version = admin.getVersion();
		this.name = admin.getName();
		this.surname = admin.getSurname();
		this.address = admin.getAddress();
		this.email = admin.getEmail();
		this.tlfn = admin.getTlfn();

	}

	public ActorForm(final Agent agent) {
		this();
		this.id = agent.getId();
		this.version = agent.getVersion();
		this.name = agent.getName();
		this.surname = agent.getSurname();
		this.address = agent.getAddress();
		this.email = agent.getEmail();
		this.tlfn = agent.getTlfn();

	}

	public User getUser() {
		final User user = new User();
		user.setId(this.id);
		user.setVersion(this.version);
		user.setName(this.name);
		user.setSurname(this.surname);
		user.setAddress(this.address);
		user.setEmail(this.email);
		user.setTlfn(this.tlfn);
		return user;
	}

	public Customer getCustomer() {
		final Customer customer = new Customer();
		customer.setId(this.id);
		customer.setVersion(this.version);
		customer.setName(this.name);
		customer.setSurname(this.surname);
		customer.setAddress(this.address);
		customer.setEmail(this.email);
		customer.setTlfn(this.tlfn);
		return customer;
	}

	public Admin getAdmin() {
		final Admin admin = new Admin();
		admin.setId(this.id);
		admin.setVersion(this.version);
		admin.setName(this.name);
		admin.setSurname(this.surname);
		admin.setAddress(this.address);
		admin.setEmail(this.email);
		admin.setTlfn(this.tlfn);
		return admin;
	}

	public Agent getAgent() {
		final Agent agent = new Agent();
		agent.setId(this.id);
		agent.setVersion(this.version);
		agent.setName(this.name);
		agent.setSurname(this.surname);
		agent.setAddress(this.address);
		agent.setEmail(this.email);
		agent.setTlfn(this.tlfn);
		return agent;
	}

}
