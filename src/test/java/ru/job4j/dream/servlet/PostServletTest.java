package ru.job4j.dream.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.model.Post;
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
import java.util.Collection;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;


/**
 * Тесты класса PostServlet.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class PostServletTest {

    /**
     * Здесь проверяется добавление вакансии в сервлете.
     * @throws ServletException исключения сервлета при работе.
     * @throws IOException исключения ввода вывода при работе с хранилищем.
     */
    @Test
    public void whenAddPostThenSuccess() throws ServletException, IOException {
        Store store = new StubStore();
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.getInst()).thenReturn(store);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        Mockito.when(req.getParameter("id")).thenReturn("4");
        Mockito.when(req.getParameter("name")).thenReturn("test_job");
        Mockito.when(req.getParameter("desc")).thenReturn("This is a just test job");
        new PostServlet().doPost(req, resp);
        Post addedPost = store.findPostById(4);
        Assert.assertThat(addedPost.getId(), is(4));
        Assert.assertThat(addedPost.getName(), is("test_job"));
        Assert.assertThat(addedPost.getDesc(), is("This is a just test job"));
        Assert.assertThat(addedPost.getCreated(), is(1L));
        Assert.assertThat(store.findAllPosts().size(), is(4));
    }

    /**
     * Здесь проверяется получение всех вакансий в сервлете.
     * @throws Exception исключения при работе.
     */
    @Test
    public void whenGetPostsThenSuccess() throws Exception {
        Store store = new StubStore();
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.getInst()).thenReturn(store);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(req.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("user")).thenReturn(new User(1, "user", "email", "password"));
        Mockito.when(req.getRequestDispatcher("/WEB-INF/views/posts.jsp")).thenReturn(dispatcher);
        Answers answers = new Answers();
        Mockito.doAnswer(answers.new SetAnswer()).when(req).setAttribute(Mockito.anyString(), Mockito.anyObject());
        Mockito.doAnswer(answers.new GetAnswer()).when(req).getAttribute(Mockito.anyString());
        new PostServlet().doGet(req, resp);
        Collection<Post> posts = (Collection<Post>) req.getAttribute("posts");
        Assert.assertThat(posts.size(), is(3));
        Post addedPost = posts.stream().filter((post)-> post.getId() == 3).collect(Collectors.toList()).get(0);
        Assert.assertThat(addedPost.getId(), is(3));
        Assert.assertThat(addedPost.getName(), is("Senior Java Job"));
        Assert.assertThat(addedPost.getDesc(), is("Работа для джуниор Java разработчика"));
        Assert.assertThat(addedPost.getCreated(), is(3L));
    }
}