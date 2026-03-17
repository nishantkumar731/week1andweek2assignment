import java.util.*;

public class problem4 {

    // n-gram size
    private static final int N = 3;

    // ngram -> set of document names
    private HashMap<String, Set<String>> index = new HashMap<>();

    // Store documents
    private HashMap<String, List<String>> documents = new HashMap<>();

    // Add document to system
    public void addDocument(String docName, String content) {

        List<String> ngrams = generateNGrams(content);
        documents.put(docName, ngrams);

        for (String gram : ngrams) {
            index.putIfAbsent(gram, new HashSet<>());
            index.get(gram).add(docName);
        }
    }

    // Generate n-grams
    private List<String> generateNGrams(String text) {
        List<String> result = new ArrayList<>();

        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            result.add(gram.toString().trim());
        }

        return result;
    }

    // Analyze document for plagiarism
    public void analyzeDocument(String docName) {

        List<String> target = documents.get(docName);

        if (target == null) {
            System.out.println("Document not found!");
            return;
        }

        HashMap<String, Integer> matchCount = new HashMap<>();

        // Count matches
        for (String gram : target) {
            Set<String> docs = index.get(gram);

            if (docs != null) {
                for (String otherDoc : docs) {
                    if (!otherDoc.equals(docName)) {
                        matchCount.put(otherDoc,
                                matchCount.getOrDefault(otherDoc, 0) + 1);
                    }
                }
            }
        }

        // Display results
        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {

            String otherDoc = entry.getKey();
            int matches = entry.getValue();

            double similarity = (matches * 100.0) / target.size();

            System.out.println("Compared with: " + otherDoc);
            System.out.println("Matching n-grams: " + matches);
            System.out.println("Similarity: " + String.format("%.2f", similarity) + "%");

            if (similarity > 50) {
                System.out.println("⚠️ PLAGIARISM DETECTED");
            } else if (similarity > 15) {
                System.out.println("⚠️ Suspicious");
            }

            System.out.println("----------------------");
        }
    }

    // Main method
    public static void main(String[] args) {

        problem4 system = new problem4();

        // Add documents
        system.addDocument("essay1",
                "data structures and algorithms are important in computer science");

        system.addDocument("essay2",
                "data structures and algorithms are very important subjects");

        system.addDocument("essay3",
                "machine learning and artificial intelligence are trending topics");

        // Analyze
        system.analyzeDocument("essay1");
    }
}