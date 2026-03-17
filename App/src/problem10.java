import java.util.*;

public class problem10 {

    // Video data class
    static class VideoData {
        String videoId;
        String content;

        VideoData(String videoId, String content) {
            this.videoId = videoId;
            this.content = content;
        }
    }

    // L1 cache: LinkedHashMap for LRU
    private LinkedHashMap<String, VideoData> l1Cache;
    private int l1Capacity = 3; // demo small size

    // L2 cache: HashMap (simulated SSD)
    private HashMap<String, VideoData> l2Cache = new HashMap<>();

    // L3 cache: database (simulated)
    private HashMap<String, VideoData> l3DB = new HashMap<>();

    // Track hits
    private int l1Hits = 0;
    private int l2Hits = 0;
    private int l3Hits = 0;

    public problem10() {
        l1Cache = new LinkedHashMap<String, VideoData>(l1Capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > l1Capacity;
            }
        };
    }

    // Add video to DB (L3)
    public void addVideo(String videoId, String content) {
        VideoData video = new VideoData(videoId, content);
        l3DB.put(videoId, video);
    }

    // Get video (promote through caches)
    public VideoData getVideo(String videoId) {
        long start = System.currentTimeMillis();

        // Check L1
        if (l1Cache.containsKey(videoId)) {
            l1Hits++;
            System.out.println("L1 Cache HIT (" + (System.currentTimeMillis() - start) + "ms)");
            return l1Cache.get(videoId);
        }

        // Check L2
        if (l2Cache.containsKey(videoId)) {
            l2Hits++;
            System.out.println("L2 Cache HIT (" + (System.currentTimeMillis() - start) + "ms)");
            VideoData video = l2Cache.get(videoId);
            l1Cache.put(videoId, video); // promote to L1
            return video;
        }

        // Fetch from DB (L3)
        l3Hits++;
        try { Thread.sleep(50); } catch (InterruptedException e) {} // simulate delay
        System.out.println("L3 Database HIT (" + (System.currentTimeMillis() - start) + "ms)");

        VideoData video = l3DB.get(videoId);
        if (video != null) {
            l2Cache.put(videoId, video); // add to L2
            l1Cache.put(videoId, video); // promote to L1
        }

        return video;
    }

    // Show stats
    public void getStatistics() {
        int total = l1Hits + l2Hits + l3Hits;

        System.out.println("L1 Hits: " + l1Hits + " (" + String.format("%.2f", l1Hits*100.0/total) + "%)");
        System.out.println("L2 Hits: " + l2Hits + " (" + String.format("%.2f", l2Hits*100.0/total) + "%)");
        System.out.println("L3 Hits: " + l3Hits + " (" + String.format("%.2f", l3Hits*100.0/total) + "%)");
    }

    // Main method
    public static void main(String[] args) {

        problem10 cache = new problem10();

        // Add videos to database
        cache.addVideo("video_1", "Video Content 1");
        cache.addVideo("video_2", "Video Content 2");
        cache.addVideo("video_3", "Video Content 3");
        cache.addVideo("video_4", "Video Content 4");

        // Access videos
        cache.getVideo("video_1");
        cache.getVideo("video_2");
        cache.getVideo("video_1"); // L1 hit
        cache.getVideo("video_3");
        cache.getVideo("video_4"); // triggers L1 eviction
        cache.getVideo("video_1"); // L2 hit if evicted from L1

        // Show stats
        cache.getStatistics();
    }
}