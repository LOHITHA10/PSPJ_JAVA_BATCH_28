package clinic;

import java.time.LocalDate;

// Module 5: implements Comparable for natural ordering (used by Collections.sort)
public class Appointment implements Comparable<Appointment> {

    private static int counter = 1000;          // static counter -> auto-generated IDs
    public static final String STATUS_BOOKED = "BOOKED";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    private final String appointmentId;
    private final String patientId;
    private final String doctorId;
    private final LocalDate date;
    private final int slotIndex;                // 0..7 -> index into Doctor.slots
    private String status;

    public Appointment(String patientId, String doctorId, LocalDate date, int slotIndex) {
        this.appointmentId = "APT" + (++counter);
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.slotIndex = slotIndex;
        this.status = STATUS_BOOKED;
    }

    public String getAppointmentId() { return appointmentId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public LocalDate getDate() { return date; }
    public int getSlotIndex() { return slotIndex; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // natural ordering: earliest date, then earliest slot
    @Override
    public int compareTo(Appointment other) {
        int dateCompare = this.date.compareTo(other.date);
        if (dateCompare != 0) return dateCompare;
        return Integer.compare(this.slotIndex, other.slotIndex);
    }

    // Module 5: value comparison via equals() (not == which compares references)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Appointment)) return false;
        return appointmentId.equals(((Appointment) obj).appointmentId);
    }

    @Override
    public int hashCode() {
        return appointmentId.hashCode();
    }

    // CSV-style record for file persistence (Module 5: File I/O)
    public String toCsv() {
        return appointmentId + "," + patientId + "," + doctorId + "," + date + "," + slotIndex + "," + status;
    }

    public static Appointment fromCsv(String line) throws InvalidAppointmentException {
        String[] parts = line.split(",");          // Module 5: split() for tokenization
        if (parts.length != 6) {
            throw new InvalidAppointmentException("Malformed record: " + line);
        }
        try {
            Appointment a = new Appointment(parts[1].trim(), parts[2].trim(),
                    LocalDate.parse(parts[3].trim()), Integer.parseInt(parts[4].trim()));
            a.status = parts[5].trim();
            return a;
        } catch (RuntimeException ex) {
            throw new InvalidAppointmentException("Could not parse record: " + line);
        }
    }

    @Override
    public String toString() {
        return String.format("%s | Patient:%s | Doctor:%s | %s slot#%d | %s",
                appointmentId, patientId, doctorId, date, slotIndex, status);
    }
}
