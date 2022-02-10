<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="formName" type="String"--%>
<%--@elvariable id="oldMeal" type="ru.javawebinar.topjava.model.Meal"--%>

<c:if test='${"create".equals(formName)}'>
    <c:set var="title" value="Create new Meal"/>
</c:if>
<c:if test='${"update".equals(formName)}'>
    <c:set var="title" value="Edit Meal"/>
</c:if>

<html>
<head>
    <title>${title}</title>
    <style>
        .form-line {
            margin-bottom: 10px;
            width: 500px;
        }
        .field-label {
            display: inline-block;
            width: 150px;
        }
        .no-style-link {
            text-decoration: none;
            color: black;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${title}</h2>
<form method="post">
    <div class="form-line">
        <label class="field-label" for="1">DateTime:</label>
        <input type="datetime-local" name="date-time" id="1" value="${oldMeal.dateTime}">
    </div>
    <div class="form-line">
        <label class="field-label" for="2">Description:</label>
        <input type="text" name="description" id="2" value="${oldMeal.description}">
    </div>
    <div class="form-line">
        <label class="field-label" for="3">Calories:</label>
        <input type="number" name="calories" id="3" value="${oldMeal.calories}">
    </div>
    <input type="hidden" name="id" value="${oldMeal.id}">
    <input type="submit" value="Save">
    <button><a href="meals" class="no-style-link">Cancel</a></button>
</form>
</body>
</html>
