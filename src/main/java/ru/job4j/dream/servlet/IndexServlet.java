package ru.job4j.dream.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет, перенаправляющий запросы
 * к главной странице приложения
 * к соответствующему представлению.
 */
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getSession().isNew()) {
            req.setAttribute("user", req.getSession().getAttribute("user"));
        }
        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }
}