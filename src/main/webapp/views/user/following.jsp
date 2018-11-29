<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
 
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%> 
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<%@taglib prefix="security" 
  uri="http://www.springframework.org/security/tags"%> 
<%@taglib prefix="display" uri="http://displaytag.sf.net"%> 
 
<display:table pagesize="5" class="displaytag" keepStatus="true" 
  name="users" requestURI="user/following.do" id="rowUser"> 
 
  <display:column property="name" titleKey="user.name" sortable="false" /> 
  <display:column property="surname" titleKey="user.surname" sortable="false" /> 
  <display:column> 
    <div> 
      <a href="actor/display.do?actorId=${rowUser.id}">
      	<spring:message code="user.profile" /> 
      </a> 
    </div> 
  </display:column> 
 
</display:table> 