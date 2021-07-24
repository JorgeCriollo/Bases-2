package Data.MainObjectData.AccountData;

import java.util.Calendar;

public class AccountRegistry {
    private final TransactionType transactionType;
    private final String description;
    private final double previousBalance;
    private final double delta;
    private final double newBalance;
    private final Calendar date;

    public AccountRegistry(TransactionType transactionType, String description, double previousBalance, double delta,
                           double newBalance, Calendar date) {
        this.transactionType = transactionType;
        this.description = description;
        this.previousBalance = previousBalance;
        this.delta = delta;
        this.newBalance = newBalance;
        this.date = date;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getDescription() {
        return description;
    }

    public double getPreviousBalance() {
        return previousBalance;
    }

    public double getDelta() {
        return delta;
    }

    public double getNewBalance() {
        return newBalance;
    }

    public Calendar getDate() {
        return date;
    }
}
