package calculator;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorUtil {
    static HashMap<Character, Integer> precedence = new HashMap<>();
    static String numberRegex = "(?<=[^\\d)])(?>-\\d+(\\.\\d+)?)|\\d+(?>\\.\\d+)?"; // no direct negative number
    static String operatorRegex = "[=()^\u002B\u00D7\u00F7\u221A-]";
    static Pattern notationPattern = Pattern.compile(numberRegex + "|" + operatorRegex);

    static void initPrecedence() {
        precedence.put('(', 0);
        precedence.put(')', 0);
        precedence.put('^', 1); // Exponentiation
        precedence.put('\u221A', 1);
        precedence.put('\u00D7', 2); // Multiplication
        precedence.put('\u00F7', 2); // Division
        precedence.put('\u002B', 3); // Addition
        precedence.put('-', 3); // Subtraction
    }

    static String calculate(String raw) {
        return calculatePostfix(convertPostfix(scanInfix(raw))).toPlainString();
    }

    static List<Notation> scanInfix(String s) {
        // scan all variables and map them to values
        List<Notation> matchList = new ArrayList<>();
        Matcher notationMatcher = notationPattern.matcher(s);
        while (notationMatcher.find()) {
            String notation = notationMatcher.group();
            if (notation.matches(operatorRegex)) {
                matchList.add(new Notation(notation.charAt(0)));  // if it's an operator, append its symbol
            } else {
                matchList.add(new Notation(new BigDecimal(notation))); // if it's a variable, append its value
            }
        }
        return matchList;
    }

    static List<Notation> convertPostfix(List<Notation> infix) {
        System.out.print(infix);
        List<Notation> postfix = new ArrayList<>();
        Stack<Character> stack = new Stack<>();

        while (!infix.isEmpty()) {
            Notation symbol = infix.remove(0);
            if (symbol.isVariable) {
                postfix.add(symbol); // 1. Add operands (numbers and variables) to the result (postfix notation) as they arrive.
            } else {
                char operator = symbol.operator; // then it's an operator
                if (operator == ')') { // 6. If the incoming element is a right parenthesis, pop the stack and add operators to the result until you see a left parenthesis.
                    while (stack.peek() != '(') {
                        postfix.add(new Notation(stack.pop()));
                    }
                    stack.pop(); // Discard the pair of parentheses.
                    continue;
                }

                if (stack.isEmpty() || stack.peek() == '(' || operator == '(') { // 5. If the incoming element is a left parenthesis, push it on the stack.
                    stack.push(operator); // 2. If the stack is empty or contains a left parenthesis on top, push the incoming operator on the stack.
                    continue;
                }

                if (precedence.get(operator) < precedence.get(stack.peek())) {
                    stack.push(operator); // 3. If the incoming operator has higher precedence than the top of the stack, push it on the stack.
                } else {
                    do {
                        postfix.add(new Notation(stack.pop())); // 4. If the incoming operator has lower or equal precedence than the top of the operator stack, pop the stack and add operators to the result until you see an operator that has a smaller precedence or a left parenthesis on the top of the stack;
                    } while (!stack.isEmpty()
                            && precedence.get(operator) >= precedence.get(stack.peek())
                            && stack.peek() != '(');
                    stack.push(operator); // then add the incoming operator to the stack.
                }
            }
        }

        while (!stack.isEmpty()) { // 7. At the end of the expression, pop the stack and add all operators to the result.
//            System.out.println(stack);
            postfix.add(new Notation(stack.pop()));
        }
        System.out.print(" -> " + postfix + "\n");
        return postfix;
    }

    private static BigDecimal calculatePostfix(List<Notation> postfix) {
        Stack<BigDecimal> result = new Stack<>();

        while (!postfix.isEmpty()) {
            Notation element = postfix.remove(0);
            if (element.isVariable) {
                result.push(element.value);
            } else {
                char operator = element.operator;
                BigDecimal a = result.pop();
                if (operator == '\u221A') {
                    result.push(a.sqrt(new MathContext(0)));
                    continue;
                }
                BigDecimal b = result.pop();
                switch (operator) {
                    case '\u00D7':
                        try {
                            result.push(b.multiply(a).setScale(0, RoundingMode.UNNECESSARY));
                        } catch (Exception e) {
                            result.push(b.multiply(a));
                        }
                        break;
                    case '\u00F7':
                        try {
                            result.push(b.divide(a));
                        } catch (Exception e) {
                            result.push(b.divide(a, 16, RoundingMode.HALF_DOWN));
                        }
                        break;
                    case '\u002B':
                        result.push(b.add(a));
                        break;
                    case '-':
                        result.push(b.subtract(a));
                        break;
                    case '^':
                        result.push(b.pow(Integer.parseInt(a.toPlainString())));
                        break;
                }
            }
        }

        if (result.size() != 1) {
            System.out.println("WHAT");
        }
        return result.pop();
    }

    static boolean validate(JLabel equationLabel) {
        String equation = equationLabel.getText();
        if (equation.isEmpty()) {
            return false;
        }
        String lastChar = equation.substring(equation.length() - 1);
        if (lastChar.equals(".")) {
            equationLabel.setText(equation + "0");
        } else if (lastChar.matches(operatorRegex) && !")".equals(lastChar)) {
            delete(equationLabel);
        }
        return true;
    }

    public static void assignSubtract(JLabel equationLabel) {
        if (validate(equationLabel)) equationLabel.setText(equationLabel.getText() + "-");
    }

    public static void assignAdd(JLabel equationLabel) {
        if (validate(equationLabel)) equationLabel.setText(equationLabel.getText() + "\u002B");
    }

    public static void assignMultiply(JLabel equationLabel) {
        if (validate(equationLabel)) equationLabel.setText(equationLabel.getText() + "\u00D7");
    }

    public static void assignDivide(JLabel equationLabel) {
        if (validate(equationLabel)) equationLabel.setText(equationLabel.getText() + "\u00F7");
    }

    public static void assignDot(JLabel equationLabel) {
        String equation = equationLabel.getText();
        if (!equation.isEmpty()) {
            String lastChar = equation.substring(equation.length() - 1);
            if (".".equals(lastChar) || lastChar.matches(operatorRegex)) return;
        }
        equationLabel.setText(equation + ".");
    }

    public static void evaluate(JLabel equationLabel, JLabel resultLabel) {
        if (!equationLabel.getText().isEmpty()) {
            String equation = equationLabel.getText();
            String lastChar = equation.substring(equation.length() - 1);
            if (lastChar.matches(operatorRegex) && !")".equals(lastChar)) {
                equationLabel.setForeground(Color.RED.darker());
            } else {
                try {
                    String result = CalculatorUtil.calculate(equationLabel.getText());
                    if (result.length() >= 16) {
                        resultLabel.setFont(resultLabel.getFont().deriveFont(15f));
                    } else if (result.length() >= 12) {
                        resultLabel.setFont(resultLabel.getFont().deriveFont(21f));
                    } else {
                        resultLabel.setFont(resultLabel.getFont().deriveFont(30f));
                    }
                    resultLabel.setText(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    equationLabel.setForeground(Color.RED.darker());
                }
            }
        }
    }

    public static void assignNumber(JLabel equationLabel, int i) {
        equationLabel.setForeground(Color.MAGENTA);
        String equation = equationLabel.getText();
        if (equation.equals(".")) {
            equationLabel.setText("0.");
        }
        equationLabel.setText(equationLabel.getText() + i);
    }

    public static void delete(JLabel equationLabel) {
        equationLabel.setForeground(Color.MAGENTA);
        String data = equationLabel.getText();
        equationLabel.setText(data.substring(0, data.length() - 1));
    }

    public static void insertParentheses(JLabel equationLabel) {
        String raw = equationLabel.getText();
        int l_parentheses = 0;
        int r_parentheses = 0;
        for (char c : raw.toCharArray()) {
            if (c == '(') {
                l_parentheses++;
            } else if (c == ')') {
                r_parentheses++;
            }
        }
        if (l_parentheses == r_parentheses) {
            equationLabel.setText(raw + '(');
            return;
        }

        String lastChar = raw.substring(raw.length() - 1);
        if (lastChar.matches(operatorRegex) && !")".equals(lastChar)
                || "(".equals(lastChar)) {
            equationLabel.setText(raw + '(');
            return;
        }

        equationLabel.setText(raw + ')');
    }

    public static void insertSqrt(JLabel equationLabel) {
        String raw = equationLabel.getText();
        equationLabel.setText(raw + "\u221A(");
    }

    public static void raiseToY(JLabel equationLabel) {
        String raw = equationLabel.getText();
        equationLabel.setText(raw + "^(");
    }

    public static void raiseTo2(JLabel equationLabel) {
        String raw = equationLabel.getText();
        equationLabel.setText(raw + "^(2)");
    }

    public static void negate(JLabel equationLabel) {
        String raw = equationLabel.getText();

        if (raw.isEmpty()) {
            equationLabel.setText("(-");
        } else if (raw.equals("(-")) { // negating negations
            equationLabel.setText("");
        } else if (raw.length() >= 5
                && "(-(".equals(raw.substring(0, 3))
                && "))".equals(raw.substring(raw.length() - 2))) {
            equationLabel.setText(raw.substring(3, raw.length() - 2));
        } else if (raw.length() == 3
                && "(-".equals(raw.substring(0, 2))) {
            equationLabel.setText(raw.substring(2));
        } else if (raw.matches(numberRegex)) {
            equationLabel.setText("(-" + raw);
        } else {
            equationLabel.setText("(-(" + raw + "))");
        }
    }
}
