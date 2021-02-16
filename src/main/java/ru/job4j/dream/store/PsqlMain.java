package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.getInst();
        store.savePost(new Post(0, "Java Job", "some new Java Job", 1L));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName() + " " + post.getDesc() + " " + post.getCreated());
        }
        store.saveCandidate(new Candidate(0, "Ivan Ivanov", 0));
        for (Candidate candidate : store.findAllCandidates()) {
            System.out.println(candidate.getId() + " " + candidate.getName() + candidate.getCity());
        }
    }
}