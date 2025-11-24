import Database.Database;

import java.util.Scanner;
import InputHandler.NewsReader;

public class Tema1 {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("USAGE: Tema1 [nr_threads] [articles_file] [additional_file]");
            return;
        }

        Database.numberOfThreads = Integer.parseInt(args[0]);
        Database.articlesFile = args[1];
        Database.additionalFile = args[2];

        // let's handle the read first
        Thread[] threads = new Thread[Database.numberOfThreads];
        for (int i = 0; i < Database.numberOfThreads; i++) {
            threads[i] = new Thread(new NewsReader(i));
            threads[i].start();
        }

        for (int i = 0; i < Database.numberOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Database.closeLog();
    }
}
