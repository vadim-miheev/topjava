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
    private static final int CALORIES_PER_DAY_LIMIT = 2000; //TODO move to User logic
    private static final MealsRepositoryInLocalMemory mealsRepository = MealsRepositoryInLocalMemory.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getParameter("delete") != null) {
            int id = Integer.parseInt(request.getParameter("delete"));
            if (id > 0) {
                mealsRepository.delete(id);
                log.debug("Meal with ID " + id + " was deleted");
                response.sendRedirect("meals");
                return;
            }
        }

        log.debug("forward to meals");

        request.setAttribute("mealsList", MealsUtil.filteredByStreams(MealsRepositoryInLocalMemory.getInstance().getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY_LIMIT));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
