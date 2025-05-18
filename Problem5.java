import java.io.*;
import java.util.*;

public class Problem5 {

    public static void main(String[] args) {
        Problem5Solver problem = new Problem5Solver();
        problem.run("input_cfg.txt", "output_cfg.txt");
    }
}

class CFG2Class {

    ArrayList<Character> terminals;
    ArrayList<Character> nonTerminals;
    Character startSymbol;
    Map<Character, ArrayList<String>> productions;

    public CFG2Class(ArrayList<Character> terminals, ArrayList<Character> nonTerminals,
            Character startSymbol, Map<Character, ArrayList<String>> productions) {
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.startSymbol = startSymbol;
        this.productions = productions;
    }

    public boolean derive(String input) {
        if (!input.matches("a*b*")) {
            return false;
        }

        int aCount = 0, bCount = 0;
        boolean seenB = false;
        for (char c : input.toCharArray()) {
            if (c == 'a') {
                if (seenB) {
                    return false;
                }
                aCount++;
            } else if (c == 'b') {
                seenB = true;
                bCount++;
            }
        }

        if (aCount <= bCount) {
            return false;
        }

        return canDerive(startSymbol.toString(), input);
    }

    private boolean canDerive(String current, String target) {
        if (current.equals(target)) {
            return true;
        }
        if (current.length() > target.length() * 2) {
            return false;
        }

        for (int i = 0; i < current.length(); i++) {
            char c = current.charAt(i);
            if (nonTerminals.contains(c)) {
                for (String prod : productions.get(c)) {
                    String next = current.substring(0, i) + prod + current.substring(i + 1);
                    String nextPattern = next.replaceAll("[A-Z]", "a");
                    if (target.startsWith(nextPattern.substring(0, Math.min(nextPattern.length(), target.length())))) {
                        if (canDerive(next, target)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }

    public void solveProblem(BufferedReader br, BufferedWriter bw) throws IOException {
        String line;
        while (!(line = br.readLine()).equals("end")) {
            boolean accepted = derive(line.trim());
            bw.write(accepted ? "accepted" : "not accepted");
            bw.newLine();
        }
        bw.write("x");
        bw.newLine();
    }
}

class Problem5Solver {

    CFG2Class cfg;

    public Problem5Solver() {
        ArrayList<Character> terminals = new ArrayList<>(Arrays.asList('a', 'b'));
        ArrayList<Character> nonTerminals = new ArrayList<>(Arrays.asList('S', 'A', 'T'));
        Character startSymbol = 'S';

        Map<Character, ArrayList<String>> productionRules = new HashMap<>();
        productionRules.put('S', new ArrayList<>(Arrays.asList("AT")));
        productionRules.put('A', new ArrayList<>(Arrays.asList("aAb", "")));
        productionRules.put('T', new ArrayList<>(Arrays.asList("aT", "a")));

        cfg = new CFG2Class(terminals, nonTerminals, startSymbol, productionRules);
    }

    public void run(String inputFilePath, String outputFilePath) {
        try (
                BufferedReader br = new BufferedReader(new FileReader(inputFilePath)); BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {
            String line;
            boolean isFifthProblemSet = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.matches("5")) {
                    // We found the 5th problem number, so process this problem set
                    isFifthProblemSet = true;
                    bw.write(line);
                    bw.newLine();
                } else if (line.equals("end")) {
                    if (isFifthProblemSet) {
                        bw.write("x");
                        bw.newLine();
                        isFifthProblemSet = false; // End of fifth problem set, stop processing
                    }
                } else if (isFifthProblemSet) {
                    // Process the strings for this problem set
                    boolean accepted = cfg.derive(line);
                    bw.write(accepted ? "accepted" : "not accepted");
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
