package service;

import model.Student;

public class FeeService {
    public double pending(Student s) { return s.getPendingFee(); }

    public String advice(Student s) {
        if (pending(s) > 0) return "Pay pending fee before deadline.";
        return "Fee clear.";
    }
}
