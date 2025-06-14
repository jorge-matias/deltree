package com.nivuk;

import java.util.*;

public class MockFileAPI implements FileAPI {
    // Abstract Node class
    private static abstract class Node {
        String name;
        Node(String name) {
            this.name = name;
        }
    }

    // Directory node
    private static class DirNode extends Node {
        Map<String, Node> children = new HashMap<>();
        DirNode(String name) {
            super(name);
        }
    }

    // File node
    private static class FileNode extends Node {
        FileNode(String name) {
            super(name);
        }
    }

    private final DirNode root = new DirNode("/");

    // Helper to find node by path
    private Node findNode(String path) {
        if (path.equals("/")) return root;
        String[] parts = path.substring(1).split("/");
        Node curr = root;
        for (String part : parts) {
            if (!(curr instanceof DirNode dir) || !dir.children.containsKey(part)) return null;
            curr = dir.children.get(part);
        }
        return curr;
    }

    // Helper to find parent node and child name
    private DirNode findParent(String path) {
        if (path.equals("/")) return null;
        int lastSlash = path.lastIndexOf('/');
        String parentPath = (lastSlash == 0) ? "/" : path.substring(0, lastSlash);
        Node parent = findNode(parentPath);
        return (parent instanceof DirNode dir) ? dir : null;
    }

    public List<String> list(String path) {
        Node node = findNode(path);
        if (!(node instanceof DirNode dir)) return new ArrayList<>();
        List<String> result = new ArrayList<>();
        for (Node child : dir.children.values()) {
            String childPath = path.equals("/") ? "/" + child.name : path + "/" + child.name;
            result.add(childPath);
        }
        return result;
    }

    public boolean isDirectory(String path) {
        Node node = findNode(path);
        return node instanceof DirNode;
    }

    public boolean delete(String path) {
        if (path.equals("/")) return false;
        DirNode parent = findParent(path);
        if (parent == null) return false;
        String name = path.substring(path.lastIndexOf('/') + 1);
        Node node = parent.children.get(name);
        if (node == null) return false;
        if (node instanceof DirNode dir && !dir.children.isEmpty()) return false;
        parent.children.remove(name);
        return true;
    }

    // Fluent builder for tree construction
    public static class Builder {
        private final DirNode root = new DirNode("/");
        private DirNode current = root;
        private final Deque<DirNode> stack = new ArrayDeque<>();

        public Builder addFile(String name) {
            current.children.put(name, new FileNode(name));
            return this;
        }
        public Builder makeDir(String name) {
            DirNode dir = new DirNode(name);
            current.children.put(name, dir);
            return this;
        }
        public Builder changeDir(String name) {
            if ("..".equals(name)) {
                if (!stack.isEmpty()) {
                    current = stack.pop();
                }
            } else {
                Node node = current.children.get(name);
                if (node instanceof DirNode dir) {
                    stack.push(current);
                    current = dir;
                }
            }
            return this;
        }
        DirNode build() { return root; }
    }

    public static Builder builder() { return new Builder(); }

    public MockFileAPI(Builder builder) {
        this.root.children.clear();
        this.root.children.putAll(builder.build().children);
    }
}
