package com.yehonatand_bezalelc.stepcounter;

public enum FireBaseStatus {
    FIELD_OK("FIELD_OK", ""),
    SUCCESS("SUCCESS", ""),
    // before request errors
    EMAIL_FIELD_EMPTY("EMAIL_FIELD_EMPTY", "Field can't be empty"),
    PASSWORD_FIELD_EMPTY("PASSWORD_FIELD_EMPTY", "Field can't be empty"),
    PASSWORD_LEN_LESS_THAN_6("PASSWORD_LEN_LESS_THAN_6", "Please enter password at least 6 characters long"),
    HEIGHT_FIELD_EMPTY("HEIGHT_FIELD_EMPTY", "Enter your height"),
    WEIGHT_FIELD_EMPTY("WEIGHT_FIELD_EMPTY", "Enter your weight"),
    EMAIL_NOT_VALID("EMAIL_NOT_VALID", "Please enter a valid email address"),
    HEIGHT_TO_HIGH("HEIGHT_TO_HIGH", "Height must be < 230"),
    HEIGHT_TO_LOW("HEIGHT_TO_LOW", "Height must be > 40"),
    WEIGHT_TO_HIGH("WEIGHT_TO_HIGH", "Weight must be < 300"),
    WEIGHT_TO_LOW("WEIGHT_TO_LOW", "Weight must be > 30"),
    // firebase errors
    ERROR_INVALID_EMAIL("ERROR_INVALID_EMAIL", "Invalid email"),
    ERROR_EMAIL_ALREADY_IN_USE("ERROR_EMAIL_ALREADY_IN_USE", "This email already in use"),
    ERROR_WRONG_PASSWORD("ERROR_WRONG_PASSWORD", "Wrong password"),
    ERROR_WEAK_PASSWORD("ERROR_WEAK_PASSWORD", "Weak password"),
    ERROR_USER_NOT_FOUND("ERROR_USER_NOT_FOUND", "User not found"),
    ERROR_USER_DISABLED("ERROR_USER_DISABLED", "User account disabled"),
    ERROR_TOO_MANY_REQUESTS("ERROR_TOO_MANY_REQUESTS", "Too many unsuccessful attempts. Please try again later"),
    ERROR_OPERATION_NOT_ALLOWED("ERROR_OPERATION_NOT_ALLOWED", "Email/password authentication is not enabled"),
    ERROR_ELSE("ERROR_ELSE", ""),
    FAILED_TO_ADD_COLLECTION("FAILED_TO_ADD_COLLECTION", "Failed to add collection");

    private final String status, errorMsg;

    FireBaseStatus(String status, String errorMsg) {
        this.status = status;
        this.errorMsg = errorMsg;
    }

    public String getStatus() {
        return status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public static FireBaseStatus fromString(String string) {
        for (FireBaseStatus value : FireBaseStatus.values()) {
            if (value.getStatus().equals(string)) {
                return value;
            }
        }
        return ERROR_ELSE;
    }
}
