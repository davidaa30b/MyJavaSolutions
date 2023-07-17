package bg.sofia.uni.fmi.mjt.grading.simulator.grader;

import bg.sofia.uni.fmi.mjt.grading.simulator.Assistant;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CodePostGrader implements AdminGradingAPI {
    private final int numberOfAssistants;
    private volatile boolean gradingIsFinalized = false;
    private int assignmentsCounter = 0;
    private Queue<Assignment> assignmentsLog = new LinkedList<>();
    private List<Assistant> assistants = new ArrayList<>();


    public CodePostGrader(int numberOfAssistants) {
        this.numberOfAssistants = numberOfAssistants;
        //startAssistants();
        for (int i = 0; i < numberOfAssistants; i++) {
            Assistant assistant = new Assistant("Assistant#" + i, this);
            assistants.add(assistant);
            assistant.start();
        }
    }

   /* private void startAssistants() {
        for (int i = 0; i < numberOfAssistants; i++) {
            Assistant assistant = new Assistant("Assistant#" + i, this);
            assistants.add(assistant);
            assistant.start();
        }
    }*/

    @Override
    public synchronized Assignment getAssignment() {
        while (!gradingIsFinalized && assignmentsLog.isEmpty()) {
            try {
                this.wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return (gradingIsFinalized || assignmentsLog.isEmpty()) ? null : assignmentsLog.poll();
    }

    @Override
    public synchronized int getSubmittedAssignmentsCount() {
        return assignmentsCounter;
    }

    @Override
    public synchronized void finalizeGrading() {
        this.gradingIsFinalized = true;
        this.notifyAll();
    }

    @Override
    public List<Assistant> getAssistants() {
        return assistants;
    }

    @Override
    public synchronized void submitAssignment(Assignment assignment) {
        assignmentsLog.add(assignment);
        assignmentsCounter++;

        this.notify();
    }
}
