package co.edu.uptc.control;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class BaseConverter extends CalculatorController {

    private static final String DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Map to cache powers for faster conversion of fractional parts
    private static final Map<Integer, BigDecimal[]> powerCache = new HashMap<>();

    // Convert a number from one base to another
    public static String convert(String number, int fromBase, int toBase) {
        if (!isValidNumberForBase(number, fromBase)) {
            throw new IllegalArgumentException("Invalid number for base " + fromBase);
        }

        if (number.contains(".")) {
            // Handle fractional part
            String[] parts = number.split("\\.");
            String integerPart = parts[0];
            String fractionalPart = parts[1];

            String integerResult = convertIntegerPart(integerPart, fromBase, toBase);
            String fractionalResult = convertFractionalPart(fractionalPart, fromBase, toBase);

            return integerResult + "." + fractionalResult;
        } else {
            return convertIntegerPart(number, fromBase, toBase);
        }
    }

    // Convert integer part of a number
    private static String convertIntegerPart(String number, int fromBase, int toBase) {
        BigInteger decimalValue = new BigInteger(number, fromBase);
        return decimalValue.toString(toBase).toUpperCase();
    }

    // Convert fractional part of a number
    private static String convertFractionalPart(String fractionalPart, int fromBase, int toBase) {
        BigDecimal decimalValue = BigDecimal.ZERO;
        BigDecimal base = new BigDecimal(fromBase);

        // Convert fractional part to decimal
        for (int i = 0; i < fractionalPart.length(); i++) {
            int digitValue = DIGITS.indexOf(fractionalPart.charAt(i));
            BigDecimal power = getPower(base, -(i + 1));
            decimalValue = decimalValue.add(power.multiply(new BigDecimal(digitValue)));
        }

        // Convert decimal fractional part to target base
        StringBuilder result = new StringBuilder();
        BigDecimal targetBase = new BigDecimal(toBase);

        while (decimalValue.compareTo(BigDecimal.ZERO) > 0 && result.length() < 10) {
            decimalValue = decimalValue.multiply(targetBase);
            int digitValue = decimalValue.intValue();
            result.append(DIGITS.charAt(digitValue));
            decimalValue = decimalValue.subtract(new BigDecimal(digitValue));
        }

        return result.toString();
    }

    // Get cached power of a base
    private static BigDecimal getPower(BigDecimal base, int exponent) {
        int baseInt = base.intValue();
        if (!powerCache.containsKey(baseInt)) {
            powerCache.put(baseInt, new BigDecimal[100]);
        }
        BigDecimal[] powers = powerCache.get(baseInt);
        int index = Math.abs(exponent);
        if (powers[index] == null) {
            powers[index] = base.pow(exponent);
        }
        return powers[index];
    }

    // Validate if the number is valid for the given base
    public static boolean isValidNumberForBase(String number, int base) {
        for (char c : number.toCharArray()) {
            if (c != '.' && DIGITS.indexOf(c) >= base) {
                return false;
            }
        }
        return true;
    }

}
