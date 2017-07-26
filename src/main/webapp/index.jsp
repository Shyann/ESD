<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="title" scope="request" value="Home"/>
<%@include file="/WEB-INF/header.jspf"%>

        <form action="login">
            <button type="submit" class="btn btn-primary btn-block">Current member? Log in</button>
        </form>
        <br>
        <form action="registration">
            <button type="submit" class="btn btn-default btn-block">Not a Member? Register</button>
        </form>

<%@include file="/WEB-INF/footer.jspf"%>
