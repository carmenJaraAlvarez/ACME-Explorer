<%--
 * list.jsp
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
 * modelo con colleccion primer nivel llamada fathers
 y carpeta selected, que en ppio será, inbox
 y coleccion de subcategorias children
 --%>
<%--
 * acceso para actor ppal
 --%>


<div>
	<jstl:forEach items="${fathers}" var="folder">
		<a href="folder/actor/list.do?folderId=${folder.id}">
		<jstl:out value="${folder.name}"/>
			</a> | 
	</jstl:forEach>
</div>
<br />
<div>
	<b> <spring:message code="message.selected" />: <jstl:out
			value="${selected.name}" />
	</b>
</div>
<b> <spring:message code="message.children" />:
</b>

<jstl:forEach items="${children}" var="child">
	<a href="folder/actor/list.do?folderId=${child.id}"> <jstl:out value="${child.name}"/></a> | 
	</jstl:forEach>
<br>
<%--
 * *****edit y create folder*********************
 --%>

<br />
<div>
	<jstl:if test="${!selected.ofTheSystem}">
		<input type="button" name="editFolder"
			value="<spring:message code="folder.edit" />"
			onclick="javascript: relativeRedir('folder/actor/edit.do?folderId=${selected.id}');" />

	</jstl:if>


	<input type="button" name="newFolder"
		value="<spring:message code="folder.create" />"
		onclick="javascript: relativeRedir('folder/actor/create.do?folderId=${selected.id}');" />


</div>
<hr>
<%--
 * *****mensajes*********************
 --%>
<h1>

	<spring:message code="folder.message.title" var="title" />

	<jstl:out value="${title}" />
</h1>
<%--
 * *****new message*********************
 --%>

<input type="button" name="newMessage"
	value="<spring:message code="folder.message.new" />"
	onclick="javascript: relativeRedir('message/actor/create.do?folderId=${selected.id}');" />

<%--
 * *****notificacion sólo administrador*********************
 --%>


<security:authorize access="hasRole('ADMIN')">

	<input type="button" name="notification"
		value="<spring:message code="folder.message.notification" />"
		onclick="javascript: relativeRedir('message/administrator/notification.do');" />
</security:authorize>


<br>


<display:table name="messages" id="rowMessages" pagesize="3"
	class="displaytag" requestURI="folder/actor/list.do">


	<jstl:if test="${!isNotificationBox}">
		<display:column 
			titleKey="folder.message.sender" >
			<jstl:out value="${rowMessages.actorSender.name }"/>
			</display:column>

	</jstl:if>



	<display:column titleKey="folder.message.subject" >
	<jstl:out value="${rowMessages.subject}"/>
	</display:column>
	
	<display:column titleKey="folder.message.body" >
		<jstl:out value="${rowMessages.body}"/>
	</display:column>

	<display:column property="priority" titleKey="folder.message.priority" />


	<spring:message code="folder.messages.date" var="date" />
	<spring:message code="folder.moment.format" var="folderDateFormat" />
	<display:column property="sendMoment" title="${date}" sortable="true"
		format="{0,date,${folderDateFormat}}" />

	<display:column>
		<a
			href="message/actor/move.do?messageId=${rowMessages.id}
		&&folderId=${selected.id}
		&&newFolderId=${selected.id}">
			<spring:message code="folder.message.move" />
		</a>
	</display:column>

	<jstl:if test="${isTrash}">
		<display:column>
	
			<a
				href="message/actor/delete.do?messageId=${rowMessages.id}&&folderId=${selected.id}" onclick="return confirm('<spring:message code="folder.message.confirm.delete" />')">
					<spring:message code="folder.delete" />
			</a>
	
		</display:column>
	</jstl:if>

</display:table>

