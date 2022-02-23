package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateCreate() {
        userMeals.forEach(meal -> assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, meal.getDateTime(), "duplicate", 100), USER_ID)));
    }

    @Test
    public void update() {
        userMeals.forEach(meal -> {
            Meal updated = getUpdated(meal);
            service.update(updated, USER_ID);
            assertMatch(service.get(updated.getId(), USER_ID), getUpdated(meal));
            service.delete(updated.getId(), USER_ID);
        });
    }

    @Test
    public void updateWithWrongUserId() {
        adminMeals.forEach(meal -> assertThrows(NotFoundException.class, () -> service.update(getUpdated(meal), USER_ID)));
    }

    @Test
    public void get() {
        userMeals.forEach(referenceMeal -> {
            Meal mealFromDB = service.get(referenceMeal.getId(), USER_ID);
            assertMatch(mealFromDB, referenceMeal);
        });
    }
    @Test
    public void getWithWrongUserId() {
        adminMeals.forEach(referenceMeal ->
                assertThrows(NotFoundException.class, () -> service.get(referenceMeal.getId(), USER_ID)));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), userMeals.stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList()));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> withoutFilters = service.getBetweenInclusive(null , null, USER_ID);
        assertMatch(withoutFilters, service.getAll(USER_ID));

        List<Meal> emptyList = service.getBetweenInclusive(TWENTY_CENTURY, TWENTY_CENTURY.plusDays(1), USER_ID);
        assertMatch(emptyList, new ArrayList<>());

        List<Meal> existingDates = service.getBetweenInclusive(TWENTY_CENTURY, THIRTY_CENTURY, USER_ID);
        assertMatch(existingDates, service.getAll(USER_ID));

        List<Meal> firstDay = service.getBetweenInclusive(EXISTING_DATE_FIRST, EXISTING_DATE_FIRST, USER_ID);
        assertMatch(firstDay, userMeals.stream()
                .filter(meal -> meal.getDate().equals(EXISTING_DATE_FIRST))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList()));

        List<Meal> secondDay = service.getBetweenInclusive(EXISTING_DATE_SECOND, EXISTING_DATE_SECOND, USER_ID);
        assertMatch(secondDay, userMeals.stream()
                .filter(meal -> meal.getDate().equals(EXISTING_DATE_SECOND))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList()));
    }

    @Test
    public void delete() {
        userMeals.forEach(meal -> {
            service.delete(meal.getId(), USER_ID);
            assertThrows(NotFoundException.class, () -> service.get(meal.getId(), USER_ID));
        });
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }
}