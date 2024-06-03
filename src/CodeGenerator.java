import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class CodeGenerator {
    private Stack<String> codeLines;

    public CodeGenerator() {
        codeLines = new Stack<>();
    }



    public void reset() {
        codeLines.clear(); // Limpa a lista de linhas de código
    }

    public void declareVariable(String name, Token token) {

        codeLines.push("VariableType " + name + ": " + token.getLexeme());
    }

    public void declareVariable(String name, String token) {

        codeLines.push("VariableType " + name + ": " + token);
    }

    public void declareArray(String name, List<String> elements) {
        StringBuilder arrayDeclaration = new StringBuilder();
        arrayDeclaration.append("VariableType ");

        arrayDeclaration.append(name).append(": ");
    
        // Adiciona os elementos do vetor separados por vírgula
        arrayDeclaration.append(String.join(",", elements));
    
        codeLines.push(arrayDeclaration.toString());
    }

    public void addInstruction(String operation, String token) {
        codeLines.push("Instruction " + operation + token);
    }

    public void generateAssembly(String fileName) {
        // Temporarily reverse the stack to write the instructions in the correct order
        List<String> reversedLines = new ArrayList<>(codeLines);
        Collections.reverse(reversedLines);
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String line : reversedLines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String popInstruction() {
        if (!codeLines.isEmpty()) {
            return codeLines.pop();
        } else {
            return null; // or throw an exception if preferred
        }
    }

    public String peekInstruction() {
        if (!codeLines.isEmpty()) {
            return codeLines.peek();
        } else {
            return null; // or throw an exception if preferred
        }
    }
    
    public List<String> getCodeLines() {
        return new ArrayList<>(codeLines);
    }
}
