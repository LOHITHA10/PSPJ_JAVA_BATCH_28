# Clinic Appointment & Prescription Manager

A menu-driven, console-based Java application for a small clinic's front desk:
patient & doctor registration, appointment booking, prescriptions, billing,
analytics (via Streams) and CSV-based file persistence.

This project was built to exercise a full introductory Java syllabus end to
end in one working codebase — see the mapping table below.

## How to compile & run

```bash
cd src
javac clinic/*.java
java clinic.ClinicApp
```

Requires JDK 17+ (uses `java.time`, `java.nio.file`, streams and lambdas).

## Project structure

```
src/clinic/
├── Person.java                     # abstract base class (encapsulation, static members)
├── Doctor.java                     # extends Person, implements Schedulable
├── Patient.java                    # extends Person, constructor overloading
├── Schedulable.java                # interface
├── Appointment.java                # Comparable, CSV (de)serialisation
├── Medicine.java / Prescription.java
├── InvalidAppointmentException.java   # checked exception
├── SlotUnavailableException.java      # checked exception
├── ClinicService.java              # collections, streams, recursion, file I/O
└── ClinicApp.java                  # main() — console menu (Scanner, switch, loops)
```

## Syllabus → code mapping

| Module | Topics | Where |
|---|---|---|
| 1 — Java Foundations | operators, typecasting, `Scanner`, `printf` | `ClinicApp.billingCalculatorDemo()` |
| 2 — Control Flow | if-else, switch, while/for, continue | `ClinicApp.main()`, `readInt()` |
| 3 — Methods, Recursion & Arrays | overloading, recursion, 1D/2D arrays | `ClinicService` (binary search, occupancy matrix) |
| 4 — OOP & Exceptions | inheritance, interfaces, encapsulation, try-catch | `Person`/`Doctor`/`Patient`, custom exceptions |
| 5 — Strings & File I/O | `StringBuilder`, `Comparable`, `java.nio.file` | `Prescription`, `Appointment`, `ClinicService` |
| 6 — Collections & Streams | `ArrayList`/`HashMap`, lambdas, Stream pipelines | `ClinicService` reports & analytics |

## Presentation

`Clinic_Manager_Presentation.pptx` (in the parent folder) covers Abstract,
Advantages, Implementation Details (with code for every module above),
Output Screenshots, and Conclusion & Future Work.

## Future work

Graphical interface (JavaFX/Swing or a web UI), database persistence (JDBC),
authentication & roles, automated reminders, full invoicing, and unit tests.
