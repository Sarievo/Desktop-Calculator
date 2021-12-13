package calculator;

import javax.swing.*;

public class Calculator extends JFrame {

    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);

        JTextField textField = new JTextField();
        textField.setName("EquationTextField");
        textField.setBounds(90, 20, 120, 20);
        add(textField);

        JButton button = new JButton("Solve");
        button.setName("Solve");
        button.setBounds(115, 150,  70, 20);
        add(button);

        button.addActionListener(l -> {
            String raw = textField.getText();
            if (CalculatorUtil.checkValidityOrAssign(raw)) {
                raw += "=" + CalculatorUtil.calculate(raw);
                textField.setText(raw);
            }
        });
    }
}
