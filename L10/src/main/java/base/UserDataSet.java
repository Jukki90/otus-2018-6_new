package base;

public class UserDataSet extends DataSet {
    protected String name;
    protected int age;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }


    public UserDataSet(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public UserDataSet(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    private UserDataSet() {
    }

    @Override
    public String toString() {
        return "UsersDataSet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
