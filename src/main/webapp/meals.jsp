<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        table {
            border-collapse: collapse;
        }

        table, th, td {
            border: solid 1px black;
            padding: 5px;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <tr>
        <td>Date</td><td>Description</td><td>Calories</td>
    </tr>
    <jsp:useBean id="mealsList" scope="request" type="java.util.List"/>
    <c:forEach items="${mealsList}" var="meal">
        <tr style="color: ${meal.isExcess() ? "red" : "green"};">
            <td><javatime:format value="${meal.getDateTime()}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td>${meal.getDescription()}</td>
            <td>${meal.getCalories()}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
