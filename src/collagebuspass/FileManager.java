package collagebuspass;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;

public class FileManager {

    private static final String DATA_FOLDER = "data";

    private static final String STUDENT_FILE =
            DATA_FOLDER + "/students.txt";

    private static final String PASS_FILE =
            DATA_FOLDER + "/passes.txt";

    private static final String REPORT_FILE =
            DATA_FOLDER + "/report.txt";


    // Create data folder
    public static void createDataFolder() {

        try {

            Files.createDirectories(Paths.get(DATA_FOLDER));

        } catch (IOException e) {

            System.out.println("Unable to create data folder.");
        }
    }


    // Save students
    public static void saveStudents(ArrayList<Student> students) {

        createDataFolder();

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                         Paths.get(STUDENT_FILE),
                         StandardCharsets.UTF_8)) {

            for (Student student : students) {

                writer.write(
                    student.getId() + "|" +
                    student.getName() + "|" +
                    student.getDepartment()
                );

                writer.newLine();
            }

        } catch (IOException e) {

            System.out.println("Error saving students: "
                    + e.getMessage());
        }
    }


    // Load students
    public static ArrayList<Student> loadStudents() {

        ArrayList<Student> students = new ArrayList<>();

        createDataFolder();

        Path path = Paths.get(STUDENT_FILE);

        if (!Files.exists(path)) {
            return students;
        }

        try (BufferedReader reader =
                     Files.newBufferedReader(
                         path,
                         StandardCharsets.UTF_8)) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split("\\|", -1);

                if (data.length != 3) {
                    continue;
                }

                int id = Integer.parseInt(data[0]);

                String name = data[1];

                String department = data[2];

                Student student =
                        new Student(id, name, department);

                students.add(student);
            }

        } catch (Exception e) {

            System.out.println(
                "Error loading students: " + e.getMessage()
            );
        }

        return students;
    }


    // Save passes
    public static void savePasses(ArrayList<BusPass> passes) {

        createDataFolder();

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                         Paths.get(PASS_FILE),
                         StandardCharsets.UTF_8)) {

            for (BusPass pass : passes) {

                writer.write(
                    pass.getId() + "|" +
                    pass.getStudentId() + "|" +
                    pass.getYear() + "|" +
                    pass.getMonth() + "|" +
                    pass.getRoute() + "|" +
                    pass.getStatus()
                );

                writer.newLine();
            }

        } catch (IOException e) {

            System.out.println(
                "Error saving passes: " + e.getMessage()
            );
        }
    }


    // Load passes
    public static ArrayList<BusPass> loadPasses() {

        ArrayList<BusPass> passes = new ArrayList<>();

        createDataFolder();

        Path path = Paths.get(PASS_FILE);

        if (!Files.exists(path)) {
            return passes;
        }

        try (BufferedReader reader =
                     Files.newBufferedReader(
                         path,
                         StandardCharsets.UTF_8)) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split("\\|", -1);

                if (data.length != 6) {
                    continue;
                }

                int id = Integer.parseInt(data[0]);

                int studentId = Integer.parseInt(data[1]);

                int year = Integer.parseInt(data[2]);

                int month = Integer.parseInt(data[3]);

                String route = data[4];

                PassStatus status =
                        PassStatus.valueOf(data[5]);

                BusPass pass =
                        new BusPass(
                            id,
                            studentId,
                            year,
                            month,
                            route,
                            status
                        );

                passes.add(pass);
            }

        } catch (Exception e) {

            System.out.println(
                "Error loading passes: " + e.getMessage()
            );
        }

        return passes;
    }


    // Save report
    public static void saveReport(String report) {

        createDataFolder();

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                         Paths.get(REPORT_FILE),
                         StandardCharsets.UTF_8)) {

            writer.write(report);

            System.out.println("Report saved successfully.");

        } catch (IOException e) {

            System.out.println(
                "Error saving report: " + e.getMessage()
            );
        }
    }
}
