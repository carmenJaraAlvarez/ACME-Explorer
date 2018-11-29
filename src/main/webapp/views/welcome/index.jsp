<%--
 * index.jsp
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" href="styles/cookies.css" type="text/css">
<!--//BLOQUE COOKIES-->
<div id="barraaceptacion">
    <div class="inner">
    	<spring:message	code="welcome.page.legal.cookiesEntry" />
        <a href="javascript:void(0);" class="ok" onclick="PonerCookie();">
        	<b><spring:message	code="welcome.page.legal.cookiesEntry.aceptar" /></b>
        </a> | 
        <a href="https://www.agpd.es/portalwebAGPD/canaldocumentacion/cookies/index-ides-idphp.php" target="_blank" class="info">
    		<spring:message	code="welcome.page.legal.cookiesEntry.info" />
    	</a>
    </div>
</div>

<script>
	function getCookie(c_name){
	    var c_value = document.cookie;
	    var c_start = c_value.indexOf(" " + c_name + "=");
	    if (c_start == -1){
	        c_start = c_value.indexOf(c_name + "=");
	    }
	    if (c_start == -1){
	        c_value = null;
	    }else{
	        c_start = c_value.indexOf("=", c_start) + 1;
	        var c_end = c_value.indexOf(";", c_start);
	        if (c_end == -1){
	            c_end = c_value.length;
	        }
	        c_value = unescape(c_value.substring(c_start,c_end));
	    }
	    return c_value;
	}
	
	function setCookie(c_name,value,exdays){
	    var exdate=new Date();
	    exdate.setDate(exdate.getDate() + exdays);
	    var c_value=escape(value) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString());
	    document.cookie=c_name + "=" + c_value;
	}
	
	if(getCookie('okcookie')!="1"){
	    document.getElementById("barraaceptacion").style.display="block";
	}
	function PonerCookie(){
	    setCookie('okcookie','1',365);
	    document.getElementById("barraaceptacion").style.display="none";
	}
</script>
<!--//FIN BLOQUE COOKIES-->
<p><spring:message code="welcome.greeting.prefix" /> 
<jstl:out value="${name}"></jstl:out>
<spring:message code="welcome.greeting.suffix" /></p>


<p>
	<spring:message code="welcome.greeting.current.time" />
	<spring:message code="welcome.dateformat" var="dateFormat" />
	<fmt:formatDate pattern="${dateFormat}" value="${moment}" />
</p>
