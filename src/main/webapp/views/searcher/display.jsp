
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
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form action="${uri}" method="get">
	<h3 style="margin-bottom:0px;"><spring:message code="searcher.select" /></h3>
	<label for="radioNewspaper"><spring:message code="searcher.newspapers" /></label><input id="radioNewspaper" type="radio" name="search" value="newspaper" <jstl:if test="${!(search eq 'article')}">checked</jstl:if>>
	<label for="radioArticle"><spring:message code="searcher.articles" /></label><input id="radioArticle" type="radio" name="search" value="article" <jstl:if test="${search eq 'article'}">checked</jstl:if>><br>

	<h3 style="margin-bottom:0px;"><spring:message code="searcher.keyWord" /></h3>
	<em><spring:message code="searcher.keyWord.note" /></em><br/>
	<input type="text" name="keyWord" value="${keyWord}">

	<acme:submit name="doSearch" code="searcher.search"/>

	<input type="button" name="clear"
		value="<spring:message code="searcher.clear" />"
		onclick="javascript: relativeRedir('/searcher/display.do');" />
	<br />

</form>

<jstl:if test="${search eq 'newspaper'}">
	<h2 style="margin-bottom:0px;"><spring:message code="searcher.newspapers.found" /></h2>
	<display:table pagesize="5" class="displaytag" keepStatus="true"
		name="newspapers" requestURI="${uri}" id="rowNewspaper">
	
		<display:column titleKey="searcher.newspaper.title" >
		<jstl:out value="${rowNewspaper.title }"/>
		</display:column>
		<display:column titleKey="searcher.newspaper.description" >
			<jstl:out value="${rowNewspaper.description }"/>
		</display:column>
		<display:column titleKey="searcher.newspaper.publisher" >
			<jstl:out value="${rowNewspaper.publisher.userAccount.username }"/>
		</display:column>
		
		<display:column>
			<a href="newspaper/display.do?newspaperId=${rowNewspaper.id}">
				<spring:message code="searcher.newspaper.display" />
			</a>
		</display:column>
	</display:table>
</jstl:if>
<jstl:if test="${search eq 'article'}">
	<h2 style="margin-bottom:0px;"><spring:message code="searcher.articles.found" /></h2>
	<display:table pagesize="5" class="displaytag" keepStatus="true"
		name="articles" requestURI="${uri}" id="rowSearchArticle">
	
		<display:column titleKey="searcher.article.title">
			<a href="article/display.do?articleId=${rowSearchArticle.id}">
				<jstl:out value="${rowSearchArticle.title}" />
			</a>
		</display:column>
		
		<display:column titleKey="searcher.article.summary">
				<spring:message code="newspaper.more" var="more" />
				
				<div id="visto${rowSearchArticle.id }"> 
				<jstl:out value="${ss}" />
				<script>			
					var ss = "${rowSearchArticle.summary}".substring(0, 12);		
					document.write(ss);
					var i="${rowSearchArticle.summary}".length;

					if(i>11){
						document.write("<input type='button' value='${more}' onClick='document.getElementById(\"oculto${rowSearchArticle.id }\").style.display=\"block\"; document.getElementById(\"visto${rowSearchArticle.id }\").style.display=\"none\"'>");
					}
				</script>	
				
				 <br> <br>
				</div> 
				<div id="oculto${rowSearchArticle.id }" style="display:none"> 
				<jstl:out value="${rowSearchArticle.summary}" /><br><br>
				</div>
								
			</display:column>
		<display:column titleKey="searcher.article.newspaper">
			<a href="newspaper/display.do?newspaperId=${rowSearchArticle.newspaper.id}">
				<jstl:out value="${rowSearchArticle.newspaper.title}"></jstl:out>
			</a>
		</display:column>
	
	</display:table>
</jstl:if>



