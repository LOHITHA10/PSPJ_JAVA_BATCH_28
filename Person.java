package clinic;

// Module 4: abstract class, encapsulation (private fields + accessors/mutators),
// constructors (default + parameterized), 'this' keyword, toString()
public abstract class Person {

    // encapsulated fields
    private String id;
    private String name;
    private int age;
    private String phone;

    // static member shared across all Person objects (Module 4: instance vs static)
    private static int totalPersons = 0;

    // parameterized constructor
    public Person(String id, String name, int age, String phone) {
        this.id = id;          // 'this' disambiguates field vs parameter
        this.name = name;
        this.age = age;
        this.phone = phone;
        totalPersons++;
    }

    // accessors / mutators (encapsulation)
    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getPhone() { return phone; }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static int getTotalPersons() {
        return totalPersons;
    }

    // abstract method -> forces subclasses to provide role-specific behaviour
    public abstract String getRole();

    // toString() override (Module 4)
    @Override
    public String toString() {
        return String.format("[%s] %-15s | Age: %-3d | Phone: %-12s | Role: %s",
                id, name, age, phone, getRole());
    }
}
