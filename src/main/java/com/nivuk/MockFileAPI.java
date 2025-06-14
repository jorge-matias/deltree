package com.nivuk;

import java.util.*;

public class MockFileAPI {
    private final Map<String, List<String>> fileSystem = new HashMap<>();
    private final Set<String> directories = new HashSet<>();

    public MockFileAPI() {
        // Root directory
        directories.add("/");
        fileSystem.put("/", new ArrayList<>());
    }

    public void addFile(String path, boolean isDir) {
        if (path.equals("/")) return; // Don't add root
        int lastSlash = path.lastIndexOf('/');
        String parent = (lastSlash <= 0) ? "/" : path.substring(0, lastSlash);
        fileSystem.computeIfAbsent(parent, k -> new ArrayList<>()).add(path);
        if (isDir) {
            directories.add(path);
            fileSystem.put(path, new ArrayList<>());
        }
    }

    public List<String> list(String path) {
        List<String> children = fileSystem.get(path);
        return children == null ? new ArrayList<>() : new ArrayList<>(children);
    }

    public boolean isDirectory(String path) {
        return directories.contains(path);
    }

    public boolean delete(String path) {
        if (isDirectory(path) && !fileSystem.get(path).isEmpty()) {
            return false; // Directory not empty
        }
        if (path.equals("/")) return false; // Don't delete root
        int lastSlash = path.lastIndexOf('/');
        String parent = (lastSlash <= 0) ? "/" : path.substring(0, lastSlash);
        List<String> siblings = fileSystem.get(parent);
        if (siblings != null) siblings.remove(path);
        directories.remove(path);
        fileSystem.remove(path);
        return true;
    }
}
