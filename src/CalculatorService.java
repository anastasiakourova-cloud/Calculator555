import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CalculatorService {
    private static final Pattern EXPRESSION_PATTERN =
            Pattern.compile("^(\\d+|[IVXLCDM]+)([+\\-*/])(\\d+|[IVXLCDM]+)$");
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 10;

    public static void run () {
        Scanner scanner = new Scanner(System.in);
        printInstructions();

        while (true) {
            try {
                System.out.print("Введите выражение: ");
                String input = scanner.nextLine().trim();

                if ("exit".equalsIgnoreCase(input)) {
                    break;
                }

                if (input.isEmpty()) {
                    continue;
                }

                input = input.replaceAll("\\s+", "");

                String result = calculate(input);
                System.out.println("Результат: " + result);

            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Калькулятор завершил работу.");
    }

    private static void printInstructions() {
        System.out.println("""
        === КАЛЬКУЛЯТОР ===
        Формат: число оператор число
        Операторы: +, -, *, /
        Числа: от 1 до 10 (арабские или римские)
        Команда 'exit' для выхода
        ===================
        """);
    }

    public static String calculate(String input) {
        Matcher matcher = EXPRESSION_PATTERN.matcher(input);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Неверный формат выражения. Используйте: число оператор число");
        }

        String firstOperand = matcher.group(1);
        String operator = matcher.group(2);
        String secondOperand = matcher.group(3);

        boolean isRoman1 = RomanNumber.isRoman(firstOperand);
        boolean isRoman2 = RomanNumber.isRoman(secondOperand);

        if (isRoman1 != isRoman2) {
            throw new IllegalArgumentException("Нельзя смешивать арабские и римские числа");
        }

        int num1 = isRoman1 ? RomanNumber.toArabic(firstOperand) : parseInt(firstOperand);
        int num2 = isRoman1 ? RomanNumber.toArabic(secondOperand) : parseInt(secondOperand);

        validateNumberRange(num1);
        validateNumberRange(num2);

        int result = performCalculation(num1, num2, operator.charAt(0));

        if (isRoman1) {
            if (result < 1) {
                throw new IllegalArgumentException("Результат операции с римскими числами должен быть не меньше I (1)");
            }
            return RomanNumber.toRoman(result);
        } else {
            return String.valueOf(result);
        }
    }

    private static int parseInt(String numberStr) {
        try {
            return Integer.parseInt(numberStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + numberStr + "' не является целым числом");
        }
    }

    private static void validateNumberRange(int number) {
        if (number < MIN_VALUE || number > MAX_VALUE) {
            throw new IllegalArgumentException(
                    "Число " + number + " не в диапазоне от " + MIN_VALUE + " до " + MAX_VALUE
            );
        }
    }

    private static int performCalculation(int num1, int num2, char operator) {
        return switch (operator) {
            case '+' -> num1 + num2;
            case '-' -> num1 - num2;
            case '*' -> num1 * num2;
            case '/' -> {
                if (num2 == 0) throw new IllegalArgumentException("Деление на ноль");
                yield num1 / num2;
            }
            default -> throw new IllegalArgumentException("Оператор не поддерживается");
        };
    }
}