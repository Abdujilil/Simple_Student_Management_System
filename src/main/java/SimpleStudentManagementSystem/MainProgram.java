package SimpleStudentManagementSystem;

import utilities.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static utilities.DataUtil.*;
import static utilities.DBUtils.*;

public class MainProgram {

    public static Scanner scn = new Scanner(System.in);
    public static List<Student> students = new ArrayList<>();
    public static List<Course> courses = new ArrayList<>();
    public static List<Enrollment> enrollments = new ArrayList<>();
    public static int studentCount;

    static {
        createConnection();
    }

    public static void main(String[] args) {

        //Ask how many students to add
        System.out.println("***** Welcome to SimpleStudentManagementSystem.Course SimpleStudentManagementSystem.Enrollment Center! *****");
        System.out.println("How many students would you like to enroll?");
        studentCount = scn.nextInt(); // Should not allow more than 5 students
        scn.nextLine();
        generateStudents(studentCount);

        //Prompt user to enter student's name and the year of study
        enterStudentInfo();

        //Assign 5 digit unique ID, with the first number being their grade level
        assignStudentID();

        //Select courses to enroll. each course cost $600 to enroll
        selectCourse();

        //Check enrollment details
        getEnrollmentDetail();



        destroy();
    }

//    public static void availableCourses() {
//        courses.add(new Course(Constants.HISTORY_COURSE_NAME, Constants.HISTORY_COURSE_INSTRUCTOR, Constants.HISTORY_COURSE_CODE));
//        courses.add(new Course(Constants.MATHEMATICS_COURSE_NAME, Constants.MATHEMATICS_COURSE_INSTRUCTOR, Constants.MATHEMATICS_COURSE_CODE));
//        courses.add(new Course(Constants.ENGLISH_COURSE_NAME, Constants.ENGLISH_COURSE_INSTRUCTOR, Constants.ENGLISH_COURSE_CODE));
//        courses.add(new Course(Constants.CHEMISTRY_COURSE_NAME, Constants.CHEMISTRY_COURSE_INSTRUCTOR, Constants.CHEMISTRY_COURSE_CODE));
//        courses.add(new Course(Constants.COMPUTER_COURSE_NAME, Constants.COMPUTER_COURSE_INSTRUCTOR, Constants.COMPUTER_COURSE_CODE));
//    }

    public static void generateStudents(int studentCount) {
        for (int i = 0; i < studentCount; i++) {
            students.add(new Student());
        }
    }

    public static void enterStudentInfo() {
        for (int i = 0; i < students.size(); i++) {
            System.out.println("\nPlease enter the first name of student " + (i+1) + ":");
            String firstName = scn.nextLine();
            students.get(i).setFirstName(firstName);

            System.out.println("\nPlease enter the last name of student " + (i+1) + ":");
            String lastName = scn.nextLine();
            students.get(i).setLastName(lastName);

            System.out.println("\nPlease enter the school year of student " + (i+1) + ":");
            int schoolYear = scn.nextInt();
            scn.nextLine();
            students.get(i).setSchoolYear(schoolYear);
        }
    }

    public static void assignStudentID() {
        for (Student student : students) {
            int studentID = Integer.parseInt(student.getSchoolYear() + randomNumber(4));
            student.setStudentID(studentID);
            System.out.println("\nStudent ID for " + student.getFirstName() + " " + student.getLastName() + " is:\t" + studentID);
        }
    }

    public static void selectCourse() { // Track how many courses chosen, allow multiple selection
        for (int i = 0; i < studentCount; i++) {
            System.out.println("\nStudent " + (i+1) + ", Please enter the course code you wish to enroll:\n");
            System.out.println("\n*************************************************************\n");
            System.out.println("\t" + "HST101 - History 101");
            System.out.println("\t" + "MTH101 - Mathematics 101");
            System.out.println("\t" + "ENG101 - English 101");
            System.out.println("\t" + "CHM101 - Chemistry 101");
            System.out.println("\t" + "CMP101 - Computer Science 101");
            System.out.println("\n*************************************************************\n");
            String courseSelection = scn.nextLine();
            enrollments.add(new Enrollment(students.get(i).getStudentID(), courseSelection));
        }
        System.out.println("------ Please wait while we complete the Course Selection process... ------");
        addStudentsDB(students);
        enrollStudents(enrollments);
        System.out.println("------ Course Selection is Complete! ------");
    }

    public static void getEnrollmentDetail() {
        System.out.println("\nPlease enter the student ID to view enrollment details:");
        int studentID = scn.nextInt(); //Add verification steps
        Student student = getStudentByID(studentID);
        System.out.println("\n######################################################\n");
        System.out.println("\tStudent Enrollment Information:\n");
        System.out.print("\tFirst Name:\t" + student.getFirstName());
        System.out.println("\tLast Name:\t" + student.getLastName());
//        System.out.print("\tCourse Code:\t" + student.getLastName());
//        System.out.println("\tTuition:\t" + student.getTuition());
    }

    public static Student findStudentByID(int studentID) {
        for (Student student : students) {
            if (student.getStudentID() == studentID) {
                return student;
            }
        }
        return null;
    }

}
