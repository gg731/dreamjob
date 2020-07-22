package Model;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private BasicDataSource dataSource = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();

        try (BufferedReader io = new BufferedReader(new FileReader("db.properties"))) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        dataSource.setDriverClassName(cfg.getProperty("jdbc.driver"));
        dataSource.setUrl(cfg.getProperty("jdbc.url"));
        dataSource.setUsername(cfg.getProperty("jdbc.username"));
        dataSource.setPassword(cfg.getProperty("jdbc.password"));
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }


    @Override
    public Collection<Post> findAllPost() {
        List<Post> posts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM post")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    posts.add(new Post(rs.getInt("id"), rs.getString("name")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidate() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM candidate")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    candidates.add(new Candidate(rs.getInt("id"), rs.getString("name")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidates;
    }

    @Override
    public void savePost(Post post) {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post where id = ?")) {
            ps.setInt(1, post.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                updatePost(post);
            } else {
                createPost(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveCandidate(Candidate candidate) {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate where id = ?")) {
            ps.setInt(1, candidate.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                updateCandidate(candidate);
            } else {
                createCandidate(candidate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Post findByIdPost(int id) {
        Post post = new Post(0, "");
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post where id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                post.setId(rs.getInt("id"));
                post.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public Candidate findByIdCandidate(int id) {
        Candidate candidate = new Candidate(0, "");
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidate;
    }

    private Post createPost(Post post) {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO  post(name) values(?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    post.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    private void updatePost(Post post) {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE post SET name= ? WHERE id= ?")
        ) {
            ps.setString(1, post.getName());
            ps.setInt(2, post.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createCandidate(Candidate candidate) {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO candidate(name) values(?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                while (rs.next()) {
                    candidate.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateCandidate(Candidate candidate) {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE candidate SET name = ? WHERE id = ?")) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
