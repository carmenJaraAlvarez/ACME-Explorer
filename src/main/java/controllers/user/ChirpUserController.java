
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ChirpService;
import controllers.AbstractController;
import domain.Chirp;

@Controller
@RequestMapping("/chirp/user")
public class ChirpUserController extends AbstractController {

	// Services
	@Autowired
	private ChirpService	chirpService;


	// Constructors -----------------------------------------------------------

	public ChirpUserController() {
		super();
	}

	// ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final Chirp chirp = this.chirpService.create();
		result = this.createEditModelAndView(chirp);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Chirp chirp, final BindingResult binding) {
		ModelAndView result;
		Chirp save = this.chirpService.reconstruct(chirp, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(chirp);
		else
			try {
				this.chirpService.save(save);
				result = new ModelAndView("redirect:/actor/myDisplay.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(save, "chirp.commit.error");
			}

		return result;
	}

	private ModelAndView createEditModelAndView(final Chirp chirp) {
		ModelAndView result;
		result = this.createEditModelAndView(chirp, null);

		return result;
	}

	private ModelAndView createEditModelAndView(final Chirp chirp, final String message) {
		ModelAndView result;
		result = new ModelAndView("chirp/create");
		result.addObject("chirp", chirp);

		return result;
	}

	@RequestMapping(value = "/stream", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Chirp> chirps = this.chirpService.getChirpsPeopleFollowing();

		result = new ModelAndView("chirp/following/stream");
		result.addObject("chirps", chirps);
		result.addObject("uri", "chirp/user/stream.do");
		return result;
	}
}
