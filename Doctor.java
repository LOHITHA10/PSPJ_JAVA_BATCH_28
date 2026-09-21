package clinic;

import java.util.Arrays;

// Module 4: inheritance (Doctor "is-a" Person), method overriding, implements interface
public class Doctor extends Person implements Schedulable {

    private String specialization;
    // Module 3: 1D array representing 8 appointment slots in a day (true = free)
    private final boolean[] slots = new boolean[8];

    public Doctor(String id, String name, int age, String phone, String specialization) {
        super(id, name, age, phone);   // constructor chaining
        this.specialization = specialization;
        Arrays.fill(slots, true);
    }

    public String getSpecialization() {
        return specialization;
    }

    @Override
    public String getRole() {          // method overriding
        return "Doctor";
    }

    @Override
    public boolean isAvailable(int slotIndex) {
        return slotIndex >= 0 && slotIndex < slots.length && slots[slotIndex];
    }

    @Override
    public void blockSlot(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < slots.length) {
            slots[slotIndex] = false;
        }
    }

    // Module 3: 1D array traversal + counting technique
    public int countFreeSlots() {
        int count = 0;
        for (boolean free : slots) {   // enhanced for-each
            if (free) count++;
        }
        return count;
    }

    @Override
    public String toString() {
        return super.toString() + " | Specialization: " + specialization
                + " | Free slots: " + countFreeSlots();
    }
}
