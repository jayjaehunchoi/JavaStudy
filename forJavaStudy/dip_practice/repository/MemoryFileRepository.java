package dip_practice.repository;

import java.util.HashMap;
import java.util.Map;

public class MemoryFileRepository {

    private Map<Long, String> store = new HashMap<>();
    private Long seq = 0L;

    public String findFileById(Long id) {
        return store.get(id);
    }

    public void save(String file) {
        store.put(++seq, file);
    }
}
