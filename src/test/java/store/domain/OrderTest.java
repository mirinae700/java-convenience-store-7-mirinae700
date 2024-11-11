package store.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    private final Inventory inventory = new Inventory();
    Order order = new Order(inventory, "[오렌지주스-1]");

    @Test
    void 요청상품목록이_형식에_맞지않으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Order(inventory, "[오렌지주스:1],[콜라:3]"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_상품을_입력하면_예외가_발생한다() {
        assertThatThrownBy(() -> new Order(inventory, "[오렌지-1]"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 재고_수량을_초과하면_예외가_발생한다() {
        assertThatThrownBy(() -> new Order(inventory, "[오렌지주스-10000]"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
