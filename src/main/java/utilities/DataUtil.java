package utilities;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;

import static utilities.DBUtils.*;

public class DataUtil {

    private static final Faker faker = new Faker();

    public static String randomNumber(int digits) {
        return faker.number().digits(digits);
    }

    public static String randomFullName() {
        return faker.name().fullName();
    }

    public static int generateUniqueStudentID(int schoolYear) {
        String studentID = "";
        boolean unique = false;
        do {
            studentID = schoolYear + randomNumber(4);
            String query = String.format(Constants.SELECT_SINGLE_COLUMN, Constants.STUDENT_ID,
                    Constants.STUDENTS);
            List<Integer> studentIDs = objectToIntList(getColumnData(query, Constants.STUDENT_ID));
            if (!studentIDs.contains(Integer.parseInt(studentID))) {
                unique = true;
            }
        } while (!unique);
        return Integer.parseInt(studentID);
    }

    public static List<Integer> objectToIntList(List<Object> objects) {
        List<Integer> strings = new ArrayList<>();
        for (Object object : objects) {
            strings.add(((Number) object).intValue());
        }
        return strings;
    }

    public static List<String> objectToStringList(List<Object> objects) {
        List<String> strings = new ArrayList<>();
        for (Object object : objects) {
            strings.add((String) object);
        }
        return strings;
    }

}
