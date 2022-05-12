package ru.javawebinar.topjava.to;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.converter.LocalDateTimeToStringConverter;

import java.time.LocalDateTime;

public class MealToUI extends MealTo {
    @JsonSerialize(converter = LocalDateTimeToStringConverter.class)
    private final LocalDateTime dateTime;
    private final Boolean excess;

    public MealToUI(Integer id, LocalDateTime dateTime, String description, int calories, Boolean excess) {
        super(id, dateTime, description, calories, false);
        this.dateTime = dateTime;
        this.excess = excess;
    }

    public static MealToUI getFromMealTo (MealTo mealTo) {
        return new MealToUI(mealTo.getId(), mealTo.getDateTime(), mealTo.getDescription(), mealTo.getCalories(), mealTo.isExcess());
    }

    public static MealToUI getFromMeal (Meal meal) {
        return new MealToUI(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), null);
    }
}
