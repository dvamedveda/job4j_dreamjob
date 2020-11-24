package ru.job4j.dream.model;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * Тесты класса ru.job4j.dream.model.Post
 */
public class PostTest {

    /**
     * Проверяем создание поста.
     */
    @Test
    public void whenCreatePostThenSuccess() {
        Post post = new Post(1, "test", "test desc", 1L);
        Assert.assertThat(post.getId(), is(1));
        Assert.assertThat(post.getName(), is("test"));
        Assert.assertThat(post.getDesc(), is("test desc"));
        Assert.assertThat(post.getCreated(), is(1L));
    }

    /**
     * Проверяем редактирование поста.
     */
    @Test
    public void whenEditPostThenSuccess() {
        Post post = new Post(1, "test", "test desc", 1L);
        post.setId(2);
        post.setName("tset");
        post.setDesc("desc test");
        post.setCreated(2L);
        Assert.assertThat(post.getId(), is(2));
        Assert.assertThat(post.getName(), is("tset"));
        Assert.assertThat(post.getDesc(), is("desc test"));
        Assert.assertThat(post.getCreated(), is(2L));
    }

    /**
     * Проверяем сравнение постов.
     */
    @Test
    public void whenCreateTwoPostsWithSameDataThenEquals() {
        Post post1 = new Post(1, "test", "desc", 1L);
        Post post2 = new Post(1, "test", "desc", 1L);
        Assert.assertThat(post1.getId(), is(1));
        Assert.assertThat(post1.getName(), is("test"));
        Assert.assertThat(post1.getDesc(), is("desc"));
        Assert.assertThat(post1.getCreated(), is(1L));
        Assert.assertThat(post2.getId(), is(1));
        Assert.assertThat(post2.getName(), is("test"));
        Assert.assertThat(post2.getDesc(), is("desc"));
        Assert.assertThat(post2.getCreated(), is(1L));
        Assert.assertTrue(post1.equals(post2));
    }
}