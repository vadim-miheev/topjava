package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.repository.MealsRepositoryInLocalMemory;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to meals");

        request.setAttribute("mealsList", MealsUtil.filteredByStreams(new MealsRepositoryInLocalMemory().getMeals(), LocalTime.MIN, LocalTime.MAX,
                MealsRepositoryInLocalMemory.CALORIES_PER_DAY_LIMIT));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
