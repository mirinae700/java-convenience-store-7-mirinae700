package store.domain;

import java.util.ArrayList;
import java.util.List;
import store.utility.OutputView;

public class Cart {
    private List<Product> itemList = new ArrayList<>();
    private List<Product> promotionItemList = new ArrayList<>();
    private boolean membership = false;

    public void addItem(Product product) {
        this.itemList.add(product);
    }

    public void addPromotionItem(Product product) {
        this.promotionItemList.add(product);
    }

    public void setMembership(boolean membership) {
        this.membership = membership;
    }

    public void summary() {
        new OutputView().printCartSummary(itemList, promotionItemList, membership);
    }
}
