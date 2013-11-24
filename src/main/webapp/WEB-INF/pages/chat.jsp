<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%
    response.setHeader("Cache-Control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setHeader("Expires","0");
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <script type="text/javascript" src="<c:url value="/resources/scripts/jquery.tools.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/scripts/jquery.atmosphere.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/scripts/json2.js"/>"></script>
    <script type="text/javascript" charset="UTF-8" src="<c:url value="/resources/scripts/chat.js" />"></script>
    <link type="text/css" rel="stylesheet"  href="<c:url value="/resources/styles/chat.css"/>"/>
</head>
<body>
<div class="container">
    <div class="users">
        <c:forEach items="${users}" var="user">
            <div class="user">
                ${user.name}
            </div>
        </c:forEach>
    </div>
    <div class="messages">
        <c:forEach items="${messages}" var="message">
            <div class="message">
                <fmt:formatDate value="${message.date}" pattern="${dateFormat}" />
                ${message.author}: ${message.message}
            </div>
        </c:forEach>
    </div>
</div>
<div class="send">
    <span id="user"></span>
    <div class="status">Connecting...</div>
    <input id="text" type="text" maxlength="1000" placeholder="Your message"></textarea>
    <button id="button" type="button">Login</button>
</div>
<div class="simple_overlay" id="mies1">
    <div class="caption">Enter your name:</div>
    <div class="input">
        <input id="name" type="text" name="username" maxlength="50" placeholder="Your name"/>
        <input id="ok" type="submit" value="Ok" />
    </div>
    <div class="error"></div>
</div>
<script>
    var websocketUrl = "<c:url value="/websockets"/>";
</script>
</body>
</html>