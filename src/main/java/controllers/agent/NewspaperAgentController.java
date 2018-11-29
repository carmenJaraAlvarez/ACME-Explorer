
package controllers.agent;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AgentService;
import services.NewspaperService;
import controllers.AbstractController;
import domain.Agent;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper/agent")
public class NewspaperAgentController extends AbstractController {

	// Services

	@Autowired
	private AgentService		agentService;
	@Autowired
	private NewspaperService	newspaperService;


	// ---------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int id) {
		final Agent log = this.agentService.findByPrincipal();
		ModelAndView result;
		Collection<Newspaper> newspapers;
		if (id == 1) {
			newspapers = new HashSet<>(this.newspaperService.findNewspapersWithAdvertisement(log.getId()));

			result = new ModelAndView("newspaper/agentListWith");
		} else {
			newspapers = new HashSet<>(this.newspaperService.findNewspapersWithoutAdvertisement(log.getId()));

			result = new ModelAndView("newspaper/agentListWithout");
		}
		result.addObject("newspapers", newspapers);
		result.addObject("uri", "newspaper/agent/list.do");

		return result;
	}

}
