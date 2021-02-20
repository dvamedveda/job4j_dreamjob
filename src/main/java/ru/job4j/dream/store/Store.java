package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
     * @param path   путь к фото.
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

    /**
     * Получение всех фотографий кандидата.
     *
     * @param userId идентификатор кандидата.
     * @return список идентификаторов фото кандидата.
     */
    List<Integer> getUserPhotos(int userId);

    /**
     * Сохранить юзера.
     *
     * @param user объект юзера.
     * @return объект созданного юзера
     */
    User saveUser(User user);

    /**
     * Получить объект юзера по адресу электронной почты.
     *
     * @param email адрес электронной почты.
     * @return объект юзера.
     */
    User getUser(String email);

    /**
     * Проверить, существует ли уже данный пользователь.
     *
     * @param email адрес электронной почты.
     * @return результат проверки.
     */
    boolean userExists(String email);

    /**
     * Получить список всех городов из хранилища.
     *
     * @return карта-список городов с идентификаторами.
     */
    Map<Integer, String> getCities();

    /**
     * Получить хранилище токенов для авторизации CORS-запросов.
     *
     * @return хранилище токенов.
     */
    TokenStorage getTokenStorage();
}