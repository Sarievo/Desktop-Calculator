import calculator.Calculator;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;

import java.util.Random;


public class CalculatorTest extends SwingTest{
    @SwingComponent(name = "Solve")
    private JButtonFixture mSolveButton;

    @SwingComponent(name = "EquationTextField")
    private JTextComponentFixture mTextField;

    public CalculatorTest() {
        super(new Calculator());
    }

    @DynamicTest(feedback = "SolveButton should be enabled")
    CheckResult test1() {
        requireEnabled(mSolveButton);
        return CheckResult.correct();
    }

    @DynamicTest(feedback = "TextField should be enabled")
    CheckResult test2() {
        requireEnabled(mTextField);
        return CheckResult.correct();
    }

    @DynamicTest(feedback = "The result of the calculation your app shows is wrong")
    CheckResult test3() {

        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int a = rand.nextInt(101);
            int b = rand.nextInt(101);
            int result = a + b;

            String equation = a + "+" + b;

            mTextField.setText(equation);
            mSolveButton.click();
            mTextField.requireText(equation + "=" + result);
        }
        return CheckResult.correct();
    }


}
