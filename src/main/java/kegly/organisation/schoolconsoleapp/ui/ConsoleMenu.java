package kegly.organisation.schoolconsoleapp.ui;

import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;
import kegly.organisation.schoolconsoleapp.service.SchoolDataFacade;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private final SchoolDataFacade dataFactory;
    private final Scanner scanner;

    public ConsoleMenu(SchoolDataFacade dataFactory) {
        this.dataFactory = dataFactory;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> handleFindGroups();
                case "2" -> handleFindStudentsByCourse();
                case "3" -> handleAddStudent();
                case "4" -> handleDeleteStudent();
                case "5" -> handleAddStudentToCourse();
                case "6" -> handleRemoveStudentFromCourse();
                case "0" -> {
                    running = false;
                    System.out.println("Exiting...");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("---SCHOOL CONSOLE APP---");
        System.out.println("1. Find all groups with less or equal students' number");
        System.out.println("2. Find all students related to the course");
        System.out.println("3. Add a new student");
        System.out.println("4. Delete a student");
        System.out.println("5. Add a student to the course");
        System.out.println("6. Remove the student from course");
        System.out.println("\nTo stop program type 0");
        System.out.print("> ");
    }

    private void handleFindGroups() {
        System.out.print("Enter max student count: ");
        int count = readInt();
        List<Group> groups = dataFactory.findGroupsWithLessOrEqualStudents(count);

        if (groups.isEmpty()) {
            System.out.println("No groups found.");
        } else {
            System.out.printf("%-10s %-20s%n", "ID", "NAME");
            System.out.println("------------------------------");
            groups.forEach(g -> System.out.printf("%-10d %-20s%n", g.getId(), g.getName()));
        }
    }

    private void handleFindStudentsByCourse() {
        System.out.print("Enter course name (e.g. Mathematics): ");
        String courseName = scanner.nextLine();
        List<Student> students = dataFactory.findByCourseName(courseName);

        if (students.isEmpty()) {
            System.out.println("No students found for this course.");
        } else {
            System.out.printf("%-5s %-15s %-15s%n", "ID", "First Name", "Last Name");
            System.out.println("----------------------------------------");
            students.forEach(s -> System.out.printf("%-5d %-15s %-15s%n",
                s.getId(), s.getFirstName(), s.getLastName()));
        }
    }

    private void handleAddStudent() {
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        dataFactory.addNewStudent(firstName, lastName, null);
        System.out.println("Student added successfully.");
    }

    private void handleDeleteStudent() {
        System.out.print("Enter student ID to delete: ");
        int id = readInt();
        dataFactory.deleteStudentById(id);
        System.out.println("Operation completed.");
    }

    private void handleAddStudentToCourse() {
        System.out.print("Enter student ID: ");
        int studentId = readInt();
        System.out.print("Enter course ID: ");
        int courseId = readInt();

        try {
            dataFactory.addStudentToCourse(studentId, courseId);
            System.out.println("Student added to course.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleRemoveStudentFromCourse() {
        System.out.print("Enter student ID: ");
        int studentId = readInt();
        System.out.print("Enter course ID: ");
        int courseId = readInt();

        dataFactory.removeStudentFromCourse(studentId, courseId);
        System.out.println("Operation completed.");
    }

    private int readInt() {
        while (true) {
            try {
                String line = scanner.nextLine();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}