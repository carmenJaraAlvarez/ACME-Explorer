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

		<spring:message code="article.Date.format" var="articleDateFormat" />
<display:table pagesize="5" class="displaytag" 
	name="articles" requestURI="${uri}" id="rowArticle">

	<display:column titleKey="article.title">
		<a href="article/display.do?articleId=${rowArticle.id}"><jstl:out
				value="${rowArticle.title}" /></a>
	</display:column>


	
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
					var ss = '<jstl:out value="${rowArticle.summary}" />'.substring(0, 12);	
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
	
	
	
	
	
	
	
	
	

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<jstl:if test="${fromGeneralList}">
				<a
					href=article/administrator/general/delete.do?articleId=${rowArticle.id}
					onclick="return confirm('<spring:message code="article.confirm.cancel" />')">
					<spring:message code="article.delete" />
				</a>
			</jstl:if>
			<jstl:if test="${fromTabooList}">
				<a href="article/administrator/taboo/delete.do?articleId=${rowArticle.id}"
					onclick="return confirm('<spring:message code="article.confirm.cancel" />')">
					<spring:message code="article.delete" />
				</a>
			</jstl:if>
		</display:column>
		<display:column>
			<jstl:if test="${rowArticle.draft eq true}">
				<spring:message code="article.draft" />
			</jstl:if>
			<jstl:if test="${rowArticle.draft eq false}">
				<spring:message code="article.published" />
			</jstl:if>
		</display:column>
	</security:authorize>
	<security:authorize access="hasRole('USER')">
		<display:column titleKey="article.edit">
			<jstl:if test="${rowArticle.draft eq true}">
				<div>
					<a href="article/user/edit.do?articleId=${rowArticle.id}"> <spring:message
							code="actor.user.edit" />
					</a>
				</div>
			</jstl:if>
		</display:column>
	</security:authorize>
</display:table>
<security:authorize access="hasRole('USER')">

	<div>
		<a href="article/user/create.do"> <spring:message
				code="actor.user.article" />
		</a>
	</div>

</security:authorize>



