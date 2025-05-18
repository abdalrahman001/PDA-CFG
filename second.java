import java.util.*;
import java.io.*;

class CFGClass {
    private ArrayList<Character> terminals;
    private ArrayList<Character> nonTerminals;
    private Character startSymbol;
    private Map<Character, ArrayList<String>> productions;

    public CFGClass(ArrayList<Character> terminals, ArrayList<Character> nonTerminals,
            Character startSymbol, Map<Character, ArrayList<String>> productions) {
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.startSymbol = startSymbol;
        this.productions = productions;
    }

    public boolean derive(String start, String target) {
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (current.equals(target))
                return true;

            if (current.replaceAll("[^ab]", "").length() > target.length())
                continue;

            for (int i = 0; i < current.length(); i++) {
                char c = current.charAt(i);
                if (nonTerminals.contains(c)) {
                    List<String> prods = productions.get(c);
                    if (prods == null)
                        continue;

                    for (String prod : prods) {
                        String next = current.substring(0, i) + prod + current.substring(i + 1);
                        if (!visited.contains(next)) {
                            visited.add(next);
                            queue.add(next);
                        }
                    }
                    break;
                }
            }
        }

        return false;
    }

    public void solveProblem(BufferedReader br, BufferedWriter bw) throws IOException {
        String line;
        while ((line = br.readLine()) != null && !line.equals("end")) {
            boolean result = derive(startSymbol.toString(), line.trim());
            bw.write(result ? "accepted\n" : "not accepted\n");
        }
    }
}

public class second {
    public static void main(String[] args) throws IOException {
        ArrayList<Character> terminals = new ArrayList<>(Arrays.asList('a', 'b'));
        ArrayList<Character> nonTerminals = new ArrayList<>(Arrays.asList('S', 'A'));
        Character startSymbol = 'S';

        Map<Character, ArrayList<String>> productionRules = new HashMap<>();

        // Updated CFG rules to allow flexible ordering with a:b ratio 1:2
        productionRules.put('S', new ArrayList<>(Arrays.asList(
                "abbS", "babS", "bbaS", "bSbA", "" // new "bSbA" rule added
        )));
        productionRules.put('A', new ArrayList<>(Arrays.asList("a"))); // helper rule for 'a'

        CFGClass cfg = new CFGClass(terminals, nonTerminals, startSymbol, productionRules);

        BufferedReader br = new BufferedReader(new FileReader("input_cfg.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("output_cfg.txt"));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().equals("2")) {
                bw.write("2\n");
                break;
            }
        }

        cfg.solveProblem(br, bw);
        bw.write("x\n");

        br.close();
        bw.close();
    }
}
