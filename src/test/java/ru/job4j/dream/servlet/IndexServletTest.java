package ru.job4j.dream.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.dream.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.hamcrest.CoreMatchers.is;

/**
 * Тесты класса IndexServlet.
 */
public class IndexServletTest {

    /**
     * В тесте проверяется перенаправление на страницу
     * и установка пользователя в объект запроса в сервлете.
     *
     * @throws Exception исключения при работе теста.
     */
    @Test
    public void whenGetPageThenSuccess() throws Exception {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(req.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("user")).thenReturn(new User(1, "user", "email", "password"));
        Mockito.when(req.getRequestDispatcher("/WEB-INF/views/index.jsp")).thenReturn(dispatcher);
        Answers answers = new Answers();
        Mockito.doAnswer(answers.new SetAnswer()).when(req).setAttribute(Mockito.anyString(), Mockito.anyObject());
        Mockito.doAnswer(answers.new GetAnswer()).when(req).getAttribute(Mockito.anyString());
        new IndexServlet().doGet(req, resp);
        User user = (User) req.getAttribute("user");
        Assert.assertThat(user.getId(), is(1));
        Assert.assertThat(user.getName(), is("user"));
        Assert.assertThat(user.getEmail(), is("email"));
        Assert.assertThat(user.getPassword(), is("password"));
    }
}