package store.utility;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    public String readPurchaseData() {
        System.out.println();
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String input = Console.readLine();
        return input;
    }

    public String readYesOrNoForPromotion(String name, int count) {
        System.out.println();
        System.out.println("현재 " + name + " " + count + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
        String input = Console.readLine();
        validateYesOrNo(input);
        return input;
    }

    public String readYesOrNoForMembership() {
        System.out.println();
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        String input = Console.readLine();
        validateYesOrNo(input);
        return input;
    }

    private void validateYesOrNo(String input) {
        boolean isValid = input.matches("[Y|N]");
        if(!isValid) {
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }

    public String readPromotionAddYesOrNo(String name, int count) {
        System.out.println();
        System.out.println("현재 " + name + "은(는) " + count + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        String input = Console.readLine();
        validateYesOrNo(input);
        return input;
    }

    public String readContinueYesOrNo() {
        System.out.println();
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        String input = Console.readLine();
        validateYesOrNo(input);
        return input;
    }

}
