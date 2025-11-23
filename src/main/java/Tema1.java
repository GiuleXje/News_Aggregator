import Database.Database;

import java.util.Scanner;
import InputHandler.NewsReader;

public class Tema1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String line = scanner.nextLine();
        String[] content = line.split(" ");
        Database.numberOfThreads = Integer.parseInt(content[0]);
        Database.articlesFile = content[1];
        Database.additionalFile = content[2];
        System.out.println(Database.numberOfThreads);
        System.out.println(Database.articlesFile);
        System.out.println(Database.additionalFile);

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

    }
}
