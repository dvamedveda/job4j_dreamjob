package ru.job4j.dream.store;

import org.junit.Assert;
import org.junit.Test;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

/**
 * Тесты класса ru.job4j.dream.store.Model.
 */
public class MemStoreTest {

    /**
     * Проверяем, что в хранилище находится три вакансии.
     */
    @Test
    public void whenCreateInstThenHaveThreePosts() {
        MemStore memStore = MemStore.getInst();
        List<Post> posts = new ArrayList<>(memStore.findAllPosts());
        for (int i = 0; i < posts.size(); i++) {
            Assert.assertThat(posts.get(i).getId(), is(i + 1));
        }
        Assert.assertThat(posts.size(), is(3));
    }

    /**
     * Проверяем, что в хранилище находятся три кандидата.
     */
    @Test
    public void whenCreateInstThenHaveThreeCandidates() {
        MemStore memStore = MemStore.getInst();
        List<Candidate> candidates = new ArrayList<>(memStore.findAllCandidates());
        for (int i = 0; i < candidates.size(); i++) {
            Assert.assertThat(candidates.get(i).getId(), is(i + 1));
        }
        Assert.assertThat(candidates.size(), is(3));
    }
}