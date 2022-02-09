package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealsRepositoryInLocalMemory implements MealsRepository {
    private static MealsRepositoryInLocalMemory instance = null;
    private final AtomicInteger idCounter = new AtomicInteger(1);
    private final Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();

    private MealsRepositoryInLocalMemory() {
        fillingRepositoryByDefaultValues();
    }

    public static MealsRepositoryInLocalMemory getInstance() {
        if (instance == null)
            instance = new MealsRepositoryInLocalMemory();

        return instance;
    }

    private void fillingRepositoryByDefaultValues() {
        create(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        create(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        create(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
        create(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
        create(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
        create(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
        create(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    }

    @Override
    public Meal get(int id) {
        return mealMap.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public Meal create(LocalDateTime date, String description, int calories) {
        Meal newMeal = new Meal(idCounter.getAndIncrement(), date, description, calories);
        mealMap.put(newMeal.getId(), newMeal);
        return newMeal;
    }

    @Override
    public Meal update(Meal meal) {
        return mealMap.replace(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        mealMap.remove(id);
    }
}
