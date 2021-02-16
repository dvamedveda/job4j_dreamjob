package ru.job4j.dream.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.model.Candidate;
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
 * Тесты класса CandidateServlet.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class CandidateServletTest {

    /**
     * Здесь проверяется получение всех кандидатов в сервлете.
     *
     * @throws Exception исключения при работе.
     */
    @Test
    public void whenGetCandidatesThenSuccess() throws Exception {
        Store store = new StubStore();
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.getInst()).thenReturn(store);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(req.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("user")).thenReturn(new User(1, "user", "email", "password"));
        Mockito.when(req.getRequestDispatcher("/WEB-INF/views/candidates.jsp")).thenReturn(dispatcher);
        Answers answers = new Answers();
        Mockito.doAnswer(answers.new SetAnswer()).when(req).setAttribute(Mockito.anyString(), Mockito.anyObject());
        Mockito.doAnswer(answers.new GetAnswer()).when(req).getAttribute(Mockito.anyString());
        new CandidateServlet().doGet(req, resp);
        Collection<Candidate> candidates = (Collection<Candidate>) req.getAttribute("candidates");
        Assert.assertThat(candidates.size(), is(3));
        Candidate candidate = candidates.stream().filter((currentCandidate) -> currentCandidate.getId() == 2).collect(Collectors.toList()).get(0);
        Assert.assertThat(candidate.getId(), is(2));
        Assert.assertThat(candidate.getName(), is("Middle Java"));
        Assert.assertThat(candidate.getCity(), is(0));
        Assert.assertThat(candidate.getUserPhotos().size(), is(0));
    }

    /**
     * Здесь проверяется добавление кандидата в сервлете.
     *
     * @throws ServletException исключения сервлета при работе.
     * @throws IOException      исключения ввода вывода при работе с хранилищем.
     */
    @Test
    public void whenAddCandidateThenSuccess() throws ServletException, IOException {
        Store store = new StubStore();
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.getInst()).thenReturn(store);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        Mockito.when(req.getParameter("id")).thenReturn("11");
        Mockito.when(req.getParameter("name")).thenReturn("Плотник Вася");
        Mockito.when(req.getParameter("city")).thenReturn("99");
        new CandidateServlet().doPost(req, resp);
        Candidate addedCandidate = store.findCandidateById(11);
        Assert.assertThat(addedCandidate.getId(), is(11));
        Assert.assertThat(addedCandidate.getName(), is("Плотник Вася"));
        Assert.assertThat(addedCandidate.getCity(), is(99));
        Assert.assertThat(store.findAllCandidates().size(), is(4));
    }
}