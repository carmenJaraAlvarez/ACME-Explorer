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

<form:form action="advertisement/agent/edit.do" modelAttribute="advertisement">

	<form:hidden path="id" />
	<form:hidden path="version" />
		
	<acme:textbox code="advertisement.title" path="title" />
	<acme:textbox code="advertisement.banner" path="banner" />
	<acme:textbox code="advertisement.targetPage" path="targetPage" />
	<spring:message code="newspaper.creditCard" var="card" />
	
	<acme:select items="${newspapers}" itemLabel="title" code="advertisement.newspaper" path="newspaper"/>
	
	<h3>
		<jstl:out value="${card }" />
	</h3>

	<acme:textbox code="newspaper.creditcard.holderName" path="creditCard.holderName" placeholder="John Doe" />

	<acme:textbox code="newspaper.creditcard.number" path="creditCard.num" placeholder="1111222233334444"/>

	<acme:textbox code="advertisement.creditcard.brand" path="creditCard.brand" placeholder="VISA"/>
	
	<acme:textbox code="newspaper.creditcard.cvv" path="creditCard.CVV" placeholder="999" />

	<acme:textbox code="newspaper.creditcard.expirationMonth" path="creditCard.expirationMonth" placeholder="MM" />

	<spring:message code="newspaper.creditcard.yy" var="y"/>
	<acme:textbox code="newspaper.creditcard.expirationYear" path="creditCard.expirationYear" placeholder="${y}"/>             

	<br />
	<input type="submit" name="save"
		value="<spring:message code="advertisement.save" />" />&nbsp; 
	
<%-- 	
	<jstl:if test="${article.id != 0 }">
		<input type="submit" name="delete"
			value="<spring:message code="article.delete" />"
			onclick="return confirm('<spring:message code="article.confirm.cancel" />')" />&nbsp;
	</jstl:if>
--%>	
	<acme:cancel code="article.cancel" url="/advertisement/agent/list.do"/>
</form:form>



