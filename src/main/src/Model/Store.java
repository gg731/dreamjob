package Model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Store {

    private static Store INST = new Store();
    private Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private static AtomicInteger POST_ID = new AtomicInteger(4);
    private static AtomicInteger CANDIDATE_ID = new AtomicInteger(4);


    private Store() {
        posts.put(1, new Post(1, "Junior Java Job", "Вакансия 1", new Date()));
        posts.put(2, new Post(2, "Middle Java Job", "Вакансия 2", new Date()));
        posts.put(3, new Post(3, "Senior Java Job", "Вакансия 3", new Date()));
        candidates.put(1, new Candidate(1, "Java Junior"));
        candidates.put(2, new Candidate(2, "Java Middle"));
        candidates.put(3, new Candidate(3, "Java Senior"));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidate() {
        return candidates.values();
    }

    public void savePost(Post post) {
        post.setId(POST_ID.incrementAndGet());
        posts.put(post.getId(), post);
    }

    public void saveCandidate(Candidate candidate) {
        candidate.setId(CANDIDATE_ID.incrementAndGet());
        candidates.put(candidate.getId(), candidate);
    }
}
