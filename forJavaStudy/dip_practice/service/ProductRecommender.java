package dip_practice.service;

import dip_practice.Product;
import java.util.List;

public interface ProductRecommender {

    List<Product> getRecommendedProducts(final Product product);
}
