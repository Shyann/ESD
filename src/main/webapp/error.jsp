<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="title" scope="request" value="Error"/>
<%@include file="/WEB-INF/header.jspf"%>

<h1>Oops! There was an error!</h1>
<h2>${errorMessage}</h2>

<%@include file="/WEB-INF/footer.jspf"%>