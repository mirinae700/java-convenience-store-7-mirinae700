package store.domain;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.utility.ResourceReader;

public class Inventory {
    private List<Product> productList;
    private Map<String, Product> productMap;

    public Inventory() {
        try {
            this.productList = new ResourceReader().getProducts();
        } catch (IOException e) {
            System.out.println("[ERROR] 죄송합니다, 상품 정보를 읽어오지 못했습니다.");
        }
        setProductMap();
    }

    private void setProductMap() {
        Map<String, Product> productMap = new HashMap<>();
        for (Product p : productList) {
            p.putProductMap(productMap);
        }
        this.productMap = productMap;
    }

    public String getProductInventory() {
        StringBuilder sb = new StringBuilder();
        for (Product p : productList) {
            sb.append(p.getInfo().trim()).append("\n");
        }
        String result = sb.toString();
        return result.trim();
    }

    public List<Product> searchProducts(String key) {
        return productMap.entrySet().stream()
                .filter(entry -> entry.getKey().contains(key))
                .map(Map.Entry::getValue)
                .toList();
    }

    public Product searchProduct(String key) {
        return productMap.get(key);
    }

    public Product searchPromotionProduct(String key) {
        Product product = null;
        for (Map.Entry<String, Product> entry : productMap.entrySet()) {
            if (entry.getKey().contains(key + "-")) {
                product = entry.getValue();
                break; // 첫 번째 일치하는 값을 찾으면 루프 종료
            }
        }
        return product;
    }
}
