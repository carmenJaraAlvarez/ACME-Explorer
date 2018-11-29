
package controllers;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.AdminService;
import services.AgentService;
import services.CustomerService;
import services.UserService;
import domain.Admin;
import domain.Agent;
import domain.Customer;
import domain.User;
import forms.CreateActorForm;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	@Autowired
	private UserService		userService;
	@Autowired
	private ActorService	actorService;
	@Autowired
	private AdminService	adminService;
	@Autowired
	private CustomerService	customerService;
	@Autowired
	private AgentService	agentService;


	// ------------------------Creación de USER----------------------------

	@RequestMapping(value = "/createUser", method = RequestMethod.GET)
	public ModelAndView createUserAccountUser() {
		return this.createEditModelAndViewUser(new CreateActorForm());
	}

	// Este método save guarda la primera vez que se ha creado un usuario
	@RequestMapping(value = "/createUser", method = RequestMethod.POST, params = "saveUser")
	public ModelAndView saveUser(@Valid final CreateActorForm createActorForm, final BindingResult binding) {
		ModelAndView result;
		if (!createActorForm.getValida()) {
			result = this.createEditModelAndViewUser(createActorForm, "actor.terms");
			return result;
		}

		if (!this.actorService.checkPassword(createActorForm)) {
			result = this.createEditModelAndViewUser(createActorForm, "actor.password.fail");
			return result;
		}

		final User user = this.userService.reconstruct(createActorForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewUser(createActorForm);
		else
			try {
				this.userService.save(user);

				final String name = user.getName();
				final Date moment = new Date();

				result = new ModelAndView("welcome/index");
				result.addObject("name", name);
				result.addObject("moment", moment);
				return result;

			} catch (final Throwable oops) {
				final CreateActorForm copia = createActorForm;
				copia.setPassword(" ");
				result = this.createEditModelAndViewUser(copia, "userAccount.commit.error");
			}
		return result;
	}

	// ------------Métodos auxiliares para crear USER ----------------------

	private ModelAndView createEditModelAndViewUser(final CreateActorForm createActorForm) {
		ModelAndView result;

		result = this.createEditModelAndViewUser(createActorForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewUser(final CreateActorForm createActorForm, final String message) {
		final ModelAndView result = new ModelAndView("actor/createUser");
		result.addObject("createActorForm", createActorForm);
		result.addObject("message", message);
		result.addObject("direction", "actor/createUser.do");
		result.addObject("save", "saveUser");

		return result;
	}

	// ------------------------Creación de CUSTOMER----------------------------

	@RequestMapping(value = "/createCustomer", method = RequestMethod.GET)
	public ModelAndView createUserAccountCustomer() {
		return this.createEditModelAndViewCustomer(new CreateActorForm());
	}

	// Este método save guarda la primera vez que se ha creado un customer
	@RequestMapping(value = "/createCustomer", method = RequestMethod.POST, params = "saveCustomer")
	public ModelAndView saveCustomer(@Valid final CreateActorForm createActorForm, final BindingResult binding) {
		ModelAndView result;
		if (!createActorForm.getValida()) {
			result = this.createEditModelAndViewCustomer(createActorForm, "actor.terms");
			return result;
		}

		if (!this.actorService.checkPassword(createActorForm)) {
			result = this.createEditModelAndViewCustomer(createActorForm, "actor.password.fail");
			return result;
		}

		final Customer customer = this.customerService.reconstruct(createActorForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewCustomer(createActorForm);
		else
			try {
				this.customerService.save(customer);

				final String name = customer.getName();
				final Date moment = new Date();

				result = new ModelAndView("welcome/index");
				result.addObject("name", name);
				result.addObject("moment", moment);
				return result;

			} catch (final Throwable oops) {
				final CreateActorForm copia = createActorForm;
				copia.setPassword(" ");
				result = this.createEditModelAndViewCustomer(copia, "userAccount.commit.error");
			}
		return result;
	}

	// ------------Métodos auxiliares para crear CUSTOMER
	// ----------------------

	private ModelAndView createEditModelAndViewCustomer(final CreateActorForm createActorForm) {
		ModelAndView result;

		result = this.createEditModelAndViewCustomer(createActorForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewCustomer(final CreateActorForm createActorForm, final String message) {
		final ModelAndView result = new ModelAndView("actor/createCustomer");
		result.addObject("createActorForm", createActorForm);
		result.addObject("message", message);
		result.addObject("direction", "actor/createCustomer.do");
		result.addObject("save", "saveCustomer");

		return result;
	}

	/*---Creación de Agents ---*/
	@RequestMapping(value = "/createAgent", method = RequestMethod.GET)
	public ModelAndView createUserAccountAgent() {
		return this.createEditModelAndViewAgent(new CreateActorForm());
	}

	// Este método save guarda la primera vez que se ha creado un customer
	@RequestMapping(value = "/createAgent", method = RequestMethod.POST, params = "saveAgent")
	public ModelAndView saveAgent(@Valid final CreateActorForm createActorForm, final BindingResult binding) {
		ModelAndView result;
		if (!createActorForm.getValida()) {
			result = this.createEditModelAndViewAgent(createActorForm, "actor.terms");
			return result;
		}

		if (!this.actorService.checkPassword(createActorForm)) {
			result = this.createEditModelAndViewAgent(createActorForm, "actor.password.fail");
			return result;
		}

		final Agent agent = this.agentService.reconstruct(createActorForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewAgent(createActorForm);
		else
			try {
				this.agentService.save(agent);

				final String name = agent.getName();
				final Date moment = new Date();

				result = new ModelAndView("welcome/index");
				result.addObject("name", name);
				result.addObject("moment", moment);
				return result;

			} catch (final Throwable oops) {
				final CreateActorForm copia = createActorForm;
				copia.setPassword(" ");
				result = this.createEditModelAndViewAgent(copia, "userAccount.commit.error");
			}
		return result;
	}
	// ------------Métodos auxiliares para crear AGENTS ----------------------

	private ModelAndView createEditModelAndViewAgent(final CreateActorForm createActorForm) {
		ModelAndView result;

		result = this.createEditModelAndViewAgent(createActorForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewAgent(final CreateActorForm createActorForm, final String message) {
		final ModelAndView result = new ModelAndView("actor/createAgent");
		result.addObject("createActorForm", createActorForm);
		result.addObject("message", message);
		result.addObject("direction", "actor/createAgent.do");
		result.addObject("save", "saveAgent");

		return result;
	}

	// Display de cualquier perfil
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int actorId, @RequestParam(required = false, defaultValue = "") final String follow, @RequestParam(required = false, defaultValue = "") final String unfollow) {
		final ModelAndView result = new ModelAndView("actor/display");
		final Authority authorityUser = new Authority();
		authorityUser.setAuthority(Authority.USER);
		try {
			final User actor = this.userService.findOne(actorId);
			result.addObject("actor", actor);
			result.addObject("requestURI", "user/edit.do");
			result.addObject("actorType", "user");
			result.addObject("userArticles", this.userService.articlesPublished(actorId));
			result.addObject("userChirps", actor.getChirps());
			if (this.actorService.checkAuthenticate() && this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(authorityUser)) {
				final Collection<User> users = this.userService.findFollowing(this.userService.findByPrincipal().getId());
				result.addObject("isFollowing", users.contains(actor));
				result.addObject("follow", follow);
				result.addObject("unfollow", unfollow);
				result.addObject("canFollow", !actor.equals(this.userService.findByPrincipal()));
			}
			result.addObject("articleURI", "actor/display.do?actorId=" + actor.getId());
			result.addObject("isMyProfile", false);
			result.addObject("error", false);

		} catch (final Throwable oops) {
			result.addObject("error", true);
		}

		return result;
	}

	// MyDisplay para todos los actores
	// ----------------------------------------------------------------
	@RequestMapping(value = "/myDisplay", method = RequestMethod.GET)
	public ModelAndView myDisplay() {
		final ModelAndView result = new ModelAndView("actor/display");

		try {
			//final Collection<Authority> authorities = this.actorService.findByPrincipal().getUserAccount().getAuthorities();

			final Authority authorityAdmin = new Authority();
			authorityAdmin.setAuthority(Authority.ADMIN);
			final Authority authorityCustomer = new Authority();
			authorityCustomer.setAuthority(Authority.CUSTOMER);
			final Authority authorityUser = new Authority();
			authorityUser.setAuthority(Authority.USER);
			final Authority authorityAgent = new Authority();
			authorityAgent.setAuthority(Authority.AGENT);

			if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(authorityUser)) {
				final User actor = this.userService.findByPrincipal();
				result.addObject("actor", actor);
				result.addObject("requestURI", "user/edit.do");
				result.addObject("actorType", "user");
				result.addObject("userArticles", actor.getArticles());
				result.addObject("userChirps", actor.getChirps());
				result.addObject("articleURI", "actor/myDisplay.do");
			} else if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(authorityCustomer)) {
				final Customer actor = this.customerService.findByPrincipal();
				result.addObject("actor", actor);
				result.addObject("requestURI", "customer/edit.do");
				result.addObject("actorType", "customer");
			} else if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(authorityAdmin)) {
				final Admin actor = this.adminService.findByPrincipal();
				result.addObject("actor", actor);
				result.addObject("requestURI", "administrator/edit.do");
				result.addObject("actorType", "administrator");
			} else if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(authorityAgent)) {
				final Agent actor = this.agentService.findByPrincipal();
				result.addObject("actor", actor);
				result.addObject("requestURI", "agent/edit.do");
				result.addObject("actorType", "agent");
			} else
				throw new IllegalArgumentException("Authority is not valid!");
			result.addObject("isMyProfile", true);
			result.addObject("error", false);

		} catch (final Throwable oops) {
			result.addObject("error", true);
		}

		return result;
	}
}
