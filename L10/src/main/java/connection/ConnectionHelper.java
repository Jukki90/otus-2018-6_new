package connection;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

class ConnectionHelper {

    static Connection getConnection() {
        try {
            Driver driver = (Driver) Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
            DriverManager.registerDriver(driver);

            String url = "jdbc:mysql://" +       //db type
                    "localhost:" +               //host name
                    "3306/" +                    //port
                    "hw10?" +               //db name
                    "user=user&" +              //login
                    "password=qwerty&" +          //password
                    "useSSL=false&" +            //do not use Secure Sockets Layer
                    "serverTimezone=UTC";


            return DriverManager.getConnection(url);
        } catch (SQLException |
                InstantiationException |
                InvocationTargetException |
                NoSuchMethodException |
                IllegalAccessException |
                ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
