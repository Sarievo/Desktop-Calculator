package calculator;

import javax.swing.*;
import java.awt.*;

public class Calculator extends JFrame {

    JButton[] buttons = new JButton[18];
    JLabel equationLabel;
    JLabel resultLabel;

    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
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
        equationLabel.setForeground(new Color(165, 0, 165));
    }

    void initButtons() {
        int width = 60;
        int x_pad = 10;

        int y_from = 100;
        int height = 35;
        int y_pad = 10;

        int y_U = -1;
        for (int i = 0; i < 18; i++) {
            if (i % 4 == 0) {
                int y_index = i / 4;
                y_U = 400 - (y_from + y_pad) - y_index * (height + y_pad);
                int y_D = y_U - height;
//                y_U = (y_from + y_pad) + y_index * (height + y_pad);
//                int y_D = y_U + height;
                System.out.printf("\ny%2d|", y_index);
                System.out.printf("%3d<->%3d ||", y_U, y_D);
            }
            int x_index = i % 4;
            System.out.printf("[x%d.%2d=", x_index, i);
            int x_R = (300 - x_pad) - x_index * (width + x_pad);
            int x_L = x_R - (width + 13); // strange magic number
//            int x_L = x_pad + x_index * (width + x_pad) - 3;
//            int x_R = x_L + width;
            System.out.printf("%3d<->%3d] ", x_L, x_R);

            buttons[i] = new JButton();
            buttons[i].setBackground(Color.white);
            buttons[i].setBounds(x_L, y_U, width, height);
            add(buttons[i]);
        }

        buttons[0].setName("Subtract");
        buttons[0].setText("\u2212");
        buttons[1].setName("Equals");
        buttons[1].setText("=");
        buttons[2].setName("Zero");
        buttons[2].setText("0");
        buttons[3].setName("Dot");
        buttons[3].setText(".");

        buttons[0].addActionListener(l -> equationLabel.setText(equationLabel.getText() + "-"));
        buttons[1].addActionListener(l -> resultLabel.setText(CalculatorUtil.calculate(equationLabel.getText())));
        buttons[2].addActionListener(l -> equationLabel.setText(equationLabel.getText() + "0"));
        buttons[3].addActionListener(l -> equationLabel.setText(equationLabel.getText() + "."));

        buttons[4].setName("Add");
        buttons[4].setText("\u002B");
        buttons[5].setName("Three");
        buttons[5].setText("3");
        buttons[6].setName("Two");
        buttons[6].setText("2");
        buttons[7].setName("One");
        buttons[7].setText("1");

        buttons[4].addActionListener(l -> equationLabel.setText(equationLabel.getText() + "\u002B"));
        buttons[5].addActionListener(l -> equationLabel.setText(equationLabel.getText() + "3"));
        buttons[6].addActionListener(l -> equationLabel.setText(equationLabel.getText() + "2"));
        buttons[7].addActionListener(l -> equationLabel.setText(equationLabel.getText() + "1"));

        buttons[8].setName("Multiply");
        buttons[8].setText("\u00D7");
        buttons[9].setName("Six");
        buttons[9].setText("6");
        buttons[10].setName("Five");
        buttons[10].setText("5");
        buttons[11].setName("Four");
        buttons[11].setText("4");

        buttons[8].addActionListener(l -> equationLabel.setText(equationLabel.getText() + "\u00D7"));
        buttons[9].addActionListener(l -> equationLabel.setText(equationLabel.getText() + "6"));
        buttons[10].addActionListener(l -> equationLabel.setText(equationLabel.getText() + "5"));
        buttons[11].addActionListener(l -> equationLabel.setText(equationLabel.getText() + "4"));

        buttons[12].setName("Divide");
        buttons[12].setText("\u00F7");
        buttons[13].setName("Nine");
        buttons[13].setText("9");
        buttons[14].setName("Eight");
        buttons[14].setText("8");
        buttons[15].setName("Seven");
        buttons[15].setText("7");

        buttons[12].addActionListener(l -> equationLabel.setText(equationLabel.getText() + "\u00F7"));
        buttons[13].addActionListener(l -> equationLabel.setText(equationLabel.getText() + "9"));
        buttons[14].addActionListener(l -> equationLabel.setText(equationLabel.getText() + "8"));
        buttons[15].addActionListener(l -> equationLabel.setText(equationLabel.getText() + "7"));

        buttons[16].setName("Delete");
        buttons[16].setText("Del");
        buttons[17].setName("Clear");
        buttons[17].setText("C");

        buttons[16].addActionListener(l -> {
            String data = equationLabel.getText();
            equationLabel.setText(data.substring(0, data.length() - 1));
        });
        buttons[17].addActionListener(l -> {
            equationLabel.setText("");
            resultLabel.setText("0");
        });
    }
}
