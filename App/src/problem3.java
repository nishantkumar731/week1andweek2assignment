import java.util.*;

public class problem3 {

    // DNS Entry class
    static class DNSEntry {
        String ipAddress;
        long expiryTime;

        DNSEntry(String ipAddress, long ttlSeconds) {
            this.ipAddress = ipAddress;
            this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    // Cache storage
    private HashMap<String, DNSEntry> cache = new HashMap<>();

    // Stats
    private int hits = 0;
    private int misses = 0;

    private int capacity = 5; // max cache size (for demo)

    // Resolve domain
    public String resolve(String domain) {

        // Check if exists in cache
        if (cache.containsKey(domain)) {
            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                return "Cache HIT → " + entry.ipAddress;
            } else {
                cache.remove(domain); // remove expired
            }
        }

        // Cache miss
        misses++;

        // Simulate upstream DNS call
        String newIP = fetchFromDNS(domain);

        // LRU eviction (simple: remove first key)
        if (cache.size() >= capacity) {
            String firstKey = cache.keySet().iterator().next();
            cache.remove(firstKey);
        }

        // Store with TTL (5 sec demo)
        cache.put(domain, new DNSEntry(newIP, 5));

        return "Cache MISS → " + newIP;
    }

    // Simulated DNS lookup
    private String fetchFromDNS(String domain) {
        return "192.168.1." + new Random().nextInt(255);
    }

    // Cache stats
    public void getStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);

        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    // Main method
    public static void main(String[] args) throws InterruptedException {

        problem3 dns = new problem3();

        System.out.println(dns.resolve("google.com")); // MISS
        System.out.println(dns.resolve("google.com")); // HIT

        Thread.sleep(6000); // wait for TTL expiry

        System.out.println(dns.resolve("google.com")); // EXPIRED → MISS

        dns.getStats();
    }
}