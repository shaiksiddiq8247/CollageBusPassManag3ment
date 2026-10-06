package collagebuspass;

public class Admin extends User {

    public Admin(int id, String name) {
        super(id, name);
    }

    @Override
    public void showProfile() {
        System.out.println("Admin ID   : " + getId());
        System.out.println("Admin Name : " + getName());
    }
}
