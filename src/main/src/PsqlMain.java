import Model.Post;
import Model.PsqlStore;
import Model.Store;

public class PsqlMain {
    public static void main(String[] args) {
        Store psql = PsqlStore.instOf();
        psql.save(new Post(5, "Java 103"));


        for (Post post : psql.findAllPost()) {
            System.out.println(post.getId() + " " + post.getName());
        }

        System.out.println(psql.findById(7).getName());


    }
}
