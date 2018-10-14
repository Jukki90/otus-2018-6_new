package base;

import javax.persistence.*;
import java.util.Objects;


@Table(name = "phones")
@Entity
public class PhoneDataSet extends DataSet {

    public PhoneDataSet() {
    }

    public PhoneDataSet(String number, UserDataSet userDataSet) {
        this.number = number;
        this.userDataSet = userDataSet;
    }

    public PhoneDataSet(String number) {
        this.number = number;
    }

    @Column(name = "number")
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", referencedColumnName = "id", nullable = false)
    private UserDataSet userDataSet;

    public void setUserDataSet(UserDataSet userDataSet) {
        this.userDataSet = userDataSet;
    }


    public UserDataSet getUserDataSet() {
        return userDataSet;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "number='" + number + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneDataSet phone = (PhoneDataSet) o;
        return Objects.equals(id, phone.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
