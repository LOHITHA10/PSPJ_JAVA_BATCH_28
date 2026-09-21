package clinic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Core service layer.
 * Module 6: Collections (ArrayList, HashMap, HashSet), generics, streams, lambdas,
 *           functional interfaces, method references, Collectors.
 * Module 5: Arrays.sort / Collections.sort / Comparator, File I/O with java.nio.file.
 * Module 3: recursion, 2D arrays.
 */
public class ClinicService {

    // Module 6: Collections framework - List, Map, Set as practical APIs
    private final List<Patient> patients = new ArrayList<>();
    private final Map<String, Doctor> doctors = new HashMap<>();        // fast lookup by id
    private final List<Appointment> appointments = new ArrayList<>();
    private final Set<String> bloodGroupsSeen = new HashSet<>();        // no duplicates

    private static final Path DATA_FILE = Paths.get("appointments.csv");

    // ---------- registration ----------

    public void registerPatient(Patient p) {
        patients.add(p);
        bloodGroupsSeen.add(p.getBloodGroup());
    }

    public void registerDoctor(Doctor d) {
        doctors.put(d.getId(), d);
    }

    public List<Patient> getPatients() { return patients; }
    public Collection<Doctor> getDoctors() { return doctors.values(); }
    public List<Appointment> getAppointments() { return appointments; }

    // ---------- booking (Module 4: throw / throws, multi-catch by caller) ----------

    public Appointment bookAppointment(String patientId, String doctorId, LocalDate date, int slotIndex)
            throws InvalidAppointmentException, SlotUnavailableException {

        if (date.isBefore(LocalDate.now())) {
            throw new InvalidAppointmentException("Cannot book an appointment in the past: " + date);
        }
        Doctor doc = doctors.get(doctorId);
        if (doc == null) {
            throw new InvalidAppointmentException("No such doctor: " + doctorId);
        }
        if (!doc.isAvailable(slotIndex)) {
            throw new SlotUnavailableException("Slot " + slotIndex + " for Dr. " + doc.getName() + " is taken");
        }
        doc.blockSlot(slotIndex);
        Appointment apt = new Appointment(patientId, doctorId, date, slotIndex);
        appointments.add(apt);
        return apt;
    }

    // ---------- Module 3: recursion ----------

    // recursive binary search over a SORTED array of appointment ids (base case + recursive case)
    public int recursiveBinarySearch(String[] sortedIds, String target, int low, int high) {
        if (low > high) {
            return -1;                                  // base case: not found
        }
        int mid = low + (high - low) / 2;
        int cmp = sortedIds[mid].compareTo(target);
        if (cmp == 0) {
            return mid;                                 // base case: found
        } else if (cmp < 0) {
            return recursiveBinarySearch(sortedIds, target, mid + 1, high);   // recursive case
        } else {
            return recursiveBinarySearch(sortedIds, target, low, mid - 1);    // recursive case
        }
    }

    // recursive sum used for simple bill calculation (medicine unit price * quantity list)
    public double recursiveSum(double[] amounts, int index) {
        if (index == amounts.length) return 0;          // base case
        return amounts[index] + recursiveSum(amounts, index + 1);   // recursive case
    }

    // ---------- Module 3: 2D array - weekly occupancy matrix (doctors x 7 days) ----------

    public int[][] buildWeeklyOccupancyMatrix() {
        List<Doctor> docList = new ArrayList<>(doctors.values());
        int[][] matrix = new int[docList.size()][7];    // rows = doctors, cols = days Mon..Sun
        for (Appointment apt : appointments) {
            int dayIndex = apt.getDate().getDayOfWeek().getValue() - 1;   // 0..6
            for (int r = 0; r < docList.size(); r++) {
                if (docList.get(r).getId().equals(apt.getDoctorId())) {
                    matrix[r][dayIndex]++;
                }
            }
        }
        return matrix;
    }

    // ---------- Module 5 & 6: sorting with Comparable / Comparator ----------

    public void sortAppointmentsNaturally() {
        Collections.sort(appointments);                 // uses Appointment.compareTo (Comparable)
    }

    public List<Appointment> appointmentsSortedByPatient() {
        List<Appointment> copy = new ArrayList<>(appointments);
        copy.sort(Comparator.comparing(Appointment::getPatientId)   // Comparator + method reference
                .thenComparing(Appointment::getDate));
        return copy;
    }

    // ---------- Module 6: streams & functional interfaces ----------

    public List<Appointment> upcomingBookedAppointments() {
        return appointments.stream()
                .filter(a -> a.getStatus().equals(Appointment.STATUS_BOOKED))   // Predicate lambda
                .filter(a -> !a.getDate().isBefore(LocalDate.now()))
                .sorted()
                .collect(Collectors.toList());
    }

    public Map<String, Long> appointmentCountPerDoctor() {
        return appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getDoctorId, Collectors.counting()));
    }

    public String joinedPatientNames() {
        return patients.stream()
                .map(Person::getName)              // Function via method reference
                .sorted()
                .collect(Collectors.joining(", "));
    }

    public long countDistinctBloodGroups() {
        return patients.stream()
                .map(Patient::getBloodGroup)
                .distinct()
                .count();
    }

    public Optional<Patient> findOldestPatient() {
        return patients.stream()
                .max(Comparator.comparingInt(Patient::getAge));
    }

    // ---------- Module 5: File I/O with java.nio.file, try-with-resources ----------

    public void saveAppointmentsToFile() throws IOException {
        List<String> lines = appointments.stream()
                .map(Appointment::toCsv)
                .collect(Collectors.toList());
        Files.write(DATA_FILE, lines, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public void appendAppointmentToFile(Appointment apt) throws IOException {
        // append mode - demonstrates StandardOpenOption.APPEND with try-with-resources
        // (BufferedWriter is AutoCloseable, so it is closed automatically, even on error)
        try (java.io.BufferedWriter writer = Files.newBufferedWriter(DATA_FILE, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(apt.toCsv());
            writer.newLine();
        }
    }

    public List<Appointment> loadAppointmentsFromFile() {
        List<Appointment> loaded = new ArrayList<>();
        if (!Files.exists(DATA_FILE)) {                  // handling a missing file gracefully
            System.out.println("No saved appointment file found yet - starting fresh.");
            return loaded;
        }
        // Module 6: reading file lines as a stream; Module 5: try-with-resources for the stream
        try (Stream<String> lineStream = Files.lines(DATA_FILE, StandardCharsets.UTF_8)) {
            List<String> lines = lineStream.filter(line -> !line.isBlank())   // validation / trimming
                    .collect(Collectors.toList());
            for (String line : lines) {
                try {
                    loaded.add(Appointment.fromCsv(line));
                } catch (InvalidAppointmentException e) {
                    System.out.println("Skipping malformed record -> " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read appointments file: " + e.getMessage());
        }
        return loaded;
    }

    // ---------- Module 3: 1D array stats (counting / summation / average) ----------

    public double averageAgeOfPatients() {
        if (patients.isEmpty()) return 0;
        int[] ages = new int[patients.size()];
        for (int i = 0; i < patients.size(); i++) {
            ages[i] = patients.get(i).getAge();
        }
        int total = 0;
        for (int age : ages) total += age;    // summation
        return (double) total / ages.length;  // typecasting int -> double
    }
}
