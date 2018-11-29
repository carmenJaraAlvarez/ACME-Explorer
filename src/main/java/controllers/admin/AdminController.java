package controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdminService;
import controllers.AbstractController;
import domain.Admin;
import forms.ActorForm;

@Controller
@RequestMapping("/administrator")
public class AdminController extends AbstractController{

	@Autowired
	private AdminService adminService;
	
	//Edit-------------------------------------------
			@RequestMapping(value = "/edit", method = RequestMethod.GET)
			public ModelAndView edit() {
				ModelAndView result;
				Admin admin = this.adminService.findByPrincipal();
				final ActorForm actorForm = new ActorForm(admin);
				result = this.createEditModelAndView(actorForm);

				return result;
			}
			// Guarda al editar el admin
			@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
			public ModelAndView save(final ActorForm actorForm, final BindingResult binding) {
				ModelAndView result;
				final Admin admin = this.adminService.reconstructAdmin(actorForm, binding);
				if (binding.hasErrors())
					result = this.createEditModelAndView(actorForm);
				else
					try {
						this.adminService.saveEdit(admin);
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
				result.addObject("direction", "administrator/edit.do");
				result.addObject("requestUri", "/actor/myDisplay.do");
				result.addObject("actorType", "admin");
				return result;
			}
		
}
