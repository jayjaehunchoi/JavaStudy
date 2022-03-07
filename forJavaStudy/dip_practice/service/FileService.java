package dip_practice.service;

import dip_practice.external_api.FakeLocalUploader;
import dip_practice.repository.MemoryFileRepository;

public class FileService {

    private final FakeLocalUploader fakeLocalUploader;
    private final MemoryFileRepository memoryFileRepository;

    public FileService(FakeLocalUploader fakeLocalUploader, MemoryFileRepository memoryFileRepository) {
        this.fakeLocalUploader = fakeLocalUploader;
        this.memoryFileRepository = memoryFileRepository;
    }

    public void saveFile(String file) {
        fakeLocalUploader.store(file);
        memoryFileRepository.save(file);
    }
}
