package Model;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPost();

    Collection<Candidate> findAllCandidate();

    void save(Post post);

    Post findById(int id);
}
