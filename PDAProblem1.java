import java.io.*;
import java.util.*;

public class PDAProblem1 {
    // ——— PDA definition ———
    ArrayList<Integer> states       = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
    ArrayList<Integer> finalStates  = new ArrayList<>(Arrays.asList(4));
    ArrayList<Character> inputAlphabet  = new ArrayList<>(Arrays.asList('a','b','c'));
    ArrayList<Character> stackAlphabet  = new ArrayList<>(Arrays.asList('$','A'));
    int startState    = 0;
    char stackInitial = '$';

    TransitionFunction tf = new TransitionFunction();
    {
        // ε-move into state 1 (start pushing a's)
        tf.addTransition(0, 'ε', '$', 1, "$");
        
        // state 1: on 'a', push A
        tf.addTransition(1, 'a', '$', 1, "A$");
        tf.addTransition(1, 'a', 'A', 1, "AA");

        // on first 'b', move to state 2, leave stack unchanged
        tf.addTransition(1, 'b', '$', 2, "$");
        tf.addTransition(1, 'b', 'A', 2, "A");

        // state 2: continue reading b's (ignore stack)
        tf.addTransition(2, 'b', '$', 2, "$");
        tf.addTransition(2, 'b', 'A', 2, "A");

        // on first 'c', move to state 3, pop one A
        tf.addTransition(2, 'c', 'A', 3, "ε");

        // state 3: continue reading c's, pop one A each
        tf.addTransition(3, 'c', 'A', 3, "ε");

        // when input done and only '$' remains, ε-move to final
        tf.addTransition(3, 'ε', '$', 4, "ε");
    }

    PDAClass pda = new PDAClass(
        states, inputAlphabet, stackAlphabet,
        tf, startState, finalStates, stackInitial
    );

    public PDAProblem1(BufferedReader br, BufferedWriter bw) {
        try {
            String line;
            // 1) skip until we see the "1" header
            while ((line = br.readLine()) != null) {
                if (line.trim().equals("1")) {
                    bw.write("1\n");
                    break;
                }
            }
            // 2) process each test string until "end"
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
            new PDAProblem1(br, bw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
