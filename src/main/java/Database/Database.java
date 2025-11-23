package Database;

import NewsData.News;

import java.util.concurrent.ConcurrentHashMap;

public final class Database {
    private final static Database db = new Database();
    public ConcurrentHashMap<News, Integer> news;
    public ConcurrentHashMap<String, Integer> languages;
    public static int numberOfThreads;
    public static String articlesFile;
    public static String additionalFile;

    private Database () {
        news = new ConcurrentHashMap<>();
        languages = new ConcurrentHashMap<>();
    }

    public static Database getInstance() {
        return db;
    }

}
