package Data.SecondaryObjectData;

public class Provider {
    private final String name;
    private final String tableName;
    private final Service service;

    public Provider(String name, String tableName, Service service) {
        this.name = name;
        this.tableName = tableName;
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public String getTableName() {
        return tableName;
    }

    public Service getService() {
        return service;
    }
}
