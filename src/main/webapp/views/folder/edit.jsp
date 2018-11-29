<%--
 * edit.jsp
 *
 * Copyright (C) 2015 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

 
<%--
 * Edit folder
 
 Recibe folder que tiene los siguientes atributos
 
 	private String				name;
	private boolean				ofTheSystem;
	private Collection<Message>	messages;
	private Collection<Folder>	childFolders;
	private Folder				fatherFolder;
	private Actor				actor;
 --%>

<%--
 * formulario
 --%>

<div>
	<jstl:forEach items="${fathers}" var="folder">
		<a href="folder/actor/list.do?folderId=${folder.id}"> ${folder.name}</a> | 
	</jstl:forEach>
</div>
<form:form action="folder/actor/edit.do" modelAttribute="folder">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="ofTheSystem" />
	<form:hidden path="messages" />
	<form:hidden path="childFolders" />
	<form:hidden path="actor" />
	
	<%--
 * para prueba. debe ser modificable
 --%>

	<jstl:if test="${folder.id != 0}">	
	<form:label path="fatherFolder" >
	<spring:message code="folder.father" />:
	</form:label>
		&nbsp;
		<form:select path="fatherFolder">	
		<form:option label="---" value="0"/>
		<form:options items="${all}" itemLabel="name" itemValue="id" />
		</form:select>
	<form:errors cssClass="error" path="fatherFolder"  />
	</jstl:if>

	<jstl:if test="${folder.id == 0}">	
	<form:hidden path="fatherFolder" />
	</jstl:if>
		<Br>
	<form:label path="name">
		<spring:message code="folder.name" />:
	</form:label>
	&nbsp; 
	<form:input path="name" />	
	<form:errors cssClass="error" path="name" />
	
	<%--
		  * botones del formulario
		 --%>
		<br>
	<input type="submit" name="save" value="<spring:message code="folder.save" />" />&nbsp; 
	
	<jstl:if test="${folder.id != 0}">		
	<input type="submit" name="delete"
			value="<spring:message code="folder.delete" />"
			onclick="return confirm('<spring:message code="folder.confirm.delete" />')" />&nbsp;
	</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="folder.cancel" />"
		onclick="javascript: relativeRedir('folder/actor/list.do')" />
	<br />

</form:form>