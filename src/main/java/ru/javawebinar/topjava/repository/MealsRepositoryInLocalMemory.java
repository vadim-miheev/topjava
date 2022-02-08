package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class MealsRepositoryInLocalMemory implements MealsRepository {
    public static final int CALORIES_PER_DAY_LIMIT = 2000;
    private List<Meal> meals;

    public MealsRepositoryInLocalMemory() {
        meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
    }

    @Override
    public List<Meal> getMeals() {
        return meals;
    }

    @Override
    public void createMeal(Meal meal) {
        //TODO to implement;
    }

    @Override
    public void updateMeal(Meal meal) {
        //TODO to implement;
    }

    @Override
    public void deleteMeal(int id) {
        //TODO to implement;
    }
}
