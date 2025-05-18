import java.io.*;
import java.util.*;

public class PDAClass {
    private final ArrayList<Integer> states;
    private final ArrayList<Character> inputAlphabet;
    private final ArrayList<Character> stackAlphabet;
    private final TransitionFunction transitionFunction;
    private final int startState;
    private final ArrayList<Integer> finalStates;
    private final char stackInitial;

    public PDAClass(ArrayList<Integer> states, ArrayList<Character> inputAlphabet,
                    ArrayList<Character> stackAlphabet, TransitionFunction transitionFunction,
                    int startState, ArrayList<Integer> finalStates, char stackInitial) {
        this.states = states;
        this.inputAlphabet = inputAlphabet;
        this.stackAlphabet = stackAlphabet;
        this.transitionFunction = transitionFunction;
        this.startState = startState;
        this.finalStates = finalStates;
        this.stackInitial = stackInitial;
    }

    public boolean isAccepted(String s) {
        Stack<Character> stack = new Stack<>();
        stack.push(stackInitial);
        return simulate(startState, s.replaceAll(" ", ""), 0, stack);
    }

    private boolean simulate(int currentState, String input, int pos, Stack<Character> stack) {
        if (pos == input.length()) {
            // Try epsilon transitions
            TransitionValue eps = transitionFunction.getEpsilonTransition(currentState, stack.isEmpty() ? 'ε' : stack.peek());
            if (eps != null) {
                Stack<Character> newStack = (Stack<Character>) stack.clone();
                if (!newStack.isEmpty()) newStack.pop();
                for (int i = eps.stackPush.length() - 1; i >= 0; i--) {
                    if (eps.stackPush.charAt(i) != 'ε') newStack.push(eps.stackPush.charAt(i));
                }
                if (simulate(eps.nextState, input, pos, newStack)) return true;
            }
            return finalStates.contains(currentState) && stack.isEmpty();
        }

        char currentInput = input.charAt(pos);
        char stackTop = stack.isEmpty() ? 'ε' : stack.peek();

        // Try normal transition
        TransitionValue val = transitionFunction.getTransition(currentState, currentInput, stackTop);
        if (val != null) {
            Stack<Character> newStack = (Stack<Character>) stack.clone();
            if (!newStack.isEmpty()) newStack.pop();
            for (int i = val.stackPush.length() - 1; i >= 0; i--) {
                if (val.stackPush.charAt(i) != 'ε') newStack.push(val.stackPush.charAt(i));
            }
            if (simulate(val.nextState, input, pos + 1, newStack)) return true;
        }

        // Try ε transition
        TransitionValue epsVal = transitionFunction.getEpsilonTransition(currentState, stackTop);
        if (epsVal != null) {
            Stack<Character> newStack = (Stack<Character>) stack.clone();
            if (!newStack.isEmpty()) newStack.pop();
            for (int i = epsVal.stackPush.length() - 1; i >= 0; i--) {
                if (epsVal.stackPush.charAt(i) != 'ε') newStack.push(epsVal.stackPush.charAt(i));
            }
            if (simulate(epsVal.nextState, input, pos, newStack)) return true;
        }

        return false;
    }

    public void solveProblem(BufferedReader br, BufferedWriter bw) {
        try {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("end")) {
                    bw.write("x\n");
                    break;
                }
                if (line.matches("\\d+")) {
                    bw.write(line + "\n");
                    continue;
                }
                boolean accepted = isAccepted(line);
                bw.write(accepted ? "accepted\n" : "not accepted\n");
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

 class TransitionKey {
    int currentState;
    char input;
    char stackTop;

    public TransitionKey(int currentState, char input, char stackTop) {
        this.currentState = currentState;
        this.input = input;
        this.stackTop = stackTop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransitionKey)) return false;
        TransitionKey key = (TransitionKey) o;
        return currentState == key.currentState && input == key.input && stackTop == key.stackTop;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentState, input, stackTop);
    }
}
 class TransitionValue {
    int nextState;
    String stackPush;

    public TransitionValue(int nextState, String stackPush) {
        this.nextState = nextState;
        this.stackPush = stackPush;
    }
}

 class TransitionFunction {
    private final Map<TransitionKey, TransitionValue> transitions = new HashMap<>();

    public void addTransition(int currentState, char input, char stackTop, int nextState, String stackPush) {
        transitions.put(new TransitionKey(currentState, input, stackTop), new TransitionValue(nextState, stackPush));
    }

    public TransitionValue getTransition(int currentState, char input, char stackTop) {
        return transitions.get(new TransitionKey(currentState, input, stackTop));
    }

    // Try ε transitions
    public TransitionValue getEpsilonTransition(int currentState, char stackTop) {
        return transitions.get(new TransitionKey(currentState, 'ε', stackTop));
    }
}

