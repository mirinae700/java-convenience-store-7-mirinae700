package store.constant;

public enum StoreResource {
    PRODUCTS("src/main/resources/products.md"),
    PROMOTIONS("src/main/resources/promotions.md");

    private String filepath;

    StoreResource(String filePath) {
        this.filepath = filePath;
    }

    public String getFilepath() {
        return filepath;
    }
}
