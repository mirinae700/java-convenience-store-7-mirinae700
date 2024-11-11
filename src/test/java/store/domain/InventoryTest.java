package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class InventoryTest {
    Inventory inventory = new Inventory();

    @Test
    void 상품이름으로_재고를_확인한다() {
        assertThat(inventory.searchProduct("오렌지주스").getInfo())
                .isEqualTo("- 오렌지주스 1,800원 재고 없음 ");
    }

    @Test
    void 상품이름으로_프로모션재고를_확인한다() {
        assertThat(inventory.searchPromotionProduct("오렌지주스").getInfo())
                .isEqualTo("- 오렌지주스 1,800원 9개 MD추천상품");
    }

    @Test
    void 상품이름으로_모든재고를_확인한다() {
        assertThat(inventory.searchProducts("오렌지주스")).hasSize(2);
    }
}
