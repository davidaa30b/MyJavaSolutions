package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.AdminGradingAPI;
public class Assistant extends Thread {

    private String name;
    private AdminGradingAPI grader;
    private int totalAssignmentsGraded = 0;

    public Assistant(String name, AdminGradingAPI grader) {
        this.name = name;
        this.grader = grader;
    }

    @Override
    public void run() {
        Assignment assignment = null;

        while ((assignment = grader.getAssignment()) != null) {
            try {
                Thread.sleep(assignment.type().getGradingTime());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            totalAssignmentsGraded++;
        }

        System.out.println("Assistant : " + name + " graded " + totalAssignmentsGraded + " assignments");

    }

    public int getNumberOfGradedAssignments() {
        return totalAssignmentsGraded;
    }

}