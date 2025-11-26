import Database.Database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import InputHandler.NewsReader;

public class Tema1 {
    public static void main(String[] args) throws IOException {

        if (args.length != 3) {
            System.err.println("USAGE: Tema1 [nr_threads] [articles_file] [additional_file]");
            return;
        }


        Database.numberOfThreads = Integer.parseInt(args[0]);
        Database.articlesFile = args[1];
        Database.additionalFile = args[2];

        // create the read barrier
        Database.initBarrier();

        File artFile = new File(Database.articlesFile);
        if (!artFile.exists()) {
            System.err.println("Can't find the articles file!");
            return;
        }

        File extraFile = new File(Database.additionalFile);
        if (!extraFile.exists()) {
            System.err.println("Can't find the additional file!");
            return;
        }

        String[] articlesFileContent = Files.readAllLines(Paths.get(Database.articlesFile)).toArray(new String[0]);

        String[] additionalFileContent = Files.readAllLines(Paths.get(Database.additionalFile)).toArray(new String[0]);

        // let's handle the read first
        Thread[] threads = new Thread[Database.numberOfThreads];
        for (int i = 0; i < Database.numberOfThreads; i++) {
            threads[i] = new Thread(new NewsReader(i, Database.articlesFile, Database.additionalFile, articlesFileContent, additionalFileContent));
            threads[i].start();
        }

        for (int i = 0; i < Database.numberOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Database db = Database.getInstance();
        db.printAll();
        Database.closeLog();
    }
}
