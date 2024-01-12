import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.InputMismatchException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class TaxCalculatorGUI extends JFrame {

    private JTextField incomeField;
    private JTextField calculationOptionField;
    private JLabel resultLabel;

    public TaxCalculatorGUI() {
        setTitle("Midnight Coders Income Tax Calculator for 2024");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel incomeLabel = new JLabel("Enter Annual Income (R):");
        incomeField = new JTextField();
        JLabel calculationOptionLabel = new JLabel("Choose option (1 for monthly, 2 for yearly):");
        calculationOptionField = new JTextField();
        JButton calculateButton = new JButton("Calculate");
        resultLabel = new JLabel("");

        // Add components to the panel
        panel.add(incomeLabel);
        panel.add(incomeField);
        panel.add(calculationOptionLabel);
        panel.add(calculationOptionField);
        panel.add(new JLabel()); // Spacer
        panel.add(calculateButton);
        panel.add(new JLabel()); // Spacer
        panel.add(resultLabel);

        // Add ActionListener to the Calculate button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTax();
            }
        });

        add(panel);
    }

    private void calculateTax() {
        try {
            double income = Double.parseDouble(incomeField.getText());
            int calculationOption = Integer.parseInt(calculationOptionField.getText());

            // Calculate UIF deduction
            double uif = calculateUIF(income);

            // Deduct UIF before calculating tax
            double incomeAfterUIF = income - uif;

            // Calculate tax based on the provided rules
            double taxObligation = calculateTax(incomeAfterUIF, calculationOption);

            // Calculate net income (takeaway payment)
            double netIncome = income - taxObligation - uif;

            // Display the result
            resultLabel.setText("<html>UIF Deduction: R" + uif +
                    "<br>Tax Obligation: R" + taxObligation +
                    (calculationOption == 1 ? " per month" : " per year") +
                    "<br>Net Income (Takeaway Payment): R" + netIncome +
                    (calculationOption == 1 ? " per month</html>" : " per year</html>"));

        } catch (NumberFormatException | InputMismatchException ex) {
            resultLabel.setText("Invalid input. Please enter valid numbers.");
        }
    }

    private double calculateTax(double income, int calculationOption) {
        // Tax rates and thresholds for the 2024 tax year
        double[] incomeThresholds = {237100, 370500, 512800, 673000, 857900, 1817000};
        double[] taxRates = {0.18, 0.26, 0.31, 0.36, 0.39, 0.41, 0.45};

        double tax = 0;

        for (int i = 0; i < incomeThresholds.length; i++) {
            if (income <= incomeThresholds[i]) {
                tax = tax + taxRates[i] * income;
                break;
            } else {
                tax = tax + taxRates[i] * (incomeThresholds[i] - ((i == 0) ? 0 : incomeThresholds[i - 1]));
            }
        }

        return (calculationOption == 1) ? tax / 12 : tax;
    }

    private double calculateUIF(double grossIncome) {
        // UIF is deducted at 1% of gross income
        double uifRate = 0.01;
        return grossIncome * uifRate;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TaxCalculatorGUI().setVisible(true);
            }
        });
    }
}
