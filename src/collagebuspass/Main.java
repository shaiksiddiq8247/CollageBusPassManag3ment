package collagebuspass;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static BusPassService service =
            new BusPassService();


    public static void main(String[] args) {

        // Create data folder
        FileManager.createDataFolder();

        // Load saved students
        ArrayList<Student> savedStudents =
                FileManager.loadStudents();

        for (Student student : savedStudents) {
            service.addStudent(student);
        }

        // Load saved passes
        ArrayList<BusPass> savedPasses =
                FileManager.loadPasses();

        for (BusPass pass : savedPasses) {
            service.addPass(pass);
        }

        // Start menu
        mainMenu();
    }


    // ================= MAIN MENU =================

    public static void mainMenu() {

        while (true) {

            System.out.println();
            System.out.println(
                "===== COLLEGE BUS PASS MANAGEMENT ====="
            );

            System.out.println("1. Register Student");
            System.out.println("2. Admin Menu");
            System.out.println("3. Student Menu");
            System.out.println("0. Exit");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    registerStudent();
                    break;

                case 2:
                    adminMenu();
                    break;

                case 3:
                    studentMenu();
                    break;

                case 0:

                    System.out.println(
                        "Thank you for using the system."
                    );

                    return;

                default:

                    System.out.println(
                        "Invalid choice."
                    );
            }
        }
    }


    // ================= REGISTER STUDENT =================

    public static void registerStudent() {

        System.out.println("\n===== REGISTER STUDENT =====");

        String name = readText("Enter student name: ");

        String department =
                readText("Enter department: ");

        int id = service.getNextStudentId();

        Student student =
                new Student(id, name, department);

        service.addStudent(student);

        FileManager.saveStudents(
                getStudents()
        );

        System.out.println(
            "Student registered successfully."
        );

        System.out.println(
            "Your Student ID is: " + id
        );
    }


    // ================= ADMIN MENU =================

    public static void adminMenu() {

        while (true) {

            System.out.println();
            System.out.println("===== ADMIN MENU =====");

            System.out.println("1. View Students");
            System.out.println("2. View Pass Requests");
            System.out.println("3. Approve Pass");
            System.out.println("4. View Monthly Reports");
            System.out.println("0. Back");

            int choice =
                    readInt("Enter choice: ");

            switch (choice) {

                case 1:

                    service.showStudents();

                    break;

                case 2:

                    service.showPasses();

                    break;

                case 3:

                    approvePass();

                    break;

                case 4:

                    monthlyReport();

                    break;

                case 0:

                    return;

                default:

                    System.out.println(
                        "Invalid choice."
                    );
            }
        }
    }


    // ================= STUDENT MENU =================

    public static void studentMenu() {

        int studentId =
                readInt("Enter student ID: ");

        Student student =
                service.findStudent(studentId);

        if (student == null) {

            System.out.println(
                "Student ID not found."
            );

            return;
        }

        while (true) {

            System.out.println();
            System.out.println(
                "===== STUDENT MENU ====="
            );

            System.out.println(
                "Student: " + student.getName()
            );

            System.out.println("1. Request Bus Pass");
            System.out.println("2. View My Pass History");
            System.out.println("3. Cancel My Pass");
            System.out.println("0. Back");

            int choice =
                    readInt("Enter choice: ");

            switch (choice) {

                case 1:

                    requestBusPass(studentId);

                    break;

                case 2:

                    service.showStudentPasses(studentId);

                    break;

                case 3:

                    cancelPass(studentId);

                    break;

                case 0:

                    return;

                default:

                    System.out.println(
                        "Invalid choice."
                    );
            }
        }
    }


    // ================= REQUEST PASS =================

    public static void requestBusPass(int studentId) {

        System.out.println(
            "\n===== BUS PASS REQUEST ====="
        );

        int year;

        while (true) {

            year = readInt(
                "Enter year (2000-2100): "
            );

            if (year >= 2000 && year <= 2100) {
                break;
            }

            System.out.println(
                "Invalid year."
            );
        }


        int month;

        while (true) {

            month = readInt(
                "Enter month (1-12): "
            );

            if (month >= 1 && month <= 12) {
                break;
            }

            System.out.println(
                "Invalid month."
            );
        }


        System.out.println(
            "Selected month: "
            + service.getMonthName(month)
        );


        System.out.println("\nRoutes:");

        System.out.println("1. North");
        System.out.println("2. South");
        System.out.println("3. East");


        int routeChoice;

        while (true) {

            routeChoice =
                    readInt("Choose route: ");

            if (routeChoice >= 1 &&
                routeChoice <= 3) {

                break;
            }

            System.out.println(
                "Invalid route."
            );
        }


        String route =
                service.getRoutes()[routeChoice - 1];


        BusPass pass =
                service.requestPass(
                    studentId,
                    year,
                    month,
                    route
                );


        if (pass == null) {

            System.out.println(
                "You already have a current pass "
                + "for this month."
            );

            return;
        }


        FileManager.savePasses(
                getPasses()
        );


        System.out.println(
            "Pass " + pass.getId()
            + " saved. Status: "
            + pass.getStatus()
        );
    }


    // ================= APPROVE PASS =================

    public static void approvePass() {

        System.out.println(
            "\n===== APPROVE PASS ====="
        );

        int passId =
                readInt("Enter pass ID: ");

        BusPass pass =
                service.findPass(passId);

        if (pass == null) {

            System.out.println(
                "Pass ID not found."
            );

            return;
        }


        if (pass.getStatus() != PassStatus.PENDING) {

            System.out.println(
                "Only PENDING passes can be approved."
            );

            return;
        }


        boolean approved =
                service.approvePass(passId);


        if (approved) {

            FileManager.savePasses(
                getPasses()
            );

            System.out.println(
                "Pass saved. Status: APPROVED"
            );
        }
    }


    // ================= CANCEL PASS =================

    public static void cancelPass(int studentId) {

        System.out.println(
            "\n===== CANCEL PASS ====="
        );

        int passId =
                readInt("Enter pass ID: ");


        BusPass pass =
                service.findPass(passId);


        if (pass == null) {

            System.out.println(
                "Pass ID not found."
            );

            return;
        }


        if (pass.getStudentId() != studentId) {

            System.out.println(
                "You can cancel only your own pass."
            );

            return;
        }


        if (pass.getStatus() == PassStatus.CANCELLED) {

            System.out.println(
                "This pass is already cancelled."
            );

            return;
        }


        boolean cancelled =
                service.cancelPass(
                    passId,
                    studentId
                );


        if (cancelled) {

            FileManager.savePasses(
                getPasses()
            );

            System.out.println(
                "Pass saved. Status: CANCELLED"
            );

            System.out.println(
                "You may submit a new request."
            );
        }
    }


    // ================= MONTHLY REPORT =================

    public static void monthlyReport() {

        System.out.println(
            "\n===== MONTHLY REPORT ====="
        );

        int year;

        while (true) {

            year =
                readInt("Enter year (2000-2100): ");

            if (year >= 2000 &&
                year <= 2100) {

                break;
            }

            System.out.println(
                "Invalid year."
            );
        }


        int month;

        while (true) {

            month =
                readInt("Enter month (1-12): ");

            if (month >= 1 &&
                month <= 12) {

                break;
            }

            System.out.println(
                "Invalid month."
            );
        }


        String report =
                service.createMonthlyReport(
                    year,
                    month
                );


        System.out.println();
        System.out.println(report);


        System.out.println(
            "1. Save Report"
        );

        System.out.println(
            "0. Back"
        );


        int choice =
                readInt("Enter choice: ");


        if (choice == 1) {

            FileManager.saveReport(report);
        }
    }


    // ================= INPUT METHODS =================

    public static int readInt(String message) {

        while (true) {

            System.out.print(message);

            String input =
                    sc.nextLine();

            try {

                return Integer.parseInt(
                    input
                );

            } catch (NumberFormatException e) {

                System.out.println(
                    "Please enter a valid number."
                );
            }
        }
    }


    public static String readText(String message) {

        while (true) {

            System.out.print(message);

            String input =
                    sc.nextLine().trim();


            if (input.isEmpty()) {

                System.out.println(
                    "This field cannot be empty."
                );

                continue;
            }


            if (input.contains("|")) {

                System.out.println(
                    "Character | is not allowed."
                );

                continue;
            }


            return input;
        }
    }


    // ================= GET RECORDS =================

    @SuppressWarnings("unchecked")
	private static ArrayList<Student> getStudents() {

        try {

            java.lang.reflect.Field field =
                    BusPassService.class
                    .getDeclaredField("students");

            field.setAccessible(true);

            return (ArrayList<Student>)
                    field.get(service);

        } catch (Exception e) {

            return new ArrayList<>();
        }
    }


    @SuppressWarnings("unchecked")
	private static ArrayList<BusPass> getPasses() {

        try {

            java.lang.reflect.Field field =
                    BusPassService.class
                    .getDeclaredField("passes");

            field.setAccessible(true);

            return (ArrayList<BusPass>)
                    field.get(service);

        } catch (Exception e) {

            return new ArrayList<>();
        }
    }
}