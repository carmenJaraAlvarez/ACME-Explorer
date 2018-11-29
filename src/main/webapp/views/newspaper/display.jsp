
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
<jstl:if test="${!deleted}">



	<div>
		<h1>
			<jstl:out value="${newspaper.title}" /> <span style="font-size: 17px;"><jstl:out value="${newspaper.pubDate}" /></span>
		</h1>
		<h2>
			<jstl:out value="${newspaper.description}" />
		</h2>
		<img src="${newspaper.picture}" alt="${newspaper.picture}" height="100px">
	</div>

	<div>
		<security:authorize access="hasRole('CUSTOMER')">
			<!-- bot�n de suscribirse -->
			<jstl:if test="${!controlSubscription}">
				<a href="newspaper/customer/subscribe.do?objectId=${newspaper.id}">
					<spring:message code="newspaper.subscription" />
				</a>
			</jstl:if>

		</security:authorize>
	</div>

	<div>
		<!-- Lista de art�culos -->
		<spring:message code="article.Date.format" var="articleDateFormat" />

		<display:table pagesize="5" class="displaytag" keepStatus="true"
			name="articles" requestURI="newspaper/display.do" id="rowArticle">

			<jstl:if test="${controlSubscription}">
				<display:column titleKey="article.title">
					<a href="article/display.do?articleId=${rowArticle.id}"> <jstl:out
							value="${rowArticle.title}" />
					</a>
				</display:column>
			</jstl:if>
			<jstl:if test="${!controlSubscription}">
				<display:column titleKey="article.title">
					<jstl:out value="${rowArticle.title }" />
				</display:column>
			</jstl:if>

			<display:column titleKey="article.writer.name">
				<a href="actor/display.do?actorId=${rowArticle.writer.id}"> <jstl:out
						value="${rowArticle.writer.name }" />
				</a>
			</display:column>
			<display:column titleKey="article.summary" >
				<spring:message code="newspaper.more" var="more" />
				
				<div id="visto${rowArticle.id }"> 
				<jstl:out value="${ss}" />
				<script>			
					var ss = "${rowArticle.summary}".substring(0, 12);		
					document.write(ss);
					var i="${rowArticle.summary}".length;

					if(i>11){
						document.write("<input type='button' value='${more}' onClick='document.getElementById(\"oculto${rowArticle.id }\").style.display=\"block\"; document.getElementById(\"visto${rowArticle.id }\").style.display=\"none\"'>");
					}
				</script>	
				
				 <br> <br>
				</div> 
				<div id="oculto${rowArticle.id }" style="display:none"> 
				<jstl:out value="${rowArticle.summary}" /><br><br>
				</div>
								
			</display:column>


			
			<display:column titleKey="newspaper">
				<jstl:out value="${rowArticle.newspaper.title }" />
			</display:column>
			<display:column property="pubMoment" titleKey="article.pubMoment"
				format="{0,date,${articleDateFormat}}" />

		</display:table>
	</div>
</jstl:if>
<jstl:if test="${deleted}">
	<spring:message code="newspaper.error.deleted" />
</jstl:if>



