<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="title" scope="request" value="Member Dashboard"/>
<%@include file="/WEB-INF/header.jspf"%>
<%@ page import="java.util.ArrayList" %>

<h2> You Currently Owe: &pound;${user.balanceAsString}</h2>
<div class="row">
    <div class="col-md-6">
        <h1>Your Payments:</h1>
        <table class="table">
            <thead>
                <tr>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Date</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${payments}" var="payment">
                    <tr>
                        <td><c:out value="${payment.type}" /></td>
                        <td><c:out value="${payment.amount}" /></td>
                        <td><c:out value="${payment.date}" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="col-md-6">
        <h1>Your Claims:</h1>
        <table class="table">
            <thead>
                <tr>
                    <th>Rationale</th>
                    <th>Amount</th>
                    <th>Status</th>
                    <th>Date</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${claims}" var="claim">
                    <tr>
                        <td><c:out value="${claim.rationale}" /></td>
                        <td><c:out value="${claim.amount}" /></td>
                        <td><c:out value="${claim.status}" /></td>
                        <td><c:out value="${claim.date}" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<br>
<div class="row">
    <div class="col-md-6">
        <h1>Make a payment</h1>
        <form action="memberDashboard" method="POST">
            Type of payment:<br>
            <input type="text" name="paymentType" class="form-control" placeholder="type of payment" maxlength="10" required><br>
            Amount:<br>
            <div class="input-group">
                <span class="input-group-addon">&pound;</span>
                <input type="number" class="form-control" name="paymentAmount" step="0.01" placeholder="0" required><br>
            </div>
            <button name="action" value="createPayment" type="submit" class="btn btn-primary">Pay</button>
            <br>
        </form>
    </div>
    <div class="col-md-6">
        <h1>Submit a new claim:</h1>
        <form action="memberDashboard" method="POST">
            Rationale:<br>
            <input type="text" name="claimRationale" class="form-control" placeholder="rationale" required><br>
            Amount:<br>
            <div class="input-group">
                <span class="input-group-addon">&pound;</span>
                <input type="number" class="form-control" name="claimAmount" step="0.01" placeholder="0" required><br>
            </div>
            <button name="action" value="createClaim" type="submit" class="btn btn-primary">Claim</button>
            <br>
        </form>
    </div>
</div>

<c:if test="${!empty error}">
    <div class="alert alert-warning">
        <strong>Oh snap!</strong> ${error}
    </div>
</c:if>


<%@include file="/WEB-INF/footer.jspf"%>
