
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
	<jstl:if test="${randomAdvertisement== null}">
		<spring:message code="newspaper.advertisementsite" />
		
	</jstl:if>
	<jstl:if test="${randomAdvertisement != null}">
		<a href="${randomAdvertisement.targetPage}"><img src="${randomAdvertisement.banner}"  height="100px"/></a>
	</jstl:if>
</div>
	<jstl:if test="${controlSubscription}">
		<div>
			<h2>
				<jstl:out value="${article.title}" />
			</h2>
			<jstl:out value="${article.summary}" /><br><br>
			<jstl:forEach items="${pictures}" var="picture">
				<img width="100px" height="100px" src="${picture}" alt="${picture}">
			</jstl:forEach>
			<p>
				<jstl:out value="${article.body}" />
			</p>
		</div>
		
		<jstl:if test="${followUps.size()!=0}">
			<div>
				<!-- Lista de Follow Ups -->
				<h3>
					<spring:message code="article.followUps" />
				</h3>
				<spring:message code="article.Date.format" var="articleDateFormat" />
				
				<display:table pagesize="5" class="displaytag" keepStatus="true"
					name="filterFollowUps" requestURI="article/display.do" id="rowArticleFollowUp">
			
					<display:column titleKey="article.title" >
						<a href="article/display.do?articleId=${rowArticleFollowUp.id}" ><jstl:out value="${rowArticleFollowUp.title}" /></a>
					</display:column>
					<display:column 	titleKey="article.writer.name" >
						<jstl:out value="${rowArticleFollowUp.writer.name}" />
					</display:column>
					<display:column titleKey="article.summary" >
						<spring:message code="newspaper.more" var="more" />
						<div id="visto${rowArticleFollowUp.id }"> 
							<script>		
								var ss = '<jstl:out value="${rowArticleFollowUp.summary}" />'.substring(0, 12);	

								
								document.write(ss);
								var i="${rowArticleFollowUp.summary}".length;
			
								if(i>11){
									document.write("<input type='button' value='${more}' onClick='document.getElementById(\"oculto${rowArticleFollowUp.id }\").style.display=\"block\"; document.getElementById(\"visto${rowArticleFollowUp.id }\").style.display=\"none\"'>");
								}
							</script>	
							<jstl:out value="${variable}" />
						</div> 
						<div id="oculto${rowArticleFollowUp.id }" style="display:none"> 
							<jstl:out value="${rowArticleFollowUp.summary}" />
						</div>
					</display:column>
					
					<display:column 	titleKey="newspaper" >
					<jstl:out value="${rowArticleFollowUp.newspaper.title}" />
					</display:column>
					<display:column property="pubMoment" 		titleKey="article.pubMoment" format="{0,date,${articleDateFormat}}" />
			
				</display:table>
			</div>
		</jstl:if>
	</jstl:if>
	<jstl:if test="${!controlSubscription}">
		<spring:message code="article.private" />
	</jstl:if>
</jstl:if>
<jstl:if test="${deleted}">
	<spring:message code="article.article.deleted" />
</jstl:if>

