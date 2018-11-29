/*
 * sponsorshipCustomerController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.actor;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.FolderService;
import services.MessageService;
import controllers.AbstractController;
import domain.Folder;
import domain.Message;

@Controller
@RequestMapping("/folder/actor")
public class FolderActorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private FolderService	folderService;
	@Autowired
	private MessageService	messageService;
	@Autowired
	private ActorService	actorService;


	// Constructors -----------------------------------------------------------

	public FolderActorController() {
		super();
	}

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) Integer folderId) {
		ModelAndView result;
		Collection<Folder> fathers;
		Collection<Folder> children;
		Folder f;
		fathers = this.folderService.primerNivel();
		if (folderId == null) {
			f = this.folderService.inbox();
			folderId = f.getId();
		} else
			f = this.folderService.findOne(folderId);

		final Folder trash = this.folderService.findTrashBoxByActor(f.getActor());
		final boolean isTrash = (f.equals(trash));
		final boolean isNotificationBox = (f.getName().equals("notification box"));
		//System.out.print(isTrash);
		children = f.getChildFolders();

		Collection<Message> messages;
		messages = this.messageService.findByFolder(folderId);

		result = new ModelAndView("folder/list");
		result.addObject("fathers", fathers);
		result.addObject("children", children);
		result.addObject("selected", f);
		result.addObject("messages", messages);
		result.addObject("isTrash", isTrash);
		result.addObject("isNotificationBox", isNotificationBox);

		System.out.print(isNotificationBox);
		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int folderId) {
		ModelAndView result;
		//actual será la carpeta padre de la creada folder
		final Folder actual = this.folderService.findOne(folderId);

		Folder folder;
		folder = this.folderService.create(actual);
		result = new ModelAndView("folder/edit");

		result.addObject(folder);

		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int folderId) {
		final ModelAndView result = new ModelAndView("folder/edit");
		final Folder folder = this.folderService.findOne(folderId);
		Assert.notNull(folder);
		Assert.isTrue(!folder.getOfTheSystem());
		Assert.isTrue(this.folderService.findOne(folderId).getActor().equals(this.actorService.findByPrincipal()));
		final List<Folder> all = this.folderService.findRest(folder);
		result.addObject("folder", folder);
		result.addObject("all", all);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Folder folder, final BindingResult binding) {
		ModelAndView result;
		if (!folder.getActor().equals(this.actorService.findByPrincipal()))
			result = new ModelAndView("redirect:list.do");
		else if (!this.folderService.validName(folder))
			result = this.createEditModelAndView(folder, "folder.errorname");
		else if (binding.hasErrors())
			result = this.createEditModelAndView(folder);
		else
			try {
				final String s = this.folderService.checkConcurrence(folder);
				if (s == null) {
				boolean hasFolder = true;
				if (folder.getId() != 0)
					hasFolder = this.folderService.findOne(folder.getId()) != null;
				Assert.isTrue(hasFolder);
				this.folderService.save(folder);
				result = new ModelAndView("redirect:list.do");
				} else
					result = this.createEditModelAndView(folder, s);

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(folder, "folder.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Folder folder, final BindingResult binding) {
		ModelAndView result;
		try {
			Assert.isTrue(folder.getActor().equals(this.actorService.findByPrincipal()));
			this.folderService.delete(folder);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(folder, "folder.commit.error");
		}

		return result;
	}
	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Folder folder) {
		ModelAndView result;

		result = this.createEditModelAndView(folder, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Folder folder, final String message) {
		final ModelAndView result;

		result = new ModelAndView("folder/edit");

		result.addObject("folder", folder);
		result.addObject("message", message);

		final List<Folder> all = this.folderService.findRest(folder);
		result.addObject("all", all);

		return result;
	}

}
