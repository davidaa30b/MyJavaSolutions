package bg.sofia.uni.fmi.mjt.grading.simulator;


import bg.sofia.uni.fmi.mjt.grading.simulator.grader.CodePostGrader;

public class Work {

    private static final int MAX_STUDENTS = 30;
    private static final int MAX_ASSISTANTS = 5;
    private static final int MAX_GRADING_TIME = 3000;

    private CodePostGrader grader;
    private int numberOfStudents;
    private int gradingTime;

    public Work(CodePostGrader grader, int numberOfStudents, int gradingTime) {
        this.grader = grader;
        this.numberOfStudents = numberOfStudents;
        this.gradingTime = gradingTime;
    }

    public static void main(String[] args) {
        int numberOfStudents = MAX_STUDENTS;
        int numberOfAssistants = MAX_ASSISTANTS;
        int gradingTime = MAX_GRADING_TIME;

        Work work = new Work(new CodePostGrader(numberOfAssistants), numberOfStudents, gradingTime);
        work.commit();
    }

    void commit() {
        Thread[] students = new Thread[numberOfStudents];
        for (int i = 0; i < students.length; i++) {
            students[i] = new Thread(new Student(i, "name" + i, grader));
            students[i].start();
        }

        try {
            Thread.sleep(gradingTime);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (grader) {
            grader.finalizeGrading();
        }

        for (int i = 0; i < students.length; i++) {
            try {
                students[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public CodePostGrader getGrader() {
        return grader;
    }

}
