import java.util.Random;


// this class will sum up the intervals
class Parallel extends Thread {

    private int[] nums; // one dimensional array of numbers
    private int low; // low index
    private int high; // high index
    private int partialSum; // the sum between the high and low index

    // constructor
    public Parallel(int[] nums, int low, int high) {
        this.nums = nums;
        this.low = low;
        this.high = Math.min(high, nums.length);
    }

    // getter
    public int getPartialSum() {
        return this.partialSum;
    }

    public void run() {
        partialSum = sum(nums, low, high);
    }

    public static int sum(int[] nums) {
        return sum(nums, 0, nums.length);

    }

    public static int sum(int[] nums, int low, int high) {

        int sum = 0;
        for (int i = low; i < high; i++) {
            sum += nums[i];
        }
        return sum;
    }

    public static int parallelSum(int[] nums) {
        return parallelSum(nums, Runtime.getRuntime().availableProcessors());

    }

    public static int parallelSum(int[] nums, int numOfthreads) {

        int size = (int) Math.ceil(nums.length * 1.0 / numOfthreads);
        Parallel[] sums = new Parallel[numOfthreads]; // 4:08 timestamp in video
        for (int i = 0; i < numOfthreads; i++) {
            sums[i] = new Parallel(nums, i * size, (i + 1) * size); // high index
            sums[i].start();
        }

        try {
            for (Parallel sum : sums) { // joins parallels
                sum.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int total = 0;
        for (Parallel sum : sums) {
            total += sum.getPartialSum();
        }
        return total;
    }
}

public class Main {

    public static void main(String[] args) {

        Random rand = new Random();
        // Make an array of 200 million random numbers between 1 and 10
        int[] arr = new int[200000000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt(10) + 1;
        }
        // display the sum and times for both cases
        long start = System.currentTimeMillis();
        System.out.println("Single sum: " + Parallel.sum(arr));
        System.out.println("Single sum takes " + (System.currentTimeMillis() - start) + "ms\n");
        start = System.currentTimeMillis();
        System.out.println("Parallel sum: " + Parallel.parallelSum(arr));
        System.out.println("Parallel sum takes " + (System.currentTimeMillis() - start) + "ms");

    }

}
// notes: https://www.youtube.com/watch?v=y3nGmZTq_RM&t=126s
// https://www.youtube.com/watch?v=Pz3Ldav_JqA
