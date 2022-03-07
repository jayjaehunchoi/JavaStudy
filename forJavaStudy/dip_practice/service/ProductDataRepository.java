package dip_practice.service;

import dip_practice.Product;

public interface ProductDataRepository {

    Product findById(final Long id);
}
