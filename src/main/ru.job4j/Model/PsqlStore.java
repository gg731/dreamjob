package Model;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PsqlStore implements Store {

    private BasicDataSource dataSource = new BasicDataSource();
    private Map<Integer, String> cities = new HashMap<>();

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

        cities.put(1, "City1");
        cities.put(2, "City2");
        cities.put(3, "City3");
        cities.put(4, "City4");
        cities.put(5, "City5");
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
                    candidates.add(new Candidate(rs.getInt("id"), rs.getString("name"), rs.getString("photoId")));
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
    public Post findPostById(int id) {
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
                candidate.setPhotoId(rs.getString("photoId"));
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
                    candidate.setPhotoId(rs.getInt(1) + ".jpg");
                }
            }
            PreparedStatement psPhotoId = cn.prepareStatement("UPDATE candidate SET photoId = ? WHERE id = ?");
            psPhotoId.setString(1, candidate.getPhotoId());
            psPhotoId.setInt(2, candidate.getId());
            psPhotoId.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateCandidate(Candidate candidate) {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE candidate SET name = ? , photoId = ?  WHERE id = ?")) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getId() + ".jpg");
            ps.setInt(3, candidate.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCandidate(int id) {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM candidate WHERE id = ?")) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(User user) {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM userdb where id = ?")) {
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                updateUser(user);
            } else {
                createUser(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(int id) {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM userdb WHERE id = ?")) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public User findUserByEmail(String email) {
        User user = new User();
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM userdb WHERE email = ?")) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user.getId() != 0) {
            return user;
        } else return null;
    }

    @Override
    public User findUserById(int id) {
        User user = new User();
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM userdb WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public Collection<User> findAllUser() {
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM user")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(
                            new User(
                                    rs.getInt("id"),
                                    rs.getString("name"),
                                    rs.getString("email"),
                                    rs.getString("password")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void updateUser(User user) {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE userdb SET name= ?, email=?,password =? WHERE id= ?")
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createUser(User user) {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO  userdb(name,email,password) values(?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                while (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<Integer, String> getAllCities() {
        return cities;
    }
}
