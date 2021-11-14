package utilities;

import SimpleStudentManagementSystem.Course;
import SimpleStudentManagementSystem.Enrollment;
import SimpleStudentManagementSystem.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtils {
    private static Connection connection;//connect with a database
    private static Statement statement;//execute query
    private static ResultSet resultSet;//store response/result set data
    private static ResultSetMetaData resultSetMetaData;

    /**
     * Performs connection with a database.
     * Credentials already pre-set
     * Throws exception in connection failed
     */
    public static void createConnection() {
        String dbUrl = ConfigurationReader.get("db_url");
        String dbUsername = System.getProperty("db_username") != null ? System.getProperty("db_username") : ConfigurationReader.get("db_username");
        String dbPassword = System.getProperty("db_password") != null ? System.getProperty("db_password") : ConfigurationReader.get("db_password");
        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect with a database!");
        }
    }

    /**
     * Performs connection with a database.
     * Connection info is required! Usually, it stored in the configuration.properties
     *
     * @param DB_URL      jdbc:type://host:port/database
     * @param DB_USERNAME like hr
     * @param DB_PASSWORD like hr
     */
    public static void createConnection(String DB_URL, String DB_USERNAME, String DB_PASSWORD) {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Failed to connect with a database!");
        }
    }

    /**
     * Run the sql query provided and return ResultSet object
     * @param query
     * @return ResultSet object  that contains data
     */
    public static ResultSet runQuery(String query){

        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(query); // setting the value of ResultSet object
            resultSetMetaData = resultSet.getMetaData() ;  // setting the value of ResultSetMetaData for reuse
        }catch(SQLException e){
            System.out.println("ERROR OCCURRED WHILE RUNNING QUERY "+ e.getMessage() );
        }

        return resultSet ;

    }

    /**
     * This method is called at the end of DB work, to close all connections.
     */
    public static void destroy() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to close database connection!");
        }
    }

    /**
     * @param query
     * @return returns a single cell value. If the results in multiple rows and/or
     * columns of data, only first column of the first row will be returned.
     * The rest of the data will be ignored
     */
    public static Object getCellValue(String query) {
        return getQueryResultList(query).get(0).get(0);
    }

    /**
     * @param query
     * @return returns a list of Strings which represent a row of data. If the query
     * results in multiple rows and/or columns of data, only first row will
     * be returned. The rest of the data will be ignored
     */
    public static List<Object> getRowList(String query) {
        return getQueryResultList(query).get(0);
    }

    /**
     * @param query
     * @return returns a map which represent a row of data where key is the column
     * name. If the query results in multiple rows and/or columns of data,
     * only first row will be returned. The rest of the data will be ignored
     */
    public static Map<String, Object> getRowMap(String query) {
        return getQueryResultMap(query).get(0);
    }

    /**
     * @param query
     * @return returns query result in a list of lists where outer list represents
     * collection of rows and inner lists represent a single row
     */
    public static List<List<Object>> getQueryResultList(String query) {
        executeQuery(query);
        List<List<Object>> rowList = new ArrayList<>();
        ResultSetMetaData rsmd;
        try {
            rsmd = resultSet.getMetaData();
            while (resultSet.next()) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    row.add(resultSet.getObject(i));
                }
                rowList.add(row);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rowList;
    }

    /**
     * @param query
     * @param column
     * @return list of values of a single column from the result set
     */
    public static List<Object> getColumnData(String query, String column) {
        executeQuery(query);
        List<Object> rowList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                rowList.add(resultSet.getObject(column));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowList;
    }

    /**
     * @param query
     * @return returns query result in a list of maps where the list represents
     * collection of rows and a map represents represent a single row with
     * key being the column name
     */
    public static List<Map<String, Object>> getQueryResultMap(String query) {
        executeQuery(query);
        List<Map<String, Object>> rowList = new ArrayList<>();
        ResultSetMetaData rsmd;
        try {
            rsmd = resultSet.getMetaData();
            while (resultSet.next()) {
                Map<String, Object> colNameValueMap = new HashMap<>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    colNameValueMap.put(rsmd.getColumnName(i), resultSet.getObject(i));
                }
                rowList.add(colNameValueMap);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rowList;
    }

    /**
     * @param query
     * @return List of columns returned in result set
     */
    public static List<String> getColumnNames(String query) {
        executeQuery(query);
        List<String> columns = new ArrayList<>();
        ResultSetMetaData rsmd;
        try {
            rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columns.add(rsmd.getColumnName(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get column names!");
        }
        return columns;
    }

    /**
     * Method to execute query.
     * Should be used only internally
     *
     * @param query to execute
     */
    private static void executeQuery(String query) {
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to execute query!");
        }
    }

    /**
     * /**
     * Executes the given SQL statement, which may be an <code>INSERT</code>,
     * <code>UPDATE</code>, or <code>DELETE</code> statement or an
     * SQL statement that returns nothing, such as an SQL DDL statement.
     *
     * @param query
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     * *      *         or (2) 0 for SQL statements that return nothing
     */
    public static int executeUpdate(String query) {
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to execute update operation!");
        }
    }

    /**
     * This method will reset the cursor to before first location
     */
    private static void resetCursor(){

        try {
            resultSet.beforeFirst();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * find out the row count
     * @return row count of this ResultSet
     */
    public static int getRowCount(){
        int rowCount = 0 ;
        try {
            resultSet.last() ;
            rowCount = resultSet.getRow() ;
        } catch (SQLException e) {
            System.out.println("ERROR OCCURRED WHILE GETTING ROW COUNT " + e.getMessage() );
        }finally {
            resetCursor();
        }

        return rowCount ;
    }

    public static void addStudentsDB(List<Student> students) {
        for (Student student : students) {
            String query = String.format(Constants.INSERT_ROW_STUDENTS, Constants.STUDENTS,
                    Constants.STUDENT_ID, Constants.FIRST_NAME, Constants.LAST_NAME,
                    Constants.SCHOOL_YEAR, student.getStudentID(), student.getFirstName(),
                    student.getLastName(), student.getSchoolYear());
            executeQuery(query);
        }
    }

    public static void enrollStudents(List<Enrollment> enrollments) {
        for (Enrollment enrollment : enrollments) {
            String query = String.format(Constants.INSERT_ROW_ENROLLMENTS,Constants.ENROLLMENTS,
                    Constants.STUDENT_ID, Constants.COURSE_CODE, enrollment.getStudentID(),
                    enrollment.getCourseCode());
            executeQuery(query);
        }
    }

    public static Student getStudentByID(int studentID) {
        Student student = new Student();
        String query = String.format(Constants.SELECT_ONE_CONDITION,Constants.STUDENTS,
                Constants.STUDENT_ID, studentID);
        List<Map<String, Object>> studentInfo = getQueryResultMap(query);
        student.setStudentID(studentID);
        student.setFirstName((String) studentInfo.get(0).get("FIRST_NAME"));
        student.setLastName((String) studentInfo.get(0).get("LAST_NAME"));
        student.setSchoolYear(((Number)studentInfo.get(0).get("SCHOOL_YEAR")).intValue());
        return student;
    }

//    public static Enrollment getEnrollmentByStudentID(int studentID) {
//
//    }
//
//    public static Course getCourseByStudentID(int studentID) {
//
//    }
}