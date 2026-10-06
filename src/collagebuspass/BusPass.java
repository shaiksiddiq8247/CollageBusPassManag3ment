
package collagebuspass;

public class BusPass {

    private int id;
    private int studentId;
    private int year;
    private int month;
    private String route;
    private PassStatus status;

    public BusPass(int id, int studentId, int year, int month,
                   String route, PassStatus status) {

        this.id = id;
        this.studentId = studentId;
        this.year = year;
        this.month = month;
        this.route = route;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public String getRoute() {
        return route;
    }

    public PassStatus getStatus() {
        return status;
    }

    public void approve() {

        if (status == PassStatus.PENDING) {
            status = PassStatus.APPROVED;
        }
    }

    public void cancel() {

        if (status == PassStatus.PENDING ||
            status == PassStatus.APPROVED) {

            status = PassStatus.CANCELLED;
        }
    }
}
