package com.example.user;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test operations with recent products.
 */
public class UserDataTest {

    @Test
    public void testAddRecent() {
        Recent r1 = new Recent("10345", "Test1", "test1.jpg");
        Recent r2 = new Recent("153242", "Test2", "test2.jpg");
        User u = new User("testUser");
        UserData data = new UserData(u);

        data.addRecent(r1);
        data.addRecent(r2);

        List<Recent> recentList = data.getRecent();
        // Note:  Results come out newest first.
        assertEquals(2, recentList.size());
        assertSame(r2, recentList.get(0));
        assertSame(r1, recentList.get(1));
    }

    @Test
    public void testVisits() {
        Recent r1 = new Recent("10345", "Test1", "test1.jpg");
        Recent r2 = new Recent("153242", "Test2", "test2.jpg");
        User u = new User("testUser");
        UserData data = new UserData(u);

        data.addRecent(r1);
        data.addRecent(r2);
        data.markVisit("45037");
        data.markVisit("77114");

        assertTrue(data.isVisited("45037"));
        assertTrue(data.isVisited("153242"));
        // In general a random number may have a hash collision, but this one works.
        assertFalse(data.isVisited("95732"));
    }
}