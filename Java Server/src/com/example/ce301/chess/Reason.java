package com.example.ce301.chess;

public class Reason {
    private boolean success;
    private String reason;

    public Reason(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "Reason{" +
                "success=" + success +
                ", reason='" + reason + '\'' +
                '}';
    }
}
