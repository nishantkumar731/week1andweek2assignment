import java.util.*;

public class problem6 {

    // Token Bucket class
    static class TokenBucket {
        int tokens;
        int maxTokens;
        long lastRefillTime;

        public TokenBucket(int maxTokens) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.lastRefillTime = System.currentTimeMillis();
        }
    }

    // clientId -> TokenBucket
    private HashMap<String, TokenBucket> clients = new HashMap<>();

    // Rate limit: 1000 requests per hour
    private final int MAX_TOKENS = 1000;
    private final long REFILL_INTERVAL = 3600000; // 1 hour in ms

    // Check rate limit
    public synchronized String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(MAX_TOKENS));

        TokenBucket bucket = clients.get(clientId);

        refillTokens(bucket);

        if (bucket.tokens > 0) {
            bucket.tokens--;
            return "Allowed (" + bucket.tokens + " requests remaining)";
        } else {
            long retryAfter = (REFILL_INTERVAL - (System.currentTimeMillis() - bucket.lastRefillTime)) / 1000;
            return "Denied (0 remaining, retry after " + retryAfter + " seconds)";
        }
    }

    // Refill tokens after interval
    private void refillTokens(TokenBucket bucket) {

        long now = System.currentTimeMillis();

        if (now - bucket.lastRefillTime >= REFILL_INTERVAL) {
            bucket.tokens = bucket.maxTokens;
            bucket.lastRefillTime = now;
        }
    }

    // Get status
    public void getStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            System.out.println("No data for client");
            return;
        }

        int used = bucket.maxTokens - bucket.tokens;

        System.out.println("Used: " + used);
        System.out.println("Remaining: " + bucket.tokens);
    }

    // Main method
    public static void main(String[] args) {

        problem6 system = new problem6();

        String client = "abc123";

        // Simulate requests
        for (int i = 1; i <= 5; i++) {
            System.out.println(system.checkRateLimit(client));
        }

        // Show status
        system.getStatus(client);
    }
}