<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<div>
	<a href=""><img src="images/logo.png" alt="Acme-Newspaper., Inc." /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message
						code="master.page.login" /></a></li>
			<!-- Registrarse (Botón de Sign Up) -->
			<li><a><spring:message code="master.page.register" /> </a>
					<ul>
								<li class="isParent"></li>
								<li><a href="actor/createUser.do"><spring:message
						code="master.page.signUpUser" /></a></li>
								<li><a href="actor/createCustomer.do"><spring:message
						code="master.page.signUpCustomer" /></a></li>
								<li><a href="actor/createAgent.do"><spring:message
						code="master.page.signUpAgent" /></a></li>
					</ul></li>
			
			
			
		
		</security:authorize>

		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv" href="actor/myDisplay.do"> <spring:message
						code="master.page.profile" /> (<security:authentication
						property="principal.username" />)
			</a>
				<ul>
					<li class="arrow"></li>
					<li><a href="folder/actor/list.do"><spring:message
								code="master.page.message" /> </a></li>
					<security:authorize access="hasRole('USER')">
						<li><a href="newspaper/user/myList.do"><spring:message
									code="master.page.newspaper.list" /></a></li>
						<li><a href="chirp/user/stream.do"><spring:message
									code="master.page.chirp.stream" /></a></li>
						<li><a href="user/followers.do"><spring:message
									code="master.page.followers" /></a></li>
						<li><a href="user/following.do"><spring:message
									code="master.page.following" /></a></li>
						<li><a href="article/user/list.do"><spring:message
									code="master.page.user.articles" /></a></li>

					</security:authorize>


					<security:authorize access="hasRole('ADMIN')">
						<li><a href="article/administrator/list.do"><spring:message
									code="master.page.administrator.articles" /></a></li>
						<li><a href="chirp/administrator/list.do"><spring:message
									code="master.page.administrator.chirps" /></a></li>
						<li><a href="advertisement/administrator/list.do"><spring:message
									code="master.page.administrator.advertisements" /></a></li>
						<li><a><spring:message code="master.page.taboo" /> </a>
							<ul>
								<li class="isParent"></li>
								<li><a href="article/administrator/taboo.do"><spring:message
											code="master.page.administrator.articlesrevision" /></a></li>
								<li><a href="chirp/administrator/taboo.do"><spring:message
											code="master.page.administrator.chirpsrevision" /></a></li>
								<li><a href="newspaper/administrator/taboo.do"><spring:message
											code="master.page.administrator.newspaperrevision" /></a></li>
								<li><a href="advertisement/administrator/taboo.do"><spring:message
											code="master.page.administrator.advertisementrevision" /></a></li>
							</ul></li>
						<li><a href="global/administrator/edit.do"><spring:message
									code="master.page.admin.globalParams" /></a></li>
						<li><a href="dashboard/administrator/display.do"><spring:message
									code="master.page.administrator.dashboard" /></a></li>


					</security:authorize>

					<security:authorize access="hasRole('AGENT')">
						<li><a href="advertisement/agent/list.do"><spring:message
									code="master.page.agent.advertisements" /></a></li>
						<li><a><spring:message
									code="master.page.general.newspaperList" /> </a>
							<ul>
								<li class="isParent"></li>
								<li><a href="newspaper/agent/list.do?id=1"><spring:message
											code="master.page.agent.newspaperWith" /></a></li>
								<li><a href="newspaper/agent/list.do?id=0"><spring:message
											code="master.page.agent.newspaperWithout" /></a></li>
							</ul></li>
					</security:authorize>

					<li><a href="j_spring_security_logout"><spring:message
								code="master.page.logout" /> </a></li>
				</ul></li>
		</security:authorize>

		<security:authorize access="permitAll">
			<security:authorize access="hasRole('ADMIN')">
				<li><a href="newspaper/administrator/list.do"><spring:message
							code="master.page.general.newspaperList" /></a></li>
			</security:authorize>
			<security:authorize access="!hasRole('ADMIN')">
				<li><a href="newspaper/publicList.do"><spring:message
							code="master.page.general.newspaperList" /></a></li>
			</security:authorize>
			<li><a href="volume/list.do"><spring:message
						code="master.page.general.volumeList" /></a></li>
			<li><a href="user/list.do"><spring:message
						code="master.page.general.usersList" /></a></li>
			<li><a href="searcher/display.do"><spring:message
						code="master.page.general.searcher" /></a></li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

