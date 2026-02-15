package Actions;

import NewsData.News;

import Database.Database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;

import Database.Pair;

public final class LanguageHandler {
    private final Database db = Database.getInstance();

    public void handle() throws IOException {
        HashMap<String, PriorityQueue<String>> languages = new HashMap<>();

        for (News n : db.news.keySet()) {
            if (db.languages.containsKey(n.getLanguage())) {
                PriorityQueue<String> q = languages.computeIfAbsent(n.getLanguage(), k-> new PriorityQueue<>(String::compareTo));

                q.add(n.getUuid());
            }
        }

        for (HashMap.Entry<String, PriorityQueue<String>> entry : languages.entrySet()) {
            PriorityQueue<String> q = entry.getValue();
            int size = q.size();

            String language = entry.getKey();

            if (Database.topLanguage == null) {
                Database.topLanguage = new Pair<>(entry.getKey(), size);
            } else if (size > Database.topLanguage.getValue()) {
                Database.topLanguage.setKey(language);
                Database.topLanguage.setValue(size);
            } else if (size == Database.topLanguage.getValue()) {
                if (language.compareTo(Database.topLanguage.getKey()) < 0) {
                    Database.topLanguage.setKey(language);
                }
            }

            String filePath = language + ".txt";
            File file = new File(filePath);
            file.createNewFile();

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            while (!q.isEmpty()) {
                String uuid = q.poll();
                bw.write(uuid);
                bw.newLine();
            }

        }
    }
}
