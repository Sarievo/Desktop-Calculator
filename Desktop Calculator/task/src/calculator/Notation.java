package calculator;

import java.math.BigInteger;

public class Notation {
    BigInteger value;
    char operator;
    boolean isVariable;

    Notation(BigInteger value) {
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
