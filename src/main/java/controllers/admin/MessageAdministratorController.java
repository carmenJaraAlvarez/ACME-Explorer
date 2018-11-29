/*
 * MessageAdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.admin;

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
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.FolderService;
import services.MessageService;
import controllers.AbstractController;
import domain.Actor;
import domain.Folder;
import domain.Message;

@Controller
@RequestMapping("/message/administrator")
public class MessageAdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private FolderService	folderService;
	@Autowired
	private MessageService	messageService;
	@Autowired
	private ActorService	actorService;


	// Constructors -----------------------------------------------------------

	public MessageAdministratorController() {
		super();
	}

	// Notificacion create---------------------------------------------------------------

	@RequestMapping(value = "/notification", method = RequestMethod.GET)
	public ModelAndView create() {

		final Actor logeado = this.actorService.findByPrincipal();

		ModelAndView result;
		final Message msg = this.messageService.create();
		final List<Actor> allActors = new ArrayList<>(this.actorService.findAll());
		msg.getActorReceivers().addAll(allActors);
		final Folder actual = this.folderService.findNotificationBoxByActor(logeado);
		final boolean esNotificacion = true;
		result = new ModelAndView("message/edit");
		result.addObject("messageToEdit", msg);
		result.addObject("folder", actual);
		result.addObject("allActors", allActors);
		result.addObject("esNotificacion", esNotificacion);

		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "notification")
	public ModelAndView notify(@ModelAttribute("messageToEdit") @Valid final Message messageToEdit, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {
			final Folder actual = this.folderService.findActual(messageToEdit);
			final List<Actor> allActors = new ArrayList<>(this.actorService.findAll());
			final boolean esNotificacion = true;
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
				this.messageService.newNotification(messageToEdit);//salva y controla spam

				result = new ModelAndView("redirect:/folder/actor/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(messageToEdit, "message.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Message msg, final Folder folder, final BindingResult binding) {
		ModelAndView result;
		try {
			this.messageService.delete(msg, folder);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(msg, "folder.commit.error");
		}

		return result;
	}
	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Message msg) {
		ModelAndView result;

		result = this.createEditModelAndView(msg, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message msg, final String message) {
		final ModelAndView result;

		result = new ModelAndView("message/edit");

		result.addObject("msg", msg);
		result.addObject("message", message);

		return result;
	}

}
