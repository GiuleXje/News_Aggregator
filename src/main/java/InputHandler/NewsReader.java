package InputHandler;

import Database.Database;
import NewsData.News;
import com.fasterxml.jackson.core.*;

public class NewsReader implements Runnable {
    private final Database db = Database.getInstance();
    private static final String articleDir = "../../../../checker/input/articles/";
    private final int id;

    public NewsReader(final int id) {
        this.id = id;
    }

    public void run() {
        return;
    }

}
