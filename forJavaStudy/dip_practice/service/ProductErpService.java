package dip_practice.service;

import dip_practice.Category;
import dip_practice.Product;
import java.util.List;

public interface ProductErpService {

    List<Product> getTopProductsInCategory(final Category category);
}
