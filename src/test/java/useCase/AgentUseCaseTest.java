
package useCase;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.AgentService;
import utilities.AbstractTest;
import domain.Agent;
import forms.CreateActorForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AgentUseCaseTest extends AbstractTest {

	@Autowired
	private AgentService	agentService;


	//--------------------------------------------------------------------------

	//Use Case 27: Registrarse como agente.
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

			final Agent agent = this.agentService.reconstruct(reg, null);
			this.agentService.flush();
			this.agentService.save(agent);
			this.agentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	@Test
	public void driverSignInAgent() {
		final Object testingData[][] = {
			//POSITIVE
			{
				"agent200", "agent200", "agent200", "agent200", "agent200 surname", "agent200@gmail.com", true, null
			},

			//NEGATIVE
			{
				"", "agent202", "agent202", "agent202", "surname", "111@h.com", true, IllegalArgumentException.class
			//simulación binding
			}, {
				"agent201", "agent201", "agent201", "agent201", "surname", "111.com", true, javax.validation.ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSignIn((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Boolean) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}
}
