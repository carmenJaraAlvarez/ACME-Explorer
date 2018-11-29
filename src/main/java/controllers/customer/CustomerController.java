package controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CustomerService;


import controllers.AbstractController;
import domain.Customer;
import forms.ActorForm;

@Controller
@RequestMapping("/customer")
public class CustomerController extends AbstractController{

	@Autowired
	private CustomerService customerService;
	
	//Edit-------------------------------------------
			@RequestMapping(value = "/edit", method = RequestMethod.GET)
			public ModelAndView edit() {
				ModelAndView result;
				Customer customer = this.customerService.findByPrincipal();
				final ActorForm actorForm = new ActorForm(customer);
				result = this.createEditModelAndView(actorForm);

				return result;
			}
			// Guarda al editar el customer
			@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
			public ModelAndView save(final ActorForm actorForm, final BindingResult binding) {
				ModelAndView result;
				final Customer customer = this.customerService.reconstructCustomer(actorForm, binding);
				if (binding.hasErrors())
					result = this.createEditModelAndView(actorForm);
				else
					try {
						this.customerService.saveEdit(customer);
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
				result.addObject("direction", "customer/edit.do");
				result.addObject("requestUri", "/actor/myDisplay.do");
				result.addObject("actorType", "customer");
				return result;
			}
		
}
