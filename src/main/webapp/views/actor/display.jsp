
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
	<spring:message code="article.Date.format" var="articleDateFormat" />
<!-- Para mostrar bot�n de follow/unfollow y mensaje de operaci�n -->
<security:authorize access="hasRole('USER')">
	<jstl:if test="${actorType eq 'user' and canFollow}">
		<jstl:choose>
			<jstl:when test="${follow eq 'true'}">
				<div style="color: green;">
					<p>
						<spring:message code="actor.follow.sucessfull" />
						<jstl:out value="${actor.name}" />
					</p>
				</div>
			</jstl:when>
			<jstl:when test="${unfollow eq 'true'}">
				<div style="color: green;">
					<p>
						<spring:message code="actor.unfollow.sucessfull" />
						<jstl:out value="${actor.name}" />
					</p>
				</div>
			</jstl:when>
			<jstl:when test="${follow eq 'false'}">
				<div style="color: red;">
					<p>
						<spring:message code="actor.follow.failed" />
						<jstl:out value="${actor.name}" />
					</p>
				</div>
			</jstl:when>
			<jstl:when test="${unfollow eq 'false'}">
				<div style="color: red;">
					<p>
						<spring:message code="actor.unfollow.failed" />
						<jstl:out value="${actor.name}" />
					</p>
				</div>
			</jstl:when>
		</jstl:choose>
		<jstl:choose>
			<jstl:when test="${!isFollowing}">
				<div>
					<a href="user/follow.do?actorId=${actor.id}"> <spring:message
							code="actor.follow" />
					</a>
				</div>
			</jstl:when>
			<jstl:otherwise>
				<div>
					<a href="user/unfollow.do?actorId=${actor.id}"> <spring:message
							code="actor.unfollow" />
					</a>
				</div>
			</jstl:otherwise>
		</jstl:choose>
	</jstl:if>
</security:authorize>
<!-- FIN Para mostrar bot�n de follow/unfollow y mensaje de operaci�n -->

<table>
	<tr>
		<th><spring:message code="actor.name" /></th>
		<td><jstl:out value="${actor.name}" /></td>
	</tr>

	<tr>
		<th><spring:message code="actor.surname" /></th>
		<td><jstl:out value="${actor.surname}" /></td>
	</tr>
	<tr>
		<th><spring:message code="actor.address" /></th>
		<td><jstl:out value="${actor.address}" /></td>
	</tr>
	<tr>
		<th><spring:message code="actor.email" /></th>
		<td><jstl:out value="${actor.email}" /></td>
	</tr>
	<tr>
		<th><spring:message code="actor.tlfn" /></th>
		<td><jstl:out value="${actor.tlfn}" /></td>
	</tr>
</table>
<jstl:if test="${actorType eq 'user'}">
	<h3 id="chirpList">
		<spring:message code="user.chirps.list" />
	</h3>
	<spring:message code="chirp.Date.format" var="userChirpDateFormat" />
	<display:table name="userChirps" id="chirpRow" pagesize="5"
		requestURI="${articleURI}#chirpList">
		<display:column property="title" titleKey="user.title" />
		<display:column property="description" titleKey="user.description" />
		<display:column property="pubMoment" format="{0,date,${userChirpDateFormat}}" titleKey="user.pubMoment" />
	</display:table>

	<security:authorize access="hasRole('USER')">
		<jstl:if test="${isMyProfile}">
			<div>
				<a href="chirp/user/edit.do"> <spring:message
						code="actor.chirp.create" />
				</a>
			</div>
		</jstl:if>
	</security:authorize>

	<h3 id="articleList">
		<spring:message code="user.articles.list" />
	</h3>
	<spring:message code="article.Date.format" var="articleDateFormat" />
	<display:table name="userArticles" id="articleRow" pagesize="5"
		requestURI="${articleURI}#articleList">

		<display:column titleKey="user.title">
			<a href="article/display.do?articleId=${articleRow.id}"> <jstl:out
					value="${articleRow.title}" />
			</a>
		</display:column>
		<display:column titleKey="user.newspaper">
			<a href="newspaper/display.do?newspaperId=${articleRow.newspaper.id}">
				<jstl:out value="${articleRow.newspaper.title}" />
			</a>
		</display:column>
		<display:column property="pubMoment" format="{0,date,${articleDateFormat}}" titleKey="user.pubMoment" />
	</display:table>

	<security:authorize access="hasRole('USER')">
		<jstl:if test="${isMine}">
			<div>
				<a href="article/user/create.do"> <spring:message
						code="actor.user.article" />
				</a>
			</div>
		</jstl:if>
	</security:authorize>

</jstl:if>

<security:authorize access="isAuthenticated()">
	<jstl:if test="${isMyProfile}">
		<div>
			<a href="${actorType}/edit.do"> <spring:message code="actor.edit" />
			</a>
		</div>
	</jstl:if>
</security:authorize>
