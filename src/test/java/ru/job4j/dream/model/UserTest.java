package ru.job4j.dream.model;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * Тесты класса ru.job4j.dream.model.User.
 */
public class UserTest {

    /**
     * Проверка создания и получения свойств пользователя.
     */
    @Test
    public void whenCreateUserThenSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("admin");
        user.setEmail("email");
        user.setPassword("password");
        Assert.assertThat(user.getId(), is(1));
        Assert.assertThat(user.getName(), is("admin"));
        Assert.assertThat(user.getEmail(), is("email"));
        Assert.assertThat(user.getPassword(), is("password"));
    }

    /**
     * Проверка сравнения одинаковых пользователей.
     */
    @Test
    public void whenCreateTwoSimilarUserThenEquals() {
        User user1 = new User();
        user1.setId(1);
        user1.setName("admin");
        user1.setEmail("email");
        user1.setPassword("password");
        User user2 = new User();
        user2.setId(1);
        user2.setName("admin");
        user2.setEmail("email");
        user2.setPassword("password");
        Assert.assertThat(user1.equals(user2), is(true));
    }

    /**
     * Проверка сравнения разных пользователей.
     */
    @Test
    public void whenCreateTwoDifferentUserThenEquals() {
        User user1 = new User();
        user1.setId(1);
        user1.setName("admin");
        user1.setEmail("email");
        user1.setPassword("password");
        User user2 = new User();
        user2.setId(2);
        user2.setName("admin");
        user2.setEmail("email");
        user2.setPassword("password");
        Assert.assertThat(user1.equals(user2), is(false));
    }
}