import calculator.Calculator;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JLabelFixture;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;

import java.awt.*;
import java.util.Map;

import static java.util.Map.entry;
import static org.hyperskill.hstest.testcase.CheckResult.correct;

public class CalculatorTest extends SwingTest {

    private Map<Character, JButtonFixture> charToButtonMap;

    @SwingComponent(name = "Equals")
    private JButtonFixture mEqual;
    @SwingComponent(name = "Add")
    private JButtonFixture mAdd;
    @SwingComponent(name = "Subtract")
    private JButtonFixture mSub;
    @SwingComponent(name = "Divide")
    private JButtonFixture mDiv;
    @SwingComponent(name = "Multiply")
    private JButtonFixture mMult;
    @SwingComponent(name = "Zero")
    private JButtonFixture mZero;
    @SwingComponent(name = "One")
    private JButtonFixture mOne;
    @SwingComponent(name = "Two")
    private JButtonFixture mTwo;
    @SwingComponent(name = "Three")
    private JButtonFixture mThree;
    @SwingComponent(name = "Four")
    private JButtonFixture mFour;
    @SwingComponent(name = "Five")
    private JButtonFixture mFive;
    @SwingComponent(name = "Six")
    private JButtonFixture mSix;
    @SwingComponent(name = "Seven")
    private JButtonFixture mSeven;
    @SwingComponent(name = "Eight")
    private JButtonFixture mEight;
    @SwingComponent(name = "Nine")
    private JButtonFixture mNine;
    @SwingComponent(name = "Dot")
    private JButtonFixture mDot;
    @SwingComponent(name = "Clear")
    private JButtonFixture mClear;
    @SwingComponent(name = "Delete")
    private JButtonFixture mDel;

    @SwingComponent(name = "EquationLabel")
    private JLabelFixture mEquationLabel;
    @SwingComponent(name = "ResultLabel")
    private JLabelFixture mResultLabel;

    private final String divideSymbol = "\u00F7";
    private final String multiplySymbol = "\u00D7";
    private final String addSymbol = "\u002B";
    private final String subtractSymbol = "-";

    public CalculatorTest() {

        super(new Calculator());
    }

    private void typeText (String text, String expectedResult, boolean checkResult) {

        for (int i = 0; i < text.length(); i++) {
            JButtonFixture button = charToButtonMap.get(text.charAt(i));
            button.click();
        }
        if (checkResult) {
            if (!mResultLabel.text().trim().equals(expectedResult)) {
                throw new WrongAnswer("Result Label contains wrong number.\n" +
                    "    Your output: " + mResultLabel.text().trim() +
                    "\nExpected output: " + expectedResult +
                    "\nEquation: " + mEquationLabel.text().trim());
            }
        } else {
            if (!mEquationLabel.text().trim().equals(expectedResult)) {
                throw new WrongAnswer("Equation Label contains wrong values.\n" +
                    "    Your output: " + mEquationLabel.text()
                    .trim() + "\n" +
                    "Expected output: " + expectedResult);
            }
        }

        mClear.click();
    }

    private void typeText (String text, String expectedResult, boolean checkResult,
                           String feedBack) {

        for (int i = 0; i < text.length(); i++) {
            JButtonFixture button = charToButtonMap.get(text.charAt(i));
            button.click();
        }
        if (checkResult) {
            if (!mResultLabel.text().trim().equals(expectedResult)) {
                throw new WrongAnswer(feedBack + "\n" + "Your output: " + mResultLabel.text() +
                    "\nExpected output: " + expectedResult +
                    "\nEquation: " + mEquationLabel.text().trim());
            }
        } else {
            if (!mEquationLabel.text().trim().equals(expectedResult)) {
                throw new WrongAnswer(feedBack + "\n" + "Your output: " + mEquationLabel.text() +
                    "\nExpected output: " + expectedResult);
            }
        }

        mClear.click();
    }

    private void typeText (String text) {

        for (int i = 0; i < text.length(); i++) {
            JButtonFixture button = charToButtonMap.get(text.charAt(i));
            button.click();
        }

        mEquationLabel.foreground().requireEqualTo(Color.RED.darker());
        mClear.click();
    }

    @DynamicTest
    CheckResult test1 () {

        charToButtonMap = Map.ofEntries(
            entry('0', mZero),
            entry('1', mOne),
            entry('2', mTwo),
            entry('3', mThree),
            entry('4', mFour),
            entry('5', mFive),
            entry('6', mSix),
            entry('7', mSeven),
            entry('8', mEight),
            entry('9', mNine),
            entry('+', mAdd),
            entry('-', mSub),
            entry('*', mMult),
            entry('/', mDiv),
            entry('=', mEqual),
            entry('.', mDot),
            entry('<', mDel),
            entry('C', mClear)

        );

        requireEnabled(mEqual, mAdd, mSub, mDiv, mMult, mOne, mTwo, mThree, mFour, mFive, mSix,
            mSeven, mEight, mNine, mZero, mDot, mClear, mDel, mEquationLabel,
            mResultLabel);

        requireVisible(mEqual, mAdd, mSub, mDiv, mMult, mOne, mTwo, mThree, mFour, mFive, mSix,
            mSeven, mEight, mNine, mZero, mDot, mClear, mDel, mEquationLabel,
            mResultLabel);

        return correct();
    }

    @DynamicTest()
    CheckResult test2 () {

        typeText("1", "1", false);
        typeText("1<", "", false,
            "Clicking on the Delete Button should delete the last character from the EquationLabel");
        typeText("111C", "", false,
            "Clicking on the Clear Button should delete all the characters from the EquationLabel");

        return correct();
    }

    // Pushing buttons
    @DynamicTest()
    CheckResult test3 () {

        typeText("1", "1", false);
        typeText("2", "2", false);
        typeText("3", "3", false);
        typeText("4", "4", false);
        typeText("5", "5", false);
        typeText("6", "6", false);
        typeText("7", "7", false);
        typeText("8", "8", false);
        typeText("9", "9", false);
        typeText("0", "0", false);
        typeText("1+", "1".concat(addSymbol), false);
        typeText("1-", "1".concat(subtractSymbol), false);
        typeText("1*", "1".concat(multiplySymbol), false);
        typeText("1/", "1".concat(divideSymbol), false);
        typeText(".", ".", false);
        mClear.click();

        return correct();
    }

    //Testing calculations
    @DynamicTest()
    CheckResult test4 () {

        typeText("9+1=", "10", true);
        typeText("1-99=", "-98", true);
        typeText("9/2=", "4.5", true);
        typeText("0/7=", "0", true);
        typeText("4.5*2=", "9", true);

        return correct();
    }

    //Test operator precedence
    @DynamicTest()
    CheckResult test5 () {
        //Add & Subtract
        typeText("11-5+4=", "10", true);
        typeText("2-17+5=", "-10", true);


        //Multiply & Divide
        typeText("9/2*8=", "36", true);

        //Combined
        typeText("2+3-5*6=", "-25", true);
        typeText("16+9-7/5=", "23.6", true);
        typeText("25+9/3-8*8=", "-36", true);
        typeText("3.8*7.5/2.5*5=", "57", true);
        typeText("9.2/2.3*12/2.4=", "20", true);

        return correct();
    }

    //test formatting equations
    @DynamicTest
    CheckResult test6 () {

        typeText(".6+", "0.6".concat(addSymbol), false,
            "Your program should properly format the equation whenever an operator is inserted.");

        typeText("7.*", "7.0".concat(multiplySymbol), false,
            "Your program should properly format the equation whenever an operator is inserted.");

        typeText("*", "", false, "Equations should not start with an operator");
        typeText("+", "", false, "Equations should not start with an operator");
        typeText("-", "", false, "Equations should not start with an operator");
        typeText("/", "", false, "Equations should not start with an operator");

        typeText("2+*", "2".concat(multiplySymbol), false,
            "Clicking on an operator should override the preceding operator");

        typeText("6+/3=", "2", true);

        return correct();
    }

    //test equation validation
    @DynamicTest(feedback = "The color of EquationLabel should change to indicate invalid equations" +
        " when the EqualButton is clicked")
    CheckResult test7 () {

        typeText("2+=");
        typeText("5/0=");

        return correct();
    }

}