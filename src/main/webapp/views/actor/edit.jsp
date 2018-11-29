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

<form:form action="${direction}" modelAttribute="actorForm">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox path="name" code="actor.name" placeholder="John" />
	<br />
	<acme:textbox path="surname" code="actor.surname" placeholder="Due Due" />
	<br />
	<acme:textbox path="email" code="actor.email" placeholder="email@client.domain" />
	<acme:textbox path="tlfn" code="actor.tlfn" />
	<acme:textbox path="address" code="actor.address" placeholder="S/Street nºX"/>
	
	<acme:submit name="save" code="actor.save"/>

	<input type="button" name="cancel"
		value="<spring:message code="actor.cancel" />"
		onclick="javascript: relativeRedir('${requestUri}');" />
	<br />

</form:form>