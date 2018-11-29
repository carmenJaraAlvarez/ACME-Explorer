<%--
 * action-1.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<p><b><spring:message code="legal.terms.data" /></b></p>
<p><spring:message code="legal.terms.data.text" /></p>
<p><b><spring:message code="legal.terms.use" /></b></p>
<p><spring:message code="legal.terms.use.text1" /></p>
<p><spring:message code="legal.terms.use.text2"/></p>
<p><b><spring:message code="legal.terms.eligibility" /></b></p>
<p><spring:message code="legal.terms.eligibility.text" /></p>
<p><b><spring:message code="legal.terms.control" /></b></p>
<p><spring:message code="legal.terms.control.text1" />
<spring:message code="legal.terms.control.text2" />
<spring:message code="legal.terms.control.text3" /></p>

<p><spring:message code="legal.terms.taboo1" />

<jstl:forEach items="${tabooWords}" var="taboo">"${taboo}
"
</jstl:forEach>

<spring:message code="legal.terms.taboo2" /></p>

<p><spring:message code="legal.terms.text" /></p>
