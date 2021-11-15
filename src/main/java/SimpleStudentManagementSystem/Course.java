package SimpleStudentManagementSystem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Course {

    private String name;
    private String courseCode;
    private double price;
    private int duration;
    private String instructor;

}
