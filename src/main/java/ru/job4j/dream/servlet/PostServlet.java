package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет для добавления вакансии в список.
 */
public class PostServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        PsqlStore.getInst().savePost(
                new Post(
                        Integer.valueOf(req.getParameter("id")),
                        req.getParameter("name"),
                        req.getParameter("desc"), 1L));
        resp.sendRedirect(req.getContextPath() + "/posts.do");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("posts", PsqlStore.getInst().findAllPosts());
        if (!req.getSession().isNew()) {
            req.setAttribute("user", req.getSession().getAttribute("user"));
        }
        req.getRequestDispatcher("/WEB-INF/views/posts.jsp").forward(req, resp);
    }
}