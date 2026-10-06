package collagebuspass;

import java.util.ArrayList;
import java.util.HashSet;

public class BusPassService {

    private ArrayList<Student> students;
    private ArrayList<BusPass> passes;

    private String[] months = {
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private String[] routes = {
        "North", "South", "East"
    };

    private int nextStudentId = 1;
    private int nextPassId = 1;

    public BusPassService() {

        students = new ArrayList<>();
        passes = new ArrayList<>();
    }

    // Generate student ID
    public int getNextStudentId() {
        return nextStudentId++;
    }

    // Generate pass ID
    public int getNextPassId() {
        return nextPassId++;
    }

    // Add student
    public void addStudent(Student student) {

        students.add(student);

        if (student.getId() >= nextStudentId) {
            nextStudentId = student.getId() + 1;
        }
    }

    // Add pass
    public void addPass(BusPass pass) {

        passes.add(pass);

        if (pass.getId() >= nextPassId) {
            nextPassId = pass.getId() + 1;
        }
    }

    // Find student
    public Student findStudent(int id) {

        for (Student student : students) {

            if (student.getId() == id) {
                return student;
            }
        }

        return null;
    }

    // Find pass
    public BusPass findPass(int id) {

        for (BusPass pass : passes) {

            if (pass.getId() == id) {
                return pass;
            }
        }

        return null;
    }

    // Check current pass
    public boolean hasCurrentPass(int studentId, int year, int month) {

        for (BusPass pass : passes) {

            if (pass.getStudentId() == studentId &&
                pass.getYear() == year &&
                pass.getMonth() == month &&
                (pass.getStatus() == PassStatus.PENDING ||
                 pass.getStatus() == PassStatus.APPROVED)) {

                return true;
            }
        }

        return false;
    }

    // Request bus pass
    public BusPass requestPass(int studentId, int year,
                               int month, String route) {

        if (hasCurrentPass(studentId, year, month)) {
            return null;
        }

        BusPass pass = new BusPass(
                getNextPassId(),
                studentId,
                year,
                month,
                route,
                PassStatus.PENDING
        );

        passes.add(pass);

        return pass;
    }

    // Approve pass
    public boolean approvePass(int passId) {

        BusPass pass = findPass(passId);

        if (pass == null) {
            return false;
        }

        if (pass.getStatus() != PassStatus.PENDING) {
            return false;
        }

        pass.approve();

        return true;
    }

    // Cancel pass
    public boolean cancelPass(int passId, int studentId) {

        BusPass pass = findPass(passId);

        if (pass == null) {
            return false;
        }

        if (pass.getStudentId() != studentId) {
            return false;
        }

        if (pass.getStatus() == PassStatus.CANCELLED) {
            return false;
        }

        pass.cancel();

        return true;
    }

    // Display students
    public void showStudents() {

        if (students.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        System.out.println("\n===== STUDENTS =====");

        for (Student student : students) {

            System.out.println(
                student.getId() + " | " +
                student.getName() + " | " +
                student.getDepartment()
            );
        }
    }

    // Display all passes
    public void showPasses() {

        if (passes.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        System.out.println("\n===== PASS REQUESTS =====");

        for (BusPass pass : passes) {

            System.out.println(
                "Pass ID: " + pass.getId() +
                " | Student ID: " + pass.getStudentId() +
                " | Year: " + pass.getYear() +
                " | Month: " + months[pass.getMonth() - 1] +
                " | Route: " + pass.getRoute() +
                " | Status: " + pass.getStatus()
            );
        }
    }

    // Student pass history
    public void showStudentPasses(int studentId) {

        boolean found = false;

        System.out.println("\n===== MY PASS HISTORY =====");

        for (BusPass pass : passes) {

            if (pass.getStudentId() == studentId) {

                found = true;

                System.out.println(
                    "Pass ID: " + pass.getId() +
                    " | " + pass.getYear() +
                    " | " + months[pass.getMonth() - 1] +
                    " | " + pass.getRoute() +
                    " | " + pass.getStatus()
                );
            }
        }

        if (!found) {
            System.out.println("No records found.");
        }
    }

    // Monthly report
    public String createMonthlyReport(int year, int month) {

        int pending = 0;
        int approved = 0;
        int cancelled = 0;

        int[] approvedByRoute = new int[routes.length];

        HashSet<Integer> approvedStudents = new HashSet<>();

        for (BusPass pass : passes) {

            if (pass.getYear() == year &&
                pass.getMonth() == month) {

                if (pass.getStatus() == PassStatus.PENDING) {
                    pending++;
                }

                else if (pass.getStatus() == PassStatus.APPROVED) {

                    approved++;

                    approvedStudents.add(pass.getStudentId());

                    for (int i = 0; i < routes.length; i++) {

                        if (pass.getRoute().equals(routes[i])) {
                            approvedByRoute[i]++;
                        }
                    }
                }

                else if (pass.getStatus() == PassStatus.CANCELLED) {
                    cancelled++;
                }
            }
        }

        String report = "";

        report += "===== MONTHLY BUS PASS REPORT =====\n";
        report += "Year: " + year + "\n";
        report += "Month: " + months[month - 1] + "\n\n";

        report += "Pending: " + pending + "\n";
        report += "Approved: " + approved + "\n";
        report += "Cancelled: " + cancelled + "\n\n";

        report += "Approved by Route:\n";
        report += "North: " + approvedByRoute[0] + "\n";
        report += "South: " + approvedByRoute[1] + "\n";
        report += "East: " + approvedByRoute[2] + "\n\n";

        report += "Distinct Approved Students: "
                + approvedStudents.size() + "\n";

        return report;
    }

    public String getMonthName(int month) {

        return months[month - 1];
    }

    public String[] getRoutes() {

        return routes;
    }
}
