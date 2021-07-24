package Data.MainObjectData.AccountData;

public enum AccountType {
    savings(0), checking(1);

    int index;

    AccountType(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        switch (this) {
            case savings: return "Cuenta de ahorros";
            case checking: return "Cuenta corriente";
        }

        return null;
    }
}
