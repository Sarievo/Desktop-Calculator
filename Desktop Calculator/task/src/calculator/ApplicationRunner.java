package calculator;

import javax.swing.*;

public class ApplicationRunner {
    public static void main(String[] args) {
        Runnable initFrame = Calculator::new;

        try {
            SwingUtilities.invokeAndWait(initFrame);
        } catch (Exception ignored) {}
    }
}
