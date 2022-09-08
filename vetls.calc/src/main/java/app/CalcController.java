package app;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;

public class CalcController {

    private Calc calc;

    public CalcController() {
        calc = new Calc(0.0, 0.0, 0.0);
    }

    public Calc getCalc() {
        return calc;
    }

    public void setCalc(Calc calc) {
        this.calc = calc;
        updateOperandsView();
    }

    @FXML
    private ListView<Double> operandsView;

    @FXML
    private Label operandView;

    @FXML
    void initialize() {
        updateOperandsView();
    }

    private void updateOperandsView() {
        List<Double> operands = operandsView.getItems();
        operands.clear();
        int elementCount = Math.min(calc.getOperandCount(), 3);
        for (int i = 0; i < elementCount; i++) {
            operands.add(calc.peekOperand(elementCount - i - 1));
        }
    }

    private String getOperandString() {
        return operandView.getText();
    }

    private boolean hasOperand() {
        return ! getOperandString().isBlank();
    }

    private double getOperand() {
        return Double.valueOf(operandView.getText());
    }
    
    private void setOperand(String operandString) {
        operandView.setText(operandString);
    }

    @FXML
    void handleEnter() {
        if (hasOperand()) {
            calc.pushOperand(getOperand());
        } else {
            calc.dup();
        }
        setOperand("");
        updateOperandsView();
    }

    private void appendToOperand(String s) {
        setOperand(getOperandString()+s);
        updateOperandsView();
        
    }

    @FXML
    void handleDigit(ActionEvent ae) {
        if (ae.getSource() instanceof Labeled l) {
            appendToOperand(l.getText());
        }
    }

    @FXML
    void handlePoint() {
    	var operandString = getOperandString();
        if (operandString.contains(".")) {
            operandString.replaceAll("\\p{Punct}", "");
        } else {
            operandString += ".";
        }
        setOperand(operandString);
        updateOperandsView();
    }

    @FXML
    void handleClear() {
        setOperand("");
        updateOperandsView();
    }

    @FXML
    void handleSwap() {
        getCalc().swap();
        updateOperandsView();
    }

    private void performOperation(UnaryOperator<Double> op) {
        getCalc().performOperation(op);
        handleClear();
        
    }

    private void performOperation(boolean swap, BinaryOperator<Double> op) {
        if (hasOperand()) {
            getCalc().pushOperand(getOperand());
        }
        
        if(swap){
            handleSwap();
        }
        getCalc().performOperation(op);
        handleClear();
        
    }

    @FXML
    void handleOpAdd() {
        performOperation(true, (x,y) -> x+y);
    }

    @FXML
    void handleOpSub() {
        performOperation(false, (x,y) -> x-y);
    }

    @FXML
    void handleOpMult() {
        performOperation(true, (x,y) -> x*y);
    }

    @FXML
    void handleOpDiv() {
        performOperation(false, (x,y) -> x/y);
    }

    @FXML
    void handleOpSqrt() {
        performOperation(x -> Math.sqrt(x));
    }

    @FXML
    void handleOpPi() {
        performOperation(x -> x*Math.PI);
    }
}