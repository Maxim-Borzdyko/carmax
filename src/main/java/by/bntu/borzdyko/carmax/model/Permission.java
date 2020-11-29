package by.bntu.borzdyko.carmax.model;

public enum Permission {

    CARS_READ("cars.read"), CARS_WRITE("cars.write"), CARS_DELETE("cars.delete"),
    USERS_READ("users.read"), USERS_UPDATE("users.update"), USERS_DELETE("users.delete"), USERS_BAN("users.ban"),
    ORDERS_READ("orders.read"), ORDERS_UPDATE("orders.edit"), ORDERS_ADD("orders.add"), ORDERS_DELETE("orders.delete");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
