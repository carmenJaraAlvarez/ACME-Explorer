<%--
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table pagesize="3" class="displaytag" keepStatus="true"
	name="advertisements" requestURI="${uri}" id="rowAdvertisement">
	<display:column> 
		<a href="${rowAdvertisement.targetPage}"><img src="${rowAdvertisement.banner}"  width="80px" height="80px"/></a>
	</display:column>

	<display:column titleKey="advertisement.title" ><jstl:out value="${rowAdvertisement.title }"/></display:column>
	<display:column titleKey="advertisement.targetPage">
			<div>
					<a href="${rowAdvertisement.targetPage}">
					 <jstl:out value="${rowAdvertisement.targetPage}" />
										</a>
				</div>
	</display:column>
	

	<display:column titleKey="advertisement.creditCard.num" >
	 <jstl:out value="${rowAdvertisement.creditCard.num}" />
	</display:column>
	<display:column property="creditCard.expirationMonth" titleKey="advertisement.creditCard.month" />
	<display:column property="creditCard.expirationYear" titleKey="advertisement.creditCard.year" />
		
<display:column titleKey="advertisement.newspaper" ><jstl:out value="${rowAdvertisement.newspaper.title }"/></display:column>
<security:authorize access="hasRole('ADMIN')">
		<display:column>
				<jstl:if test="${fromTabooList}">
				<a href="advertisement/administrator/taboo/delete.do?advertisementId=${rowAdvertisement.id}"
					onclick="return confirm('<spring:message code="advertisement.confirm.cancel" />')">
					<spring:message code="advertisement.delete" />
				</a>
				</jstl:if>
				<jstl:if test="${!fromTabooList}">
				<a href="advertisement/administrator/delete.do?advertisementId=${rowAdvertisement.id}"
					onclick="return confirm('<spring:message code="advertisement.confirm.cancel" />')">
					<spring:message code="advertisement.delete" />
				</a>
				</jstl:if>
		</display:column>
		
	</security:authorize>
</display:table>
<security:authorize access="hasRole('AGENT')">



	<div>
		<a href="advertisement/agent/create.do"> <spring:message
				code="advertisement.register" />
		</a>
	</div>

	</security:authorize>

