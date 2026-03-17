import java.util.*;

public class problem9 {

    // Transaction class
    static class Transaction {
        int id;
        int amount;
        String merchant;
        long timestamp; // epoch millis
        String account;

        Transaction(int id, int amount, String merchant, long timestamp, String account) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.timestamp = timestamp;
            this.account = account;
        }
    }

    private List<Transaction> transactions = new ArrayList<>();

    // Add transaction
    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Classic Two-Sum
    public List<int[]> findTwoSum(int target) {
        Map<Integer, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : transactions) {
            int complement = target - t.amount;
            if (map.containsKey(complement)) {
                result.add(new int[]{map.get(complement).id, t.id});
            }
            map.put(t.amount, t);
        }

        return result;
    }

    // Detect duplicates: same amount + merchant, different accounts
    public List<String> detectDuplicates() {
        Map<String, Set<String>> map = new HashMap<>();
        List<String> duplicates = new ArrayList<>();

        for (Transaction t : transactions) {
            String key = t.amount + "_" + t.merchant;
            map.putIfAbsent(key, new HashSet<>());
            Set<String> accounts = map.get(key);

            if (accounts.contains(t.account)) continue;

            if (!accounts.isEmpty()) {
                duplicates.add("Amount: " + t.amount + ", Merchant: " + t.merchant +
                        ", Accounts: " + accounts + " + " + t.account);
            }

            accounts.add(t.account);
        }

        return duplicates;
    }

    // Main method for demo
    public static void main(String[] args) {

        problem9 system = new problem9();

        // Add sample transactions
        system.addTransaction(new Transaction(1, 500, "StoreA", System.currentTimeMillis(), "acc1"));
        system.addTransaction(new Transaction(2, 300, "StoreB", System.currentTimeMillis(), "acc2"));
        system.addTransaction(new Transaction(3, 200, "StoreC", System.currentTimeMillis(), "acc3"));
        system.addTransaction(new Transaction(4, 500, "StoreA", System.currentTimeMillis(), "acc2")); // duplicate

        // Two-sum
        List<int[]> pairs = system.findTwoSum(500);
        System.out.println("Two-Sum pairs for 500:");
        for (int[] pair : pairs) {
            System.out.println(Arrays.toString(pair));
        }

        // Detect duplicates
        List<String> duplicates = system.detectDuplicates();
        System.out.println("\nDuplicates:");
        for (String dup : duplicates) {
            System.out.println(dup);
        }
    }
}