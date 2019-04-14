/*
    File: InsufficientFunds.java
    Purpose: Simple class that defines the funds exception
    Date: 14 APR 19
 */
class InsufficientFunds extends  Exception {
    public InsufficientFunds(String errMsg) {
        super(errMsg);
    }
}
