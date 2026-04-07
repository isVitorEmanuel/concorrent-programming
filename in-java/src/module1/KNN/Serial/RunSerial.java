package module1.KNN.Serial;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @class RunSerial
 * @brief Entry point for the serial execution of the KNN algorithm.
 * This class orchestrates the process of creating a target point,
 * initializing the KNN engine, and triggering the stream-based prediction
 * to handle large datasets (e.g., 1GB) without exceeding memory limits.
 */
public class RunSerial {
    /**
     * @brief Main execution method.
     * This method defines the dataset path, creates the target point to be classified,
     * and measures the flow of the serial KNN prediction.
     * * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        String path = "dataset_1gb.csv";

        ArrayList<Integer> targetValues = new ArrayList<>(Arrays.asList(500, 500, 500));
        Neighbor target = new Neighbor(targetValues, "Unknow");

        KNNSerial knn = new KNNSerial();
        int k = 3;

        System.out.println(">>> init prediction...");
        String result = knn.predictStream(path, target, k);

        System.out.println("predict class: " + result);
    }
}
