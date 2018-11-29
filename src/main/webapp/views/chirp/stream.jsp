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


<spring:message code="chirp.Date.format" var="chirpDateFormat" />

<display:table pagesize="5" class="displaytag" 
	name="chirps" requestURI="${uri}" id="rowChirp">

	<display:column titleKey="chirp.user">
		<a href="actor/display.do?actorId=${rowChirp.user.id}"> <jstl:out
				value="${rowChirp.user.userAccount.username}" />
		</a>
	</display:column>
	<display:column titleKey="chirp.title" >
	<jstl:out value="${rowChirp.title }"/>
	</display:column>
	<display:column titleKey="chirp.description" >
	<jstl:out value="${rowChirp.description }"/>
	</display:column>
	<display:column property="pubMoment" titleKey="chirp.pubMoment"
		format="{0,date,${chirpDateFormat}}" />
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<jstl:if test="${fromGeneralList}">
				<a href="chirp/administrator/general/delete.do?chirpId=${rowChirp.id}"
					onclick="return confirm('<spring:message code="chirp.confirm.delete" />')">
					<spring:message code="chirp.delete" />
				</a>
			</jstl:if>
			<jstl:if test="${fromTabooList}">
				<a href="chirp/administrator/taboo/delete.do?chirpId=${rowChirp.id}"
					onclick="return confirm('<spring:message code="chirp.confirm.delete" />')">
					<spring:message code="chirp.delete" />
				</a>
			</jstl:if>
		</display:column>
	</security:authorize>

</display:table>

<!-- Botón de create cuando toque -->