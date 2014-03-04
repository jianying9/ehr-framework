package com.ehr.framework.util;

import java.text.DecimalFormat;
import java.util.Random;

/**
 *
 * @author zoe
 */
public final class NumberUtils {

    private NumberUtils() {
    }
    public final static Random random = new Random();
    public final static DecimalFormat moneyDf = new DecimalFormat("#0.00");
    public final static DecimalFormat numberDf = new DecimalFormat("#.####");

    public static String moneyFormat(double money) {
        return moneyDf.format(money);
    }

    public static int getRandomIntegerValue() {
        return random.nextInt();
    }

    public static int getRandomIntegerValue(int n) {
        return random.nextInt(n);
    }

    public static long getRandomLongValue() {
        return random.nextLong();
    }

    public static double getRandomDoubleValue() {
        int value = random.nextInt();
        double dvalue = random.nextDouble();
        return dvalue * value;
    }
}
