package testobjects;

public enum CustomEnum {
    ENUM1("first"),
    ENUM2("second"),
    ENUM3("third"),
    ENUM4("fourth");

    private String name;


    CustomEnum(String name) {
        this.name = name;
    }
}
