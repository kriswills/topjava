package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(ru.javawebinar.topjava.web.UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("redirect to meals");

   //     List<MealTo> mealList = MealsUtil.getFilteredTos(MealsUtil.MEAL_LIST, LocalTime.MIN, LocalTime.MAX, 2000);

                /*MealsUtil.MEAL_LIST.stream()
                .map(m -> new MealTo(m.getDateTime(), m.getDescription(), m.getCalories(), m.getCalories() > MealsUtil.CALORIES_PER_DAY))
                .collect(Collectors.toList())*/;

        request.setAttribute("mealList", MealsUtil.getWithExceeded(MealsUtil.MEAL_LIST, 2000));

        String path = "/meals.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(path);
        requestDispatcher.forward(request, response);
        //      response.sendRedirect("meals.jsp");
    }

}
