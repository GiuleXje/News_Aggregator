package Actions;

import Database.Database;
import NewsData.News;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;

public final class NewsReader {
    private final Database db = Database.getInstance();
    private final int id;
    private final String[] artFilesContent;
    private final String[] extraFilesContent;

    public NewsReader(final int id, final String[] artFilesContent, final String[] extraFilesContent) {
        this.id = id;
        this.artFilesContent = artFilesContent;
        this.extraFilesContent = extraFilesContent;
    }

    private void JsonToNews(final String jsonPath, final LinkedHashSet<News> news) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectReader reader = mapper.readerFor(News.class);

            File jsonFile = Paths.get(jsonPath).toFile();

            MappingIterator<News> mp = reader.readValues(jsonFile);
            while (mp.hasNext()) {
                News n = mp.next();
                news.add(n);
                db.newsWithDuplicateTitles.compute(n.getTitle(), (_, value) -> value == null ? 1 : value + 1);
                db.newsWithDuplicateUUID.compute(n.getUuid(), (_, value) -> value == null ? 1 : value + 1);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void languageReader(final String languageFilePath) throws IOException {
        List<String> content = Files.readAllLines(new File(languageFilePath).toPath());

        for (int i = 1; i < content.size(); i++) {
            db.languages.put(content.get(i), 1);
        }
    }

    private void categoryReader(final String categoryFilePath) throws IOException{
        List<String> content = Files.readAllLines(new File(categoryFilePath).toPath());

        for (int i = 1; i < content.size(); i++) {
            db.categories.put(content.get(i), 1);
        }
    }

    private void wordsReader(final String wordsFilePath) throws IOException {
        List<String> content = Files.readAllLines(new File(wordsFilePath).toPath());

        for (int i = 1; i < content.size(); i++) {
            db.words.put(content.get(i), 1);
        }
    }

    public LinkedHashSet<News> handleRead() {
        int articles = Integer.parseInt(artFilesContent[0]);

        int start = 1 + (int)((double)articles / Database.numberOfThreads * id);
        int end = 1 + Math.min((int)((id + 1) * (double)articles / Database.numberOfThreads), articles);

        // contains the news that are being read by this thread
        LinkedHashSet<News> news = new LinkedHashSet<>();
        for (int i = start; i < end; i++) {
            // this is for the news files
            int poz = Database.articlesFile.lastIndexOf(File.separator);
            String dir = Database.articlesFile.substring(0, poz);
            String pathToOpen = dir + "/" + artFilesContent[i];

            try {
                JsonToNews(pathToOpen, news);
            } catch(IOException e) {
                Database.out.println(e.getMessage());
            }
        }

        // wait so all finish to read the news
        try {
            Database.readBarrier.await();
        } catch(InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        int files = Integer.parseInt(extraFilesContent[0]);
        start = 1 + (int)((double)files / Database.numberOfThreads * id);
        end = 1 + Math.min((int)((id + 1) * (double)files / Database.numberOfThreads), files);

        for (int i = start; i < end; i++) {
            int pos = Database.additionalFile.lastIndexOf(File.separator);
            String dir =  Database.additionalFile.substring(0, pos);
            String pathToOpen = dir + "/" + extraFilesContent[i];

            switch (i) {
                case 1:
                    try {
                        languageReader(pathToOpen);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 2:
                    try {
                        categoryReader(pathToOpen);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 3:
                    try {
                        wordsReader(pathToOpen);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    break;
            }
        }

        return news;
    }

}
