import java.util.ArrayList;
import java.util.Scanner;

public class TodoListApp {
    private ArrayList<String> todoList = new ArrayList<>();

    public void displayList() {
        if (todoList.isEmpty()) {
            System.out.println("\nYour to-do list is empty.");
        } else {
            System.out.println("\n════════════════════════════════");
            System.out.println("            To-Do List");
            System.out.println("════════════════════════════════");
            for (int i = 0; i < todoList.size(); i++) {
                System.out.println(" [ " + (i + 1) + " ] " + todoList.get(i));
            }
            System.out.println("════════════════════════════════");
        }
    }

    public void addTask(String task) {
        todoList.add(task);
        System.out.println("\nTask added: " + task);
    }

    public void removeTask(int taskIndex) {
        if (taskIndex >= 0 && taskIndex < todoList.size()) {
            String removedTask = todoList.remove(taskIndex);
            System.out.println("\nTask removed: " + removedTask);
        } else {
            System.out.println("\nInvalid task index.");
        }
    }

    public static void main(String[] args) {
        TodoListApp app = new TodoListApp();
        Scanner scanner = new Scanner(System.in);

        int choice;

        do {
            System.out.println("\n════════════════════════════════");
            System.out.println("             To-Do List");
            System.out.println("════════════════════════════════");
            System.out.println(" [1] Display To-Do List");
            System.out.println(" [2] Add Task");
            System.out.println(" [3] Remove Task");
            System.out.println(" [4] Quit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    app.displayList();
                    break;
                case 2:
                    System.out.print("\nEnter the task to add: ");
                    String newTask = scanner.nextLine();
                    app.addTask(newTask);
                    break;
                case 3:
                    System.out.print("\nEnter the task index to remove: ");
                    int taskIndex = scanner.nextInt();
                    app.removeTask(taskIndex - 1);
                    break;
                case 4:
                    System.out.println("\nGoodbye!");
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        } while (choice != 4);

        scanner.close();
    }
}
