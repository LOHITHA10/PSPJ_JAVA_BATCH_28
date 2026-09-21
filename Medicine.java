package clinic;

public class Medicine {
    private String name;
    private String dosage;      // e.g. "500mg"
    private int timesPerDay;

    public Medicine(String name, String dosage, int timesPerDay) {
        this.name = name;
        this.dosage = dosage;
        this.timesPerDay = timesPerDay;
    }

    public String getName() { return name; }
    public String getDosage() { return dosage; }
    public int getTimesPerDay() { return timesPerDay; }

    @Override
    public String toString() {
        return name + " (" + dosage + ") x" + timesPerDay + "/day";
    }
}
