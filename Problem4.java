import java.io.*;
import java.util.*;

public class Problem4 {
    public static void main(String[] args) {
        Problem4Solver solver = new Problem4Solver();
        solver.run("input_cfg.txt", "output_cfg.txt");
    }
}

class CFG1Class {
    private final Map<Character, List<String>> productions;

    public CFG1Class(Map<Character, List<String>> productions) {
        this.productions = productions;
    }

    // Try to derive 'target' from the sentential form 'current'
    public boolean derive(String current, String target) {
        if (current.equals(target)) return true;
        if (current.length() > target.length()) return false;
        for (int i = 0; i < current.length(); i++) {
            char c = current.charAt(i);
            if (productions.containsKey(c)) {
                for (String rhs : productions.get(c)) {
                    String next = current.substring(0, i) + rhs + current.substring(i + 1);
                    if (derive(next, target)) return true;
                }
                return false; // no expansion of this nonterminal worked
            }
        }
        return false; // no nonterminals left but strings differ
    }
}

class Problem4Solver {
    private final CFG1Class cfg;
    private final char startSymbol = 'S';

    public Problem4Solver() {
        // Define grammar: S → aaa | aaSb
        Map<Character, List<String>> prods = new HashMap<>();
        prods.put('S', Arrays.asList("aaa", "aaSb"));
        this.cfg = new CFG1Class(prods);
    }

    public void run(String inputPath, String outputPath) {
        try (
            BufferedReader br = new BufferedReader(new FileReader(inputPath));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))
        ) {
            String line;
            boolean inProblem4 = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!inProblem4) {
                    if (line.equals("4")) {
                        // We've found the start of problem 4
                        inProblem4 = true;
                        bw.write("4");
                        bw.newLine();
                    }
                    // otherwise skip
                } else {
                    // We're inside the Problem 4 block
                    if (line.equals("end")) {
                        // End of Problem 4 block
                        bw.write("x");
                        bw.newLine();
                        break;
                    }
                    // Process this test string
                    boolean accepted = cfg.derive(Character.toString(startSymbol), line);
                    bw.write(accepted ? "accepted" : "not accepted");
                    bw.newLine();
                }
            }

            bw.flush();
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}
