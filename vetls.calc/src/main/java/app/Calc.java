package app;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class Calc {
    
    private final List<Double> operandStack;

    public Calc(double... operands) {
        operandStack = new ArrayList<>(operands.length + 2);
        for (var d : operands) {
            operandStack.add(d);
        }
    }

    /**
     * @return the number of operands on the stack
     */
    public int getOperandCount() {
        return operandStack.size();
    }

    /**
     * Pushes a new operand onto top of the stack.
     *
     * @param d the new operand
     */
    public void pushOperand(double d) {
        operandStack.add(d);
    }

    /**
     * @param n the place (from the top) to peek
     * @return the n'th operand from the top
     * @throws IllegalArgumentException if n is larger than the operand count
     */
    public double peekOperand(int n) {
        if (n >= getOperandCount()) {
            throw new IllegalArgumentException("Cannot peek at position " + n + " when the operand count is " + getOperandCount());
        }
        return operandStack.get(getOperandCount() - n - 1);
    }

    /**
     * @return the top operand
     */
    public double peekOperand() {
        return peekOperand(0);
    }

    /**
     * Removes and returns the top operand.
     *
     * @return the top operand
     * @throws IllegalStateException if the stack is empty
     */
    public double popOperand() {
        if (getOperandCount() == 0) {
            throw new IllegalStateException("Cannot remove an operand when stack is empty");
        }
        return operandStack.remove(operandStack.size() - 1);
    }
    
    /**
     * Performs the provided operation in the top operand, and
     * replaces it with the result.
     *
     * @param op the operation to perform
     * @return the result of performing the operation
     * @throws IllegalStateException if the operand stack is empty
     */
    public double performOperation(UnaryOperator<Double> op) throws IllegalStateException {
        if(getOperandCount() == 0){
            throw new IllegalStateException("Cannot operate with an empty stack");
        }
        double op1 = op.apply(popOperand());
        pushOperand(op1);
        return op1;
    }

    /**
     * Performs the provided operation in the two topmost operands, and
     * replaces them with the result.
     *
     * @param op the operation to perform
     * @return the result of performing the operation
     * @throws IllegalStateException if the operand count is less than two
     */
    public double performOperation(BinaryOperator<Double> op) throws IllegalStateException {
        if (getOperandCount() < 2) {
            throw new IllegalStateException("Too few operands on the stack");
        }
        var op2 = popOperand();
        var op1 = popOperand();
        var finalOperand = op.apply(op1, op2);
        pushOperand(finalOperand);
        return finalOperand;

    }

    /**
     * Swaps the two topmost operands.
     *
     * @throws IllegalStateException if the operand count is less than two
     */
    public void swap() {
        if (getOperandCount() < 2) {
            throw new IllegalStateException("Cannot swap when there are less than two operands");
        }
        double op1 = popOperand();
        double op2 = popOperand();
        pushOperand(op1);
        pushOperand(op2);        
    }

    /**
     * Duplicates the top operand.
     *
     * @throws IllegalStateException if the operand stack is empty
     */
    public void dup() {
        if (getOperandCount() == 0) {
            throw new IllegalStateException("Cannot duplicate when stack is empty");
        }
        pushOperand(peekOperand());
    }
}
