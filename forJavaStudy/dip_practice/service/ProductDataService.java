package dip_practice.service;

import dip_practice.Category;
import dip_practice.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductDataService {

    private static final int TOP_NUMBERS = 5;

    private final ProductDataRepository productDataRepository;
    private final ProductRecommender productRecommender;
    private final ProductErpService productErpService;

    public ProductDataService(ProductDataRepository productDataRepository,
                              ProductRecommender productRecommender,
                              ProductErpService productErpService) {
        this.productDataRepository = productDataRepository;
        this.productRecommender = productRecommender;
        this.productErpService = productErpService;
    }

    public List<Product> getRecommendedProductsData(final Long id) {
        Product findProduct = productDataRepository.findById(id);
        List<Product> recommendedProducts = productRecommender.getRecommendedProducts(findProduct);

        int currentProductsSize = recommendedProducts.size();
        if (currentProductsSize < TOP_NUMBERS) {
            recommendedProducts.addAll(cutProductsBySize(findProduct.getCategory(),
                    TOP_NUMBERS - currentProductsSize));
        }
        return recommendedProducts;
    }

    private List<Product> cutProductsBySize(Category category, int size) {
        List<Product> topProductsInCategory = productErpService.getTopProductsInCategory(category);
        return new ArrayList<>(topProductsInCategory.subList(0, size));
    }
}
