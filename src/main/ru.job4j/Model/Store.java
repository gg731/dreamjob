package Model;

import java.util.Collection;
import java.util.Map;

public interface Store {
    Collection<Post> findAllPost();

    Collection<Candidate> findAllCandidate();

    void savePost(Post post);

    void saveCandidate(Candidate candidate);

    Post findPostById(int id);

    Candidate findByIdCandidate(int id);

    void deleteCandidate(int id);

    void saveUser(User user);

    User findUserById(int id);

    Collection<User> findAllUser();

    void deleteUser(int id);

    User findUserByEmail(String email);

    Map<Integer,String> getAllCities();

}
