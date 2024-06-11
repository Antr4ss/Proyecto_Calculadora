package co.edu.uptc.control;

import java.util.Stack;

public class ExpressionEvaluator extends CalculatorController {

public static double evaluate(String expression) {
    char[] tokens = expression.toCharArray();

    Stack<Double> values = new Stack<>();
    Stack<Character> ops = new Stack<>();

    for (int i = 0; i < tokens.length; i++) {
        if (tokens[i] == ' ')
            continue;

        if ((tokens[i] >= '0' && tokens[i] <= '9') || (tokens[i] == '-' && (i == 0 || tokens[i-1] == '('))) {
            StringBuffer buffer = new StringBuffer();
            if (tokens[i] == '-') {
                buffer.append(tokens[i++]);
            }
            while (i < tokens.length && (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.'))
                buffer.append(tokens[i++]);
            values.push(Double.parseDouble(buffer.toString()));
            i--;
        } else if (tokens[i] == '(') {
            ops.push(tokens[i]);
        } else if (tokens[i] == ')') {
            while (ops.peek() != '(')
                values.push(applyOp(ops.pop(), values.pop(), values.pop()));
            ops.pop();
        } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/' || tokens[i] == '^') {
            while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
                values.push(applyOp(ops.pop(), values.pop(), values.pop()));
            ops.push(tokens[i]);
        }
    }

    while (!ops.empty())
        values.push(applyOp(ops.pop(), values.pop(), values.pop()));

    return values.pop();
}

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        return (op1 != '*' && op1 != '/' && op1 != '^') || (op2 != '+' && op2 != '-');
    }

    private static double applyOp(char op, double b, double a) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0)
                    throw new UnsupportedOperationException("No se puede dividir por cero");
                yield a / b;
            }
            case '^' -> Math.pow(a, b);
            default -> 0;
        };
    }
}