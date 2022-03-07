package dip_practice.external_api;

import java.util.ArrayList;
import java.util.List;

public class FakeS3Uploader {

    private List<String> files = new ArrayList<>();

    public void upload(String file) {
        files.add(file);
    }
}
