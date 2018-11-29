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

<form:form modelAttribute="newspaper">

	<form:hidden path="id" />
	<form:hidden path="version" />
<%-- 	<form:hidden path="publisher" /> --%>
<%-- 	<form:hidden path="pubDate" /> --%>
<%-- 	<form:hidden path="articles"/> --%>
<%-- 	<form:hidden path="advertisements" /> --%>
<%-- 	<form:hidden path="draft" /> --%>
	
	<spring:message var="yes" code="newspaper.yes"></spring:message>

	<acme:textbox code="newspaper.title" 		path="title" />
	<acme:textbox code="newspaper.description" 	path="description" />
	<acme:textbox code="newspaper.picture"	 	path="picture" />
	<spring:message code="newspaper.private" />
	<form:radiobutton path="isPrivate" value="false" label="No"/>
	<form:radiobutton path="isPrivate" value="true" label="${yes}"/>
	
	
	<br/>
	
	<acme:submit name="save" code="newspaper.save" />
	<acme:cancel code="newspaper.cancel" url="newspaper/user/myList.do"/>



</form:form>