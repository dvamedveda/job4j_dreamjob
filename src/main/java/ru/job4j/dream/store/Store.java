package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface Store {

    /**
     * Найти все вакансии
     *
     * @return список вакансий.
     */
    Collection<Post> findAllPosts();

    /**
     * Найти всех кандидатов
     *
     * @return список кандидатов.
     */
    Collection<Candidate> findAllCandidates();

    /**
     * Сохранить вакансию
     *
     * @param post объект вакансии.
     */
    void savePost(Post post);

    /**
     * Сохранить кандидата.
     *
     * @param candidate объект кандидата.
     */
    void saveCandidate(Candidate candidate);

    /**
     * Найти вакансию по идентификатору.
     *
     * @param id идентификатор вакансии.
     * @return объект вакансии.
     */
    Post findPostById(int id);

    /**
     * Найти кандидата по индентификатору.
     *
     * @param id идентификатор кандидата.
     * @return объект кандидата.
     */
    Candidate findCandidateById(int id);

    /**
     * Сохранить фото в хранилище.
     *
     * @param userId идентификатор пользователя.
     * @param path путь к фото.
     * @return индентификатор фото.
     */
    int saveImage(int userId, String path);

    /**
     * Получить путь к фото по идентификатору.
     *
     * @param id идентификатор фото.
     * @return путь к фото.
     */
    String getImagePath(int id);

    /**
     * Удалить фото по идентификатору.
     *
     * @param id идентификатор фото.
     */
    void removeImage(int id);

    List<Integer> getUserPhotos(int userId);
}