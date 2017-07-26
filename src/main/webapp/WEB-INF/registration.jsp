<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="title" scope="request" value="Registration"/>
<%@include file="/WEB-INF/header.jspf"%>

<form action="registration" method="POST">
    <div class="form-group">
        <label for="fname">First Name:</label>
        <input name="fname" type="text" class="form-control" placeholder="Enter first name" value="${fname}" <c:if test="${!empty addresses}">required</c:if>>
    </div>
    <div class="form-group">
        <label for="lname">Last Name:</label>
        <input name="lname" type="text" class="form-control" placeholder="Enter last name" value="${lname}" <c:if test="${!empty addresses}">required</c:if>>
    </div>
    <div class="form-group">
        <c:if test="${empty addresses}">
            <c:if test="${manualAddress == true}">
                <label for="address">Address: (Could not find address, please enter address manually)</label>
                <input name="address" type="text" class="form-control" placeholder="Enter Full Address" required>
            </c:if>
            <c:if test="${manualAddress != true}">
                <label for="address">Post Code:</label>
                <input name="postcode" type="text" class="form-control" placeholder="Enter postcode" required><button name="submit" type="submit" value="lookupAddress" class="btn btn-default">Lookup Postcode</button>
            </c:if>
        </c:if>
        <c:if test="${!empty addresses}">
            <label for="address">Please Select an address:</label>
            <select class="form-control" name="address">
                <c:forEach items="${addresses}" var="address">
                    <option>
                        <c:out value="${address}" />
                    </option>
                </c:forEach>
            </select>
        </c:if>
    </div>
    <div class="form-group">
        <label for="dob">Date Of Birth:</label>
        <input name="dob" type="text" class="form-control" placeholder="Enter DOB (yyyy-mm-dd)" value="${dob}" <c:if test="${!empty addresses}">required</c:if>>
    </div>
    <c:if test="${(!empty addresses) || manualAddress == true}">
        <button name="submit" type="submit" value="createUser" class="btn btn-default">Submit</button>
    </c:if>
</form>

<%@include file="/WEB-INF/footer.jspf"%>
