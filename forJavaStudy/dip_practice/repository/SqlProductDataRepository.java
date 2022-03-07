package dip_practice.repository;

import dip_practice.Product;
import dip_practice.service.ProductDataRepository;

public class SqlProductDataRepository implements ProductDataRepository {

    @Override
    public Product findById(Long id) {
        return null;
    }
}
