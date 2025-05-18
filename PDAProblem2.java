import java.io.*;
import java.util.*;

public class PDAProblem2 {
    // ——— PDA definition ———
    // States: 0 = init, 1 = reading a's, 2 = reading b's, 3 = final
    ArrayList<Integer> states      = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
    ArrayList<Integer> finalStates = new ArrayList<>(Arrays.asList(3));
    ArrayList<Character> inputAlphabet  = new ArrayList<>(Arrays.asList('a', 'b'));
    ArrayList<Character> stackAlphabet  = new ArrayList<>(Arrays.asList('$', 'A'));
    int startState    = 0;
    Character stackInitial = '$';

    TransitionFunction transitionFunction = new TransitionFunction();
    {
        // — ε-transition from init to a-phase
        transitionFunction.addTransition(0, 'ε', '$', 1, "$");

        // — W-phase (state 1) — process a's
        // on every 'a': push one 'A' (after reading 3 'a's, 3 'A's will be pushed)
        transitionFunction.addTransition(1, 'a', '$', 1, "A$");
        transitionFunction.addTransition(1, 'a', 'A', 1, "AA");

        // When 3 'a's are processed, the stack will have exactly 3 'A's
        // Once the first 'b' is encountered, transition to state 2

        // — B-phase (state 2) — process b's
        // for every 2 b's, pop 1 A
        transitionFunction.addTransition(1, 'b', 'A', 2, "ε"); // move to state 2 when first 'b' comes

        // In state 2, pop 1 'A' for each 'b'
        transitionFunction.addTransition(2, 'b', 'A', 2, "ε");

        // — Final phase (state 3) — when done, check if the stack is empty
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
     *  1) skip ahead to the line "2", echo it;
     *  2) for each subsequent line until "end", run pda.isAccepted(...) and echo result;
     *  3) when seeing "end", write "x" and stop.
     */
    public PDAProblem2(BufferedReader br, BufferedWriter bw) {
        try {
            String line;
            // 1) skip to block "2"
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("2")) {
                    bw.write("2\n");
                    break;
                }
            }
            // 2) process each test-string until "end"
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
            new PDAProblem2(br, bw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
