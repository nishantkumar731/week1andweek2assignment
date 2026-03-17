import java.util.*;

public class problem2 {

    // productId -> stock count
    private HashMap<String, Integer> stockMap = new HashMap<>();

    // productId -> waiting list (FIFO)
    private HashMap<String, Queue<Integer>> waitingList = new HashMap<>();

    // Add product with stock
    public void addProduct(String productId, int stock) {
        stockMap.put(productId, stock);
        waitingList.put(productId, new LinkedList<>());
    }

    // Check stock availability
    public String checkStock(String productId) {
        int stock = stockMap.getOrDefault(productId, 0);
        return stock + " units available";
    }

    // Purchase item (thread-safe)
    public synchronized String purchaseItem(String productId, int userId) {

        int stock = stockMap.getOrDefault(productId, 0);

        if (stock > 0) {
            stockMap.put(productId, stock - 1);
            return "Success! Remaining stock: " + (stock - 1);
        } else {
            Queue<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            return "Out of stock. Added to waiting list. Position: " + queue.size();
        }
    }

    // Show waiting list
    public void showWaitingList(String productId) {
        Queue<Integer> queue = waitingList.get(productId);
        System.out.println("Waiting List: " + queue);
    }

    // Main method
    public static void main(String[] args) {

        problem2 system = new problem2();

        // Add product with 5 units (for demo)
        system.addProduct("IPHONE15_256GB", 5);

        // Check stock
        System.out.println(system.checkStock("IPHONE15_256GB"));

        // Simulate purchases
        for (int i = 1; i <= 7; i++) {
            System.out.println(system.purchaseItem("IPHONE15_256GB", 1000 + i));
        }

        // Check stock after purchases
        System.out.println(system.checkStock("IPHONE15_256GB"));

        // Show waiting list
        system.showWaitingList("IPHONE15_256GB");
    }
}