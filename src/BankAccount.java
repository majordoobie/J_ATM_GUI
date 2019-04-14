/*
    File: BankAccount.java
    Purpose: Class for managing bank actions. This class is utilized
        by the InterfaceATM2 class
    Date: 14 APR 19
 */
public class BankAccount {

    // attributes
    private float checking;
    private float saving;
    private int serviceCounter;

    // default constructor
    public BankAccount() {
        this.checking = 0f;
        this.saving = 0f;
        this.serviceCounter = 1;
    }

    // get checking amount
    public float getChecking() {
        return this.checking;
    }

    // get saving amount
    public float getSaving() {
        return this.saving;
    }

    // increment serviceCounter
    private void incrementServCount() {
        this.serviceCounter ++;
    }

    // check if the incoming withdraw will cause a charge
    private boolean chargeAccount() {
        if (this.serviceCounter > 4) {
            return true;
        } else {
            return false;
        }
    }

    // check funds
    private boolean checkFunds(String accountType, float amount) {
        // if account to check is checking
        if (accountType.equals("checking")) {
            float result = this.checking - amount;
            if (result >= 0f) {
                return true;
            } else {
                return false;
            }
        // if account to check is savings
        } else {
            float result = this.saving - amount;
            if (result >= 0f) {
                return true;
            } else {
                return false;
            }
        }
    }

    // Method used to withdraw funds from a account
    public void withdrawFunds(String accountType, float amount) throws InsufficientFunds {
        // check if they're about to hit their service charge
        if (chargeAccount()) {
            amount = amount + 1.5f;
        }
        System.out.println(this.serviceCounter);
        if (accountType.equals("checking")) {
            if (!checkFunds("checking", amount)) {
                throw new InsufficientFunds("Insufficient funds");
            } else {
                this.checking = this.checking - amount;
                incrementServCount();
            }
        } else {
            if (!checkFunds("saving", amount)){
                throw new InsufficientFunds("Insufficient funds");
        } else {
                this.saving = this.saving - amount;
                incrementServCount();
            }
        }
    }

    // Method used to deposit funds
    public void depositFunds(String accountType, float amount) {
        if (accountType.equals("checking")) {
            this.checking = this.checking + amount;
        } else {
            this.saving = this.saving + amount;
        }
    }

    // Method used to transfer funds
    public void transferFunds(String accountType, float amount) throws InsufficientFunds {
        if (accountType.equals("checking")) {
            if (!checkFunds("checking", amount)) {
                throw new InsufficientFunds("Insufficient funds");
            } else {
                this.checking = this.checking - amount;
                this.saving = this.saving + amount;
            }
        } else {
            if (!checkFunds("saving", amount)) {
                throw new InsufficientFunds("Insufficient funds");
            } else {
                this.saving = this.saving - amount;
                this.checking = this.checking + amount;
            }
        }
    }
}
