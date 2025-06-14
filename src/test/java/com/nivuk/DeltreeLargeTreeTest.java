package com.nivuk;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeltreeLargeTreeTest {
    private static final int filesAndDirs = 10;
    private static final int LEVELS = 2;

    @Test
    void testLargeTreeMemoryEfficiency() {
        MockFileAPI.Builder builder = initTree(filesAndDirs, LEVELS);
        MockFileAPI largeApi = new MockFileAPI(builder);
        Deltree deltree = new Deltree(largeApi);
        // Check root and /big
        assertTrue(largeApi.isDirectory("/big"));
        assertEquals(filesAndDirs * 2, largeApi.list("/big").size());
        // Check a subdirectory
        assertTrue(largeApi.isDirectory("/big/dir_0"));
        assertTrue(largeApi.isDirectory("/big/dir_0/dir_0_dir_0"));
        assertEquals(filesAndDirs * 2, largeApi.list("/big/dir_0").size());
        // Check a file
        assertFalse(largeApi.isDirectory("/big/file_0"));
        // Now test recursive delete
        assertTrue(deltree.deleteRecursive("/big"));
        assertFalse(largeApi.isDirectory("/big"));
    }

    private static MockFileAPI.Builder initTree(int filesAndDirs, int levels) {
        MockFileAPI.Builder builder = MockFileAPI.builder().makeDir("big").changeDir("big");
        buildLevel(builder, filesAndDirs, levels);
        return builder;
    }

    private static void buildLevel(MockFileAPI.Builder builder, int filesAndDirs, int levels) {
        buildLevel(builder, filesAndDirs, levels, "");
    }

    private static void buildLevel(MockFileAPI.Builder builder, int filesAndDirs, int levels, String prefix) {
        if (levels == 0) return;
        for (int i = 0; i < filesAndDirs; i++) {
            String dirName = prefix + "dir_" + i;
            String fileName = prefix + "file_" + i;
            builder.makeDir(dirName);
            builder.addFile(fileName);
            builder.changeDir(dirName);
            buildLevel(builder, filesAndDirs, levels - 1, dirName + "_");
            builder.changeDir("..");
        }
    }
}
