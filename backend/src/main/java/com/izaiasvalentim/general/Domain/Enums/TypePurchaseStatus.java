package com.izaiasvalentim.general.Domain.Enums;

public enum TypePurchaseStatus {
    RECEIVED(1, "Received"),
    IN_PROGRESS(2, "In Progress"),
    READY(3, "Ready"),
    COMPLETED(4, "Completed"),
    CANCELLED(5, "Cancelled");

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
