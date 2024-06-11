package co.edu.uptc.model;

public class Operation {
    private String expression;
    private String result;
    private String base;

    public Operation(String base, String expression, String result) {
        this.expression = expression;
        this.result = result;
        this.base = base;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }
}
