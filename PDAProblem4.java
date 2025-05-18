import java.io.*;
import java.util.*;

public class PDAProblem4 {
    // ——— PDA definition ———
    ArrayList<Integer> states       = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
    ArrayList<Integer> finalStates  = new ArrayList<>(Arrays.asList(4));
    ArrayList<Character> inputAlphabet  = new ArrayList<>(Arrays.asList('a', 'b', 'c'));
    ArrayList<Character> stackAlphabet  = new ArrayList<>(Arrays.asList('$', 'A', 'B'));
    int startState    = 0;
    Character stackInitial = '$';

    TransitionFunction transitionFunction = new TransitionFunction();
    {
        // ε-move into a-phase
        transitionFunction.addTransition(0, 'ε', '$', 1, "$");

        transitionFunction.addTransition(1, 'a', '$', 1, "A$");
        transitionFunction.addTransition(1, 'a', 'A', 1, "AA");

        transitionFunction.addTransition(1, 'b', 'A', 2, "ε");

        transitionFunction.addTransition(2, 'b', 'A', 2, "ε");
        transitionFunction.addTransition(2, 'b', '$', 2, "B$");   // <<< swapped order here
        transitionFunction.addTransition(2, 'b', 'B', 2, "BB");

        transitionFunction.addTransition(2, 'c', 'B', 3, "ε");

        transitionFunction.addTransition(3, 'c', 'B', 3, "ε");

        transitionFunction.addTransition(3, 'ε', '$', 4, "ε");
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
     * Skips ahead to the “4” header, echoes it, then
     * reads lines until “end”, running each through the PDA,
     * writing accepted/not accepted, and finally “x”.
     */
    public PDAProblem4(BufferedReader br, BufferedWriter bw) {
        try {
            String line;
            // 1) skip to block “4”
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("4")) {
                    bw.write("4\n");
                    break;
                }
            }
            // 2) process until “end”
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

    public static void main(String[] args) {
        try (
            BufferedReader br = new BufferedReader(new FileReader("input_pda.txt"));
            BufferedWriter bw = new BufferedWriter(new FileWriter("output_pda.txt"))
        ) {
            new PDAProblem4(br, bw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
