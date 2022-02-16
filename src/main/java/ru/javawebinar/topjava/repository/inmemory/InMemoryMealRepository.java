package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        AtomicInteger count = new AtomicInteger(6);
        MealsUtil.meals.forEach(meal -> save(meal, count.getAndDecrement() > 0 ? 1 : 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> userMeals;
        synchronized (repository) {
            userMeals = repository.get(userId);
            if (userMeals == null) {
                userMeals = new ConcurrentHashMap<>();
                repository.put(userId, userMeals);
            }
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMeals.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return userMeals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals != null && userMeals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals != null ? userMeals.get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAllWithPredicate(userId, meal -> true);
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate) {
        return getAllWithPredicate(
                userId,
                meal -> (startDate == null || !meal.getDate().isBefore(startDate))
                        && (endDate == null || !meal.getDate().isAfter(endDate))
        );
    }

    private List<Meal> getAllWithPredicate(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals == null) return new ArrayList<>();
        return userMeals.values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

