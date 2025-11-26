package Actions;

import Database.Database;
import NewsData.News;

import java.io.*;
import java.util.*;

import Database.Pair;


public final class CategoriesHandler {
    private final Database db = Database.getInstance();

    static class CompareUUID implements Comparator<News> {
        @Override
        public int compare(News n1, News n2) {
            return n1.getUuid().compareTo(n2.getUuid());
        }
    }

    public void handle() throws IOException {
        HashMap<String, PriorityQueue<News>> categories = new HashMap<>();
        HashSet<News> validNews = new HashSet<>();
        for (News n : db.news.keySet()) {
            String[] categoriesArray = n.getCategories();
            boolean ok = true;
            for (String c : categoriesArray) {
                if (!db.categories.containsKey(c)) {
                    ok = false;
                    break;
                }
            }

            if (ok) {
                validNews.add(n);
            }
        }

        CompareUUID comparator = new CompareUUID();
        for (News n : validNews) {
            String[] categoriesArray = n.getCategories();
            for (String c : categoriesArray) {
                PriorityQueue<News> q = categories.computeIfAbsent(c, k -> new PriorityQueue<>(comparator));

                q.add(n);
            }
        }

        for (HashMap.Entry<String, PriorityQueue<News>> entry : categories.entrySet()) {
            String category = entry.getKey();
            PriorityQueue<News> q = entry.getValue();
            Integer size = q.size();

            if (Database.topCategory == null) {
                Database.topCategory = new Pair<>(category, size);
            } else if (size.compareTo(Database.topCategory.getValue()) > 0) {
                Database.topCategory.setKey(category);
                Database.topCategory.setValue(size);
            } else if (Database.topCategory.getValue() == q.size()) {
                if (category.compareTo(Database.topCategory.getKey()) < 0)
                    Database.topCategory.setKey(category);
            }

            String[] words = category.split("[ ,]");
            StringBuilder fileName = new StringBuilder();
            for (String word : words) {
                if (!word.isEmpty()) {
                    fileName.append(word).append("_");
                }
            }

            fileName.replace(fileName.length() - 1, fileName.length(), "");
            fileName.append(".txt");

            File file = new File(fileName.toString());
            file.createNewFile();

            BufferedWriter fw = new BufferedWriter(new FileWriter(file));

            while (!q.isEmpty()) {
                String uuid = Objects.requireNonNull(q.poll()).getUuid();
                fw.write(uuid);
                fw.newLine();
            }
        }
    }
}
