package com.nivuk;

public class Deltree {
    private final MockFileAPI fileAPI;

    public Deltree(MockFileAPI fileAPI) {
        this.fileAPI = fileAPI;
    }

    // Naive recursive delete
    public boolean deleteRecursive(String path) {
        if (fileAPI.isDirectory(path)) {
            for (String child : fileAPI.list(path)) {
                if (!deleteRecursive(child)) {
                    return false;
                }
            }
        }
        return fileAPI.delete(path);
    }
}
