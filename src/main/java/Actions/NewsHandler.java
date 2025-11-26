package Actions;

import Database.Database;
import NewsData.News;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
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


        int num_tasks = 4;
        int start = (int)((double)num_tasks / Database.numberOfThreads * id);
        int end = (int)((double)(num_tasks / Database.numberOfThreads) * (id + 1));

        // each thread, if possible, is assigned a task
        for (int i = start; i < end; i++) {
            switch (i) {
                case 0: // handle the category output files
                    CategoriesHandler worker0 = new CategoriesHandler();
                    try {
                        worker0.handle();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 1: // handle the language output files
                    LanguageHandler worker1 = new LanguageHandler();
                    try {
                        worker1.handle();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 2: // generate the file containing all the sorted news
                    AllArticlesGenerator worker2 = new AllArticlesGenerator();
                    try {
                        worker2.handle();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 3:
                    break;
                default:
                    break;
            }
        }
    }

}
