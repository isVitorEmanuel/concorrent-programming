package module1.KNN.Serial;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * @class DataSetGenerator
 * @brief Utility class to generate a large-scale dataset for performance testing.
 * This class creates a CSV file of approximately 1GB containing random
 * numerical features and labels to simulate a real-world Big Data scenario
 * for the KNN algorithm.
 */
public class DataSetGenerator {
        public static void main(String[] args) {
            String filename = "dataset_1gb.csv";
            long targetSizeBytes = 1024L * 1024L * 1024L;

            String[] labels = {"Class_A", "Class_B", "Class_C", "Class_D", "Class_E"};
            Random random = new Random();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                File file = new File(filename);
                long currentSize = 0;
                int count = 0;

                System.out.println(">>> start generating the 1GB dataset...");

                while (currentSize < targetSizeBytes) {
                    int f1 = random.nextInt(1000);
                    int f2 = random.nextInt(1000);
                    int f3 = random.nextInt(1000);
                    String label = labels[random.nextInt(labels.length)];

                    String line = f1 + "," + f2 + "," + f3 + "," + label + "\n";
                    writer.write(line);

                    count++;
                    if (count % 500000 == 0) {
                        writer.flush();
                        currentSize = file.length();
                        System.out.printf("- progress: %.2f mb generated...\r", currentSize / (1024.0 * 1024.0));
                    }
                }

                System.out.println("\n\n>>> dataset generated successfully");
                System.out.println(">>> final size: " + (file.length() / (1024.0 * 1024.0)) + " mb");
                System.out.println(">>> total lines: " + count);

            } catch (IOException e) {
                System.err.println("error in generate file: " + e.getMessage());
            }
    }
}
