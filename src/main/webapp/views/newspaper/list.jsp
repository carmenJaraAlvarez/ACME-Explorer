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

<link rel="stylesheet" href="styles/common.css" type="text/css">

<spring:message code="newspaper.Date.format" var="newspaperDateFormat" />

<display:table pagesize="5" class="displaytag" 
	name="newspapers" requestURI="${uri}" id="rowNewspaper">

	<display:column titleKey="newspaper.title" ><jstl:out value="${rowNewspaper.title }"/></display:column>
	<display:column titleKey="newspaper.description" ><jstl:out value="${rowNewspaper.description }"/></display:column>
	
	<display:column titleKey="newspaper.publisher" ><jstl:out value="${rowNewspaper.publisher.userAccount.username }"/></display:column>
	<display:column property="pubDate" 		titleKey="newspaper.pubDate" format="{0,date,${newspaperDateFormat}}" />

	<display:column>
		<a href="newspaper/display.do?newspaperId=${rowNewspaper.id}"> <spring:message
				code="newspaper.display" />
		</a>
	</display:column>

	<security:authorize access="hasRole('USER')">
		<display:column>
			<jstl:if test="${rowNewspaper.draft eq true}">
				<a href="newspaper/user/publish.do?newspaperId=${rowNewspaper.id}"
					onclick="return confirm('<spring:message code="newspaper.confirm.publish" />')">
					<spring:message code="newspaper.publish" />
				</a>
			</jstl:if>
			<jstl:if test="${rowNewspaper.draft eq false}">
				<spring:message code="newspaper.published" />
			</jstl:if>
		</display:column>
	</security:authorize>

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a
				href="newspaper/administrator/${delete}.do?newspaperId=${rowNewspaper.id}"
				onclick="return confirm('<spring:message code="newspaper.confirm.delete" />')">
				<spring:message code="newspaper.delete" />
			</a>
		</display:column>
	</security:authorize>

</display:table>

<jstl:if test="${showCreateButton eq true}">
	<security:authorize access="hasRole('USER')">
		<div>
			<a href="newspaper/user/edit.do"> <spring:message
					code="newspaper.create" />
			</a>
		</div>
	</security:authorize>
</jstl:if>


<!-- Botón de create cuando toque -->
