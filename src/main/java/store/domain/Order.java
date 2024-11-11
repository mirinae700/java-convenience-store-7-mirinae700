package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.utility.InputView;
import store.utility.ResourceReader;

public class Order {
    private final Map<String, Integer> purchaseData;
    private final Inventory inventory;

    public Order(Inventory inventory, String input) {
        this.inventory = inventory;
        this.purchaseData = setPurchaseData(input);
    }

    private Map<String, Integer> setPurchaseData(String input) {
        String[] splitByComma = input.split(",");
        Map<String, Integer> purchaseProduct = new HashMap<>();
        for (String str : splitByComma) {
            validateUserInput(str);
            str = str.replaceAll("[\\[\\]]", "");
            String name = str.substring(0, str.indexOf("-"));
            String quantity = str.substring(str.indexOf("-") + 1);
            purchaseProduct.put(name, Integer.parseInt(quantity));
        }
        return purchaseProduct;
    }

    private void validateUserInput(String str) {
        if (!isMatched(str)) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        if (!isProductExists(str)) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않은 상품입니다. 다시 입력해 주세요.");
        }
        if (isExceedQuantity(str)) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    private boolean isMatched(String str) {
        String regex = "^\\[[가-힣]+-\\d+\\](,\\[[가-힣]+-\\d+\\])*$";
        return str.matches(regex);
    }

    private boolean isProductExists(String str) {
        str = str.replaceAll("[\\[\\]]", "");
        String name = str.substring(0, str.indexOf("-"));
        return inventory.searchProduct(name) != null || inventory.searchPromotionProduct(name) != null;
    }

    private boolean isExceedQuantity(String str) {
        str = str.replaceAll("[\\[\\]]", "");
        String name = str.substring(0, str.indexOf("-"));
        int quantity = Integer.parseInt(str.substring(str.indexOf("-") + 1));

        List<Product> products = inventory.searchProducts(name);
        int totalAvailableQuantity = products.stream()
                .mapToInt(Product::getQuantity).sum();
        return quantity > totalAvailableQuantity;
    }

    private boolean hasPromotion(String key) {
        List<Product> products = inventory.searchProducts(key);
        Product promotionProduct = null;
        for (Product p : products) {
            if (p.getPromotion() != null) {
                promotionProduct = p;
            }
        }
        return promotionProduct != null;
    }

    private Product getCartItem(Product product) {
        return new Product(product.getName(), product.getPrice(), purchaseData.get(product.getName()), "null");
    }

    private Product getCartPromotionItem(Product product, int count) {

        return new Product(product.getName(), product.getPrice(), count, product.getPromotion());
    }

    private Promotion getPromotion(String promotionName) {
        Promotion promotion = null;
        try {
            if (promotionName != null) {
                promotion = new ResourceReader().getPromotions().stream()
                        .filter(promo -> promo.getName().equals(promotionName)).findFirst().get();
            }
        } catch (IOException e) {
            System.out.println("[ERROR] 죄송합니다, 프로모션 정보를 확인하는 도중 오류가 발생했습니다.");
        }
        return promotion;
    }

    private void decreaseProductQuantity(String key) {
        Product promotionProduct = inventory.searchPromotionProduct(key);
        Product product = inventory.searchProduct(key);

        int purchaseQuantity = purchaseData.get(key);
        int promotionQuantity = 0;
        if (promotionProduct != null) {
            promotionQuantity = promotionProduct.getQuantity();
        }
        int productQuantity = 0;
        if (product != null) {
            productQuantity = product.getQuantity();
        }

        while (purchaseQuantity != 0) {
            // 프로모션 재고 먼저
            if(promotionProduct != null) {
                if (promotionQuantity != 0 && promotionQuantity >= purchaseQuantity) {
                    promotionProduct.decreaseQuantity(purchaseQuantity);
                    purchaseQuantity = 0;
                }
                if (promotionQuantity != 0 && promotionQuantity <= purchaseQuantity) {
                    promotionProduct.decreaseQuantity(promotionQuantity);
                    purchaseQuantity -= promotionQuantity;
                    promotionQuantity = 0;
                }
            }

            // 일반재고
            if (product != null) {
                if (productQuantity != 0 && productQuantity >= purchaseQuantity) {
                    product.decreaseQuantity(purchaseQuantity);
                    purchaseQuantity = 0;
                }
            }
        }
    }

    private String notifyPromotionNotApplied(String name, int nonAppliedCount) {
        String input = null;
        while (input == null) {
            try {
                input = new InputView().readYesOrNoForPromotion(name, nonAppliedCount);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return input;
    }

    private String notifyMembershipApplied() {
        String input = null;
        while (input == null) {
            try {
                input = new InputView().readYesOrNoForMembership();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return input;
    }

    public Cart addToCart() {
        Cart cart = new Cart();
        purchaseData.keySet().forEach(key -> {
            Product product = inventory.searchProduct(key);
            if (!hasPromotion(key)) {
                cart.addItem(getCartItem(product));
            }

            product = inventory.searchPromotionProduct(key);
            if (product != null) {
                LocalDateTime currentDate = DateTimes.now();
                Promotion promotion = getPromotion(product.getPromotion());

                LocalDateTime promoStart = LocalDateTime.parse(promotion.getStartDate() + "T00:00:00");
                LocalDateTime promoEnd = LocalDateTime.parse(promotion.getEndDate() + "T23:59:59");

                if ((currentDate.isAfter(promoStart) || currentDate.isEqual(promoStart))
                        && (currentDate.isBefore(promoEnd) || currentDate.isEqual(promoEnd))) {

                    int purchaseCount = purchaseData.get(key);
                    int promotionCount = purchaseCount / promotion.getBuy();
                    if (promotion.getBuy() == 1 && purchaseCount != 1) {
                        promotionCount = purchaseCount / (promotion.getBuy() + promotion.getGet());
                    }

                    int promoMinCount = promotion.getBuy() + promotion.getGet();
                    if (product.getQuantity() > promoMinCount && product.getQuantity() > purchaseCount) {
                        String addPromotion = null;
                        int addCount = Math.abs((promoMinCount * promotionCount) - purchaseCount);
                        if (purchaseCount < promoMinCount || (purchaseCount % promoMinCount) > 0) {
                            while (addPromotion == null) {
                                try {
                                    addPromotion = new InputView().readPromotionAddYesOrNo(product.getName(), addCount);
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        }
                        if (addPromotion != null) {
                            if (addPromotion.equals("Y")) {
                                purchaseCount += addCount;
                                if (purchaseCount != promotionCount + addCount) {
                                    promotionCount += addCount;
                                }
                                purchaseData.put(product.getName(), purchaseCount);
                            } else {
                                promotionCount -= addCount;
                            }
                        }
                    }

                    if (product.getQuantity() < purchaseCount) {
                        promotionCount = product.getQuantity() / (promotion.getBuy() + promotion.getGet());
                        int nonAppliedCount = purchaseData.get(key) - (promotionCount + (promotionCount * promotion.getBuy()));
                        String promoAnswer = notifyPromotionNotApplied(key, nonAppliedCount); // 프로모션 할인 미적용 개수 안내
                        if (promoAnswer.equals("N")) {
                            int count = purchaseData.get(key) - nonAppliedCount;
                            purchaseData.put(key, count);
                        }
                    }
                    if (promotionCount > 0) {
                        cart.addPromotionItem(getCartPromotionItem(product, promotionCount));
                    }
                }
                cart.addItem(getCartItem(product));
            }
            decreaseProductQuantity(key); // 재고 반영
        });

        String membershipAnswer =  notifyMembershipApplied();
        if (membershipAnswer.equals("Y")) {
            cart.setMembership(true); // 할인 적용
        }
        return cart;
    }
}
