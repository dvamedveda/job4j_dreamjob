package ru.job4j.dream.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;
import ru.job4j.dream.store.StubStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static org.hamcrest.CoreMatchers.is;

/**
 * Тесты класса LoadCityServlet.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class LoadCityServletTest {

    /**
     * Здесь проверяется правильная вадача списка городов сервелетом.
     *
     * @throws ServletException ошибки сервлета.
     * @throws IOException      ошибки ввода и вывода.
     */
    @Test
    public void whenGetCitiesThenSuccess() throws ServletException, IOException {
        Store store = new StubStore();
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.getInst()).thenReturn(store);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        Mockito.when(resp.getWriter()).thenReturn(writer);
        new LoadCityServlet().doGet(req, resp);
        String result = out.toString();
        String expected = "{\"0\":\"UNKNOWN\",\"1\":\"Москва\",\"2\":\"Владивосток\",\"3\":\"Воронеж\"}";
        Assert.assertThat(result, is(expected));
        out.close();
    }
}