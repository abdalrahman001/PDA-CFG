import java.io.*;
import java.util.*;

public class Problem3 {
    // ——— PDA specification ———
    ArrayList<Integer> states       = new ArrayList<>(Arrays.asList(0, 1));
    ArrayList<Integer> finalStates  = new ArrayList<>(Arrays.asList(1));
    ArrayList<Character> inputAlphabet  = new ArrayList<>(Arrays.asList('{', '}'));
    ArrayList<Character> stackAlphabet  = new ArrayList<>(Arrays.asList('$', '{'));
    int startState    = 0;
    Character stackInitial = '$';

    TransitionFunction transitionFunction = new TransitionFunction();
    {
        // ε-move into processing state, keep bottom marker
        transitionFunction.addTransition(0, 'ε', '$', 1, "$");
        // push on '{'
        transitionFunction.addTransition(1, '{', '$', 1, "{$");
        transitionFunction.addTransition(1, '{', '{', 1, "{{");
        // pop on '}'
        transitionFunction.addTransition(1, '}', '{', 1, "ε");
        // at end, pop bottom marker so stack empties
        transitionFunction.addTransition(1, 'ε', '$', 1, "ε");
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
     * Constructor: skip ahead to the line "3", echo it,
     * then for every input‐line until the next "x" run the PDA,
     * echoing "accepted"/"not accepted", then echo "x" and return.
     */
    public Problem3(BufferedReader br, BufferedWriter bw) {
        try {
            String line;
            // 1) skip until we see "3"
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("3")) {
                    bw.write("3\n");
                    break;
                }
            }
            // 2) now process each line until "x"
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
            new Problem3(br, bw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
