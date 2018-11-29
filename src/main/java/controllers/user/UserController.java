
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.UserService;
import controllers.AbstractController;
import domain.User;
import forms.ActorForm;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {

	@Autowired
	private UserService	userService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<User> users = this.userService.findAll();
		result = new ModelAndView("user/list");
		result.addObject("users", users);
		return result;
	}

	@RequestMapping(value = "/followers", method = RequestMethod.GET)
	public ModelAndView followers() {
		ModelAndView result;
		final Collection<User> users = this.userService.findFollowers(this.userService.findByPrincipal().getId());
		result = new ModelAndView("user/followers");
		result.addObject("users", users);
		return result;
	}

	@RequestMapping(value = "/following", method = RequestMethod.GET)
	public ModelAndView following() {
		ModelAndView result;
		final Collection<User> users = this.userService.findFollowing(this.userService.findByPrincipal().getId());
		result = new ModelAndView("user/following");
		result.addObject("users", users);
		return result;
	}

	@RequestMapping(value = "/follow", method = RequestMethod.GET)
	public ModelAndView follow(@RequestParam final int actorId) {
		ModelAndView result;
		final String follow = this.userService.follow(actorId);
		result = new ModelAndView("redirect:/actor/display.do?actorId=" + actorId + "&follow=" + follow);
		return result;
	}

	@RequestMapping(value = "/unfollow", method = RequestMethod.GET)
	public ModelAndView unfollow(@RequestParam final int actorId) {
		ModelAndView result;
		final String unfollow = this.userService.unfollow(actorId);
		result = new ModelAndView("redirect:/actor/display.do?actorId=" + actorId + "&unfollow=" + unfollow);
		return result;
	}

	//Edit-------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		User user;

		user = this.userService.findByPrincipal();
		final ActorForm actorForm = new ActorForm(user);
		result = this.createEditModelAndView(actorForm);

		return result;
	}
	// Guarda al editar el user
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final ActorForm actorForm, final BindingResult binding) {
		ModelAndView result;
		final User user = this.userService.reconstructUser(actorForm, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm);
		else
			try {
				this.userService.saveEdit(user);
				result = new ModelAndView("redirect:/actor/myDisplay.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(actorForm, "actor.commit.error");
			}
		return result;
	}

	private ModelAndView createEditModelAndView(final ActorForm actorForm) {
		ModelAndView result;

		result = this.createEditModelAndView(actorForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final ActorForm actorForm, final String message) {
		final ModelAndView result = new ModelAndView("actor/edit");
		result.addObject("actorForm", actorForm);
		result.addObject("message", message);
		result.addObject("direction", "user/edit.do");
		result.addObject("requestUri", "/actor/myDisplay.do");
		result.addObject("actorType", "user");
		return result;
	}

}
