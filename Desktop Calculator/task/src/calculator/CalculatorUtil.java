package calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorUtil {
    static HashMap<Character, Integer> precedence = new HashMap<>();
    static String numberRegex = "\\d+(\\.\\d+)?"; // no direct negative number
//    static Pattern numberPattern = Pattern.compile(numberRegex);
    static String operatorRegex = "[-\u002B\u00D7\u00F7]";
//    static Pattern operatorPattern = Pattern.compile(operatorRegex);
    static Pattern notationPattern = Pattern.compile(numberRegex + "|" + operatorRegex);
    //    static String numberRegex = "-?\\d+";
    //    static String latinRegex = "[a-wyzA-WYZ]+"; // no x allowed
    //    static String latinRegex = "[a-zA-Z]+";
    //    static Pattern latinPattern = Pattern.compile(latinRegex);
//    static HashMap<String, BigDecimal> variables = new HashMap<>();
//    static String variableRegex = numberRegex + "|" + latinRegex;

//    static String[] errMsg = {
//            "Invalid expression", "Unknown command", "Invalid assignment", "Unknown variable"
//    };

    static void initPrecedence() {
//        precedence.put('(', 1);
//        precedence.put(')', 1); // Parentheses
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
            if (notation.matches(numberRegex)) { // if it's a variable, append its value
//                if (variables.containsKey(notation)) {
//                    matchList.add(new Notation(variables.get(notation)));
//                } else {
                    matchList.add(new Notation(new BigDecimal(notation)));
//                }
            } else { // if it's an operator, append its symbol
                if (notation.charAt(0) == '\u2212' && notation.length() % 2 == 0) {
                    matchList.add(new Notation('\u002B')); // if it's some even minus, append plus
                } else {
                    matchList.add(new Notation(notation.charAt(0)));
                }
            }
        }
        return matchList;
    }

    static List<Notation> convertPostfix(List<Notation> infix) {
        List<Notation> postfix = new ArrayList<>();
        Stack<Character> stack = new Stack<>();

        while (!infix.isEmpty()) {
            Notation symbol = infix.remove(0);
            if (symbol.isVariable) {
                postfix.add(symbol); // 1. Add operands (numbers and variables) to the result (postfix notation) as they arrive.
            } else {
                char operator = symbol.operator; // then it's an operator
                if (stack.isEmpty() || stack.peek() == '(' || operator == '(') { // 5. If the incoming element is a left parenthesis, push it on the stack.
                    stack.push(operator); // 2. If the stack is empty or contains a left parenthesis on top, push the incoming operator on the stack.
                    continue;
                }
//
//                if (operator == ')') { // 6. If the incoming element is a right parenthesis, pop the stack and add operators to the result until you see a left parenthesis.
//                    while (stack.peek() != '(') {
//                        postfix.add(new Notation(stack.pop()));
//                    }
//                    stack.pop(); // Discard the pair of parentheses.
//                    continue;
//                }

                if (precedence.get(operator) < precedence.get(stack.peek())) {
                    stack.push(operator); // 3. If the incoming operator has higher precedence than the top of the stack, push it on the stack.
                } else {
                    do {
                        postfix.add(new Notation(stack.pop())); // 4. If the incoming operator has lower or equal precedence than the top of the operator stack, pop the stack and add operators to the result until you see an operator that has a smaller precedence or a left parenthesis on the top of the stack;
                    } while (!stack.isEmpty()
                            && precedence.get(operator) >= precedence.get(stack.peek()));
//                            && stack.peek() != '(');
                    stack.push(operator); // then add the incoming operator to the stack.
                }
            }
        }

        while (!stack.isEmpty()) { // 7. At the end of the expression, pop the stack and add all operators to the result.
//            System.out.println(stack);
            postfix.add(new Notation(stack.pop()));
        }
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
                }
            }
        }

        if (result.size() != 1) {
            System.out.println("WHAT");
        }
        return result.pop();
    }

//    static List<String> scanNames(String s) {
//        // only scan names
//        List<String> matchList = new ArrayList<>();
//        Matcher regexMatcher = latinPattern.matcher(s);
//        while (regexMatcher.find()) {
//            matchList.add(regexMatcher.group());
//        }
//        return matchList;
//    }

//    static List<BigDecimal> scanNumbers(String s) {
//        // only scan numbers
//        List<BigDecimal> matchList = new ArrayList<>();
//        Matcher regexMatcher = numberPattern.matcher(s);
//        while (regexMatcher.find()) {
//            matchList.add(new BigDecimal(regexMatcher.group()));
//        }
//        return matchList;
//    }
//
//    static List<Character> scanOperators(String s) {
//        // only scan operator symbols
//        List<Character> matchList = new ArrayList<>();
//        Matcher regexMatcher = operatorPattern.matcher(s);
//        while (regexMatcher.find()) {
//            matchList.add(regexMatcher.group().charAt(0)); // if it's any notation just add at this step
//        }
//        return matchList;
//    }

//    static boolean checkValidityOrAssign(String raw) {
//        List<String> names = scanNames(raw);
//        List<BigDecimal> numbers = scanNumbers(raw);
//        List<Character> assignsOrOperators = scanOperators(raw);
//
//        if(raw.isEmpty()) return false;
//        if (names.isEmpty()) { // if no variables
//            if (!assignsOrOperators.isEmpty()) {
//                for (char operator : assignsOrOperators) {
//                    if (operator == '=') {
////                        err(2); // err if it has an assign pattern
//                        return false;
//                    }
//                }
//            }
//        } else { // if it has variables
//            if (!assignsOrOperators.isEmpty() && assignsOrOperators.get(0) == '=') { // if it has an assign pattern
//                if (names.size() + numbers.size() != 2) {
////                    err(2); // err if it has not equal to 1 assign pattern
//                    return false;
//                } else if (numbers.size() != 0) {
//                    variables.put(names.get(0), numbers.get(0)); // assign number to variable
//                } else {
//                    if (!variables.containsKey(names.get(1))) {
////                        err(3); // err if the second variable is unknown
//                        return false;
//                    }
//                    variables.put(names.get(0), variables.get(names.get(1))); // assign variable to variable
//                }
//                return false;
//            } else {
//                for (String name : names) {
//                    if (!variables.containsKey(name)) {
////                        err(3); // err if it has unknown variables
//                        return false;
//                    }
//                }
//            }
//        }
//        return true; // return true if it is a valid non-assign infix notation
//    }
}
