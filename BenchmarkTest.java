import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class BenchmarkTest {

    private BufferedWriter writer;

    public BenchmarkTest() {
        File newFile = new File("data.txt");
        try {
            writer = new BufferedWriter(new FileWriter(newFile));
        } catch (IOException ignored) {
        }
    }
    
    public BenchmarkTest(String fileLocation) {
        try {
            writer = new BufferedWriter(new FileWriter(fileLocation));
        } catch (IOException e) {
            System.out.println("File not  found");
        }
    }

    public static void main(String[] args) {
        try {
            new BenchmarkTest().test();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void test() throws IOException {
        for (int i = 5000; i <= 1_000_000; i *= 1.2) {
            writer.write(i + "   " + benchmarkLatency(i, 100_000_000) + "\n");
            writer.flush();
        }
        writer.close();
    }

    private double benchmarkLatency(int sizeBytes, int iterations) {
        int [] array = randomCyclicPermutation(sizeBytes / 4);
	    int pointer = 0;
	    long start = new Date().getTime();
	    for (int i = 0; i < iterations; i++) {
		    pointer = array[pointer];
	    }
        long end = new Date().getTime();
	    return ((double) (end - start))  / iterations * 1_000_000;
    }

    private int[] randomCyclicPermutation(int length) {
        int[] result = new int[length];
        int[] unusedIndexes = new int[length - 1];
        for (int i = 0; i < length - 1; i++) {
            unusedIndexes[i] = i + 1;
        }
        int currentIndex = 0;
        for (int i = 0; i < length - 1; i++) {
            int indexToRemove = (int) (Math.random() * Integer.MAX_VALUE) % unusedIndexes.length;
            int nextInd = unusedIndexes[indexToRemove];
            unusedIndexes = remove(unusedIndexes, indexToRemove);
            result[currentIndex] = nextInd;
            currentIndex = nextInd;
        }
        return result;
    }

    private int[] remove(int[] unusedIndexes, int index) {
        int[] copyArray = new int[unusedIndexes.length - 1];
        System.arraycopy(unusedIndexes, 0, copyArray, 0, index); 
        System.arraycopy(unusedIndexes, index + 1, copyArray, index, unusedIndexes.length - index - 1);
        return copyArray;
    }
}