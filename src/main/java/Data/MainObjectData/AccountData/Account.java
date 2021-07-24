package Data.MainObjectData.AccountData;

import Data.DB;
import Data.Utils.StringUtils;

import java.util.ArrayList;

public class Account {
    private final long accountNumber;
    private final AccountType type;
    private double balance;
    private AccountRegistry[] registries;

    public Account(long accountNumber, AccountType type, double balance) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.balance = balance;
    }

    public Account(long accountNumber, AccountType type) {
        this(accountNumber, type, -1);
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public AccountType getType() {
        return type;
    }

    public double getBalance() {
        return balance;
    }

    public void transaction(int delta) {
        balance += delta;
    }

    public AccountRegistry[] getRegistries() {
        if (registries == null)
            updateRegistries();

        return registries;
    }

    public void updateRegistries() {
        registries = DB.getRegistries(this);
    }

    @Override
    public String toString() {
        return "No." + accountNumber + " (" + type.toString() + ") - " + StringUtils.formatCurrency(balance);
    }
}
