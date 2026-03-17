import java.util.*;

public class problem5 {

    // page -> total visits
    private HashMap<String, Integer> pageViews = new HashMap<>();

    // page -> unique users
    private HashMap<String, Set<String>> uniqueUsers = new HashMap<>();

    // source -> count
    private HashMap<String, Integer> sourceCount = new HashMap<>();

    // Process incoming event
    public void processEvent(String url, String userId, String source) {

        // Count page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Track unique users
        uniqueUsers.putIfAbsent(url, new HashSet<>());
        uniqueUsers.get(url).add(userId);

        // Track traffic source
        sourceCount.put(source, sourceCount.getOrDefault(source, 0) + 1);
    }

    // Get Top 10 pages
    public List<Map.Entry<String, Integer>> getTopPages() {

        List<Map.Entry<String, Integer>> list = new ArrayList<>(pageViews.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        return list.subList(0, Math.min(10, list.size()));
    }

    // Display dashboard
    public void getDashboard() {

        System.out.println("=== Top Pages ===");

        List<Map.Entry<String, Integer>> topPages = getTopPages();

        for (Map.Entry<String, Integer> entry : topPages) {

            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueUsers.get(url).size();

            System.out.println(url + " - " + views + " views (" + unique + " unique)");
        }

        System.out.println("\n=== Traffic Sources ===");

        int total = sourceCount.values().stream().mapToInt(i -> i).sum();

        for (Map.Entry<String, Integer> entry : sourceCount.entrySet()) {

            double percent = (entry.getValue() * 100.0) / total;

            System.out.println(entry.getKey() + ": " + String.format("%.2f", percent) + "%");
        }
    }

    // Main method
    public static void main(String[] args) {

        problem5 system = new problem5();

        // Simulate events
        system.processEvent("/article/news", "user1", "google");
        system.processEvent("/article/news", "user2", "facebook");
        system.processEvent("/article/news", "user1", "google");

        system.processEvent("/sports/match", "user3", "direct");
        system.processEvent("/sports/match", "user4", "google");

        system.processEvent("/tech/ai", "user5", "google");

        // Show dashboard
        system.getDashboard();
    }
}