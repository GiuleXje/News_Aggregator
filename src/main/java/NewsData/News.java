package NewsData;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


public final class News {
    private final String title;
    private final String uuid;
    private final String author;
    private final String text;
    private final String published;
    private final String language;
    private final ArrayList<String> categories;

    public News(String title, String uuid, String author, String text, String published, String language, ArrayList<String> categories) {
        this.title = title;
        this.uuid = uuid;
        this.author = author;
        this.text = text;
        this.published = published;
        this.language = language;
        this.categories = categories;
    }

    public News(ConcurrentHashMap<String, ObjectNode> input) {
        title = input.get("title").toString();
        uuid = input.get("uuid").toString();
        author = input.get("author").toString();
        text = input.get("text").toString();
        published = input.get("published").toString();
        language = input.get("language").toString();
        // TODO: let's see for categories
        categories = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getUuid() {
        return uuid;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getPublished() {
        return published;
    }

    public String getLanguage() {
        return language;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof News news))
            return false;

        return news.getTitle().equals(title) || news.getUuid().equals(uuid);
    }
}
