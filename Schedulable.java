package clinic;

// Module 4: interface - "is-a" capability, programming to interfaces
public interface Schedulable {
    boolean isAvailable(int slotIndex);
    void blockSlot(int slotIndex);
}
