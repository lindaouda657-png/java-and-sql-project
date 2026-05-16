package com.marketplace.shop.util;

import com.marketplace.shop.model.User;
import com.marketplace.shop.model.Customer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SessionManager {

    private static User currentUser;
    private static Customer currentCustomer;
    private static String selectedRole;
    private static int selectedCategoryId;
    private static String selectedCategoryName;
    private static int currentCartId;
    private static Set<String> currentPermissions = new HashSet<>();

    public static User getCurrentUser() { return currentUser; }
    public static void setCurrentUser(User user) { currentUser = user; }

    public static Customer getCurrentCustomer() { return currentCustomer; }
    public static void setCurrentCustomer(Customer customer) { currentCustomer = customer; }

    public static String getSelectedRole() { return selectedRole; }
    public static void setSelectedRole(String role) { selectedRole = role; }

    public static int getSelectedCategoryId() { return selectedCategoryId; }
    public static void setSelectedCategoryId(int id) { selectedCategoryId = id; }
    public static String getSelectedCategoryName() { return selectedCategoryName; }
    public static void setSelectedCategoryName(String n) { selectedCategoryName = n; }

    public static int getCurrentCartId() { return currentCartId; }
    public static void setCurrentCartId(int id) { currentCartId = id; }

    public static Set<String> getCurrentPermissions() {
        return Collections.unmodifiableSet(currentPermissions);
    }
    public static void setCurrentPermissions(Set<String> perms) {
        currentPermissions = (perms == null) ? new HashSet<>() : new HashSet<>(perms);
    }

    public static void logout() {
        currentUser = null;
        currentCustomer = null;
        selectedRole = null;
        selectedCategoryId = 0;
        selectedCategoryName = null;
        currentCartId = 0;
        currentPermissions = new HashSet<>();
    }

    public static boolean isLoggedIn() {
        return currentUser != null || currentCustomer != null;
    }
}
