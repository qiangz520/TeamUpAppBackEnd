import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    // table

    public static final String TABLE_USERINFO = "table_user_info";  //用户信息表
    public static final String TABLE_ACTIVITY_INFO ="table_campus_activity_all"; //活动信息表
    public static final String JDBC_MYSQL_LOCALHOST_3306_TEAMUP_USE_SSL_FALSE = "jdbc:mysql://localhost:3306/teamup?useSSL=false&serverTimezone=UTC";

    // connect to MySql database
    public static Connection getConnection() {
        String url = JDBC_MYSQL_LOCALHOST_3306_TEAMUP_USE_SSL_FALSE; // 数据库的Url
        Connection connecter = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // java反射，固定写法
            connecter = DriverManager.getConnection(url, "root", "18031415zq");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return connecter;
    }
}