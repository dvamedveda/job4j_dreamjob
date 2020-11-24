package ru.job4j.dream.store;

import org.junit.Assert;
import org.junit.Test;
import ru.job4j.dream.model.Post;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

/**
 * Тесты класса ru.job4j.dream.store.Model.
 */
public class StoreTest {

    /**
     * Проверяем создание и работу с хранилищем.  .
     */
    @Test
    public void whenCreateInstThenHaveThreePosts() {
        Store store = Store.getInst();
        List<Post> posts = new ArrayList<>(store.findAll());
        for (int i = 0; i < posts.size(); i++) {
            Assert.assertThat(posts.get(i).getId(), is(i + 1));
        }
        Assert.assertThat(posts.size(), is(3));
    }
}