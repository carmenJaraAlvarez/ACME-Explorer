

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


<div>
	<h2>
		<jstl:out value="${subscription.newspaper.title}" />
	</h2>
	
</div>

<div>

	<!-- tarjeta de crédito -->
	<form:form action="newspaper/customer/subscription.do" modelAttribute="subscription">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="customer" />
	<form:hidden path="newspaper" />
	
	<spring:message code="newspaper.creditCard" var="card" />

	<h3>
		<jstl:out value="${card }" />
	</h3>

	<acme:textbox code="newspaper.creditcard.holderName" path="creditCard.holderName" placeholder="John Doe" />
	<br>
	<acme:textbox code="newspaper.creditcard.number" path="creditCard.num" placeholder="1111222233334444"/>
	<br>
	<acme:textbox code="newspaper.creditcard.cvv" path="creditCard.CVV" placeholder="999" />
	<br>
	<acme:textbox code="newspaper.creditcard.expirationMonth" path="creditCard.expirationMonth" placeholder="MM" />
	<br>
	<spring:message code="newspaper.creditcard.yy" var="y"/>
	<acme:textbox code="newspaper.creditcard.expirationYear" path="creditCard.expirationYear" placeholder="${y}"/>             
	<br>

	<input type="submit" name="save"
		value="<spring:message code="newspaper.save" />" />&nbsp; 
	
	<input type="button" name="cancel"
		value="<spring:message code="newspaper.cancel" />"
		onclick="javascript: relativeRedir('newspaper/display.do?newspaperId=${newspaperId}')" />
	<br />
</form:form>
</div>




