package Data.SecondaryObjectData;

public class Bill {
    private final String description;
    private final double value;

    public Bill(String description, double value) {
        this.description = description;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public double getValue() {
        return value;
    }
}
