package com.fta.stock.tracking.helper;

public enum TrendyolEndpoint {
    BASE_URL("https://api.trendyol.com/sapigw/suppliers/"),
    UPDATE_PRICE_AND_AMOUNT("/products/price-and-inventory"),
    GET_ALL_PRODUCT("/products?size=100"),
    GET_ALL_ORDERS("/orders");

    private String endpoint;

    TrendyolEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }
}