package store;

import store.domain.Cart;
import store.domain.Inventory;
import store.domain.Order;
import store.utility.InputView;
import store.utility.OutputView;

public class Application {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        OutputView outputView = new OutputView();
        InputView inputView = new InputView();

        String continueYesOrNo = "Y";
        while (continueYesOrNo.equals("Y")) {
            run(inventory, inputView, outputView);
            continueYesOrNo = null;
            while (continueYesOrNo == null) {
                try {
                    continueYesOrNo = inputView.readContinueYesOrNo();
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private static void run(Inventory inventory, InputView inputView, OutputView outputView) {
        outputView.printInventory(inventory);
        Order order = null;
        while (order == null) {
            try {
                order = new Order(inventory, inputView.readPurchaseData());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        Cart cart = order.addToCart();
        cart.summary();
    }
}
