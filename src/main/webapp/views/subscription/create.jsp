

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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:choose>
	<jstl:when test="${notExists}">
		<h3>
			<spring:message code="subscription.notExists" />
		</h3>
	</jstl:when>
	<jstl:otherwise>
		<div>
			<h2>
				<jstl:out value="${title}" />
			</h2>
			
		</div>
		
		<div>
			<!-- subscription form -->
			<form:form action="${formURL}" modelAttribute="subscriptionForm">
		
			<form:hidden path="id" />
			<form:hidden path="version" />
			<form:hidden path="objectId" />
			<fieldset>
				<legend><spring:message code="subscription.creditcard" /></legend>
			
				<acme:textbox code="subscription.creditcard.holdername" path="creditCard.holderName" placeholder="John Doe" />
				<br>
				<acme:textbox code="subscription.creditcard.number" path="creditCard.num" placeholder="1111222233334444"/>
				<br>
				<acme:textbox code="subscription.creditcard.brand" path="creditCard.brand" placeholder="Visa, MasterCard, ..."/>
				<br>
				<acme:textbox code="subscription.creditcard.cvv" path="creditCard.CVV" placeholder="999" />
				<br>
				<acme:textbox code="subscription.creditcard.expirationMonth" path="creditCard.expirationMonth" placeholder="MM" />
				<br>
				<spring:message code="subscription.creditcard.yy" var="year"/>
				<acme:textbox code="subscription.creditcard.expirationYear" path="creditCard.expirationYear" placeholder="${year}"/>    
			</fieldset>         
			<br>
			<input type="submit" name="save"
				value="<spring:message code="subscription.save" />" />&nbsp; 
			
			<input type="button" name="cancel"
				value="<spring:message code="subscription.cancel" />"
				onclick="javascript: relativeRedir('${cancelURL}')" />
			<br />
		</form:form>
		</div>
	</jstl:otherwise>
</jstl:choose>





