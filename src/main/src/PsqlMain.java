import Model.Candidate;
import Model.Post;
import Model.PsqlStore;
import Model.Store;

public class PsqlMain {
    public static void main(String[] args) {
        Store psql = PsqlStore.instOf();
        psql.savePost(new Post(9, "Java 105"));

        for (Post post : psql.findAllPost()) {
            System.out.println(post.getId() + " " + post.getName());
        }

        psql.saveCandidate(new Candidate(0, "Java Middle"));

        for (Candidate c : psql.findAllCandidate()) {
            System.out.println(c.getId() + " " + c.getName());
        }
    }
}
