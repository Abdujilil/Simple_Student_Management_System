package utilities;

import com.github.javafaker.Faker;

public class DataUtil {

    private static final Faker faker = new Faker();

    public static String randomNumber(int digits) {
        return faker.number().digits(digits);
    }

    public static String randomFullName() {
        return faker.name().fullName();
    }

}
