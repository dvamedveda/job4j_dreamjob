package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет для регистрации нового пользователя.
 */
public class RegServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/reg.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = new User();
        user.setName(req.getParameter("name"));
        user.setEmail(req.getParameter("email"));
        user.setPassword(req.getParameter("password"));
        Store store = PsqlStore.getInst();
        if (!store.userExists(user.getEmail())) {
            if (user.getName().equals("") || user.getEmail().equals("") || user.getPassword().equals("")) {
                req.setAttribute("error", "Нужно заполнить все поля.");
                req.getRequestDispatcher("/WEB-INF/views/reg.jsp").forward(req, resp);
            } else {
                store.saveUser(user);
                req.setAttribute("info", "Регистрация прошла успешно. Используйте введенные данные для входа.");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            }
        } else {
            req.setAttribute("error", "Пользователь с таким email уже существует!");
            req.getRequestDispatcher("/WEB-INF/views/reg.jsp").forward(req, resp);
        }
    }
}