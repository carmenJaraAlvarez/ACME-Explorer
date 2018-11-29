
package useCase;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.CustomerService;
import services.NewspaperService;
import services.SubscriptionService;
import utilities.AbstractTest;
import domain.CreditCard;
import domain.Customer;
import domain.Newspaper;
import domain.Subscription;
import forms.CreateActorForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CustomerUseCaseTest extends AbstractTest {

	@Autowired
	private CustomerService		customerService;
	@Autowired
	private NewspaperService	newspaperService;
	@Autowired
	private SubscriptionService	creditcardService;
	@Autowired
	private ActorService		actorService;


	//--------------------------------------------------------------------------

	//UC13: A logged customer list published newspapers and select a private one to subscript on it
	//UC15: An unauthenticated person sign in as Customer

	protected void templateSubscription(final String username, final String newspaperName, final int cvv, String brand, final int expirationMonth, final int expirationYear, final String holderName, final String number, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(username);
			final Collection<Newspaper> newspapers = this.newspaperService.getPublishedNewspapers();
			final int idNewspaper = this.getEntityId(newspaperName);
			final Newspaper selected = this.newspaperService.findOne(idNewspaper);
			final Subscription creditcard = this.creditcardService.create();
			CreditCard cCard = new CreditCard();
			cCard.setCVV(cvv);
			cCard.setBrand(brand);
			cCard.setExpirationMonth(expirationMonth);
			cCard.setExpirationYear(expirationYear);
			cCard.setHolderName(holderName);
			cCard.setNum(number);
			creditcard.setCreditCard(cCard);
			creditcard.setNewspaper(selected);
			this.creditcardService.checkTarjeta(creditcard.getCreditCard());
			this.creditcardService.checkTarjeta(creditcard.getCreditCard());
			this.creditcardService.controlAndSave(creditcard);
			this.creditcardService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverSubscriptionCustomer() {
		final Object testingData[][] = {
			//POSITIVE
			{
				"customer2", "newspaper4", 100, "visa", 12, 20, "customer 2", "1111222233334444", null
			},
			//NEGATIVE (creditcard expired)
			{
				"customer2", "newspaper4", 100, "visa", 01, 18, "customer 2", "1111222233334444", IllegalArgumentException.class
			},
			//NEGATIVE (crediitcard blank)
			{
				"customer2", "newspaper5", 100, "visa", 01, 20, "customer 2", "", javax.validation.ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSubscription((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);
	}

	protected void templateSignIn(final String username, final String password, final String password2, final String name, final String surname, final String email, final boolean accept, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(null);
			final CreateActorForm reg = new CreateActorForm();
			reg.setUsername(username);
			reg.setPassword(password);
			reg.setPassword2(password2);
			reg.setEmail(email);
			reg.setName(name);
			reg.setSurname(surname);
			reg.setValida(accept);
			//simulación del binding @unique de form
			Assert.isTrue(username != "");

			final Customer customer = this.customerService.reconstruct(reg, null);
			this.customerService.flush();
			this.customerService.save(customer);
			this.customerService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	@Test
	public void driverSignInCustomer() {
		final Object testingData[][] = {
			//POSITIVE
			{
				"customer200", "customer200", "customer200", "customer200", "customer200 surname", "customer200@gmail.com", true, null
			},

			//NEGATIVE
			{
				"", "customer202", "customer202", "customer202", "surname", "111@h.com", true, IllegalArgumentException.class
			//simulación binding
			}, {
				"customer201", "customer201", "customer201", "customer201", "surname", "111.com", true, javax.validation.ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSignIn((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Boolean) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}
}
