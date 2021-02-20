package ru.job4j.dream.store;

import org.apache.commons.codec.digest.DigestUtils;
import ru.job4j.dream.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Хранилище токенов для авторизации CORS-запросов.
 * Хранилище содержит пары юзер-токен и вспомогательные методы.
 */
public class TokenStorage {

    /**
     * Сектретная часть токена.
     */
    private final static String SECRET = "0123456789";

    /**
     * Хранилище пар юзер-токен.
     */
    private final Map<User, String> tokenStorage = new ConcurrentHashMap<>();

    /**
     * Добавить токен для юзера.
     *
     * @param user объект юзера.
     */
    public void addToken(User user) {
        String data = user.getId() + user.getName() + user.getEmail() + SECRET;
        String token = DigestUtils.sha256Hex(data);
        this.tokenStorage.put(user, token);
    }

    /**
     * Получить пользователя по токену.
     *
     * @param token строка токена.
     * @return объект пользователя, если не найдено - null.
     */
    public User getUserByToken(String token) {
        User result = null;
        for (Map.Entry<User, String> entry : this.tokenStorage.entrySet()) {
            if (entry.getValue().equals(token)) {
                result = entry.getKey();
                break;
            }
        }
        return result;
    }

    /**
     * Получить токен для пользователя.
     *
     * @param user объект пользователя.
     * @return строка токена.
     */
    public String getToken(User user) {
        return this.tokenStorage.get(user);
    }

    /**
     * Проверить токен. Существует ли юзер, соответствующий данному токену.
     *
     * @param token строка токена.
     * @return результат проверки.
     */
    public boolean verifyToken(String token) {
        boolean result = false;
        for (String nextToken : this.tokenStorage.values()) {
            if (nextToken.equals(token)) {
                result = true;
                break;
            }
        }
        return result;
    }
}