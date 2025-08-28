import java.util.HashMap;
import java.util.Map;

public class RomanNumber {
    private static final Map<Character, Integer> ROMAN_VALUES = new HashMap<>();
    private static final String[] ROMAN_SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    private static final int[] ARABIC_VALUES = {100, 90, 50, 40, 10, 9, 5, 4, 1};

    static {
        ROMAN_VALUES.put('I', 1);
        ROMAN_VALUES.put('V', 5);
        ROMAN_VALUES.put('X', 10);
        ROMAN_VALUES.put('L', 50);
        ROMAN_VALUES.put('C', 100);
        ROMAN_VALUES.put('D', 500);
        ROMAN_VALUES.put('M', 1000);
    }

    public static boolean isRoman(String numberStr) {
        return numberStr != null && numberStr.matches("^[IVXLCDM]+$");
    }

    public static int toArabic(String roman) {
        if (roman == null || roman.isEmpty()) {
            throw new IllegalArgumentException("Римское число не может быть пустым");
        }

        int result = 0;
        int prevValue = 0;

        for (int i = roman.length() - 1; i >= 0; i--) {
            char currentChar = roman.charAt(i);
            if (!ROMAN_VALUES.containsKey(currentChar)) {
                throw new IllegalArgumentException("Недопустимый символ в римском числе: " + currentChar);
            }

            int currentValue = ROMAN_VALUES.get(currentChar);
            result += (currentValue < prevValue) ? -currentValue : currentValue;
            prevValue = currentValue;
        }

        return result;
    }

    public static String toRoman(int arabic) {
        // необязательно, но защита от тупого
        if (arabic < 1) {
            throw new IllegalArgumentException("Римские числа не могут быть меньше I (1)");
        }

        if (arabic > 3999) {
            throw new IllegalArgumentException("Римские числа не могут быть больше MMMCMXCIX (3999)");
        }

        StringBuilder roman = new StringBuilder();

        for (int i = 0; i < ARABIC_VALUES.length; i++) {
            while (arabic >= ARABIC_VALUES[i]) {
                roman.append(ROMAN_SYMBOLS[i]);
                arabic -= ARABIC_VALUES[i];
            }
        }

        return roman.toString();
    }
}