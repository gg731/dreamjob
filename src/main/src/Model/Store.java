package Model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static Store INST = new Store();
    private Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Junior Java Job", "Вакансия 1", new Date()));
        posts.put(2, new Post(2, "Middle Java Job", "Вакансия 2", new Date()));
        posts.put(3, new Post(3, "Senior Java Job", "Вакансия 3", new Date()));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }
}
