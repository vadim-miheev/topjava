<%@ page contentType="text/html;charset=UTF-8" %>
<%--@elvariable id="title" type="String"--%>
<%--@elvariable id="oldMeal" type="ru.javawebinar.topjava.model.Meal"--%>
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
    <button><a href="meals" style="text-decoration: none; color: black;">Cancel</a></button>
</form>
</body>
</html>
