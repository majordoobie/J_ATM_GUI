/*
    File: InterfaceATM2.java
    Purpose: Class is used to instantiate the GUI using GroupLayout
    Date: 14 APR 19
 */
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.GroupLayout.Alignment.*;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class InterfaceATM2 extends JFrame {

    // create the button objects
    private JButton buttonWithdraw = new JButton("Withdraw");
    private JButton buttonDeposit = new JButton("Deposit");
    private JButton buttonTransfer = new JButton("Transfer to");
    private JButton buttonBalance = new JButton("Balance");

    // create the radio buttons then group them
    private ButtonGroup radioGroup = new ButtonGroup();
    private JRadioButton radioChecking = new JRadioButton("Checking", true);
    private JRadioButton radioSaving = new JRadioButton("Savings", false);

    // label
    private JTextField labelInput = new JTextField();

    // bank object
    private BankAccount bankAccount = new BankAccount();

    //  The only constructor
    public InterfaceATM2() {
        // Call the method that creates the button listeners
        this.addActions();

        // add items to frame
        this.addButtons();

        // modify the main frame
        this.setSize(300, 160);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle("ATM Machine");
        this.setVisible(true);
    }

    // Method used to add all the listeners to the buttons
    private void addActions()  {
        buttonWithdraw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!inputIsFloat()) {
                    String msg = "Input must be a float!";
                    displayError(msg);
                } else if ((Float.parseFloat(labelInput.getText()) %20f) != 0.0) {
                    String msg = "You can only withdraw in increments of $20";
                    displayError(msg);
                } else {
                    try {
                        bankAccount.withdrawFunds(radioSelected(), Float.parseFloat(labelInput.getText()));
                        displaySuccess(String.format(
                                "Successfully withdrawn $%.2f from %s",
                                Float.parseFloat(labelInput.getText()),
                                radioSelected()));
                    } catch (InsufficientFunds err) {
                        displayError((err.getMessage()));
                    }
                }
            }
        });
        buttonDeposit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!inputIsFloat()) {
                    String msg = "Input must be a float!";
                    displayError(msg);
                } else {
                    bankAccount.depositFunds(radioSelected(),
                            Float.parseFloat(labelInput.getText()));
                    displaySuccess(String.format(
                            "Successfully deposited $%.2f to %s",
                            Float.parseFloat(labelInput.getText()),
                            radioSelected()
                    ));
                }
            }
        });
        buttonTransfer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!inputIsFloat()) {
                    String msg = "Input must be a float!";
                    displayError(msg);
                } else {
                    try {
                        bankAccount.transferFunds(radioSelected(), Float.parseFloat(labelInput.getText()));
                        displaySuccess(String.format(
                                "Successfully transferred $%.2f from %s to %s",
                                Float.parseFloat(labelInput.getText()),
                                radioSelected(),
                                oppositeSelected()
                        ));
                    } catch (InsufficientFunds err) {
                        displayError((err.getMessage()));
                    }
                }
            }
        });
        buttonBalance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (radioSelected().equals("checking")) {
                    String msg = String.format("Checking Balance: $%.2f",bankAccount.getChecking());
                    displayBalance(msg);
                } else {
                    String msg = String.format("Savings Balance: $%.2f", bankAccount.getSaving());
                    displayBalance(msg);
                }
            }
        });
    }


    @SuppressWarnings("Duplicates") // duplicate code needed
    // method used to add buttons to the main panel using GroupLayout
    private void addButtons() {
        // create a panel
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);

        // configure panel
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // set radio group
        radioGroup.add(radioChecking);
        radioGroup.add(radioSaving);

        // set horizontal
        layout.setHorizontalGroup(layout.createSequentialGroup()
                // Create one parallel since it all flows downwards
                // This parallel group will have 4 "sandwich" layers
                // each layer will have "2 columns" per say and that is controlled
                // using a parallel + sequential + parallel
            .addGroup(layout.createParallelGroup(CENTER)
                    // add top layer 1/4
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(buttonWithdraw))
                    .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(buttonDeposit))
                    )
                    // add second layer under top 2/4
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(buttonTransfer))
                    .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(buttonBalance))
                    )
                    // add third layer under second 3/4
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(CENTER)
                        .addComponent(radioChecking))
                    .addGroup(layout.createParallelGroup(CENTER)
                        .addComponent(radioSaving))
                    )
                    // last layer needs to span two "columns"
                    // so we don't use the parallel alignment here
                .addGroup(layout.createSequentialGroup()
                    .addComponent(labelInput))));

        // force all buttons to be the same size
        layout.linkSize(SwingConstants.HORIZONTAL, buttonWithdraw, buttonDeposit, buttonTransfer, buttonBalance);

        // The vertical stack is super easy it's all straight forward
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(BASELINE)
                .addComponent(buttonWithdraw)
                .addComponent(buttonDeposit))
            .addGroup(layout.createParallelGroup(LEADING)
                .addComponent(buttonTransfer)
                .addComponent(buttonBalance))
            .addGroup(layout.createParallelGroup(CENTER)
                .addComponent(radioChecking)
                .addComponent(radioSaving))
            .addGroup(layout.createParallelGroup(CENTER)
                .addComponent(labelInput)));

        // Pack the group layouts together using auto alignment logic
        pack();

    }
    // simple method to extract the radio selected
    private String radioSelected() {
        if (radioChecking.isSelected()) {
            return "checking";
        } else {
            return "saving";
        }
    }

    // method to get the opposite radio selected
    private String oppositeSelected() {
        if (radioChecking.isSelected()) {
            return "saving";
        } else {
            return "checking";
        }
    }
    // simple method to check that the text is a float
    private boolean inputIsFloat() {
        // check if the input is nothing
        if (labelInput.getText().equals("")) {
            return false;
        }
        try {
            Float f = Float.parseFloat(labelInput.getText());
        } catch (NumberFormatException err) {
            return false;
        }
        return true;
    }

    // JOptionsPanes methods Error, Balance and Success
    private void displayError(String msg) {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, msg, "Error", ERROR_MESSAGE);
    }

    private void displayBalance(String msg) {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, msg, radioSelected().toUpperCase(), INFORMATION_MESSAGE);
    }

    private void displaySuccess(String msg) {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                msg, "Successful Operation", INFORMATION_MESSAGE);
    }

    // Main to start GUI. Object is never saved since we
    // are not interacting with it through code but through the
    // GUI
    static public void main(String[] args) {
        // main method that instanciates the GUI
        new InterfaceATM2();
    }
}