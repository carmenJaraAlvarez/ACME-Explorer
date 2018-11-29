<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<link rel="stylesheet" href="styles/common.css" type="text/css">



<display:table pagesize="5" class="displaytag" keepStatus="true" name="volumes" requestURI="${uri}" id="rowVolume">

	<display:column titleKey="volume.title">
		<a href="volume/display.do?volumeId=${rowVolume.id}">
			<jstl:out value="${rowVolume.title}" />
		</a>
	</display:column>
	<display:column  titleKey="volume.description" >
	  <jstl:out value="${rowVolume.description}" />
	</display:column>
	<display:column property="year" titleKey="volume.year" />

	<display:column titleKey="volume.newspapers.size">
		${fn:length(rowVolume.newspapers)}
	</display:column>

	<security:authorize access="hasRole('USER')">
		<display:column titleKey="volume.edit">
			<jstl:if test="${rowVolume.publisher eq user}">
				<a href="volume/user/edit.do?volumeId=${rowVolume.id}">
					<spring:message code="volume.edit" />
				</a>
			</jstl:if>
		</display:column>
	</security:authorize>


</display:table>

<security:authorize access="hasRole('USER')">
	<div>
		<a href="volume/user/edit.do">
			<spring:message code="volume.create" />
		</a>
	</div>
</security:authorize>


