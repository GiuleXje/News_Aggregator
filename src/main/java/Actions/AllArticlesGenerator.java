package Actions;

import Database.Database;
import NewsData.News;
import Database.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

public final class AllArticlesGenerator {
    private final Database db = Database.getInstance();
    private static final String filePath = "all_articles.txt";

    static class CompareNews implements Comparator<News> {
        @Override
        public int compare(News n1, News n2) {
            if (n1.getPublished().equals(n2.getPublished()))
                return n1.getUuid().compareTo(n2.getUuid());

            return -n1.getPublished().compareTo(n2.getPublished());
        }
    }

    public void handle() throws IOException {
        PriorityQueue<News> queue = new PriorityQueue<>(new CompareNews());

        queue.addAll(db.news.keySet());

        File file = new File(filePath);
        file.createNewFile();

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        while (!queue.isEmpty()) {
            News n1 = queue.poll();
            if (Database.mostRecentArticle == null) {
                Database.mostRecentArticle = new Pair<>(n1.getPublished(), n1.getUrl());
            }

            bw.write(n1.getUuid() + ' ' + n1.getPublished());
            bw.newLine();
        }
    }
}
