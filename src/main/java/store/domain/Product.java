package store.domain;

import java.text.DecimalFormat;
import java.util.Map;

public class Product {
    private String name;
    private int price;
    private int quantity;
    private String promotion;

    public Product(String name, int price, int quantity, String promotion) {
        if (promotion.equals("null")) {
            promotion = null;
        }
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public void putProductMap(Map<String, Product> map) {
        String key = this.name;
        if (this.promotion != null) {
            key = this.name + "-" + this.promotion;
        }
        map.put(key, this);
    }

    public void decreaseQuantity(int count) {
        this.quantity -= count;
    }

    public String getInfo() {
        DecimalFormat formatter = new DecimalFormat("#,###");
        String quantityString = this.quantity + "개";
        if (this.quantity == 0) {
            quantityString = "재고 없음";
        }
        String promotion = this.promotion;
        if (promotion == null) {
            promotion = "";
        }
        return "- " + this.name + " " + formatter.format(this.price) + "원 " + quantityString + " " + promotion;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getPromotion() {
        return this.promotion;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
