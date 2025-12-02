package com.izaiasvalentim.general.Domain.Enums;

public enum TypePurchaseStatus {
    RECEIVED(1, "RECEBIDO"),
    IN_PROGRESS(2, "EM PROGRESSO"),
    READY(3, "PRONTO"),
    COMPLETED(4, "COMPLETADO"),
    CANCELLED(5, "CANCELADO");

    private final int id;
    private final String status;

    public static String getStatusById(int id) {
        for (TypePurchaseStatus status : TypePurchaseStatus.values()) {
            if (status.id == id) {
                return status.getStatus();
            }
        }
        return null;
    }

    public static TypePurchaseStatus getStatusEnumById(int id) {
        for (TypePurchaseStatus status : TypePurchaseStatus.values()) {
            if (status.id == id) {
                return status;
            }
        }
        return null;
    }

    public static TypePurchaseStatus getStatusEnumByStatus(String status) {
        for (TypePurchaseStatus type : TypePurchaseStatus.values()) {
            if (type.status.equals(status)) {
                return type;
            }
        }
        return null;
    }

    TypePurchaseStatus(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
