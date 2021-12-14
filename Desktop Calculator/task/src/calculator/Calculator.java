package calculator;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class Calculator extends JFrame {

    JButton[] buttons = new JButton[24];
    JLabel equationLabel;
    JLabel resultLabel;

    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 450);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
        setResizable(false); // :D

        initLabels();
        initButtons();
        CalculatorUtil.initPrecedence();
    }

    void initLabels() {
        resultLabel = new JLabel();
        resultLabel.setName("ResultLabel");
        resultLabel.setBounds(80, 20, 200, 30);
        resultLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        resultLabel.setText("0");
        add(resultLabel);

        Font font = new Font("Courier", Font.BOLD, 30);
        resultLabel.setFont(font);

        equationLabel = new JLabel();
        equationLabel.setName("EquationLabel");
        equationLabel.setBounds(80, 55, 200, 40);
        equationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(equationLabel);

        equationLabel.setFont(font.deriveFont(15f));
//        equationLabel.setForeground(Color.MAGENTA);
    }

    void initButtons() {
        int width = 66;
        int x_pad = 3;

        int y_from = 75;
        int height = 37;
        int y_pad = 3;

        int y_U = -1;
        Stack<String> x_format = new Stack<>();
        Stack<String> y_format = new Stack<>();
        for (int i = 0; i < 24; i++) {
            if (i % 4 == 0) {
                int y_index = i / 4;
                y_U = 400 - (y_from + y_pad) - y_index * (height + y_pad);
            }
            int x_index = i % 4;
            int x_R = (300 - x_pad) - x_index * (width + x_pad);
            int x_L = x_R - (width + 18); // strange magic number

            buttons[i] = new JButton();
            buttons[i].setBackground(Color.white);
            buttons[i].setBounds(x_L, y_U, width, height);
            add(buttons[i]);

            x_format.push(String.format("[x%d.%2d=%3d<->%3d] ", x_index, i, x_L, x_R));
            if ((i + 1) % 4 == 0) {
                int y_index = i / 4;
                y_U = 400 - (y_from + y_pad) - y_index * (height + y_pad);
                int y_D = y_U - height;
                y_format.push(String.format("|| y%2d|%3d<->%3d\n", y_index, y_U, y_D));
                while (!x_format.isEmpty()) { y_format.push(x_format.pop()); }
            }
            buttons[i].setBounds(x_L, y_U, width, height);
        }
        while (!y_format.isEmpty()) { System.out.print(y_format.pop()); }

        buttons[0].setName("Equals");
        buttons[0].setText("=");
        buttons[1].setName("Dot");
        buttons[1].setText(".");
        buttons[2].setName("Zero");
        buttons[2].setText("0");
        buttons[3].setName("PlusMinus");
        buttons[3].setText("\u00B1");

        buttons[0].addActionListener(l -> CalculatorUtil.evaluate(equationLabel, resultLabel));
        buttons[1].addActionListener(l -> CalculatorUtil.assignDot(equationLabel));
        buttons[2].addActionListener(l -> CalculatorUtil.assignNumber(equationLabel, 0));
        buttons[3].addActionListener(l -> CalculatorUtil.negate(equationLabel)); // TODO

        buttons[4].setName("Add");
        buttons[4].setText("\u002B");
        buttons[5].setName("Three");
        buttons[5].setText("3");
        buttons[6].setName("Two");
        buttons[6].setText("2");
        buttons[7].setName("One");
        buttons[7].setText("1");

        buttons[4].addActionListener(l -> CalculatorUtil.assignAdd(equationLabel));
        buttons[5].addActionListener(l -> CalculatorUtil.assignNumber(equationLabel, 3));
        buttons[6].addActionListener(l -> CalculatorUtil.assignNumber(equationLabel, 2));
        buttons[7].addActionListener(l -> CalculatorUtil.assignNumber(equationLabel, 1));

        buttons[8].setName("Subtract");
        buttons[8].setText("\u2212");
        buttons[9].setName("Six");
        buttons[9].setText("6");
        buttons[10].setName("Five");
        buttons[10].setText("5");
        buttons[11].setName("Four");
        buttons[11].setText("4");

        buttons[8].addActionListener(l -> CalculatorUtil.assignSubtract(equationLabel));
        buttons[9].addActionListener(l -> CalculatorUtil.assignNumber(equationLabel, 6));
        buttons[10].addActionListener(l -> CalculatorUtil.assignNumber(equationLabel, 5));
        buttons[11].addActionListener(l -> CalculatorUtil.assignNumber(equationLabel, 4));

        buttons[12].setName("Multiply");
        buttons[12].setText("\u00D7");
        buttons[13].setName("Nine");
        buttons[13].setText("9");
        buttons[14].setName("Eight");
        buttons[14].setText("8");
        buttons[15].setName("Seven");
        buttons[15].setText("7");

        buttons[12].addActionListener(l -> CalculatorUtil.assignMultiply(equationLabel));
        buttons[13].addActionListener(l -> CalculatorUtil.assignNumber(equationLabel, 9));
        buttons[14].addActionListener(l -> CalculatorUtil.assignNumber(equationLabel, 8));
        buttons[15].addActionListener(l -> CalculatorUtil.assignNumber(equationLabel, 7));

        buttons[16].setName("Divide");
        buttons[16].setText("\u00F7");
        buttons[17].setName("SquareRoot");
        buttons[17].setText("\u221A");
        buttons[18].setName("PowerY");
        buttons[18].setText("x\u02B8");
        buttons[18].setFont(getFont().deriveFont(Font.ITALIC));
        buttons[19].setName("PowerTwo");
        buttons[19].setText("x\u00B2");
        buttons[19].setFont(getFont().deriveFont(Font.ITALIC));

        buttons[16].addActionListener(l -> CalculatorUtil.assignDivide(equationLabel));
        buttons[17].addActionListener(l -> CalculatorUtil.insertSqrt(equationLabel));
        buttons[18].addActionListener(l -> CalculatorUtil.raiseToY(equationLabel)); // TODO
        buttons[19].addActionListener(l -> CalculatorUtil.raiseTo2(equationLabel)); // TODO

        buttons[20].setName("Delete");
        buttons[20].setText("Del");
        buttons[21].setName("Clear");
        buttons[21].setText("C");
        buttons[22].setName("ClearRecent"); // ???
        buttons[22].setText("CE");
        buttons[23].setName("Parentheses");
        buttons[23].setText("( )");

        buttons[20].addActionListener(l -> CalculatorUtil.delete(equationLabel));
        buttons[21].addActionListener(l -> {
            equationLabel.setForeground(Color.MAGENTA.darker());
            equationLabel.setText("");
            resultLabel.setFont(resultLabel.getFont().deriveFont(30f));
            resultLabel.setText("0");
        });
        buttons[22].addActionListener(l -> {
            // it currently does nothing yet
            equationLabel.setForeground(Color.MAGENTA.darker());
            equationLabel.setText("");
            resultLabel.setFont(resultLabel.getFont().deriveFont(30f));
            resultLabel.setText("0");
        });
        buttons[23].addActionListener(l -> CalculatorUtil.insertParentheses(equationLabel)); // TODO
    }
}
