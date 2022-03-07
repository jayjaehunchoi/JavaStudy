package dip_practice.repository;

import dip_practice.Product;
import dip_practice.service.ProductDataRepository;
import java.util.HashMap;
import java.util.Map;

public class MemoryProductDataRepository implements ProductDataRepository {

    private Map<Long, Product> store = new HashMap<>();

    @Override
    public Product findById(Long id) {
        return store.get(id);
    }
}
