<%--
 * action-1.jsp
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



<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.ast" />
	<spring:message code="administrator.dashboard.newsUser" />
</h3>
<table style="width: 45%">
	<tr>
		<th><spring:message code="administrator.dashboard.avg" /></th>
		<th><jstl:out value="${avgNewsPerUser}" /></th>
	</tr>
	<tr>
		<th><spring:message code="administrator.dashboard.stdev" /></th>
		<th><jstl:out value="${stdvNewsPerUser}" /></th>
	</tr>
</table>

<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.ast" />
	<spring:message code="administrator.dashboard.articlesUser" />
</h3>
<table style="width: 45%">
	<tr>
		<th><spring:message code="administrator.dashboard.avg" /></th>
		<th><jstl:out value="${avgArticlesPerUser}" /></th>
	</tr>
	<tr>
		<th><spring:message code="administrator.dashboard.stdev" /></th>
		<th><jstl:out value="${stdvArticlesPerUser}" /></th>
	</tr>
</table>

<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.ast" />
	<spring:message code="administrator.dashboard.articlesNews" />
</h3>
<table style="width: 45%">
	<tr>
		<th><spring:message code="administrator.dashboard.avg" /></th>
		<th><jstl:out value="${avgArticlesPerNews}" /></th>
	</tr>
	<tr>
		<th><spring:message code="administrator.dashboard.stdev" /></th>
		<th><jstl:out value="${stdvArticlesPerNews}" /></th>
	</tr>
</table>

<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.newsArticlesPlus10" />
</h3>

<display:table style="width: 45%" pagesize="3" class="displaytag"
	keepStatus="true" name="newsMore10"
	requestURI="${requestURI}#newsMore10" id="rowMore10">

	<display:column property="title" titleKey="newspaper.title" />
	<display:column property="description" titleKey="newspaper.description" />
	<display:column property="publisher.name"
		titleKey="newspaper.publisher" />
</display:table>

<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.newsArticlesLess10" />
</h3>

<display:table style="width: 45%" pagesize="3" class="displaytag"
	keepStatus="true" name="newsLess10"
	requestURI="${requestURI}#newsLess10" id="rowLess10">

	<display:column property="title" titleKey="newspaper.title" />
	<display:column property="description" titleKey="newspaper.description" />
	<display:column property="publisher.name"
		titleKey="newspaper.publisher" />
</display:table>


<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.ratio" />
	<spring:message code="administrator.dashboard.creatorsNews" />
</h3>
<table style="width: 45%">
	<tr>
		<th><jstl:out value="${ratioCreatorNews}%" /></th>
	</tr>
</table>

<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.ratio" />
	<spring:message code="administrator.dashboard.creatorsArticles" />
</h3>
<table style="width: 45%">
	<tr>
		<th><jstl:out value="${ratioCreatorArticle}%" /></th>
	</tr>
</table>

<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.avg" />
	<spring:message code="administrator.dashboard.followsArticle" />
</h3>
<table style="width: 45%">
	<tr>
		<th><jstl:out value="${avgFollowUpsPerArticle}" /></th>
	</tr>
</table>

<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.avg" />
	<spring:message code="administrator.dashboard.followsArticle1Week" />
</h3>
<table style="width: 45%">
	<tr>
		<th><jstl:out value="${avgFollowUpsPerArticle1Week}" /></th>
	</tr>
</table>


<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.avg" />
	<spring:message code="administrator.dashboard.followsArticle2Week" />
</h3>
<table style="width: 45%">
	<tr>
		<th><jstl:out value="${avgFollowUpsPerArticle2Week}" /></th>
	</tr>
</table>

<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.chirps75" />
</h3>
<table style="width: 45%">
	<tr>
		<th><jstl:out value="${ratioUserChirpsOver75}%" /></th>
	</tr>
</table>

<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.ratio" />
	<spring:message code="administrator.dashboard.publicPrivate" />
</h3>
<table style="width: 45%">
	<tr>
		<th><jstl:out value="${ratioPublicVsPrivate}%" /></th>
	</tr>
</table>

<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.avg" />
	<spring:message code="administrator.dashboard.articlesPrivateNews" />
</h3>
<table style="width: 45%">
	<tr>
		<th><jstl:out value="${avgArticlesPerPrivateNews}" /></th>
	</tr>
</table>

<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.avg" />
	<spring:message code="administrator.dashboard.articlesPublicNews" />
</h3>
<table style="width: 45%">
	<tr>
		<th><jstl:out value="${avgArticlesPerPublicNews}" /></th>
	</tr>
</table>

<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.ratio" />
	<spring:message code="administrator.dashboard.suscribersPerPrivate" />
</h3>
<table style="width: 45%">
	<tr>
		<th><jstl:out value="${ratioPrivateVsPublicPerPublisher}%" /></th>
	</tr>
</table>

<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.ratio" />
	<spring:message
		code="administrator.dashboard.privatePublicPerPublisher" />
</h3>
<table style="width: 45%">
	<tr>
		<th><jstl:out value="${ratioSuscribersPerPrivateVsCustomers}%" /></th>
	</tr>
</table>

<!-- Req 2.5.3 -->
<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.ratio" />
	<spring:message
		code="administrator.dashboard.newspaperWithAdverVsWithout" />
</h3>
<table style="width: 45%">
	<tr>
		<th><jstl:out value="${ratioNewspAdverWithVsWithout}%" /></th>
	</tr>
</table>

<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.ratio" />
	<spring:message
		code="administrator.dashboard.advetisementsTabooVsTotal" />
</h3>
<table style="width: 45%">
	<tr>
		<th><jstl:out value="${ratioAvertisementsTabooVsTotal}%" /></th>
	</tr>
</table>

<!-- Req 11.1 -->
<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.avg" />
	<spring:message code="administrator.dashboard.newspapersPerVolume" />
</h3>
<table style="width: 45%">
	<tr>
		<th><jstl:out value="${averageNewspapersPerVolume}" /></th>
	</tr>
</table>

<h3 style="text-decoration: underline;">
	<spring:message code="administrator.dashboard.ratio" />
	<spring:message
		code="administrator.dashboard.subscriptionsNewsVsVol" />
</h3>
<table style="width: 45%">
	<tr>
		<th><jstl:out value="${ratioSubsNewsVsVol}%" /></th>
	</tr>
</table>
