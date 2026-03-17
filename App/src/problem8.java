import java.util.*;

public class problem8 {

    // Parking spot class
    static class Spot {
        String licensePlate;
        long entryTime;
        boolean occupied;

        Spot() {
            this.licensePlate = null;
            this.entryTime = 0;
            this.occupied = false;
        }
    }

    private Spot[] spots;
    private int totalSpots;
    private int occupiedCount = 0;
    private int totalProbes = 0;

    public problem8(int totalSpots) {
        this.totalSpots = totalSpots;
        spots = new Spot[totalSpots];
        for (int i = 0; i < totalSpots; i++) {
            spots[i] = new Spot();
        }
    }

    // Hash function for license plate
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % totalSpots;
    }

    // Park a vehicle
    public void parkVehicle(String licensePlate) {

        int spotIndex = hash(licensePlate);
        int probes = 0;

        while (spots[spotIndex].occupied) {
            spotIndex = (spotIndex + 1) % totalSpots;
            probes++;
        }

        spots[spotIndex].licensePlate = licensePlate;
        spots[spotIndex].entryTime = System.currentTimeMillis();
        spots[spotIndex].occupied = true;

        occupiedCount++;
        totalProbes += probes;

        System.out.println("Vehicle " + licensePlate + " parked at spot #" + spotIndex +
                " (" + probes + " probes)");
    }

    // Exit a vehicle
    public void exitVehicle(String licensePlate) {

        for (int i = 0; i < totalSpots; i++) {
            if (spots[i].occupied && licensePlate.equals(spots[i].licensePlate)) {

                long duration = (System.currentTimeMillis() - spots[i].entryTime) / 1000; // in sec
                double fee = duration * 0.01; // simple rate: $0.01/sec

                spots[i].occupied = false;
                spots[i].licensePlate = null;
                spots[i].entryTime = 0;

                occupiedCount--;

                System.out.println("Vehicle " + licensePlate + " exited from spot #" + i +
                        ", Duration: " + duration + " sec, Fee: $" + String.format("%.2f", fee));
                return;
            }
        }

        System.out.println("Vehicle " + licensePlate + " not found!");
    }

    // Display parking statistics
    public void getStatistics() {
        double occupancy = (occupiedCount * 100.0) / totalSpots;
        double avgProbes = totalProbes * 1.0 / (occupiedCount == 0 ? 1 : occupiedCount);

        System.out.println("Occupancy: " + String.format("%.2f", occupancy) + "%");
        System.out.println("Avg Probes: " + String.format("%.2f", avgProbes));
    }

    // Main method
    public static void main(String[] args) throws InterruptedException {

        problem8 lot = new problem8(5); // small lot for demo

        // Park vehicles
        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        Thread.sleep(1000); // simulate time

        // Exit vehicle
        lot.exitVehicle("ABC-1234");

        // Park another vehicle
        lot.parkVehicle("LMN-4567");

        // Show statistics
        lot.getStatistics();
    }
}