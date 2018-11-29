
package controllers.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AgentService;
import controllers.AbstractController;
import domain.Agent;
import forms.ActorForm;

@Controller
@RequestMapping("/agent")
public class AgentController extends AbstractController {

	@Autowired
	private AgentService	agentService;


	//Edit-------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		final Agent agent = this.agentService.findByPrincipal();
		final ActorForm actorForm = new ActorForm(agent);
		result = this.createEditModelAndView(actorForm);

		return result;
	}
	// Guarda al editar el agent
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final ActorForm actorForm, final BindingResult binding) {
		ModelAndView result;
		final Agent agent = this.agentService.reconstructAgent(actorForm, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm);
		else
			try {
				this.agentService.saveEdit(agent);
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
		result.addObject("direction", "agent/edit.do");
		result.addObject("requestUri", "/actor/myDisplay.do");
		result.addObject("actorType", "agent");
		return result;
	}

}
