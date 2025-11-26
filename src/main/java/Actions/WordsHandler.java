package Actions;

import Database.Database;

import java.io.IOException;
import java.util.*;

import NewsData.News;
import Database.Pair;
import java.io.File;

public final class WordsHandler {
    private final Database db = Database.getInstance();
    private static final String filePath = "keywords_count.txt";


    public void handle() throws IOException {
        HashMap<String, Integer> wordsCount = new HashMap<>();

        for (News n : db.news.keySet()) {
            String text = n.getText().toLowerCase();
            String[] words = text.split(" ");
            HashSet<String> seenWords = new HashSet<>();
            for (String word : words) {
                String newWord = word.replaceAll("[^a-z]", "");
                if (!newWord.isEmpty() && !seenWords.contains(newWord)) {
                    seenWords.add(newWord);
                    int prev_val = wordsCount.computeIfAbsent(newWord, k->0);
                    wordsCount.put(newWord, prev_val + 1);
                }
            }
        }

        // TODO: sort the data
    }
}
