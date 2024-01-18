import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class TaxCalculatorGUI {
    private JFrame frame;
    private JTextField incomeTextField;
    private JTextField ageTextField;
    private JComboBox<String> yearComboBox;
    private JComboBox<String> frequencyComboBox;
    private JTextArea resultTextArea;
    private JTextField finalAmountTextField;

    private JLabel taxLabel;
    private JLabel rebateLabel;
    private JLabel finalAmountLabel;

    public TaxCalculatorGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Tax Calculator");
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel incomeLabel = new JLabel("Enter your taxable income:");
        incomeTextField = new JTextField();
        JLabel ageLabel = new JLabel("Enter your age:");
        ageTextField = new JTextField();
        JLabel yearLabel = new JLabel("Choose tax year:");
        yearComboBox = new JComboBox<>(new String[]{"2022-2023", "2023-2024"});
        JLabel frequencyLabel = new JLabel("Choose tax calculation frequency:");
        frequencyComboBox = new JComboBox<>(new String[]{"Monthly", "Yearly"});

        inputPanel.add(incomeLabel);
        inputPanel.add(incomeTextField);
        inputPanel.add(ageLabel);
        inputPanel.add(ageTextField);
        inputPanel.add(yearLabel);
        inputPanel.add(yearComboBox);
        inputPanel.add(frequencyLabel);
        inputPanel.add(frequencyComboBox);

        JButton calculateButton = new JButton("Calculate Tax");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTax();
            }
        });

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(calculateButton);
        buttonPanel.add(resultTextArea, BorderLayout.EAST);  // Add result text area to the right of the button

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void calculateTax() {
        try {
            double taxableIncome = Double.parseDouble(incomeTextField.getText());
            int age = Integer.parseInt(ageTextField.getText());
            String year = (String) yearComboBox.getSelectedItem();
            String frequency = (String) frequencyComboBox.getSelectedItem();

            double tax = calculateTax(taxableIncome, age, year, frequency);

            double rebate = getRebateAmount(age, year);
            double finalAmount = Math.max(0, taxableIncome - tax + rebate);

            resultTextArea.setText("Your calculated tax is: " + tax + "\nRebate: " + rebate + "\nFinal Amount: " + finalAmount);
        } catch (NumberFormatException e) {
            resultTextArea.setText("Please enter valid numbers for income and age.");
        }
    }

    private double calculateTax(double taxableIncome, int age, String year, String frequency) {
        double tax = 0;

        if ("Yearly".equals(frequency)) {
            // Apply tax rules for the selected tax year
            if ("2023-2024".equals(year)) {
                tax = calculateTax2023_2024(taxableIncome, age, frequency);
            } else if ("2022-2023".equals(year)) {
                tax = calculateTax2022_2023(taxableIncome, age, frequency);
            }
        } else if ("Monthly".equals(frequency)) {
            // Monthly tax calculation
            if ("2023-2024".equals(year)) {
                tax = calculateTax2023_2024(taxableIncome * 12, age, frequency) / 12;
            } else if ("2022-2023".equals(year)) {
                tax = calculateTax2022_2023(taxableIncome * 12, age, frequency) / 12;
            }
        }

        return tax;
    }

    private double calculateTax2023_2024(double taxableIncome, int age, String calculationFrequency) {
        // Existing tax rules for the year 2023-2024
        double tax = 0;

        try {
            if (age < 65 && taxableIncome < 95750) {
                // No tax for people under 65 with income less than R95,750
                tax = 0;
            } else if (age >= 65 && age < 75 && taxableIncome < 148217) {
                // No tax for people aged 65 to 74 with income less than R148,217
                tax = 0;
            } else if (age >= 75 && taxableIncome < 165689) {
                // No tax for people aged 75 and older with income less than R165,689
                tax = 0;
            } else if (taxableIncome <= 237100) {
                tax = 0.18 * taxableIncome;
            } else if (taxableIncome <= 370500) {
                tax = 42678 + 0.26 * (taxableIncome - 237100);
            } else if (taxableIncome <= 512800) {
                tax = 77362 + 0.31 * (taxableIncome - 370500);
            } else if (taxableIncome <= 673000) {
                tax = 121475 + 0.36 * (taxableIncome - 512800);
            } else if (taxableIncome <= 857900) {
                tax = 179147 + 0.39 * (taxableIncome - 673000);
            } else if (taxableIncome <= 1817000) {
                tax = 251258 + 0.41 * (taxableIncome - 857900);
            } else {
                tax = 644489 + 0.45 * (taxableIncome - 1817000);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Error in tax calculation: " + e.getMessage());
        }
        double rebate = getRebateAmount(age, "2024"); // Change the year accordingly
        rebate = rebate + taxableIncome;
    
        // Ensure tax is not negative
        tax = Math.max(tax, 0);
    
        return tax;
    }


    private double calculateTax2022_2023(double taxableIncome, int age, String calculationFrequency) {
        // New tax rules for the year 2022-2023
        double tax = 0;
    
        try {
            if (age < 65 && taxableIncome < 91250) {
                // No tax for people under 65 with income less than R95,750
                tax = 0;
            } else if (age >= 65 && age < 75 && taxableIncome < 141250) {
                // No tax for people aged 65 to 74 with income less than R148,217
                tax = 0;
            } else if (age >= 75 && taxableIncome < 157900) {
                // No tax for people aged 75 and older with income less than R165,689
                tax = 0;
             }else if (taxableIncome <= 216200) {
                tax = 0.18 * taxableIncome;
            } else if (taxableIncome <= 337800) {
                tax = 38916 + 0.26 * (taxableIncome - 216200);
            } else if (taxableIncome <= 467500) {
                tax = 70532 + 0.31 * (taxableIncome - 337800);
            } else if (taxableIncome <= 613600) {
                tax = 110739 + 0.36 * (taxableIncome - 467500);
            } else if (taxableIncome <= 782200) {
                tax = 163335 + 0.39 * (taxableIncome - 613600);
            } else if (taxableIncome <= 1656600) {
                tax = 229089 + 0.41 * (taxableIncome - 782200);
            } else if (taxableIncome > 1656600) {
                tax = 587593 + 0.45 * (taxableIncome - 1656600);
            } else {
                // Handle other age categories
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in tax calculation: " + e.getMessage());
        }
        double rebate = getRebateAmount(age, "2023"); // Change the year accordingly
         tax -=rebate;
    
        // Ensure tax is not negative
        tax = Math.max(tax, 0);
    
        return tax;
    }
    
    private double getRebateAmount(int age, String year) {
        // Return the rebate amount based on age and year
        if (age < 65) {
            return ("2024".equals(year)) ? 17235 : ("2023".equals(year) ? 16425 : 15714);
        } else if (age < 75) {
            return ("2024".equals(year)) ? 9444 : ("2023".equals(year) ? 9000 : 8613);
        } else {
            return ("2024".equals(year)) ? 3145 : ("2023".equals(year) ? 2997 : 2871);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TaxCalculatorGUI();
            }
        });
    }
}