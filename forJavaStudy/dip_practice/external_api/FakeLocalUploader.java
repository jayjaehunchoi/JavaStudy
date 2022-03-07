package dip_practice.external_api;

import java.util.ArrayList;
import java.util.List;

public class FakeLocalUploader {

    private List<String> files = new ArrayList<>();

    public void store(String file) {
        files.add(file);
    }
}
