
package controllers.admin;

import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertisementService;
import services.ArticleService;
import services.ChirpService;
import services.NewspaperService;
import services.UserService;
import controllers.AbstractController;

@Controller
@RequestMapping("/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {

	//Services

	@Autowired
	private NewspaperService		newspaperService;
	@Autowired
	private UserService				userService;
	@Autowired
	private ArticleService			articleService;
	@Autowired
	private ChirpService			chirpService;
	@Autowired
	private AdvertisementService	advertisementService;


	//Constructors

	public DashboardAdministratorController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;

		final DecimalFormat df = new DecimalFormat("###.##");

		result = new ModelAndView("administrator/dashboard");
		result.addObject("requestURI", "/dashboard/administrator/display.do");

		// 7.1

		result.addObject("avgNewsPerUser", df.format(this.newspaperService.getAverageNewspapersPerUser()));
		result.addObject("stdvNewsPerUser", df.format(this.newspaperService.getStandardDeviationNewspapersPerUser()));
		result.addObject("avgArticlesPerNews", df.format(this.articleService.getAverageArticlesPerNewspaper()));
		result.addObject("stdvArticlesPerNews", df.format(this.articleService.getStandardDeviationArticlesPerNewspaper()));
		result.addObject("avgArticlesPerUser", df.format(this.articleService.getAverageArticlesPerUser()));
		result.addObject("stdvArticlesPerUser", df.format(this.articleService.getStandardDeviationArticlesPerUser()));

		result.addObject("newsMore10", this.newspaperService.getNewspaperWithLessArticlesThanAvgMinus10());
		result.addObject("newsLess10", this.newspaperService.getNewspaperWithMoreArticlesThanAvgPlus10());
		result.addObject("ratioCreatorNews", this.userService.getRatioCreatorsNewspapers());
		result.addObject("ratioCreatorArticle", this.userService.getRatioCreatorsArticles());

		// 17.1

		result.addObject("avgFollowUpsPerArticle", df.format(this.articleService.getAverageFollowUpsPerArticle()));
		result.addObject("avgFollowUpsPerArticle1Week", df.format(this.articleService.getAverageFollowUpsPerArticle1Week()/100));
		result.addObject("avgFollowUpsPerArticle2Week", df.format(this.articleService.getAverageFollowUpsPerArticle2Week()/100));

		result.addObject("avgChirpPerUser", df.format(this.chirpService.getAverageChirpsPerUser()));
		result.addObject("stdevChirpPerUser", df.format(this.chirpService.getStandardDeviationChirpsPerUser()));
		result.addObject("ratioUserChirpsOver75", this.userService.getUsersChirpsAbove75Average());

		// 24.1

		result.addObject("ratioPublicVsPrivate", this.newspaperService.getRatioPublicVsPrivateNewspaper());
		result.addObject("avgArticlesPerPrivateNews", df.format(this.newspaperService.getAverageArticlesPerPrivateNewspaper()));
		result.addObject("avgArticlesPerPublicNews", df.format(this.newspaperService.getAverageArticlesPerPublicNewspaper()));
		result.addObject("ratioPrivateVsPublicPerPublisher", this.newspaperService.getRatioPrivateVsPublicNewspapersPerPublisher());
		result.addObject("ratioSuscribersPerPrivateVsCustomers", this.newspaperService.getRatioSuscribersPerPrivateNewspapersVsTotalCustomers());

		//2.5.3

		result.addObject("ratioNewspAdverWithVsWithout", df.format(this.newspaperService.ratioNewspapersWithAdvertisementsVsWithout()));
		result.addObject("ratioAvertisementsTabooVsTotal", df.format(this.advertisementService.ratioAdvertisementsTabooVsTotal()));

		//2.11.1

		result.addObject("averageNewspapersPerVolume", this.newspaperService.averageNewspapersPerVolume());
		result.addObject("ratioSubsNewsVsVol", this.newspaperService.ratioSubscriptionsToNewspapersVsVolumes());

		return result;
	}
}
