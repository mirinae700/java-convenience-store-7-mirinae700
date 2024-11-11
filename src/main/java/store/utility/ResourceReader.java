package store.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.constant.StoreResource;
import store.domain.Product;
import store.domain.Promotion;

public class ResourceReader {

    public List<Product> getProducts() throws IOException {
        List<Product> products = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(StoreResource.PRODUCTS.getFilepath()));
        br.readLine();
        String str;
        while ((str = br.readLine()) != null) {
            String[] arr = str.split(",");
            products.add(new Product(arr[0], Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), arr[3]));
        }
        br.close();
        return products;
    }

    public List<Promotion> getPromotions() throws IOException {
        List<Promotion> promotions = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(StoreResource.PROMOTIONS.getFilepath()));
        br.readLine();
        String str;
        while ((str = br.readLine()) != null) {
            String[] arr = str.split(",");
            promotions.add(new Promotion(arr[0], Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), arr[3], arr[4]));
        }
        br.close();
        return promotions;
    }
}
