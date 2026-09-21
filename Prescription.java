package clinic;

import java.util.ArrayList;
import java.util.List;

public class Prescription {
    private String appointmentId;
    private String doctorNotes;
    private List<Medicine> medicines = new ArrayList<>();   // Module 6: ArrayList / generics

    public Prescription(String appointmentId, String doctorNotes) {
        this.appointmentId = appointmentId;
        this.doctorNotes = doctorNotes;
    }

    public void addMedicine(Medicine m) {
        medicines.add(m);
    }

    // method overloading (Module 4/3): add by raw values too
    public void addMedicine(String name, String dosage, int timesPerDay) {
        addMedicine(new Medicine(name, dosage, timesPerDay));
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }

    // Module 5: building text with StringBuilder (mutable, unlike immutable String)
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Prescription for Appointment: ").append(appointmentId).append("\n");
        sb.append("Doctor's Notes: ").append(doctorNotes).append("\n");
        sb.append("Medicines:\n");
        for (Medicine m : medicines) {
            sb.append("  - ").append(m.toString()).append("\n");
        }
        return sb.toString();
    }
}
