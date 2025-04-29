// Zyra Joy Niones

package ExerciseOne;

public class ExerciseOne {
    public static void main(String[] args) {
        System.out.println("\t\tUsing for loop with continue statement:\n");
        printEvenNumbersForLoop();
        
        System.out.println("__________________________________________________________________________________");
        System.out.println("\t\tUsing while loop with a boolean flag:\n");
        printEvenNumbersWhileLoop();
    }

    // (a) Using a for loop with a continue statement
    public static void printEvenNumbersForLoop() {
        int count = 0; // Counter for newline control

        for (int i = 2; i <= 100; i++) {
            if (i % 2 != 0) { // for the new line
                continue; // Skip odd numbers
            }

            System.out.print(i + "\t"); // Print number with a tab
            count++;

            if (count % 10 == 0) { 
                System.out.println(); // New line after every 10 numbers
            }
        }
        System.out.println();
    }

    // (b) Using a while loop with a boolean flag
    public static void printEvenNumbersWhileLoop() {
        int i = 2, count = 0;
        boolean isEven = true; // Boolean flag to track even numbers

        while (i <= 100) {
            if (isEven) { // Check flag
                System.out.print(i + "\t");
                count++;

                if (count % 10 == 0) {
                    System.out.println(); // New line after every 10 numbers
                }
            }
            i += 2; // Move to next even number
        }
        System.out.println();
    }
}




