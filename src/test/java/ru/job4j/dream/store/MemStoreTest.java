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
        Store memStore = MemStore.getInst();
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
        Store memStore = MemStore.getInst();
        List<Candidate> candidates = new ArrayList<>(memStore.findAllCandidates());
        for (int i = 0; i < candidates.size(); i++) {
            Assert.assertThat(candidates.get(i).getId(), is(i + 1));
        }
        Assert.assertThat(candidates.size(), is(3));
    }

    /**
     * Проверяем, что картинки успешно сохраняются и удаляются.
     * В одном тесте, так как работаем с синглтоном из-за чего не гарантируется
     * правильное выполнение тестов в разном порядке.
     */
    @Test
    public void whenSaveImageThenSuccess() {
        Store store = MemStore.getInst();
        store.saveImage(1, "abcd");
        store.saveImage(2, "dcba");
        store.saveImage(2, "aaaa");
        Assert.assertThat(store.getImagePath(1), is("abcd"));
        Assert.assertThat(store.getImagePath(2), is("dcba"));
        Assert.assertThat(store.getImagePath(3), is("aaaa"));
        Assert.assertThat(store.getUserPhotos(1).size(), is(1));
        Assert.assertThat(store.getUserPhotos(2).size(), is(2));
        store.removeImage(2);
        Assert.assertThat(store.getImagePath(1), is("abcd"));
        Assert.assertThat(store.getImagePath(2), is(""));
        Assert.assertThat(store.getImagePath(3), is("aaaa"));
        Assert.assertThat(store.getUserPhotos(1).size(), is(1));
        Assert.assertThat(store.getUserPhotos(2).size(), is(1));
    }
}