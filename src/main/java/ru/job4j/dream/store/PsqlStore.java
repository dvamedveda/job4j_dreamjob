package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

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
                    candidates.add(new Candidate(result.getInt("id"), result.getString("name")));
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
                    candidate = new Candidate(result.getInt("id"), result.getString("name"));
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
        String command = "insert into candidate(name) values (?)";
        try (Connection conn = pool.getConnection();
             PreparedStatement statement = conn.prepareStatement(command, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, candidate.getName());
            statement.execute();
            try (ResultSet result = statement.getGeneratedKeys()) {
                if (result.next()) {
                    candidate.setId(result.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidate;
    }

    private void updateCandidate(Candidate candidate) {
        String command = "update candidate set name = ? where id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement statement = conn.prepareStatement(command)) {
            statement.setString(1, candidate.getName());
            statement.setInt(2, candidate.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}