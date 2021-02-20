package ru.job4j.dream.store;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;
import ru.job4j.dream.model.User;

import static org.hamcrest.CoreMatchers.is;


/**
 * Тесты класса ru.job4j.dream.store.TokenStorage.
 */
public class TokenStorageTest {

    /**
     * Проверяется, что для пользователя правильно генерируется токен.
     */
    @Test
    public void whenAddToStorageThenSuccess() {
        Store store = MemStore.getInst();
        TokenStorage tokenStorage = store.getTokenStorage();
        int id = 11;
        String name = "test name";
        String email = "test email";
        String pass = "test pass";
        String secret = "0123456789";
        User user = new User(id, name, email, pass);
        tokenStorage.addToken(user);
        String expect = DigestUtils.sha256Hex(id + name + email + secret);
        Assert.assertTrue(tokenStorage.verifyToken(expect));
    }

    /**
     * Проверяется, что некорректный токен не проходит проверку.
     */
    @Test
    public void whenWrongUserThenInvalidToken() {
        Store store = MemStore.getInst();
        TokenStorage tokenStorage = store.getTokenStorage();
        int id = 11;
        String name = "test name";
        String email = "test email";
        String pass = "test pass";
        String secret = " ";
        User user = new User(id, name, email, pass);
        tokenStorage.addToken(user);
        String expect = DigestUtils.sha256Hex(id + name + email + secret);
        Assert.assertFalse(tokenStorage.verifyToken(expect));
    }

    /**
     * Проверяется, что по токену можно получить объект пользователя.
     */
    @Test
    public void whenGetUserByTokenThenSuccess() {
        Store store = MemStore.getInst();
        TokenStorage tokenStorage = store.getTokenStorage();
        int id = 11;
        String name = "test name";
        String email = "test email";
        String pass = "test pass";
        String secret = "0123456789";
        User user = new User(id, name, email, pass);
        tokenStorage.addToken(user);
        String expect = DigestUtils.sha256Hex(id + name + email + secret);
        User resultUser = tokenStorage.getUserByToken(expect);
        Assert.assertEquals(user, resultUser);
    }

    /**
     * Проверяется, что по пользователю правильно получается токен.
     */
    @Test
    public void whenGetTokenThenSuccess() {
        Store store = MemStore.getInst();
        TokenStorage tokenStorage = store.getTokenStorage();
        int id = 11;
        String name = "test name";
        String email = "test email";
        String pass = "test pass";
        String secret = "0123456789";
        User user = new User(id, name, email, pass);
        tokenStorage.addToken(user);
        String expect = DigestUtils.sha256Hex(id + name + email + secret);
        String result = tokenStorage.getToken(user);
        Assert.assertThat(result, is(expect));
    }
}