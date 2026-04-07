package module1.KNN.Serial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @class KNNSerial
 * @brief Implements the K-Nearest Neighbors algorithm in a serial (single-threaded) manner.
 * This class provides methods to classify data points using both in-memory
 * and stream-based (file-system) approaches.
 */
public class KNNSerial {

    /**
     * @class DistanceRecord
     * @brief Auxiliary class to store the distance between a neighbor and the target point.
     * Implements Comparable to allow sorting by distance.
     */
    private static class DistanceRecord implements Comparable<DistanceRecord> {
        /** @brief Reference to the neighbor data point. */
        Neighbor neighbor;
        /** @brief Calculated distance from the target. */
        double distance;

        /**
         * @brief Constructor for DistanceRecord.
         * @param neighbor The data point.
         * @param distance The calculated distance value.
         */
        public DistanceRecord(Neighbor neighbor, double distance) {
            this.neighbor = neighbor;
            this.distance = distance;
        }

        /**
         * @brief Compares this record with another based on distance.
         * @param other The other record to compare with.
         * @return Integer as this distance is less than, equal to, or greater than the other.
         */
        @Override
        public int compareTo(DistanceRecord other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    /**
     * @brief Performs KNN prediction using a stream-based approach to save memory.
     * Processes the file line by line without loading the full dataset into RAM.
     * @param filePath Path to the CSV dataset.
     * @param target The point to be classified.
     * @param k The number of nearest neighbors to consider.
     * @return The predicted label (String).
     */
    public String predictStream(String filePath, Neighbor target, int k) {
        PriorityQueue<DistanceRecord> pq = new PriorityQueue<>(Collections.reverseOrder());

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Neighbor current = parseLineToNeighbor(line);
                if (current == null) continue;

                double dist = calculateEuclideanDistance(target, current);
                pq.add(new DistanceRecord(current, dist));

                if (pq.size() > k) {
                    pq.poll();
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o ficheiro no Stream: " + e.getMessage());
        }

        Map<String, Integer> labelFrequencies = new HashMap<>();
        for (DistanceRecord record : pq) {
            String label = record.neighbor.getLabel();
            labelFrequencies.put(label, labelFrequencies.getOrDefault(label, 0) + 1);
        }

        return Collections.max(labelFrequencies.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    /**
     * @brief Performs KNN prediction by loading the entire dataset into RAM.
     * @param filePath Path to the CSV dataset.
     * @param target The point to be classified.
     * @param k The number of nearest neighbors to consider.
     * @return The predicted label (String).
     */
    public String predict(String filePath, Neighbor target, int k) {
        List<Neighbor> dataset = readDataset(filePath);
        List<DistanceRecord> distances = new ArrayList<>();

        for (Neighbor dataPoint : dataset) {
            double dist = calculateEuclideanDistance(target, dataPoint);
            distances.add(new DistanceRecord(dataPoint, dist));
        }

        Collections.sort(distances);

        Map<String, Integer> labelFrequencies = new HashMap<>();
        for (int i = 0; i < k && i < distances.size(); i++) {
            String label = distances.get(i).neighbor.getLabel();
            labelFrequencies.put(label, labelFrequencies.getOrDefault(label, 0) + 1);
        }

        return Collections.max(labelFrequencies.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    /**
     * @brief Calculates the Euclidean Distance between two neighbors.
     * @param target The target neighbor point.
     * @param dataPoint The reference neighbor point from the dataset.
     * @return The double value of the Euclidean distance.
     */
    private double calculateEuclideanDistance(Neighbor target, Neighbor dataPoint) {
        double sum = 0.0;
        ArrayList<Integer> targetValues = target.getValues();
        ArrayList<Integer> dataValues = dataPoint.getValues();

        for (int i = 0; i < targetValues.size(); i++) {
            double diff = targetValues.get(i) - dataValues.get(i);
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    /**
     * @brief Helper method to parse a CSV line into a Neighbor object.
     * @param line A single line from the CSV file.
     * @return A Neighbor object or null if the line is malformed.
     */
    private Neighbor parseLineToNeighbor(String line) {
        String[] parts = line.split(",");
        if (parts.length < 2) return null;

        ArrayList<Integer> values = new ArrayList<>();
        try {
            for (int i = 0; i < parts.length - 1; i++) {
                values.add(Integer.parseInt(parts[i].trim()));
            }
            return new Neighbor(values, parts[parts.length - 1].trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * @brief Reads the dataset from a file and returns a list of neighbors.
     * @param path Path to the CSV file.
     * @return A List of Neighbor objects.
     */
    private List<Neighbor> readDataset(String path) {
        List<Neighbor> dataset = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                Neighbor current = parseLineToNeighbor(line);
                if (current != null) {
                    dataset.add(current);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o ficheiro: " + e.getMessage());
        }
        return dataset;
    }
}