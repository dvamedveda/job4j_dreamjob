package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет для добавления кандидата в список.
 */
public class CandidateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Store store = PsqlStore.getInst();
        Candidate candidate = new Candidate(
                Integer.parseInt(req.getParameter("id")),
                req.getParameter("name"),
                Integer.parseInt(req.getParameter("city")));
        store.saveCandidate(candidate);
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("candidates", PsqlStore.getInst().findAllCandidates());
        if (!req.getSession().isNew()) {
            req.setAttribute("user", req.getSession().getAttribute("user"));
        }
        req.getRequestDispatcher("/WEB-INF/views/candidates.jsp").forward(req, resp);
    }
}