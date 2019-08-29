package com.neuedu.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {

    //加法
    public static BigDecimal add(double d1,double d2){

        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(d2));

        return bigDecimal1.add(bigDecimal2);

    }

    //减法
    public static BigDecimal val(double d1,double d2){

        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(d2));

        return bigDecimal1.subtract(bigDecimal2);

    }

    //乘法
    public static BigDecimal mul(double d1,double d2){

        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(d2));

        return bigDecimal1.multiply(bigDecimal2);

    }

    //除法
    public static BigDecimal dev(double d1,double d2){

        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(d2));

        return bigDecimal1.divide(bigDecimal2);

    }

}
