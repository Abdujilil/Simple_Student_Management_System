package SimpleStudentManagementSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static utilities.DataUtil.*;
import static utilities.DBUtils.*;

public class MainProgram {

    public static Scanner scn = new Scanner(System.in);
    public static List<Student> students = new ArrayList<>();
    public static List<Enrollment> enrollments = new ArrayList<>();
    public static int studentCount;

    static {
        createConnection();
    }

    public static void main(String[] args) {
        displayStartingOptions();
        String answer = scn.nextLine();

        switch (answer) {
            case "1":
                //Check enrollment details
                getEnrollmentDetail();
                break;
            case "2":
                //Ask how many students to add
                studentCount = requestNumberOfStudents();
                generateStudents(studentCount);
                //Prompt user to enter student's name and the year of study
                enterStudentInfo();
                //Assign 5 digit unique ID, with the first number being their grade level
                assignStudentID();
                //Select courses to enroll
                selectCourseNew();
                continueOrEnd();
                break;
            case "3":
                int studentID = requestStudentID();
                //Select courses to enroll
                selectCourseExist(studentID);
                continueOrEnd();
                break;
            case "4":
                //disenroll a student
                disenrollAStudent();
                break;
            case "5":
                //Check availability of a course
                courseAvailabilityCheck();
        }
        destroy();
    }

    public static int requestNumberOfStudents() {
        System.out.println("\tHow many students would you like to enroll?");
        int count;
        do {
            count = Integer.parseInt(scn.nextLine()); // Will not allow more than 5 students
            if (count > 5) {
                System.err.println("\tMaximum number of students allowed to enroll at once is 5!\n");
                System.out.println("\tPlease enter a valid number:");
            }
        } while (count > 5);

        return count;
    }

    public static void generateStudents(int studentCount) {
        for (int i = 0; i < studentCount; i++) {
            students.add(new Student());
        }
    }

    public static void enterStudentInfo() {
        for (int i = 0; i < students.size(); i++) {
            System.out.println("\nPlease enter the first name of student " + (i+1) + ":");
            String firstName = formatName();
            students.get(i).setFirstName(firstName);

            System.out.println("\nPlease enter the last name of student " + (i+1) + ":");
            String lastName = formatName();
            students.get(i).setLastName(lastName);

            System.out.println("\nPlease enter the school year of student " + (i+1) + ":");
            int schoolYear = verifySchoolYear();
            students.get(i).setSchoolYear(schoolYear);
        }
    }

    public static String formatName() {
        String name;
        boolean correct = false;
        do {
            name = scn.nextLine();
            for (char character : name.toCharArray()) {
                if (!Character.isAlphabetic(character)) {
                    System.err.println("\tIncorrect value entered! \n\tPlease enter a valid input!");
                    correct = false;
                    break;
                }
                correct = true;
            }
        } while (!correct);

        return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public static int verifySchoolYear() {
        int schoolYear;
        boolean correct = false;
        do {
            schoolYear = Integer.parseInt(scn.nextLine());
            if (schoolYear < 0 || schoolYear > 4) {
                System.err.println("\tIncorrect value entered! \n\tPlease enter a valid input!");
            } else {
                correct = true;
            }
        } while (!correct);
        return schoolYear;
    }

    public static void assignStudentID() {
        for (Student student : students) {
            int studentID = generateUniqueStudentID(student.getSchoolYear());
            student.setStudentID(studentID);
            System.out.println("\nStudent ID for " + student.getFirstName() + " " + student.getLastName() + " is:\t" + studentID);
        }
    }

    public static void selectCourseNew() { // Track how many courses chosen, allow multiple selection
        String courseSelection = "";
        for (int i = 0; i < studentCount; i++) {
            boolean eligible = false;
            do {
                System.out.println("\nPlease enter the code of the course you wish to enroll:\n");
                System.out.println("\n*************************************************************\n");
                printAvailableCourses();
                System.out.println("\n*************************************************************\n");
                courseSelection = verifyCourseCode();
                if (enrollmentCheck(students.get(i).getStudentID(), courseSelection)) {
                    eligible = true;
                } else if (!enrollmentCheck(students.get(i).getStudentID(), courseSelection)) {
                    System.err.println("\tThis student is already enrolled in this course!\n");
                    System.out.println("\tEnter 'yes' to select a different course:");
                    System.out.println("\tEnter 'no' to end the course selection process:");
                    String answer = scn.nextLine().toLowerCase();
                    if (answer.equals("no")) {
                        return;
                    }
                } else if (verifyCourseIsFull(courseSelection)) {
                    System.err.println("\n\tThere is not spot left for this course!");
                    System.out.println("\tEnter 'yes' to select a different course:");
                    System.out.println("\tEnter 'no' to end the course selection process:");
                    String answer = scn.nextLine().toLowerCase();
                    if (answer.equals("no")) {
                        return;
                    }
                }
            } while (!eligible);
            enrollments.add(new Enrollment(students.get(i).getStudentID(), courseSelection));
        }
        System.out.println("------ Please wait while we complete the Course Selection process... ------");
        addStudentsDB(students);
        enrollStudents(enrollments);
        increaseCourseEnrollmentCount(courseSelection);
        wait(1);
        System.out.println("------ Course Selection is Complete! ------");
    }

    public static void selectCourseExist(int studentID) { // Track how many courses chosen, allow multiple selection
        String courseSelection;
        boolean eligible = false;
        do {
            System.out.println("\nPlease enter the code of the course you wish to enroll:\n");
            System.out.println("\n*************************************************************\n");
            printAvailableCourses();
            System.out.println("\n*************************************************************\n");
            courseSelection = verifyCourseCode();
            if (enrollmentCheck(studentID, courseSelection)) {
                eligible = true;
            } else if (!enrollmentCheck(studentID, courseSelection)) {
                System.err.println("\tThis student is already enrolled in this course!\n");
                System.out.println("\tEnter 'yes' to select a different course:");
                System.out.println("\tEnter 'no' to end the course selection process:");
                String answer = scn.nextLine().toLowerCase();
                if (answer.equals("no")) {
                    return;
                }
            } else if (verifyCourseIsFull(courseSelection)) {
                System.err.println("\n\tThere is not spot left for this course!");
                System.out.println("\tEnter 'yes' to select a different course:");
                System.out.println("\tEnter 'no' to end the course selection process:");
                String answer = scn.nextLine().toLowerCase();
                if (answer.equals("no")) {
                    return;
                }
            }
        } while (!eligible);
        enrollments.add(new Enrollment(studentID, courseSelection));
        System.out.println("------ Please wait while we complete the Course Selection process... ------");
        enrollStudents(enrollments);
        increaseCourseEnrollmentCount(courseSelection);
        wait(1);
        System.out.println("------ Course Selection is Complete! ------");
    }

    public static void printAvailableCourses() {
        List<Course> allCourses = getAllCourses();
        for (Course course : allCourses) {
            System.out.println("\t" + course.getCourseCode() + " - " + course.getName());
        }
    }

    public static int requestStudentID() {
        System.out.println("\tPlease enter the student ID:");
        Student student;
        int studentID;
        do {
            studentID = Integer.parseInt(scn.nextLine());
            student = getStudentByID(studentID);
            if (student == null) {
                System.err.println("\tThis student ID is not in the database!\n");
                System.out.println("\tPlease enter a valid student ID:");
            }
        } while (student == null);
        return studentID;
    }

    public static void getEnrollmentDetail() {
        int studentID = requestStudentID();
        Student student = getStudentByID(studentID);
        List<Course> courses = getCoursesByStudentID(studentID);
        System.out.println("\n######################################################\n");
        System.out.println("\tStudent Enrollment Information:\n");
        System.out.println("\tFirst Name:\t" + student.getFirstName());
        System.out.println("\tLast Name:\t" + student.getLastName());
        System.out.println("\tStudent ID:\t" + student.getStudentID());
        System.out.println("\tSchool Year:\t" + student.getSchoolYear());
        printCourseDetails(courses);
        System.out.println("\n\tThank you and have a great day!\n");
        System.out.println("\n######################################################\n");
        wait(1);
    }

    public static void printCourseDetails(List<Course> courses) {
        for (Course course : courses) {
            System.out.println("\n\tCourse Code:\t" + course.getCourseCode());
            System.out.println("\tCourse Name:\t" + course.getName());
            System.out.println("\tCourse Instructor:\t" + course.getInstructor());
            System.out.println("\tCourse Duration:\t" + course.getDuration());
            System.out.println("\tCourse Price:\t" + course.getPrice());
        }
    }

    public static void displayStartingOptions() {
        System.out.println("\n********** Welcome Enrollment Manager! **********\n");
        System.out.println("\tPlease choose one of the following options:\n");
        System.out.println("\t\t1. Check Enrollment Status of an existing student");
        System.out.println("\t\t2. Enroll new student(s) to a course");
        System.out.println("\t\t3. Enroll existing student to a course");
        System.out.println("\t\t4. Disenroll a student from a course");
        System.out.println("\t\t5. Check availability of a course");
    }

    public static void continueOrEnd() {
        System.out.println("\n\tPlease choose one of the following options:\n");
        System.out.println("\t1. Check Enrollment Status of a student");
        System.out.println("\t2. I'm done");
        String answer = scn.nextLine();
        if (answer.equals("1")) {
            //Check enrollment details
            getEnrollmentDetail();
        } else {
            System.out.println("\n\tThank you and have a great day!\n");
            wait(1);
        }
    }

    public static void disenrollAStudent() {
        boolean enrolled = false;
        int studentID;
        String courseCode;
        do {
            studentID = requestStudentID();
            System.out.println("\tPlease enter the course code:");
            courseCode = verifyCourseCode();
            if (enrollmentCheck(studentID, courseCode)) {
                System.err.println("\n\tThis student is not enrolled in this course!\n");
                System.err.println("\tPlease verify the information and try again!\n");
            } else {
                enrolled = true;
            }
        } while (!enrolled);
        yesNoDecision();
        System.out.println("\t------ Please wait for the disenrollment process to complete... ------");
        deleteEnrollment(studentID, courseCode);
        decreaseCourseEnrollmentCount(courseCode);
        wait(1);
        System.out.println("\t------ Course Disenrollment is Complete! ------");
        System.out.println("\n\tThank you and have a great day!\n");
        wait(1);
    }

    public static void courseAvailabilityCheck() {
        System.out.println("\n\tPlease enter the course code:");
        String courseCode = verifyCourseCode();
        int availableSpots = getCourseEnrollmentLimit(courseCode) - getCourseEnrollmentCount(courseCode);
        if (availableSpots > 0) {
            System.out.println("\n\tThere are total of " + availableSpots + " spots available!");
        } else if (availableSpots == 0) {
            System.err.println("\n\tThere is not spot left for this course!");
        }
    }

    public static boolean verifyCourseIsFull(String courseCode) {
        return getCourseEnrollmentCount(courseCode) == getCourseEnrollmentLimit(courseCode);
    }

    public static String verifyCourseCode() {
        String courseCode;
        boolean correct = false;
        List<String> allCourseCodes = getAllCourseCodes();
        do {
            courseCode = scn.nextLine().toUpperCase();
            if (allCourseCodes.contains(courseCode)) {
                correct = true;
            } else {
                System.err.println("\tInvalid entry!\tPlease enter a valid course code:");
            }
        } while (!correct);
        return courseCode;
    }

    public static void yesNoDecision() {
        System.out.println("\n\tAre you sure you want to disenroll this student? (Y/N)");
        String answer = scn.nextLine().toUpperCase();
        if (answer.equals("N")) {
            System.out.println("You have cancelled the disenrollment process!");
            System.out.println("\n\tThank you and have a great day!\n");
            System.exit(1);
        }
    }

    public static void wait(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
