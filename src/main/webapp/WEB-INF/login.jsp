<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="title" scope="request" value="Login"/>
<%@include file="/WEB-INF/header.jspf"%>

<h1>Login Page!</h1>
<h2>${loginMessage}</h2>

<form action="login" method="POST">
    <div class="form-group">
        <label for="username">Username:</label>
        <input name="username" type="text" class="form-control" placeholder="Username" required>
    </div>
    <div class="form-group">
        <label for="password">Password:</label>
        <input name="password" type="password" class="form-control" placeholder="Password" required>
    </div>

    <button name="submit" type="submit" class="btn btn-default">Submit</button>
</form>

<%@include file="/WEB-INF/footer.jspf"%>
