package utilities;

public class Constants {

    //Database Tables
    public static final String STUDENTS = "STUDENTS";
    public static final String COURSES = "COURSES";
    public static final String ENROLLMENTS = "ENROLLMENTS";

    //Database Columns
    public static final String STUDENT_ID = "STUDENT_ID";
    public static final String COURSE_CODE = "COURSE_CODE";
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String SCHOOL_YEAR = "SCHOOL_YEAR";

    //SQL QUERIES
    public static final String INSERT_ROW_ENROLLMENTS = "INSERT INTO %s (%s, %s) VALUES('%s','%s')";
    public static final String INSERT_ROW_STUDENTS = "INSERT INTO %s (%s, %s, %s, %s) VALUES(%s, '%s','%s', %s)";
    public static final String SELECT_ONE_CONDITION = "SELECT * FROM %s WHERE %s=%s";

    //COURSES
    public static final String HISTORY_COURSE_NAME = "History 101";
    public static final String HISTORY_COURSE_INSTRUCTOR = "Mrs. Mel Kirlin";
    public static final String HISTORY_COURSE_CODE = "HST101";
    public static final String MATHEMATICS_COURSE_NAME = "Mathematics 101";
    public static final String MATHEMATICS_COURSE_INSTRUCTOR = "Miss Isaura Willms";
    public static final String MATHEMATICS_COURSE_CODE = "MTH101";
    public static final String ENGLISH_COURSE_NAME = "English 101";
    public static final String ENGLISH_COURSE_INSTRUCTOR = "Miss Pete Zemlak";
    public static final String ENGLISH_COURSE_CODE = "ENG101";
    public static final String CHEMISTRY_COURSE_NAME = "Chemistry 101";
    public static final String CHEMISTRY_COURSE_INSTRUCTOR = "Ms. Buffy Sawayn";
    public static final String CHEMISTRY_COURSE_CODE = "CHM101";
    public static final String COMPUTER_COURSE_NAME = "Computer Science 101";
    public static final String COMPUTER_COURSE_INSTRUCTOR = "Miss Michael Bergstrom";
    public static final String COMPUTER_COURSE_CODE = "CMP101";

}
