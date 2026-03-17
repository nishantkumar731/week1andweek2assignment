import java.util.*;

public class problem1 {

    // username -> userId
    private HashMap<String, Integer> usernameMap = new HashMap<>();

    // username -> attempt count
    private HashMap<String, Integer> attemptFrequency = new HashMap<>();

    // Check username availability
    public boolean checkAvailability(String username) {
        attemptFrequency.put(username, attemptFrequency.getOrDefault(username, 0) + 1);
        return !usernameMap.containsKey(username);
    }

    // Register username
    public boolean register(String username, int userId) {
        if (usernameMap.containsKey(username)) {
            return false;
        }
        usernameMap.put(username, userId);
        return true;
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            String newName = username + i;
            if (!usernameMap.containsKey(newName)) {
                suggestions.add(newName);
            }
        }

        if (username.contains("_")) {
            String alt = username.replace("_", ".");
            if (!usernameMap.containsKey(alt)) {
                suggestions.add(alt);
            }
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        if (attemptFrequency.isEmpty()) {
            return "No attempts yet";
        }

        String most = null;
        int max = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                most = entry.getKey();
            }
        }

        return most + " (" + max + " attempts)";
    }

    // Main method
    public static void main(String[] args) {

        problem1 system = new problem1();

        // Register users
        system.register("john_doe", 101);
        system.register("alex99", 102);

        // Availability check
        System.out.println(system.checkAvailability("john_doe"));   // false
        System.out.println(system.checkAvailability("jane_smith")); // true

        // Suggestions
        System.out.println(system.suggestAlternatives("john_doe"));

        // Simulate attempts
        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("admin");

        // Most attempted
        System.out.println(system.getMostAttempted());
    }
}