package com.dino_d.pancakes_unlimited.utils;

import java.math.BigDecimal;

public class AppConstants {

    // Pancake constants
    public static final int MINIMUM_PANCAKES_PER_ORDER = 1;
    public static final int HEALTHY_DISCOUNT_THRESHOLD = 75;    // 75%


    // Category constants
    public static final int CATEGORY_BASE_ID = 1;               // kategorija 'baza'
    public static final int CATEGORY_STUFFING_ID = 2;           // kategorija 'nadjev'


    // Role constants
    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    public static final String ROLE_EMPLOYEE = "ROLE_EMPLOYEE";
    public static final String ROLE_STORE_OWNER = "ROLE_STORE_OWNER";


    //Order constants

    // for 5% discount
    // current price is 100%
    // after 5% discount
    // new price = 100% - 5%
    // new price = 95%
    // HEALTHY_DISCOUNT_AMOUNT = 0.95
    public static final BigDecimal DISCOUNT_0_PERCENT = BigDecimal.valueOf(1);
    public static final BigDecimal DISCOUNT_5_PERCENT = BigDecimal.valueOf(0.95);
    public static final BigDecimal DISCOUNT_10_PERCENT = BigDecimal.valueOf(0.9);
    public static final BigDecimal DISCOUNT_15_PERCENT = BigDecimal.valueOf(0.85);
}
