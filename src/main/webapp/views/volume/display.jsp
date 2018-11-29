
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
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<jstl:if test="${!notExists}">
	<div>
		<h1>
			<jstl:out value="${volume.title}" />
		</h1>
		<h2>
			<jstl:out value="${volume.description}" />
		</h2>
	</div>
	
	<div>
	<security:authorize access="hasRole('CUSTOMER')">
			<!-- botón de suscribirse -->
		<jstl:if test="${!controlSubscription}">
			<a href="volume/customer/subscribe.do?objectId=${volume.id}"> <spring:message
					code="volume.subscription" />
			</a>
			</jstl:if>
			
			</security:authorize>
	</div>
	
	<div>
		<!-- Lista de periódicos -->
		<spring:message code="volume.newspaper.Date.format" var="newspaperDateFormat" />
		
		<display:table pagesize="5" class="displaytag" keepStatus="true"
			name="newspapers" requestURI="volume/display.do" id="rowNewspaper">
	
			<display:column titleKey="volume.newspaper.title">
				<a href="newspaper/display.do?newspaperId=${rowNewspaper.id}">
					<jstl:out value="${rowNewspaper.title}" />
				</a>
			</display:column>
			<display:column 		titleKey="volume.newspaper.publisher.name" >
			<jstl:out value="${rowNewspaper.publisher.name}" />
			</display:column>
			<display:column property="pubDate" 		titleKey="volume.newspaper.pubDate" format="{0,date,${newspaperDateFormat}}" />
			<display:column titleKey="volume.newspaper.articles.size">
				${fn:length(rowNewspaper.articles)}
			</display:column>
			
		</display:table>
	</div>
</jstl:if>
<jstl:if test="${notExists}">
	<spring:message code="volume.error.notExists" />
</jstl:if>