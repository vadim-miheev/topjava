package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController extends AbstractMealController {
    @GetMapping("/meals")
    public String getAll(Model model, HttpServletRequest request) {
        if (request.getParameter("startDate") != null || request.getParameter("endDate") != null
                || request.getParameter("startTime") != null || request.getParameter("endTime") != null) {
            LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
            LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
            LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
            LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
            model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        } else {
            model.addAttribute("meals", super.getAll());
        }
        return "meals";
    }

    @PostMapping("/meals")
    public String createOrUpdate(HttpServletRequest request) {
        Meal meal = new Meal(
                StringUtils.hasLength(request.getParameter("id")) ? Integer.parseInt(request.getParameter("id")) : null,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (meal.isNew()) {
            super.create(meal);
        } else {
            super.update(meal, meal.id());
        }
        return "redirect:/meals";
    }

    @GetMapping("/meals/delete")
    public String deleteMeal(@RequestParam(name = "id") int id) {
        super.delete(id);
        return "redirect:/meals";
    }

    @GetMapping("/meals/new")
    public String createMealPage(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                "", 1000));
        return "mealForm";
    }

    @GetMapping("/meals/edit")
    public String updateMealPage(Model model, @RequestParam(name = "id") int id) {
        model.addAttribute("meal", super.get(id));
        return "mealForm";
    }
}