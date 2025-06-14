package com.nivuk;

import java.util.List;

public interface FileAPI {
    List<String> list(String path);
    boolean delete(String path);
    boolean isDirectory(String path);
}
