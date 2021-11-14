package SimpleStudentManagementSystem;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Course {

    private String name;
    private String courseCode;
    private double price;
    private int duration;
    private String instructor;

    public Course(String name, String instructor, String courseCode) {
        price = 600;
        duration = 15;
        this.name = name;
        this.instructor = instructor;
        this.courseCode = courseCode;
    }

}
