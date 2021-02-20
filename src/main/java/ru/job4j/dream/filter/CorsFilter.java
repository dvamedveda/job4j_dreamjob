package ru.job4j.dream.filter;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр, авторизующий CORS-запросы.
 */
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Store store = PsqlStore.getInst();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, HEAD, OPTIONS, PUT");
        if (request.getMethod().equals("OPTIONS")) {
            response.addHeader("Access-Control-Allow-Headers", "Authority");
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }
        String token = request.getHeader("Authority");
        if (store.getTokenStorage().verifyToken(token)) {
            User user = store.getTokenStorage().getUserByToken(token);
            request.getSession().setAttribute("user", user);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}