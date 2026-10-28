public class Member {
    private int id;
    private String name;
    private String phone;

    public Member(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public Member(String name, String phone) {
        this(0, name, phone);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }

    @Override
    public String toString() {
        return String.format("[%d] %s | %s", id, name, phone);
    }
}
