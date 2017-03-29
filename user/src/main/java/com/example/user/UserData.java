package com.example.user;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all the data we store for a user, including data used internally in this servlet.
 *
 * This uses an array of 256 short ints (4096 bits) to record visits to product IDs based on their hashes.
 * We use the last 3 hex digits of the product ID's hashcode to map to the 4096 bits in this array; bits 4-11 specify
 * the bucket, and bits 0-3 map to individual bits in the values in the bucket.
 *
 * Note that this will have many false positives, but since we're picking random products we don't care.
 * In a complete system, this would need to be regularly purged.  (Simplest would be to have 2 such tables,
 * record entries in both, test against the older one and swap to the newer one every 1000 entries, starting
 * a blank new one.  That would ensure no duplicates in the past 1000 suggestions, which is much better than
 * most people's memories.)
 */
public class UserData {

    // The user identification.
    private final User user;

    // Used to store a list of recently-viewed products.
    // Note that this is both more compact and more efficient than using a structure like a Deque.
    private static final int RECENT_COUNT = 5;
    private Recent[] recent = new Recent[RECENT_COUNT];
    private int next = RECENT_COUNT - 1;

    // Used to record visited pages to prevent showing visited products as random.
    private short[] visited = new short[256];

    public UserData(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    /**
     * Add an entry to the user's recent products list.  This is a circular queue of length 5.
     * @param r The product to add.
     */
    public void addRecent(Recent r) {
        recent[next] = r;
        // Go to the next lower position (circularly).
        next = (next - 1 + RECENT_COUNT) % RECENT_COUNT;

        // Record a visit.
        markVisit(r.getId());
    }

    /**
     * Get the list of this user's recently viewed products (newest first).
     * @return The List of Recent entries.
     */
    public List<Recent> getRecent() {
        List<Recent> recentList = new ArrayList<>(RECENT_COUNT);
        for (int i = 1; i <= RECENT_COUNT; i++) {
            Recent r = recent[(next + i) % RECENT_COUNT];
            if (r != null) {
                recentList.add(r);
            }
        }
        return recentList;
    }

    /**
     * Mark a visit (user selection or random display) to a particular product so it isn't shown again.
     * @param id The ID to mark as visited.
     */
    public void markVisit(String id) {
        int hashcode = id.hashCode();
        int bucket = (hashcode & 0x0fff) >> 8;
        int index = hashcode & 0x0f;
        visited[bucket] |= 1 << index;
    }

    /**
     * Test whether a given product ID has potentially been viewed already by this user.
     *
     * @param id The product ID to test
     * @return True if the product ID has potentially been shown to the user already.
     */
    public boolean isVisited(String id) {
        int hashcode = id.hashCode();
        int bucket = (hashcode & 0x0fff) >> 8;
        int index = hashcode & 0x0f;
        return (visited[bucket] & 1 << index) != 0;
    }
}
