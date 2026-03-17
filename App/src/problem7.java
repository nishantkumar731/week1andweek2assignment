import java.util.*;

public class problem7 {

    // Trie Node
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEnd;
        String query;
        int frequency;

        TrieNode() {
            isEnd = false;
            query = "";
            frequency = 0;
        }
    }

    private TrieNode root = new TrieNode();

    // Insert query with frequency
    public void insert(String query, int freq) {
        TrieNode node = root;
        for (char ch : query.toCharArray()) {
            node.children.putIfAbsent(ch, new TrieNode());
            node = node.children.get(ch);
        }
        node.isEnd = true;
        node.query = query;
        node.frequency += freq; // update frequency
    }

    // Update frequency for a query
    public void updateFrequency(String query) {
        insert(query, 1);
    }

    // Get top K suggestions for prefix
    public List<String> getSuggestions(String prefix) {
        TrieNode node = root;
        for (char ch : prefix.toCharArray()) {
            if (!node.children.containsKey(ch)) {
                return new ArrayList<>();
            }
            node = node.children.get(ch);
        }

        // DFS to collect all queries under prefix
        PriorityQueue<TrieNode> pq = new PriorityQueue<>(
                (a, b) -> b.frequency - a.frequency
        );

        collect(node, pq);

        List<String> result = new ArrayList<>();
        int count = 0;
        while (!pq.isEmpty() && count < 10) {
            result.add(pq.poll().query);
            count++;
        }

        return result;
    }

    // DFS to collect TrieNodes with queries
    private void collect(TrieNode node, PriorityQueue<TrieNode> pq) {
        if (node.isEnd) {
            pq.offer(node);
        }
        for (TrieNode child : node.children.values()) {
            collect(child, pq);
        }
    }

    // Main method
    public static void main(String[] args) {

        problem7 system = new problem7();

        // Insert sample queries
        system.insert("java tutorial", 1234567);
        system.insert("javascript", 987654);
        system.insert("java download", 456789);
        system.insert("java 21 features", 200000);
        system.insert("javatpoint", 100000);

        // Get suggestions for prefix "jav"
        System.out.println("Suggestions for 'jav': " + system.getSuggestions("jav"));

        // Update frequency
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");

        System.out.println("Updated Suggestions for 'jav': " + system.getSuggestions("jav"));
    }
}