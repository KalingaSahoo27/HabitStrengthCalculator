import java.io.*;
import java.util.*;

class Habit {
    String name;
    String description;
    String frequency;
    List<Boolean> completions = new ArrayList<>();

    public Habit(String name, String description, String frequency) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
    }

    public void addCompletion(boolean completed) {
        completions.add(completed);
    }

    public double getStrength() {
        int total = completions.size();
        int completed = 0;
        for (boolean b : completions) {
            if (b) completed++;
        }
        return total == 0 ? 0 : ((double) completed / total) * 100;
    }

    public String getFeedback() {
        double strength = getStrength();
        if (strength >= 80) return "Great job! You're consistently keeping up with your habit.";
        else if (strength >= 50) return "Good job! You're making progress, but there's room for improvement.";
        else return "Keep going! Try to be more consistent in completing your habit.";
    }

    @Override
    public String toString() {
        return name + "," + description + "," + frequency + "," + completions.toString().replaceAll("[\\[\\] ]", "");
    }
}

public class HabitStrengthCalculator {
    static final String FILE_NAME = "habit_data.txt";
    static Scanner scanner = new Scanner(System.in);
    static List<Habit> habits = new ArrayList<>();

    public static void main(String[] args) {
        loadHabitsFromFile();

        while (true) {
            System.out.println("\n--- Habit Strength Calculator ---");
            System.out.println("1. Create a New Habit");
            System.out.println("2. Mark Today's Habit Completion");
            System.out.println("3. View Habit Strength & Feedback");
            System.out.println("4. Save and Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1 -> createHabit();
                case 2 -> markCompletion();
                case 3 -> viewFeedback();
                case 4 -> {
                    saveHabitsToFile();
                    System.out.println("Data saved. Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void createHabit() {
        System.out.print("Enter habit name: ");
        String name = scanner.nextLine();
        System.out.print("Enter habit description: ");
        String desc = scanner.nextLine();
        System.out.print("Enter frequency (Daily/Weekly): ");
        String freq = scanner.nextLine();

        Habit habit = new Habit(name, desc, freq);
        habits.add(habit);
        System.out.println("Habit created successfully.");
    }

    static void markCompletion() {
        if (habits.isEmpty()) {
            System.out.println("No habits found. Create one first.");
            return;
        }

        for (int i = 0; i < habits.size(); i++) {
            System.out.println((i + 1) + ". " + habits.get(i).name);
        }

        System.out.print("Select a habit to mark (number): ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index >= 0 && index < habits.size()) {
            System.out.print("Did you complete it today? (yes/no): ");
            String input = scanner.nextLine().trim().toLowerCase();
            habits.get(index).addCompletion(input.equals("yes"));
            System.out.println("Completion status recorded.");
        } else {
            System.out.println("Invalid habit selected.");
        }
    }

    static void viewFeedback() {
        if (habits.isEmpty()) {
            System.out.println("No habits to show.");
            return;
        }

        for (Habit habit : habits) {
            System.out.println("\nHabit: " + habit.name);
            System.out.printf("Strength: %.2f%%\n", habit.getStrength());
            System.out.println("Feedback: " + habit.getFeedback());
        }
    }

    static void saveHabitsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Habit habit : habits) {
                writer.println(habit);
            }
        } catch (IOException e) {
            System.out.println("Error saving habits: " + e.getMessage());
        }
    }

    static void loadHabitsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 4);
                Habit habit = new Habit(parts[0], parts[1], parts[2]);
                String[] completed = parts[3].split(",");
                for (String c : completed) {
                    habit.addCompletion(c.equalsIgnoreCase("true"));
                }
                habits.add(habit);
            }
        } catch (IOException e) {
            System.out.println("Error loading habits: " + e.getMessage());
        }
    }
}
