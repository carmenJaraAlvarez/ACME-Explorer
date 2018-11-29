<%--
 * edit.jsp
 *
 * Copyright (C) 2015 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<%--
 * Recibe messageToEdit y folder (en la que estaba)
 * Tambien recibe coleccion de actores del sistema 
 para seleccionar destinatario allActors
 propiedades de message:
 	private Date	sendMoment;
	private String	subject;
	private String	body;
	private String	priority;
		private Actor				actorSender;
	private Collection<Actor>	actorReceivers;
	private Collection<Folder>	folders;
 --%>

<form:form action="message/actor/edit.do" modelAttribute="messageToEdit">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="sendMoment" />
	<form:hidden path="actorSender" />
	<form:hidden path="folders" />

	<%--
 	* *****destinatarios para elegir si no esnotificacion, si sí lo es, escondidos*********************
 	--%>
	<jstl:choose>

		<jstl:when test="${!esNotificacion}">
			<form:label path="actorReceivers">
				<spring:message code="message.receivers" />:
	</form:label>
			<form:select multiple="multiple" path="actorReceivers">
				<form:options items="${allActors}" itemLabel="name" itemValue="id" />
			</form:select>
			<form:errors cssClass="error" path="actorReceivers" />
		</jstl:when>

		<jstl:when test="${esNotificacion}">
			<h1>
				<spring:message code="folder.message.notification" />

			</h1>
			<form:hidden path="actorReceivers" />
		</jstl:when>

	</jstl:choose>
	<br />
	<br />


	<form:label path="subject">
		<spring:message code="message.subject" />:
	</form:label>
		&nbsp; 
	<form:input path="subject" />
	<form:errors cssClass="error" path="subject" />
	<br />
	<br />

	<form:label path="body">
		<spring:message code="message.body" />:
	</form:label>
	
		&nbsp; 
	<form:textarea path="body" />
	<form:errors cssClass="error" path="body" />
	<br />
	<br />
	<form:label path="priority">
		<spring:message code="message.priority" />:
	</form:label>
	
		&nbsp;
	<form:select path="priority">
		<form:option label="LOW" value="LOW" />
		<form:option label="HIGH" value="HIGH" />
		<form:option label="NEUTRAL" value="NEUTRAL" />
	</form:select>
	<form:errors cssClass="error" path="priority" />
	<br />
	<br />


	<%--
		  * botones del formulario
		 --%>

	<jstl:choose>

		<jstl:when test="${!esNotificacion}">

			<input type="submit" name="save"
				value="<spring:message code="folder.message.save" />" />&nbsp; 
	</jstl:when>
		<jstl:when test="${esNotificacion}">
			<input type="submit" name="notification"
				formaction="message/administrator/edit.do"
				value="<spring:message code="message.notify" />" />&nbsp; 
	</jstl:when>
	</jstl:choose>
	<jstl:if test="${messageToEdit.id != 0 && !esNotificacion}">
		<input type="submit" name="delete"
			value="<spring:message code="message.delete" />"
			onclick="return confirm('<spring:message code="message.confirm.delete" />')" />&nbsp;
	</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="message.cancel" />"
		onclick="javascript: relativeRedir('folder/actor/list.do')" />

	<br />

</form:form>