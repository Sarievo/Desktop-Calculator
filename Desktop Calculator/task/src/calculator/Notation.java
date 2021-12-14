package calculator;

import java.math.BigDecimal;

public class Notation {
    BigDecimal value;
    char operator;
    boolean isVariable;

    Notation(BigDecimal value) {
        this.value = value;
        isVariable = true;
    }

    Notation(char operator) {
        this.operator = operator;
        isVariable = false;
    }

    @Override
    public String toString() {
        return isVariable ? String.valueOf(value) : String.valueOf(operator);
    }
}
