package clinic;

public class Patient extends Person {

    private String bloodGroup;
    private String[] allergies;   // 1D array of known allergies

    public Patient(String id, String name, int age, String phone, String bloodGroup, String[] allergies) {
        super(id, name, age, phone);
        this.bloodGroup = bloodGroup;
        this.allergies = allergies;
    }

    // constructor overloading (Module 4) - patient with no known allergies
    public Patient(String id, String name, int age, String phone, String bloodGroup) {
        this(id, name, age, phone, bloodGroup, new String[0]);
    }

    public String getBloodGroup() { return bloodGroup; }
    public String[] getAllergies() { return allergies; }

    @Override
    public String getRole() {
        return "Patient";
    }

    @Override
    public String toString() {
        return super.toString() + " | Blood Group: " + bloodGroup
                + " | Allergies: " + (allergies.length == 0 ? "None" : String.join(", ", allergies));
    }
}
