package base;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;


@Table(name = "users")
@Entity
public class UserDataSet extends DataSet {
    @Column(name = "name")
    protected String name;

    @Column(name = "age")
    protected int age;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @OneToMany(mappedBy = "userDataSet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhoneDataSet> phones;

    public List<PhoneDataSet> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDataSet> numbers) {
        this.phones = numbers;
        for (PhoneDataSet phone : phones) {
            phone.setUserDataSet(this);
        }
    }

    private void addPhone(PhoneDataSet number) {
        phones.add(number);
    }


    @OneToOne(cascade = CascadeType.ALL)
    private AddressDataSet address;

    public AddressDataSet getAddress() {
        return address;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }


    public UserDataSet() {
    }

    public UserDataSet(String name, Integer age) {
        super.setId(-1);
        this.name = name;
        this.age = age;
    }

    public UserDataSet(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public UserDataSet(Long id, String name, Integer age, AddressDataSet address, List<PhoneDataSet> phones) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
        this.phones = phones;
        for (PhoneDataSet phone : phones) {
            phone.setUserDataSet(this);
        }
    }

    public UserDataSet(String name, Integer age, AddressDataSet address, List<PhoneDataSet> phones) {
        this.id = -1;
        this.name = name;
        this.age = age;
        this.address = address;
        this.phones = phones;
        for (PhoneDataSet phone : phones) {
            phone.setUserDataSet(this);
        }
    }

    @Override
    public String toString() {
        return "UsersDataSet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", address = '" + address.toString() + ", phones = '" + phones.stream().map(PhoneDataSet::toString).collect(Collectors.joining(", ")) +
                '}';
    }
}
