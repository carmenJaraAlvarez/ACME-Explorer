<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
 
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%> 
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<%@taglib prefix="security" 
  uri="http://www.springframework.org/security/tags"%> 
<%@taglib prefix="display" uri="http://displaytag.sf.net"%> 
 
<display:table pagesize="5" class="displaytag" 
  name="users" requestURI="user/list.do" id="rowUser"> 
 
  <display:column  titleKey="user.name" sortable="false" > 
  <jstl:out value="${rowUser.name}" />
  </display:column>
  <display:column titleKey="user.surname" sortable="false" > 
  <jstl:out value="${rowUser.surname}" />
  </display:column>
  <display:column> 
    <div> 
      <a href="actor/display.do?actorId=${rowUser.id}">
      	<spring:message code="user.profile" /> 
      </a> 
    </div> 
  </display:column> 
 
</display:table> 