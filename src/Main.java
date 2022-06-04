import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1)
            System.exit(-args.length);
        List<String> lines = readFromFile(args[0]);
        int capacity = Integer.parseInt(lines.get(0).split(" ")[0]);
        int numberOfItems = Integer.parseInt(lines.get(0).split(" ")[1]);

        int[] values = new int[numberOfItems];
        int[] weights = new int[numberOfItems];
        for (int i = 0; i < numberOfItems; i++) {
            values[i] = Integer.parseInt(lines.get(1).split(",")[i]);
            weights[i] = Integer.parseInt(lines.get(2).split(",")[i]);
        }
        List<Product> products = createProducts(values, weights);
        findOptimalAnswer(capacity, products);
    }

    private static void findOptimalAnswer(int capacity, List<Product> products) {
        int tmpCapacity = capacity;
        boolean[] answer = new boolean[products.size()];
        boolean[] tmpAnswer = new boolean[products.size()];
        int value = 0;
        int tmpValue = 0;
        int numberOfIterations = 0;
        for (int i = 0; i < products.size() - 1; i++) {
            if (products.get(i).getWeight() > tmpCapacity)
                break;
            tmpCapacity -= products.get(i).getWeight();
            tmpValue += products.get(i).getValue();
            tmpAnswer[i] = true;
            for (int j = products.size() - 1; j > i; j--) {
                numberOfIterations++;
                if (products.get(j).getWeight() <= tmpCapacity) {
                    tmpAnswer[j] = true;
                    tmpCapacity -= products.get(j).getWeight();
                    tmpValue += products.get(j).getValue();
                }
            }
            if (value < tmpValue) {
                value = tmpValue;
                System.arraycopy(tmpAnswer, 0, answer, 0, answer.length);
            }
            tmpValue = 0;
            java.util.Arrays.fill(tmpAnswer, false);
            tmpCapacity = capacity;
        }
        printAnswer(numberOfIterations, answer, value);
    }

    private static void printAnswer(int numberOfIterations, boolean[] answer, int value) {
        System.out.print("The final vector is: ");
        for (boolean b : answer)
            System.out.print(b ? '1' : '0');
        System.out.print("\nThe number of iterations is " + numberOfIterations);

        System.out.println(", and the value is " + value);
    }

    private static List<String> readFromFile(String path) {
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                lines.add(line);
        } catch (java.io.IOException ignored) {
            System.out.println("File not found!");
            System.exit(-538);
        }
        return lines;
    }

    private static List<Product> createProducts(int[] values, int[] weights) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < values.length; i++)
            products.add(new Product(values[i], weights[i]));
        return products;
    }
}

record Product(int value, int weight) {

    public int getValue() {
        return value;
    }

    public int getWeight() {
        return weight;
    }
}
