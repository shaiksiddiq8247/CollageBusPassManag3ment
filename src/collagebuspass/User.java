package collagebuspass;

public abstract class User {
//This gives you abstraction, encapsulation, and inheritance.
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract void showProfile();
}
