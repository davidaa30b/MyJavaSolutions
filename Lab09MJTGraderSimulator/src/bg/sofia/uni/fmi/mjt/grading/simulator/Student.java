package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.AssignmentType;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.StudentGradingAPI;

import java.util.Random;

import static bg.sofia.uni.fmi.mjt.grading.simulator.assignment.AssignmentType.LAB;

public class Student implements Runnable {

    private int fn;
    private String name;
    private StudentGradingAPI studentGradingAPI;
    private static final int MAX_TIME = 1000;
    private static Random random = new Random();



    public Student(int fn, String name, StudentGradingAPI studentGradingAPI) {
        this.fn = fn;
        this.name = name;
        this.studentGradingAPI = studentGradingAPI;
    }



    @Override
    public void run() {
        try {
            Thread.sleep(random.nextInt(MAX_TIME));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AssignmentType[] assignmentTypes = AssignmentType.values();
        Random assignmentTypeRand = new Random();
        AssignmentType assignmentType = assignmentTypes[assignmentTypeRand.nextInt(assignmentTypes.length)];

        studentGradingAPI.submitAssignment(new Assignment(fn, name, assignmentType));
        //doAssignment();
    }

    public int getFn() {
        return fn;
    }

    public String getName() {
        return name;
    }

    public StudentGradingAPI getGrader() {
        return studentGradingAPI;
    }

}