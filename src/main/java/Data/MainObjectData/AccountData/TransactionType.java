package Data.MainObjectData.AccountData;

public enum TransactionType {
    deposit(0), withdrawal(1), transference(2), payment(3), reversal(4);

    int index;

    TransactionType(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        switch (this) {
            case deposit: return "Depósito";
            case withdrawal: return "Retiro";
            case transference: return "Transferencia";
            case payment: return "Pago";
            case reversal: return "Reverso";
        }

        return null;
    }

    public static TransactionType valueOf(int index) {
        try {
            return TransactionType.values()[index];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
