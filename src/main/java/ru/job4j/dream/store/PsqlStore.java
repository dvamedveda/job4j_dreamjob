package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;

public class PsqlStore implements Store {

    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(new FileReader("db.properties"))) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException();
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store getInst() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from post")) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    posts.add(new Post(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getString("description"),
                            result.getTimestamp("created").getTime()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from candidate")) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Candidate candidate = new Candidate(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getInt("city_id"));
                    candidate.setUserPhotos(this.getUserPhotos(result.getInt("id")));
                    candidates.add(candidate);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }

    @Override
    public void savePost(Post post) {
        if (post.getId() == 0) {
            createPost(post);
        } else {
            updatePost(post);
        }
    }

    @Override
    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            createCandidate(candidate);
        } else {
            updateCandidate(candidate);
        }
    }

    @Override
    public Post findPostById(int id) {
        String command = "select * from post where id = ?";
        Post post = null;
        try (Connection conn = pool.getConnection();
             PreparedStatement statement = conn.prepareStatement(command)) {
            statement.setInt(1, id);
            statement.execute();
            try (ResultSet result = statement.getResultSet()) {
                while (result.next()) {
                    post = new Post(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getString("description"),
                            result.getTimestamp("created").getTime()
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public Candidate findCandidateById(int id) {
        String command = "select * from candidate where id = ?";
        Candidate candidate = null;
        try (Connection conn = pool.getConnection();
             PreparedStatement statement = conn.prepareStatement(command)) {
            statement.setInt(1, id);
            statement.execute();
            try (ResultSet result = statement.getResultSet()) {
                while (result.next()) {
                    candidate = new Candidate(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getInt("city_id"));
                    candidate.setUserPhotos(this.getUserPhotos(result.getInt("id")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidate;
    }

    private Post createPost(Post post) {
        String command = "insert into post(name, description, created) values (?, ?, ?)";
        try (Connection conn = pool.getConnection();
             PreparedStatement statement = conn.prepareStatement(command, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getName());
            statement.setString(2, post.getDesc());
            statement.setTimestamp(3, new Timestamp(post.getCreated()));
            statement.execute();
            try (ResultSet result = statement.getGeneratedKeys()) {
                if (result.next()) {
                    post.setId(result.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    private void updatePost(Post post) {
        String command = "update post set name = ?, description = ? where id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement statement = conn.prepareStatement(command)) {
            statement.setString(1, post.getName());
            statement.setString(2, post.getDesc());
            statement.setInt(3, post.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Candidate createCandidate(Candidate candidate) {
        String command = "insert into candidate(name, city_id) values (?, ?)";
        try (Connection conn = pool.getConnection();
             PreparedStatement statement = conn.prepareStatement(command, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, candidate.getName());
            statement.setInt(2, candidate.getCity());
            statement.execute();
            try (ResultSet result = statement.getGeneratedKeys()) {
                if (result.next()) {
                    candidate.setId(result.getInt(1));
                }
            }
            String updateCommand = "update photo set user_id = ? where user_id = 0";
            try (PreparedStatement updateStatement = conn.prepareStatement(updateCommand)) {
                updateStatement.setInt(1, candidate.getId());
                updateStatement.executeUpdate();
            }
            candidate.setUserPhotos(this.getUserPhotos(candidate.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidate;
    }

    private void updateCandidate(Candidate candidate) {
        String command = "update candidate set name = ?, city_id = ? where id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement statement = conn.prepareStatement(command)) {
            statement.setString(1, candidate.getName());
            statement.setInt(2, candidate.getCity());
            statement.setInt(3, candidate.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int saveImage(int userId, String path) {
        String command = "insert into photo(path, user_id) values (?, ?)";
        int photoId = 0;
        try (Connection conn = pool.getConnection();
             PreparedStatement statement = conn.prepareStatement(command, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, path);
            statement.setInt(2, userId);
            statement.execute();
            try (ResultSet result = statement.getGeneratedKeys()) {
                if (result.next()) {
                    photoId = result.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photoId;
    }

    @Override
    public String getImagePath(int id) {
        String path = "";
        String command = "select * from photo where id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement statement = conn.prepareStatement(command)) {
            statement.setInt(1, id);
            statement.execute();
            try (ResultSet result = statement.getResultSet()) {
                while (result.next()) {
                    path = result.getString("path");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    @Override
    public void removeImage(int id) {
        String command = "delete from photo where id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement statement = conn.prepareStatement(command)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Integer> getUserPhotos(int userId) {
        String command = "select id from photo where user_id = ?";
        ArrayList<Integer> userPhotos = new ArrayList<>();
        try (Connection conn = pool.getConnection();
             PreparedStatement statement = conn.prepareStatement(command)) {
            statement.setInt(1, userId);
            statement.execute();
            try (ResultSet result = statement.getResultSet()) {
                while (result.next()) {
                    userPhotos.add(result.getInt("id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userPhotos;
    }

    @Override
    public User saveUser(User user) {
        User result = new User();
        result.setName(user.getName());
        result.setEmail(user.getEmail());
        result.setPassword(user.getPassword());
        String command = "insert into users(name, email, password) values (?, ?, ?)";
        try (Connection conn = pool.getConnection();
             PreparedStatement statement = conn.prepareStatement(command, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.execute();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                while (resultSet.next()) {
                    result.setId(resultSet.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public User getUser(String email) {
        User result = null;
        String command = "select * from users where email = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement statement = conn.prepareStatement(command)) {
            statement.setString(1, email);
            statement.execute();
            try (ResultSet rs = statement.getResultSet()) {
                while (rs.next()) {
                    result = new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean userExists(String email) {
        boolean result = false;
        String command = "select id from users where email = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement statement = conn.prepareStatement(command)) {
            statement.setString(1, email);
            statement.execute();
            try (ResultSet rs = statement.getResultSet()) {
                if (rs.next()) {
                    result = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<Integer, String> getCities() {
        Map<Integer, String> result = new HashMap<>();
        String command = "select * from city";
        try (Connection conn = pool.getConnection();
             PreparedStatement statement = conn.prepareStatement(command)) {
            statement.execute();
            try (ResultSet rs = statement.getResultSet()) {
                while (rs.next()) {
                    result.put(rs.getInt("id"), rs.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}