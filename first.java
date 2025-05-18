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

        int maxLength = target.length() + 5;

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (current.equals(target))
                return true;

            String filtered = current.replaceAll("[^ab]", "");
            if (filtered.length() > target.length() || current.length() > maxLength)
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

public class first {
    public static void main(String[] args) throws IOException {
        ArrayList<Character> terminals = new ArrayList<>(Arrays.asList('a', 'b'));
        ArrayList<Character> nonTerminals = new ArrayList<>(Arrays.asList('S'));
        Character startSymbol = 'S';

        Map<Character, ArrayList<String>> productionRules = new HashMap<>();
        productionRules.put('S', new ArrayList<>(Arrays.asList(
                "", // ε
                "aSb",
                "bSa",
                "SS")));

        CFGClass cfg = new CFGClass(terminals, nonTerminals, startSymbol, productionRules);

        BufferedReader br = new BufferedReader(new FileReader("input_cfg.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("output_cfg.txt"));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().equals("1")) {
                bw.write("1\n");
                break;
            }
        }

        cfg.solveProblem(br, bw);
        bw.write("x\n");

        br.close();
        bw.close();
    }
}
