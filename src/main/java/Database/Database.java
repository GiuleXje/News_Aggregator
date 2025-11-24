package Database;

import NewsData.News;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

public final class Database {
    private final static Database db = new Database();
    public ConcurrentHashMap<News, Integer> news;
    public ConcurrentHashMap<String, Integer> languages;
    public static int numberOfThreads;
    public static String articlesFile;
    public static String additionalFile;

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
        news = new ConcurrentHashMap<>();
        languages = new ConcurrentHashMap<>();
    }

    public static Database getInstance() {
        return db;
    }

    public static void closeLog() {
        if (out != null)
            out.close();
    }

}
