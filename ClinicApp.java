package clinic;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Entry point: menu-driven console application.
 * Module 1: primitives, constants, operators, typecasting, formatted printing, Scanner input.
 * Module 2: if-else-if ladder, switch-case, while/for loops, continue, nested loops.
 */
public class ClinicApp {

    // Module 1: constants (final), identifiers, literals
    private static final double CONSULTATION_FEE = 500.0;
    private static final double MEDICINE_SERVICE_TAX = 0.05;   // 5%
    private static final int MAX_SLOTS_PER_DAY = 8;

    private static final ClinicService service = new ClinicService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        seedDemoData();

        boolean running = true;                 // Module 2: while loop controlled by boolean flag
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            // Module 2: switch-case with break and default
            switch (choice) {
                case 1:
                    viewAllDoctors();
                    break;
                case 2:
                    viewAllPatients();
                    break;
                case 3:
                    bookAppointmentFlow();
                    break;
                case 4:
                    viewUpcomingAppointments();
                    break;
                case 5:
                    generatePrescriptionFlow();
                    break;
                case 6:
                    billingCalculatorDemo();
                    break;
                case 7:
                    reportsAndAnalyticsDemo();
                    break;
                case 8:
                    fileIoDemo();
                    break;
                case 0:
                    running = false;
                    System.out.println("Thank you for using Clinic Manager. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("========================================");
        System.out.println("   CLINIC APPOINTMENT & PRESCRIPTION MANAGER");
        System.out.println("========================================");
        System.out.println("1. View Doctors");
        System.out.println("2. View Patients");
        System.out.println("3. Book Appointment");
        System.out.println("4. View Upcoming Appointments");
        System.out.println("5. Generate Prescription");
        System.out.println("6. Billing Calculator (operators demo)");
        System.out.println("7. Reports & Analytics (streams demo)");
        System.out.println("8. Save / Load Appointments (file I/O demo)");
        System.out.println("0. Exit");
    }

    // ---------------- demo seed data ----------------

    private static void seedDemoData() {
        service.registerDoctor(new Doctor("D1", "Dr. Asha Rao", 41, "9000000001", "Cardiology"));
        service.registerDoctor(new Doctor("D2", "Dr. Vikram Shah", 37, "9000000002", "Dermatology"));
        service.registerDoctor(new Doctor("D3", "Dr. Neha Iyer", 33, "9000000003", "Pediatrics"));

        service.registerPatient(new Patient("P1", "Rahul Verma", 29, "9111111111", "B+"));
        service.registerPatient(new Patient("P2", "Sneha Kulkarni", 45, "9222222222", "O+",
                new String[]{"Penicillin"}));
        service.registerPatient(new Patient("P3", "Aman Gupta", 8, "9333333333", "A+"));

        try {
            service.bookAppointment("P1", "D1", LocalDate.now().plusDays(1), 2);
            service.bookAppointment("P2", "D2", LocalDate.now().plusDays(2), 0);
        } catch (InvalidAppointmentException | SlotUnavailableException e) {
            // Module 4: multi-catch - both exception types handled identically here
            System.out.println("Seed data booking issue: " + e.getMessage());
        }
    }

    // ---------------- menu actions ----------------

    private static void viewAllDoctors() {
        System.out.println("--- Doctors ---");
        for (Doctor d : service.getDoctors()) {     // enhanced for-each
            System.out.println(d);
        }
    }

    private static void viewAllPatients() {
        System.out.println("--- Patients ---");
        for (Patient p : service.getPatients()) {
            System.out.println(p);
        }
        // Module 1: formatted printing with printf
        System.out.printf("Average patient age: %.2f%n", service.averageAgeOfPatients());
    }

    private static void bookAppointmentFlow() {
        String patientId = readLine("Patient ID (e.g. P1): ");
        String doctorId = readLine("Doctor ID (e.g. D1): ");
        int slot = readInt("Slot index (0-" + (MAX_SLOTS_PER_DAY - 1) + "): ");
        int daysFromNow = readInt("Days from now: ");

        // Module 4: try-catch-finally with multiple catch blocks
        try {
            Appointment apt = service.bookAppointment(patientId, doctorId,
                    LocalDate.now().plusDays(daysFromNow), slot);
            System.out.println("Booked successfully: " + apt);
        } catch (InvalidAppointmentException e) {
            System.out.println("Invalid appointment: " + e.getMessage());
        } catch (SlotUnavailableException e) {
            System.out.println("Slot unavailable: " + e.getMessage());
        } finally {
            System.out.println("Booking attempt finished at " + LocalDate.now());
        }
    }

    private static void viewUpcomingAppointments() {
        List<Appointment> upcoming = service.upcomingBookedAppointments();
        if (upcoming.isEmpty()) {
            System.out.println("No upcoming appointments.");
            return;
        }
        for (int i = 0; i < upcoming.size(); i++) {   // classic for loop
            System.out.println((i + 1) + ". " + upcoming.get(i));
        }
    }

    private static void generatePrescriptionFlow() {
        List<Appointment> upcoming = service.upcomingBookedAppointments();
        if (upcoming.isEmpty()) {
            System.out.println("No appointment to prescribe for.");
            return;
        }
        Appointment apt = upcoming.get(0);
        Prescription rx = new Prescription(apt.getAppointmentId(), "Rest and hydration advised");
        rx.addMedicine("Paracetamol", "500mg", 2);
        rx.addMedicine(new Medicine("Vitamin C", "500mg", 1));   // overloaded method call
        System.out.println(rx.generateReport());
    }

    private static void billingCalculatorDemo() {
        // Module 1: arithmetic, relational, logical, assignment, ternary, unary, bitwise operators
        int medicineCount = readInt("Number of medicines prescribed: ");
        boolean isInsured = readLine("Is patient insured? (yes/no): ").equalsIgnoreCase("yes");

        double medicineCost = medicineCount * 40.0;              // arithmetic operator
        double subtotal = CONSULTATION_FEE + medicineCost;       // arithmetic operator
        double tax = subtotal * MEDICINE_SERVICE_TAX;            // arithmetic operator
        double discount = isInsured ? subtotal * 0.20 : 0.0;     // ternary operator

        boolean isBillLarge = subtotal > 2000;                   // relational operator
        boolean applyLoyaltyBonus = isInsured && isBillLarge;    // logical AND

        double total = subtotal + tax - discount;
        int roundedTotal = (int) Math.round(total);              // typecasting double -> int
        int flagBits = (isInsured ? 1 : 0) | (isBillLarge ? 2 : 0);  // bitwise OR to pack two flags
        int loyaltyFlag = flagBits & 3;                          // bitwise AND (mask)
        int doubledFlag = flagBits << 1;                         // bitwise left shift

        System.out.println("--- Bill Summary ---");
        System.out.printf("Consultation Fee : Rs. %.2f%n", CONSULTATION_FEE);
        System.out.printf("Medicine Cost     : Rs. %.2f%n", medicineCost);
        System.out.printf("Tax (5%%)          : Rs. %.2f%n", tax);
        System.out.printf("Discount          : Rs. %.2f%n", discount);
        System.out.printf("Total (rounded)   : Rs. %d%n", roundedTotal);
        System.out.println("Loyalty bonus applies: " + applyLoyaltyBonus);
        System.out.println("Flag bits: " + flagBits + " | masked: " + loyaltyFlag + " | shifted: " + doubledFlag);
    }

    private static void reportsAndAnalyticsDemo() {
        System.out.println("--- Reports (Streams & Collections) ---");
        Map<String, Long> perDoctor = service.appointmentCountPerDoctor();
        for (Map.Entry<String, Long> entry : perDoctor.entrySet()) {
            System.out.println("Doctor " + entry.getKey() + " -> " + entry.getValue() + " appointment(s)");
        }
        System.out.println("Patients (joined): " + service.joinedPatientNames());
        System.out.println("Distinct blood groups: " + service.countDistinctBloodGroups());
        service.findOldestPatient().ifPresent(p ->
                System.out.println("Oldest patient: " + p.getName() + " (" + p.getAge() + ")"));

        // Module 3: recursion demo - recursive sum
        double[] amounts = {40.0, 40.0, 25.0};
        System.out.printf("Recursive medicine subtotal: Rs. %.2f%n", service.recursiveSum(amounts, 0));

        // Module 3 + 5: recursion demo - recursive binary search over a sorted id array
        String[] sortedIds = {"P1", "P2", "P3"};
        java.util.Arrays.sort(sortedIds);                     // Arrays.sort (Module 5)
        int foundAt = service.recursiveBinarySearch(sortedIds, "P2", 0, sortedIds.length - 1);
        System.out.println("Recursive binary search for P2 -> index " + foundAt);

        // Module 3: 2D array demo
        int[][] matrix = service.buildWeeklyOccupancyMatrix();
        System.out.println("Weekly occupancy matrix (rows=doctors, cols=Mon..Sun):");
        for (int[] row : matrix) {
            StringBuilder sb = new StringBuilder();
            for (int cell : row) {
                sb.append(cell).append(" ");
            }
            System.out.println(sb.toString().trim());
        }
    }

    private static void fileIoDemo() {
        try {
            service.saveAppointmentsToFile();
            System.out.println("Appointments saved to appointments.csv");
        } catch (java.io.IOException e) {
            System.out.println("Failed to save: " + e.getMessage());
        }
        List<Appointment> reloaded = service.loadAppointmentsFromFile();
        System.out.println("Reloaded " + reloaded.size() + " appointment record(s) from disk:");
        for (Appointment a : reloaded) {
            System.out.println("  " + a);
        }
    }

    // ---------------- input helpers ----------------

    private static int readInt(String prompt) {
        while (true) {                              // nested loop pattern: retry until valid
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;                            // Module 2: continue statement
            }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
