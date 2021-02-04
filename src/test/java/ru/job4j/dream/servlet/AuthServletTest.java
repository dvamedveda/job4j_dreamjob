package ru.job4j.dream.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;
import ru.job4j.dream.store.StubStore;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;

/**
 * Тесты класса AuthServlet.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class AuthServletTest {

    /**
     * Здесь проверяется направление запроса на страницу логина в сервлете.
     * @throws Exception исключения при работе.
     */
    @Test
    public void whenGetPageThenSuccess() throws Exception {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(req.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(dispatcher);
        new AuthServlet().doGet(req, resp);
    }

    /**
     * Здесь проверяется авторизация в сервлете.
     * @throws ServletException исключения сервлета при работе.
     * @throws IOException исключения ввода вывода при работе с хранилищем.
     */
    @Test
    public void whenPostWrongEmailThenFail() throws ServletException, IOException {
        Store store = new StubStore();
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.getInst()).thenReturn(store);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);
        Answers answers = new Answers();
        Mockito.doAnswer(answers.new SetAnswer()).when(req).setAttribute(Mockito.anyString(), Mockito.anyObject());
        Mockito.doAnswer(answers.new GetAnswer()).when(req).getAttribute(Mockito.anyString());
        Mockito.when(req.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(dispatcher);
        Mockito.when(req.getParameter("email")).thenReturn("email999");
        Mockito.when(req.getParameter("password")).thenReturn("password1");
        new AuthServlet().doPost(req, resp);
        String message = (String) req.getAttribute("error");
        Assert.assertThat(message, is("Пользователя с таким email не существует!"));
    }

    /**
     * Здесь проверяется авторизация в сервлете.
     * @throws ServletException исключения сервлета при работе.
     * @throws IOException исключения ввода вывода при работе с хранилищем.
     */
    @Test
    public void whenPostWrongPasswordThenFail() throws ServletException, IOException {
        Store store = new StubStore();
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.getInst()).thenReturn(store);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);
        Answers answers = new Answers();
        Mockito.doAnswer(answers.new SetAnswer()).when(req).setAttribute(Mockito.anyString(), Mockito.anyObject());
        Mockito.doAnswer(answers.new GetAnswer()).when(req).getAttribute(Mockito.anyString());
        Mockito.when(req.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(dispatcher);
        Mockito.when(req.getParameter("email")).thenReturn("email1");
        Mockito.when(req.getParameter("password")).thenReturn("password111");
        new AuthServlet().doPost(req, resp);
        String message = (String) req.getAttribute("error");
        Assert.assertThat(message, is("Неверный пароль!"));
    }

    /**
     * Здесь проверяется авторизация в сервлете.
     * @throws ServletException исключения сервлета при работе.
     * @throws IOException исключения ввода вывода при работе с хранилищем.
     */
    @Test
    public void whenPostRightDataThenSuccess() throws ServletException, IOException {
        Store store = new StubStore();
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.getInst()).thenReturn(store);
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        Answers answers = new Answers();
        Mockito.doAnswer(answers.new SetAnswer()).when(session).setAttribute(Mockito.anyString(), Mockito.anyObject());
        Mockito.doAnswer(answers.new GetAnswer()).when(session).getAttribute(Mockito.anyString());
        Mockito.when(req.getParameter("email")).thenReturn("email1");
        Mockito.when(req.getParameter("password")).thenReturn("password1");
        Mockito.when(req.getSession()).thenReturn(session);
        new AuthServlet().doPost(req, resp);
        User user = (User) session.getAttribute("user");
        Assert.assertThat(user.getId(), is(1));
        Assert.assertThat(user.getName(), is("name1"));
        Assert.assertThat(user.getEmail(), is("email1"));
        Assert.assertThat(user.getPassword(), is("password1"));
    }
}