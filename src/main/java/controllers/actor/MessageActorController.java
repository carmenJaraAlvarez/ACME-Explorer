/*
 * MessageActorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.actor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.FolderService;
import services.MessageService;
import controllers.AbstractController;
import domain.Actor;
import domain.Folder;
import domain.Message;

@Controller
@RequestMapping("/message/actor")
public class MessageActorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private FolderService folderService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private ActorService actorService;

	// Constructors -----------------------------------------------------------

	public MessageActorController() {
		super();
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int folderId) {
		ModelAndView result;
		final Folder actual = this.folderService.findOne(folderId);
		final Message msg = this.messageService.create();
		final List<Actor> allActors = new ArrayList<>(
				this.actorService.findAll());
		final boolean esNotificacion = false;
		final Collection<Folder> fathers = this.folderService.primerNivel();
		final Collection<Folder> children = actual.getChildFolders();
		result = new ModelAndView("message/edit");
		result.addObject("messageToEdit", msg);
		result.addObject("folder", actual);
		result.addObject("allActors", allActors);
		result.addObject("esNotificacion", esNotificacion);
		result.addObject("fathers", fathers);
		result.addObject("children", children);

		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(
			@ModelAttribute("messageToEdit") @Valid final Message messageToEdit,
			final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {

			final Folder actual = this.folderService.findActual(messageToEdit);
			final List<Actor> allActors = new ArrayList<>(
					this.actorService.findAll());
			final boolean esNotificacion = false;
			final Collection<Folder> fathers = this.folderService.primerNivel();
			final Collection<Folder> children = actual.getChildFolders();
			result = this.createEditModelAndView(messageToEdit);
			result.addObject("folder", actual);
			result.addObject("allActors", allActors);
			result.addObject("esNotificacion", esNotificacion);
			result.addObject("fathers", fathers);
			result.addObject("children", children);

		} else
			try {
				final Message msg = this.messageService
						.saveNewMessage(messageToEdit);
				final Message msg2 = this.messageService.newMessageControl(msg);
				this.messageService.newMessageControl2(msg2);

				result = new ModelAndView("redirect:/folder/actor/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(messageToEdit,
						"message.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Message msg, final Folder folder,
			final BindingResult binding) {
		ModelAndView result;
		try {
			this.messageService.delete(msg, folder);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(msg, "folder.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/move", method = RequestMethod.GET)
	public ModelAndView move(@RequestParam final int messageId,
			@RequestParam final int folderId,
			@RequestParam final int newFolderId) {

		ModelAndView result;
		final Message messageToEdit = this.messageService.findOne(messageId);
		final Folder f = this.folderService.findOne(folderId);
		final Folder f2 = this.folderService.findOne(newFolderId);
		final Collection<Folder> fathers = this.folderService.primerNivel();
		final Collection<Folder> children = f2.getChildFolders();

		// Comprobación para saber si el que está editando el mensaje es
		// propietario
		boolean isPrincipalActor = false;
		final Actor logged = this.actorService.findByPrincipal();
		final Folder folder = this.folderService.findOne(folderId);
		final Actor actor = folder.getActor();
		if (actor.equals(logged))
			isPrincipalActor = true;
		if (isPrincipalActor) {
			result = new ModelAndView("message/move");
			result.addObject("messageToEdit", messageToEdit);
			result.addObject("actual", f);
			result.addObject("selected", f2);
			result.addObject("fathers", fathers);
			result.addObject("children", children);
		} else
			result = new ModelAndView("redirect:/folder/actor/list.do");
		return result;
	}

	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public ModelAndView change(@RequestParam final int messageId,
			@RequestParam final int folderId,
			@RequestParam final int newFolderId) {
		ModelAndView result;

		try {
			final Message messageToEdit = this.messageService
					.findOne(messageId);
			final Folder f = this.folderService.findOne(folderId);
			final Folder f2 = this.folderService.findOne(newFolderId);
			final Collection<Folder> fathers = this.folderService.primerNivel();
			final Collection<Folder> children = f2.getChildFolders();
				this.messageService.changeFolder(messageToEdit, f, f2);

				result = new ModelAndView("redirect:/folder/actor/list.do");
				result.addObject("selected", f2);
				result.addObject("fathers", fathers);
				result.addObject("children", children);

		} catch (final Throwable oops) {

			result = new ModelAndView("redirect:/folder/actor/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int messageId,
			@RequestParam final int folderId) {
		ModelAndView result;
		final Message messageToEdit = this.messageService.findOne(messageId);
		final Folder f = this.folderService.findOne(folderId);

		this.messageService.delete(messageToEdit, f);
		result = new ModelAndView("redirect:/folder/actor/list.do");

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Message msg) {
		ModelAndView result;

		result = this.createEditModelAndView(msg, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message msg,
			final String message) {
		final ModelAndView result;

		result = new ModelAndView("message/edit");

		result.addObject("messageToEdit", msg);
		result.addObject("message", message);

		return result;
	}
	
}
