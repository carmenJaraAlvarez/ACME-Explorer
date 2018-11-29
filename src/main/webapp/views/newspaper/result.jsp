
<!--  * display.jsp -->
<!--  * -->
<!--  * Copyright (C) 2017 Universidad de Sevilla -->
<!--  *  -->
<!--  * The use of this project is hereby constrained to the conditions of the  -->
<!--  * TDG Licence, a copy of which you may download from  -->
<!--  * http://www.tdg-seville.info/License.html -->


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
	<jstl:choose>
		<jstl:when test="${messageErrorPublisher eq true}">
			<spring:message code="newspaper.error.publisher"/>
		</jstl:when>
		<jstl:when test="${messageErrorPublished eq true}">
			<spring:message code="newspaper.error.published"/>
		</jstl:when>
		<jstl:when test="${messageErrorArticles eq true}">
			<spring:message code="newspaper.error.articles"/>
		</jstl:when>
		<jstl:when test="${messageErrorDeleted eq true}">
			<spring:message code="newspaper.error.deleted"/>
		</jstl:when>
	</jstl:choose>
</div>

<div>
	<a href="./"> <spring:message code="newspaper.back.index"></spring:message></a>
</div>

