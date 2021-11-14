package SimpleStudentManagementSystem;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Student {

    private String firstName;
    private String lastName;
    private int schoolYear;
    private int studentID;
    private double tuition;

}
