import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection1 {

    public java.sql.Connection connect() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");

        java.sql.Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost/db_emp", "root","");
        return con1;

    }


}
