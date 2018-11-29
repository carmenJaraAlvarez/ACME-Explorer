<%--
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:if test="${!notAuthorized}">
<form:form action="article/user/edit.do" modelAttribute="article">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="writer" />
	<form:hidden path="followUps" />
	<form:hidden path="pubMoment" />
	
	<acme:textbox code="article.title" path="title" />
	<acme:textbox code="article.summary" path="summary" />
	<acme:textarea code="article.body" path="body" />
	<acme:textarea code="article.pictures" path="pictures" /><%--poner en tag placeholder="URL1, URL2 ,URL3..." --%>
	
	<acme:select items="${articles}" itemLabel="title" code="article.father" path="father"/>

	<br />

	<form:label path="draft">
				<spring:message code="article.draft" />
				</form:label>
				<spring:message code="article.afirm" var="afirm" />
				<form:select id="draft" path="draft">
					<form:option value="true" label="${afirm}" />
					<form:option value="false" label="No" />
					
				</form:select>
				<br />
	<acme:select items="${newspapers}" itemLabel="title" code="article.newspaper" path="newspaper"/>

	<br />
	<input type="submit" name="save"
		value="<spring:message code="article.save" />" />&nbsp; 
	
<%-- 	
	<jstl:if test="${article.id != 0 }">
		<input type="submit" name="delete"
			value="<spring:message code="article.delete" />"
			onclick="return confirm('<spring:message code="article.confirm.cancel" />')" />&nbsp;
	</jstl:if>
--%>	
	<acme:cancel code="article.cancel" url="/article/user/list.do"/>
</form:form>
</jstl:if>
<jstl:if test="${notAuthorized}">
	<spring:message code="article.error" />
</jstl:if>

