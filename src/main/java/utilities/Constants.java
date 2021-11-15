package utilities;

public class Constants {

    //Database Tables
    public static final String STUDENTS = "STUDENTS";
    public static final String COURSES = "COURSES";
    public static final String ENROLLMENTS = "ENROLLMENTS";

    //Database Columns
    public static final String STUDENT_ID = "STUDENT_ID";
    public static final String COURSE_CODE = "COURSE_CODE";
    public static final String COURSE_NAME = "COURSE_NAME";
    public static final String COURSE_INSTRUCTOR = "COURSE_INSTRUCTOR";
    public static final String COURSE_DURATION = "COURSE_DURATION";
    public static final String COURSE_PRICE = "COURSE_PRICE";
    public static final String ENROLLMENT_COUNT = "ENROLLMENT_COUNT";
    public static final String ENROLLMENT_LIMIT = "ENROLLMENT_LIMIT";
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String SCHOOL_YEAR = "SCHOOL_YEAR";

    //SQL QUERIES
    public static final String INSERT_ROW_ENROLLMENTS = "INSERT INTO %s (%s, %s) VALUES('%s','%s')";
    public static final String INSERT_ROW_STUDENTS = "INSERT INTO %s (%s, %s, %s, %s) VALUES(%s, '%s','%s', %s)";
    public static final String SELECT_ONE_NUMERIC_CONDITION = "SELECT * FROM %s WHERE %s=%s";
    public static final String ENROLLMENTS_JOIN_COURSES = "SELECT * FROM %s join %s on %s.%s = %s.%s";
    public static final String SELECT_SINGLE_COLUMN = "SELECT %s FROM %s";
    public static final String SELECT_ENROLLMENTS = "SELECT * FROM %s WHERE %s=%s AND %s='%s'";
    public static final String SELECT_ALL = "SELECT * FROM %s";
    public static final String DELETE_ENROLLMENT = "DELETE FROM %s WHERE %s=%s AND %s='%s'";
    public static final String SELECT_SINGLE_STRING_VALUE = "SELECT %s FROM %s WHERE %S='%S'";
    public static final String UPDATE_ENROLLMENT_COUNT = "UPDATE %s SET %s=%s WHERE %s='%s'";

}
