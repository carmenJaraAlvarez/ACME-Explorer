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

<form:form action="${direction}" modelAttribute="createActorForm">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox path="username" code="actor.userAccount.username" />
	<br />
	<acme:password path="password" code="actor.userAccount.password" />
	<br />
	<acme:password path="password2" code="actor.userAccount.password" />
	<br />
	
	<acme:textbox path="name" code="actor.name" />
	<acme:textbox path="surname" code="actor.surname" />
	<acme:textbox path="email" code="actor.email" />

	<form:checkbox path="valida" />
	<a href="legal/terms.do">
		<spring:message code="actor.terms" />
	</a>
	<form:errors cssClass="error" path="valida" />
	<br />

	<acme:submit name="${save}" code="actor.save"/>

	<acme:cancel code="actor.cancel" url="/"/>

	<br />

</form:form>