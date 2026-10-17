import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    // deliveries are represented by their id, start time, and finish time.
    record Delivery(String id, int start, int finish) {}
    // has an id, a list of deliveries, and the time at which the driver becomes available.
    record Driver(int id, int availableAt, List<Delivery> schedule) {}

    public static void main(String[] args) throws IOException {
        // file location and input them into a list of deliveries
        // then required test case will run 
        Path input = Path.of(args.length > 0 ? args[0] : "data/deliveries.txt");
        List<Delivery> deliveries = readDeliveries(input);

        System.out.println("Pioneer Delivery - Greedy Algorithm Portfolio");
        System.out.println("Input: " + input);
        taskA(deliveries);
        taskB(deliveries);
        taskC(deliveries);
        runRequiredTests();
    }

    // read file in id, start, finish format and return the list
    static List<Delivery> readDeliveries(Path path) throws IOException {
        List<Delivery> result = new ArrayList<>();
        for (String line : Files.readAllLines(path)) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            if (parts.length != 3) throw new IllegalArgumentException("Expected: id start finish");
            int start = Integer.parseInt(parts[1]);
            int finish = Integer.parseInt(parts[2]);
            if (start >= finish) throw new IllegalArgumentException("Start must be before finish: " + line);
            result.add(new Delivery(parts[0], start, finish));
        }
        return result;
    }

    //choose the delivery that finishes earliest.
    static List<Delivery> maximumCompatible(List<Delivery> input) {
        List<Delivery> sorted = new ArrayList<>(input);
        sorted.sort(Comparator.comparingInt(Delivery::finish).thenComparingInt(Delivery::start));
        List<Delivery> selected = new ArrayList<>();
        int availableAt = Integer.MIN_VALUE;
        for (Delivery d : sorted) {
            if (d.start() >= availableAt) {
                selected.add(d);
                availableAt = d.finish();
            }
        }
        return selected;
    }

    static void taskA(List<Delivery> deliveries) {
        List<Delivery> selected = maximumCompatible(deliveries);
        System.out.println("\nTask A - Maximum deliveries for one driver");
        for (Delivery d : selected) {
            System.out.println(d.id() + " [" + d.start() + ", " + d.finish() + ")");
        }
        System.out.println("Total completed: " + selected.size());
    }

    static List<Driver> minimumDrivers(List<Delivery> input) {
        List<Delivery> sorted = new ArrayList<>(input);
        //process devliveries in start time order
        sorted.sort(Comparator.comparingInt(Delivery::start).thenComparingInt(Delivery::finish));
        // the priority queue will always have the earliest available driver at the head
        PriorityQueue<Driver> available = new PriorityQueue<>(Comparator.comparingInt(Driver::availableAt));
        List<Driver> all = new ArrayList<>();
        for (Delivery delivery : sorted) {
            Driver driver = available.peek();
            if (driver == null || driver.availableAt() > delivery.start()) {
                // Every existing driver is busy, so a new driver is required.
                driver = new Driver(all.size() + 1, delivery.finish(), new ArrayList<>());
                all.add(driver);
            } else {
                // Reuse the earliest-free driver and update their availability.
                available.poll();
                driver = new Driver(driver.id(), delivery.finish(), driver.schedule());
            }
            driver.schedule().add(delivery);
            available.add(driver);
        }
        return all;
    }
// uses minimumDrivers to complete task b
    static void taskB(List<Delivery> deliveries) {
        List<Driver> drivers = minimumDrivers(deliveries);
        System.out.println("\nTask B - Minimum drivers: " + drivers.size());
        for (Driver driver : drivers) {
            System.out.print("Driver " + driver.id() + ": ");
            driver.schedule().forEach(d -> System.out.print(d.id() + " "));
            System.out.println();
        }
    }

    static void runRequiredTests() throws IOException {
        String[] taskAFiles = {
                "taskA_case1.txt", "taskA_case2.txt", "taskA_case3.txt",
                "taskA_case4.txt", "taskA_case5.txt", "taskA_case6.txt"
        };
        int[] taskAExpected = {0, 1, 3, 1, 3, 2};

        String[] taskBFiles = {
                "taskB_case1.txt", "taskB_case2.txt", "taskB_case3.txt",
                "taskB_case4.txt", "taskB_case5.txt", "taskB_case6.txt"
        };
        int[] taskBExpected = {0, 1, 1, 3, 2, 2};

        System.out.println("\nRequired Task A tests");
        for (int i = 0; i < taskAFiles.length; i++) {
            List<Delivery> deliveries = readDeliveries(Path.of("data/tests", taskAFiles[i]));
            int actual = maximumCompatible(deliveries).size();
            printTestResult(taskAFiles[i], actual, taskAExpected[i]);
        }

        System.out.println("\nRequired Task B tests");
        for (int i = 0; i < taskBFiles.length; i++) {
            List<Delivery> deliveries = readDeliveries(Path.of("data/tests", taskBFiles[i]));
            int actual = minimumDrivers(deliveries).size();
            printTestResult(taskBFiles[i], actual, taskBExpected[i]);
        }

        String[] taskCFiles = {
                "taskC_case1.txt", "taskC_case2.txt", "taskC_case3.txt",
                "taskC_case4.txt", "taskC_case5.txt", "taskC_case6.txt"
        };
        int[] taskCExpected = {0, 0, 0, 2, 1, 1};

        System.out.println("\nRequired Task C tests");
        for (int i = 0; i < taskCFiles.length; i++) {
            List<Delivery> deliveries = readDeliveries(Path.of("data/tests", taskCFiles[i]));
            int actual = deliveries.size() - maximumCompatible(deliveries).size();
            printTestResult(taskCFiles[i], actual, taskCExpected[i]);
        }
    }

    static void printTestResult(String fileName, int actual, int expected) {
        String result = actual == expected ? "PASS" : "FAIL";
        System.out.println(fileName + " - expected: " + expected
                + ", actual: " + actual + " - " + result);
    }

    static void taskC(List<Delivery> deliveries) {
        // If k deliveries can be accepted, the other n-k must be rejected.
        int accepted = maximumCompatible(deliveries).size();
        System.out.println("\nTask C - Minimum rejected: " + (deliveries.size() - accepted));
    }
}
