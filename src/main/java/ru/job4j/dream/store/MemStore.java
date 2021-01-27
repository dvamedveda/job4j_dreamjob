package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Хранилище заявок. Хранилище является синглтоном.
 */
public class MemStore implements Store {
    private static final MemStore INST = new MemStore();
    private static final AtomicInteger POST_ID = new AtomicInteger(4);
    private static final AtomicInteger CANDIDATE_ID = new AtomicInteger(4);
    private static final AtomicInteger PHOTO_ID = new AtomicInteger();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final Map<Integer, Map<Integer, String>> usersWithPhoto = new ConcurrentHashMap<>();

    private MemStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Работа для джуниор Java разработчика", 1L));
        posts.put(2, new Post(2, "Middle Java Job", "Работа для джуниор Java разработчика", 2L));
        posts.put(3, new Post(3, "Senior Java Job", "Работа для джуниор Java разработчика", 3L));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static MemStore getInst() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return this.posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return this.candidates.values();
    }

    public void savePost(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(CANDIDATE_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    public Post findPostById(int id) {
        return posts.get(id);
    }

    public Candidate findCandidateById(int id) {
        return candidates.get(id);
    }

    @Override
    public int saveImage(int userId, String path) {
        int id = PHOTO_ID.incrementAndGet();
        if (this.usersWithPhoto.containsKey(userId)) {
            this.usersWithPhoto.get(userId).put(id, path);
        } else {
            Map<Integer, String> newUser = new ConcurrentHashMap<>();
            newUser.put(id, path);
            this.usersWithPhoto.put(userId, newUser);
        }
        return id;
    }

    @Override
    public String getImagePath(int id) {
        String result = "";
        for (Map<Integer, String> userPhotos : this.usersWithPhoto.values()) {
            if (userPhotos.containsKey(id)) {
                result = userPhotos.get(id);
                break;
            }
        }
        return result;
    }

    @Override
    public void removeImage(int id) {
        for (Map<Integer, String> userPhotos : this.usersWithPhoto.values()) {
            if (userPhotos.containsKey(id)) {
                userPhotos.remove(id);
                break;
            }
        }
    }

    @Override
    public List<Integer> getUserPhotos(int userId) {
        ArrayList<Integer> result = new ArrayList<>();
        if (this.usersWithPhoto.containsKey(userId)) {
            result.addAll(this.usersWithPhoto.get(userId).keySet());
        }
        return result;
    }
}