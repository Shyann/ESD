<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="title" scope="request" value="Administration Dashboard"/>
<%@include file="/WEB-INF/header.jspf"%>
<h2>${dashboardMessage}</h2>

<div class="btn-group">
    <form action="adminDashboard" method="POST">
        <button name="act" value="listMembers" type="submit" class="btn btn-primary">List all members</button>
        <button name="act" value="listBalances" type="submit" class="btn btn-primary">List outstanding balances</button>
        <button name="act" value="listClaims" type="submit" class="btn btn-primary">List all claims</button>
        <button name="act" value="listProvisionals" type="submit" class="btn btn-primary">List all provisional member applications</button>
        <button name="act" value="listApprovedClaims" type="submit" class="btn btn-primary">List all approved claims</button>
        <button name="act" value="listFinancialStats" type="submit" class="btn btn-primary">List financial statistics</button>
    </form>
</div>

<c:choose>
    <c:when test="${!empty listMembers}">
        <form action="adminDashboard" method="POST">
            <button name="act" value="chargeMembershipFee" type="submit" class="btn btn-primary">Charge Yearly Membership Fee(10GBP)</button>
        </form>

        <%@include file="/WEB-INF/admin/listMembers.jspf"%>
    </c:when>
    <c:when test="${!empty listClaims}">
        <%@include file="/WEB-INF/admin/listClaims.jspf"%>
    </c:when>
    <c:when test="${!empty listBalances}">
        <%@include file="/WEB-INF/admin/listBalances.jspf"%>
    </c:when>
    <c:when test="${!empty listProvisionals}">
        <%@include file="/WEB-INF/admin/listProvisionals.jspf"%>
    </c:when>
    <c:when test="${!empty listApprovedClaims}">
        <%@include file="/WEB-INF/admin/listApprovedClaims.jspf"%>
    </c:when>
    <c:when test="${!empty numUsers}">
        <%@include file="/WEB-INF/admin/financialStatistics.jspf"%>
    </c:when>
</c:choose>

<div>${financialStats}</div>

<%@include file="/WEB-INF/footer.jspf"%>
