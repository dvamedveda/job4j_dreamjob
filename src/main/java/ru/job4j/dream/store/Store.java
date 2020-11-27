package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Хранилище заявок. Хранилище является синглтоном.
 */
public class Store {
    private static final Store INST = new Store();

    private Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Junior Java Job", "Работа для джуниор Java разработчика", 1L));
        posts.put(2, new Post(2, "Middle Java Job", "Работа для джуниор Java разработчика", 2L));
        posts.put(3, new Post(3, "Senior Java Job", "Работа для джуниор Java разработчика", 3L));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static Store getInst() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return this.posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return this.candidates.values();
    }
}