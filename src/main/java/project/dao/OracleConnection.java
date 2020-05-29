package project.dao;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.util.*;

@PropertySource("classpath:application.properties")
public class OracleConnection {
    private Context ctx;
    protected Connection connection;
    protected ResultSet resultSet;
    protected PreparedStatement statement;
    private Hashtable<String, String> ht = new Hashtable<>();
    @Value("${contextFactory}")
    private String contextFactor;
    @Value("${url}")
    private String url;
    @Value("${dataSource}")
    private String dataSource;
    private static final Logger LOGGER = Logger.getLogger(OracleConnection.class);

    public void connect() {
        ht.put(Context.INITIAL_CONTEXT_FACTORY, contextFactor);
        ht.put(Context.PROVIDER_URL, url);
        try {
            ctx = new InitialContext(ht);
            DataSource ds = (DataSource) ctx.lookup(dataSource);
            connection = ds.getConnection();
        } catch (NamingException | SQLException e) {
            LOGGER.error("Error in connection to db", e);
        }
    }

    public void disconnect() {
        try {
            connection.close();
            if (resultSet != null) {
                resultSet.close();
            }
            if(statement != null) {
                statement.close();
            }
            ctx.close();
        } catch (Exception e) {
            LOGGER.error("Error in disconnection from db", e);
        }
    }

    public void delete(String table, int id) {
        connect();
        try {
            statement = connection.prepareStatement("DELETE FROM " + table + " WHERE " +
                    table + "ID = ?");
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Error in deleting row in table "  + table, e);
        }
        disconnect();
    }

    @PostConstruct
    public void createFillDB() {
        connect();
        try {
            File creation = ResourceUtils.getFile("classpath:createTables.sql");
            File insert = ResourceUtils.getFile("classpath:insertData.sql");
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.setDelimiter("/");
            scriptRunner.setStopOnError(false);
            scriptRunner.setAutoCommit(true);
            scriptRunner.setSendFullScript(false);
            scriptRunner.runScript(new BufferedReader(new FileReader(creation)));
            FileInputStream fis = new FileInputStream(insert);
            scriptRunner.runScript(new BufferedReader(new InputStreamReader(fis, "UTF-8")));
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            LOGGER.error("Files for creating/inserting data into bd not found", e);
        }
        disconnect();
    }
}
