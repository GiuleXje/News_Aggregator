package NewsData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class News {
    private String title;
    private String uuid;
    private String author;
    private String text;
    private String published;
    private String language;
    private String url;
    private String[] categories;

    public News(){}


    public News(JsonNode node) {
        title = node.get("title").asText();
        uuid = node.get("uuid").asText();
        author = node.get("author").asText();
        text = node.get("text").asText();
        published = node.get("published").asText();
        language = node.get("language").asText();
        url = node.get("url").asText();
        categories = node.get("categories").asText().split(",");// shouldn't work
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

    public String[] getCategories() {
        return categories;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof News news))
            return false;

        return news.getTitle().equals(title) || news.getUuid().equals(uuid);
    }

}
