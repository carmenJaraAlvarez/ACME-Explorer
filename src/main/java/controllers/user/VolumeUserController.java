
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.NewspaperService;
import services.UserService;
import services.VolumeService;
import controllers.AbstractController;
import domain.Newspaper;
import domain.User;
import domain.Volume;
import forms.VolumeForm;

@Controller
@RequestMapping("/volume/user")
public class VolumeUserController extends AbstractController {

	// Services

	@Autowired
	private VolumeService		volumeService;

	@Autowired
	private NewspaperService	newspaperService;
	@Autowired
	private UserService			userService;


	// Constructors -----------------------------------------------------------

	public VolumeUserController() {
		super();
	}

	// ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(required = false) final Integer volumeId) {
		ModelAndView result;
		try {
			final VolumeForm volumeForm;
			if (volumeId != null && volumeId != 0) {
				Volume volumeBBDD = this.volumeService.findOne(volumeId);
				User publisherBBDD = volumeBBDD.getPublisher();
				if (publisherBBDD.equals(this.userService.findByPrincipal())) {
					result = new ModelAndView("volume/edit");
					volumeForm = new VolumeForm(this.volumeService.findOne(volumeId));
					result.addObject("volumeForm", volumeForm);
				} else {
					result = new ModelAndView("redirect:/volume/list.do");
				}
			} else {
				result = new ModelAndView("volume/create");
				volumeForm = new VolumeForm();
				result.addObject("volumeForm", volumeForm);
			}
			Collection<Newspaper> newspapers = this.newspaperService.findNewspapersForVolume();
			result.addObject("newspapers", newspapers);
		} catch (Throwable oops) {
			result = this.panic(oops);
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(VolumeForm volumeForm, final BindingResult binding) {
		ModelAndView result;
		Volume volume = this.volumeService.reconstruct(volumeForm, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(volumeForm);
		else
			try {
				this.volumeService.save(volume);
				result = new ModelAndView("redirect:/volume/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(volumeForm, "volume.commit.error");
			}
		return result;
	}

	private ModelAndView createEditModelAndView(final VolumeForm volumeForm) {
		ModelAndView result;
		result = this.createEditModelAndView(volumeForm, null);

		return result;
	}

	private ModelAndView createEditModelAndView(final VolumeForm volumeForm, final String message) {
		ModelAndView result;
		if (volumeForm.getId() != 0) {
			result = new ModelAndView("volume/edit");
		} else {
			result = new ModelAndView("volume/create");
		}
		result.addObject("volumeForm", volumeForm);
		Collection<Newspaper> newspapers = this.newspaperService.findNewspapersForVolume();
		result.addObject("newspapers", newspapers);
		result.addObject("message", message);

		return result;
	}

}
