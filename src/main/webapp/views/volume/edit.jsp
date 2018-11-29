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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form modelAttribute="volumeForm">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox code="volume.title" 			path="title" />
	<br />
	<acme:textbox code="volume.description" 	path="description" />
	
	<br />
 	<fieldset>
	    <legend><spring:message code="volume.newspapers" /></legend>
		<jstl:forEach var="newspaper" items="${newspapers}">
			<form:checkbox path="newspapers" value="${newspaper.id}" id="${newspaper.id}"/>
			<label for="${newspaper.id}"><jstl:out value="${newspaper.title}"></jstl:out></label>
			<br />
		</jstl:forEach>
	</fieldset>
	<br/>
	
	<acme:submit name="save" code="newspaper.save" />
	<acme:cancel code="newspaper.cancel" url="volume/list.do"/>



</form:form>