package Actions;

import Database.Database;
import NewsData.News;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.util.LinkedHashSet;
import java.util.concurrent.BrokenBarrierException;

public final class NewsHandler implements Runnable {
    private final Database db = Database.getInstance();
    private final int id;
    private final String[] artFilesContent;
    private final String[] extraFilesContent;


    public NewsHandler(final int id, final String[] artFilesContent, final String[] extraFilesContent) {
        this.id = id;
        this.artFilesContent = artFilesContent;
        this.extraFilesContent = extraFilesContent;
    }


    @Override
    public void run() {
        // read all the data and get the news read by this thread
        NewsReader nr = new NewsReader(id,  artFilesContent, extraFilesContent);
        LinkedHashSet<News> news = nr.handleRead();

        // now proceed with adding only the news that are not duplicates
        for (News n : news) {
            assert db.newsWithDuplicateTitles.containsKey(n.getTitle());
            assert db.newsWithDuplicateUUID.containsKey(n.getUuid());
            if (db.newsWithDuplicateTitles.get(n.getTitle()) == 1 && db.newsWithDuplicateUUID.get(n.getUuid()) == 1) {
                db.news.put(n, id);
            } else {
                Database.duplicatesFound++;
            }
        }

        try {
            Database.readBarrier.await();
        } catch(InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        // clear the duplicates
        if (id == 0) {
            db.newsWithDuplicateTitles.clear();
            db.newsWithDuplicateUUID.clear();
        }


    }

}
