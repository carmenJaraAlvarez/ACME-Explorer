
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
import controllers.AbstractController;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper/user")
public class NewspaperUserController extends AbstractController {

	// Services

	@Autowired
	private NewspaperService	newspaperService;


	// Constructors -----------------------------------------------------------

	public NewspaperUserController() {
		super();
	}

	// ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final Newspaper newspaper = this.newspaperService.create();
		result = this.createEditModelAndView(newspaper);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Newspaper newspaper, final BindingResult binding) {
		ModelAndView result;
		Newspaper save = this.newspaperService.reconstruct(newspaper, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(newspaper);
		else
			try {
				this.newspaperService.save(save);
				result = new ModelAndView("redirect:myList.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(newspaper, "newspaper.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/myList", method = RequestMethod.GET)
	public ModelAndView myList() {
		final ModelAndView result;
		final Collection<Newspaper> myNewspapers = this.newspaperService.getMyNewspapers();

		result = new ModelAndView("newspaper/myList");
		result.addObject("newspapers", myNewspapers);
		result.addObject("uri", "newspaper/user/myList.do");
		result.addObject("showCreateButton", true);
		return result;
	}

	@RequestMapping(value = "/publish", method = RequestMethod.GET)
	public ModelAndView publish(@RequestParam final int newspaperId) {
		ModelAndView result;
		final Newspaper newspaper = this.newspaperService.findOne(newspaperId);
		if (newspaper != null) {
			try {
				this.newspaperService.publishNewspaper(newspaper);
				result = new ModelAndView("redirect:myList.do");
			} catch (final Exception e) {
				result = new ModelAndView("newspaper/error");
				if (e.getMessage().equals("You are not the publisher"))
					result.addObject("messageErrorPublisher", true);
				else if (e.getMessage().equals("This newspaper is already published"))
					result.addObject("messageErrorPublished", true);
				else if (e.getMessage().equals("All articles are not published yet"))
					result.addObject("messageErrorArticles", true);
			}
		} else {
			result = new ModelAndView("newspaper/error");
			result.addObject("messageErrorDeleted", true);
		}
		return result;
	}

	private ModelAndView createEditModelAndView(final Newspaper newspaper) {
		ModelAndView result;
		result = this.createEditModelAndView(newspaper, null);

		return result;
	}

	private ModelAndView createEditModelAndView(final Newspaper newspaper, final String message) {
		ModelAndView result;
		result = new ModelAndView("newspaper/create");
		result.addObject("newspaper", newspaper);

		return result;
	}

}
