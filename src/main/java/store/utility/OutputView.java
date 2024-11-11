package store.utility;

import java.text.DecimalFormat;
import java.util.List;
import store.domain.Inventory;
import store.domain.Product;

public class OutputView {

    public void printInventory(Inventory inventory) {
        System.out.println();
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        System.out.println(inventory.getProductInventory());
    }

    public void printCartSummary(List<Product> itemList, List<Product> promotionItemList, boolean membership) {
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#,###");
        int itemPriceSum = 0;
        int promoItemPriceSum = 0;
        int itemQuantitySum = 0;

        sb.append("\n");
        sb.append("==============W 편의점================\n");
        sb.append(String.format("%-15s\t%-6s\t%-8s%n", "상품명", "수량", "금액"));
        for (Product p : itemList) {
            int price = p.getPrice() * p.getQuantity();
            itemPriceSum += price;
            itemQuantitySum += p.getQuantity();
            sb.append(String.format("%-15s\t%-6d\t%-8s%n", p.getName(), p.getQuantity(), df.format(price)));
        }

        if (!promotionItemList.isEmpty()) {
            sb.append("=============증     정===============\n");
            for (Product p : promotionItemList) {
                promoItemPriceSum += (p.getPrice() * p.getQuantity());
                sb.append(String.format("%-15s\t%-6d%n", p.getName(), p.getQuantity()));
            }
        }

        sb.append("====================================\n");
        sb.append(String.format("%-15s\t%-6d\t%8s%n", "총구매액", itemQuantitySum, df.format(itemPriceSum)));
        sb.append(String.format("%-15s\t%-6s\t%8s%n", "행사할인", "", df.format(-promoItemPriceSum)));

        double membershipSale = 0;
        if (membership) {
            membershipSale = Math.floor(((itemPriceSum - promoItemPriceSum) * 0.3) / 1000) * 1000;
            if (membershipSale > 8000) {
                membershipSale = 8000;
            }
        }

        sb.append(String.format("%-15s\t%-6s\t%8s%n", "멤버십할인", "", df.format(-membershipSale)));
        int amount = itemPriceSum - promoItemPriceSum - (int)membershipSale;
        sb.append(String.format("%-15s\t%-6s\t%8s", "내실돈", "", df.format(amount)));

        String result = sb.toString();
        System.out.println(result);
    }
}
