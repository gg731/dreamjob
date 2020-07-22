package Model;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPost();

    Collection<Candidate> findAllCandidate();

    void savePost(Post post);

    void saveCandidate(Candidate candidate);

    Post findByIdPost(int id);

    Candidate findByIdCandidate(int id);
}
