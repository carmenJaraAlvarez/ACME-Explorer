
package forms;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.validation.constraints.Size;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import security.UserAccount;
import domain.Agent;
import domain.Customer;
import domain.Folder;
import domain.Message;
import domain.User;

public class CreateActorForm {

	private Integer						id;
	private Integer						version;
	private String						name;
	private String						surname;
	private String						address;
	private String						tlfn;
	private String						email;
	private String						username;
	private String						password;
	private String						password2;
	private Boolean						valida;
	private final Collection<Folder>	folders;
	private final Collection<Message>	messagesSent;
	private final Collection<Message>	messagesReceived;


	// Form

	public CreateActorForm() {
		this.id = 0;
		this.version = 0;
		this.folders = new ArrayList<Folder>();
		this.messagesReceived = new ArrayList<Message>();
		this.messagesSent = new ArrayList<Message>();
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(final Integer version) {
		this.version = version;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
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

	@Size(min = 5, max = 32)
	@Column(unique = true)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	@Size(min = 5, max = 32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	@Size(min = 5, max = 32)
	public String getPassword2() {
		return this.password2;
	}

	public void setPassword2(final String password2) {
		this.password2 = password2;
	}

	public Boolean getValida() {
		return this.valida;
	}

	public void setValida(final Boolean valida) {
		this.valida = valida;
	}

	public User getUser() {
		final User user = new User();
		user.setId(this.id);
		user.setVersion(this.version);
		user.setName(this.name);
		user.setSurname(this.surname);
		user.setAddress(this.address);
		user.setEmail(this.email);
		user.setFolders(this.folders);
		user.setMessagesReceived(this.messagesReceived);
		user.setMessagesSent(this.messagesSent);
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
		customer.setFolders(this.folders);
		customer.setMessagesReceived(this.messagesReceived);
		customer.setMessagesSent(this.messagesSent);
		return customer;
	}

	public Agent getAgent() {
		final Agent agent = new Agent();
		agent.setId(this.id);
		agent.setVersion(this.version);
		agent.setName(this.name);
		agent.setSurname(this.surname);
		agent.setAddress(this.address);
		agent.setEmail(this.email);
		agent.setFolders(this.folders);
		agent.setMessagesReceived(this.messagesReceived);
		agent.setMessagesSent(this.messagesSent);
		return agent;
	}

	public UserAccount getUserAccount() {
		final UserAccount userAccount = new UserAccount();
		userAccount.setUsername(this.username);
		final Md5PasswordEncoder encode = new Md5PasswordEncoder();
		final String pwdHash = encode.encodePassword(this.password, null);
		userAccount.setPassword(pwdHash);
		return userAccount;
	}

}
