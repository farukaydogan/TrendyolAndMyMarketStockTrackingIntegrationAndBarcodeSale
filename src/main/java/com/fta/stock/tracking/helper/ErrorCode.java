package com.fta.stock.tracking.helper;

public enum ErrorCode {
    UNAUTHORIZED("Unauthorized", "Bu sayfaya erişim yetkiniz bulunmamaktadır."),
    FORBIDDEN("Forbidden", "Bu işlemi gerçekleştirme yetkiniz bulunmamaktadır."),
    // Buraya istediğiniz kadar hata kodu ve açıklaması ekleyebilirsiniz.
    ;

    private String title;
    private String description;

    ErrorCode(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}