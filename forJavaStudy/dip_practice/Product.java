package dip_practice;

public class Product {

    private final Long id;
    private final String name;
    private final Category category;
    private final int price;

    public Product(Long id, String name, Category category, int price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }
}