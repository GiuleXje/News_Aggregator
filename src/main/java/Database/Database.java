package Database;

import NewsData.News;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

public final class Database {
    private final static Database db = new Database();
    // duplicate free hashmap
    public ConcurrentHashMap<News, Integer> news;
    public ConcurrentHashMap<String, Integer> languages;
    public ConcurrentHashMap<String, Integer> categories;
    public ConcurrentHashMap<String, Integer> words;
    public static int numberOfThreads;
    public static String articlesFile;
    public static String additionalFile;
    public static int duplicatesFound;

    public ConcurrentHashMap<String, Integer> newsWithDuplicateTitles;
    public ConcurrentHashMap<String, Integer> newsWithDuplicateUUID;

    public static CyclicBarrier readBarrier;

    // use to output some logs, TODO: delete when done
    public static PrintWriter out;

    static {
        try {
            out = new PrintWriter(new FileWriter("../logs.txt", true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Database () {
        languages = new ConcurrentHashMap<>();
        categories = new ConcurrentHashMap<>();
        words = new ConcurrentHashMap<>();
        news = new ConcurrentHashMap<>();
        duplicatesFound = 0;
        newsWithDuplicateTitles = new ConcurrentHashMap<>();
        newsWithDuplicateUUID = new ConcurrentHashMap<>();
    }

    public static Database getInstance() {
        return db;
    }

    // TODO: delete when done
    public static void closeLog() {
        if (out != null)
            out.close();
    }

    public void printAll() {
        for (News news : news.keySet()) {
            out.println(news.getTitle());
        }

        out.println();

        for (String l : languages.keySet()) {
            out.println(l);
        }

        out.println();

        for (String c : categories.keySet()) {
            out.println(c);
        }

        out.println();

        for (String w : words.keySet()) {
            out.println(w);
        }

        out.println();
    }

    // just to make sure we have read the number of threads before we use the barrier
    public static void initBarrier() {
        readBarrier = new CyclicBarrier(numberOfThreads);
    }

}
