import java.io.*;
import java.util.*;

public class PDAroblem5 {
    // ——— PDA definition ———
    // States: 0 = init, 1 = reading W, 2 = reading c's, 3 = final
    ArrayList<Integer> states      = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
    ArrayList<Integer> finalStates = new ArrayList<>(Arrays.asList(3));
    ArrayList<Character> inputAlphabet  = new ArrayList<>(Arrays.asList('a', 'b', 'c'));
    ArrayList<Character> stackAlphabet  = new ArrayList<>(Arrays.asList('$', 'B'));
    int startState    = 0;
    Character stackInitial = '$';

    TransitionFunction transitionFunction = new TransitionFunction();
    {
        // ε: from init to W‐phase, keep $
        transitionFunction.addTransition(0, 'ε', '$', 1, "$");

        // — W‐phase (state 1) —
        // on 'a': leave stack unchanged
        transitionFunction.addTransition(1, 'a', '$', 1, "$");
        transitionFunction.addTransition(1, 'a', 'B', 1, "B");
        // on 'b': push one B
        transitionFunction.addTransition(1, 'b', '$', 1, "B$");
        transitionFunction.addTransition(1, 'b', 'B', 1, "BB");
        // on first 'c': pop one B and move to c‐phase (only valid if at least one B)
        transitionFunction.addTransition(1, 'c', 'B', 2, "ε");
        // if W has zero b’s, allow immediate ε‐move to final (will accept W without any c)
        transitionFunction.addTransition(1, 'ε', '$', 3, "ε");

        // — c‐phase (state 2) —
        // for each c, pop one B
        transitionFunction.addTransition(2, 'c', 'B', 2, "ε");
        // when c’s done, pop $ and go to final
        transitionFunction.addTransition(2, 'ε', '$', 3, "ε");
    }

    PDAClass pda = new PDAClass(
        states,
        inputAlphabet,
        stackAlphabet,
        transitionFunction,
        startState,
        finalStates,
        stackInitial
    );

    /**
     * Constructor:
     *  1) skip ahead to the line "5", echo it;
     *  2) for each subsequent line until "end", run pda.isAccepted(...) and echo result;
     *  3) when seeing "end", write "x" and stop.
     */
    public PDAroblem5(BufferedReader br, BufferedWriter bw) {
        try {
            String line;
            // 1) skip to block "5"
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("5")) {
                    bw.write("5\n");
                    break;
                }
            }
            // 2) process each test‐string until "end"
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("end")) {
                    bw.write("x\n");
                    break;
                }
                boolean accepted = pda.isAccepted(line);
                bw.write(accepted ? "accepted\n" : "not accepted\n");
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** main reads input_pda.txt and writes output_pda.txt */
    public static void main(String[] args) {
        try (
            BufferedReader br = new BufferedReader(new FileReader("input_pda.txt"));
            BufferedWriter bw = new BufferedWriter(new FileWriter("output_pda.txt"))
        ) {
            new PDAroblem5(br, bw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
