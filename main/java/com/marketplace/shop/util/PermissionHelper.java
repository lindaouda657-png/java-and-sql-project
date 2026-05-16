package com.marketplace.shop.util;

public class PermissionHelper {

    public static final String MANAGE_PRODUCTS   = "MANAGE_PRODUCTS";
    public static final String MANAGE_ORDERS     = "MANAGE_ORDERS";
    public static final String MANAGE_CUSTOMERS  = "MANAGE_CUSTOMERS";
    public static final String MANAGE_PAYMENTS   = "MANAGE_PAYMENTS";
    public static final String MANAGE_CATEGORIES = "MANAGE_CATEGORIES";
    public static final String MANAGE_REVIEWS    = "MANAGE_REVIEWS";

    public static final String MANAGE_EMPLOYEES  = "MANAGE_EMPLOYEES";

    public static boolean isAdmin()    { return "ADMIN".equals(SessionManager.getSelectedRole()); }
    public static boolean isEmployee() { return "EMPLOYEE".equals(SessionManager.getSelectedRole()); }
    public static boolean isStaff()    { return isAdmin() || isEmployee(); }
    public static boolean isCustomer() { return "CUSTOMER".equals(SessionManager.getSelectedRole()); }

    public static boolean employeeHas(String permissionCode) {
        return isEmployee() && SessionManager.getCurrentPermissions().contains(permissionCode);
    }

    public static boolean canPerform(String permissionCode) {
        if (isAdmin()) return true;
        return employeeHas(permissionCode);
    }

    public static boolean requirePermission(String permissionCode, String action) {
        if (isAdmin()) return true;
        if (employeeHas(permissionCode)) return true;
        if (isEmployee()) {
            AlertHelper.showPermissionDenied(
                "You don't have permission to " + action + ". " +
                "Ask your Administrator to grant you the \"" + permissionCode + "\" permission.");
        } else {
            AlertHelper.showPermissionDenied("You must sign in as an employee to " + action + ".");
        }
        return false;
    }

    public static boolean requireAdmin(String action) {
        if (isAdmin()) return true;
        AlertHelper.showPermissionDenied("Only the Administrator can " + action + ".");
        return false;
    }

    public static boolean requireCustomer(String action) {
        if (isCustomer()) return true;
        AlertHelper.showPermissionDenied("You must be signed in as a Customer to " + action + ".");
        return false;
    }
}
