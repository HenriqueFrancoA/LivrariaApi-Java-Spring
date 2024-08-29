package br.com.henrique.JWT.enums;

public enum PaymentStatus {
    WAITING(1, "AGUARDANDO"),
    APPROVED(2, "APROVADO"),
    REJECTED(3, "NEGADO"),
    REFUNDED(4, "REEMBOLSADO");

    private final int code;
    private final String description;

    PaymentStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}