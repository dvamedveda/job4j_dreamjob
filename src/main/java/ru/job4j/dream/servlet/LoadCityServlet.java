package ru.job4j.dream.servlet;

import org.json.JSONObject;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Сервлет для отдачи списка городов из бд в виде json.
 * Используется при ajax запросах.
 */
public class LoadCityServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Store store = PsqlStore.getInst();
        resp.setContentType("text/json");
        resp.setCharacterEncoding("UTF-8");
        JSONObject cities = new JSONObject();
        for (Map.Entry<Integer, String> pair : store.getCities().entrySet()) {
            cities.put(pair.getKey().toString(), pair.getValue());
        }
        try (PrintWriter out = resp.getWriter()) {
            out.print(cities);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}