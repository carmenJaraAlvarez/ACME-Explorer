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


<div>
	<h3>
		<spring:message code="message.how" />
		:
	</h3>
</div>

<div>
	<jstl:forEach items="${fathers}" var="folder">
		<a
			href="message/actor/move.do?messageId=${messageToEdit.id}
		&&folderId=${actual.id}
		&&newFolderId=${folder.id}">
			${folder.name}</a> | 
	</jstl:forEach>
</div>
<br>
<div>
	<b> <spring:message code="message.selected" />: <jstl:out
			value="${selected.name}" />
	</b>
</div>
<b> <spring:message code="message.children" />:
</b>

<jstl:forEach items="${children}" var="child">
	<a
		href="message/actor/move.do?messageId=${messageToEdit.id}
		&&folderId=${actual.id}
		&&newFolderId=${child.id}">
		${child.name}</a> | 
	</jstl:forEach>
<br>



<display:table name="messageToEdit" id="rowMessageEdit" class="displaytag">



	<display:column titleKey="folder.message.subject" >
		<jstl:out value="${rowMessageEdit.subject }"/>
	</display:column>
 
	<display:column titleKey="folder.message.body" >
	<jstl:out value="${rowMessageEdit.body }"/>
	</display:column>

	<display:column titleKey="folder.message.priority" >
	<jstl:out value="${rowMessageEdit.priority }"/>
	</display:column>


	<spring:message code="folder.messages.date" var="date" />
	<spring:message code="folder.moment.format" var="folderDateFormat" />
	<display:column property="sendMoment" title="${date}" sortable="true"
		format="{0,date,${folderDateFormat}}" />

	<spring:message code="folder.message.move" var="mover" />

	<display:column>
		<a
			href="message/actor/change.do?messageId=${rowMessageEdit.id}&&folderId=${actual.id}&&newFolderId=${selected.id}">
			<jstl:out value="${mover}: ${actual.name} -> ${selected.name}" />
		</a>
	</display:column>
	<display:column>
		<a href="folder/actor/list.do"> <spring:message
				code="folder.cancel" /></a>
	</display:column>

</display:table>


