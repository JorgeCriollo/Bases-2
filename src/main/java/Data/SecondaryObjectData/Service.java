package Data.SecondaryObjectData;

public enum Service {
    power(0), water(1), telephone(2), internet(3);

    int index;

    Service(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        switch (this) {
            case power: return "Electricidad";
            case water: return "Agua";
            case telephone: return "Teléfono";
            case internet: return "Internet";
        }

        return null;
    }
}
