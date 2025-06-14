package com.nivuk;

import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DeltreeTest {
    private MockFileAPI mockApi;
    private Deltree deltree;

    @BeforeEach
    void setUp() {
        mockApi = new MockFileAPI();
        // Simulate structure:
        // /
        // ├── /a
        // │   ├── /a/b
        // │   └── /a/file1
        // └── /file2
        mockApi.addFile("/a", true);
        mockApi.addFile("/a/b", true);
        mockApi.addFile("/a/file1", false);
        mockApi.addFile("/file2", false);
        deltree = new Deltree(mockApi);
    }

    @Test
    void testListAndIsDirectory() {
        assertTrue(mockApi.isDirectory("/a"));
        assertFalse(mockApi.isDirectory("/a/file1"));
        List<String> rootList = mockApi.list("/");
        assertTrue(rootList.contains("/a"));
        assertTrue(rootList.contains("/file2"));
    }

    @Test
    void testDeleteFile() {
        assertTrue(mockApi.delete("/file2"));
        assertFalse(mockApi.isDirectory("/file2"));
        assertFalse(mockApi.list("/").contains("/file2"));
    }

    @Test
    void testDeleteNonEmptyDirectoryFails() {
        assertFalse(mockApi.delete("/a")); // /a is not empty
    }

    @Test
    void testRecursiveDelete() {
        assertTrue(deltree.deleteRecursive("/a"));
        assertFalse(mockApi.isDirectory("/a"));
        assertFalse(mockApi.list("/").contains("/a"));
    }
}

