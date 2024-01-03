package valonnue.javaproject.dao.sql;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import hr.algebra.utilities.MessageUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.sql.DataSource;


public final class DataSourceSingleton {
    private static final String PATH = "/config/db.properties";

    private static final String SERVER_NAME = "SERVER_NAME";
    private static final String DATABASE_NAME = "DATABASE_NAME";
    private static final String USER = "USER";
    private static final String PASSWORD = "PASSWORD";

    private static final Properties properties = new Properties();

    static {
        try (InputStream is = DataSourceSingleton.class.getResourceAsStream(PATH))
        {
            properties.load(is);

        } catch (IOException ex) {
            MessageUtils.showErrorMessage("Error", "Something went extremely wrong, please contact customer support");
        }    
    }

    private DataSourceSingleton() {}

    private static DataSource instance;

    public static DataSource getInstance() {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    private static DataSource createInstance() {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setServerName(properties.getProperty(SERVER_NAME));
        dataSource.setDatabaseName(properties.getProperty(DATABASE_NAME));
        dataSource.setUser(properties.getProperty(USER));
        dataSource.setPassword(properties.getProperty(PASSWORD));
        return dataSource;
    }
}
