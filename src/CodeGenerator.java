import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {
    private List<String> codeLines;

    public CodeGenerator() {
        codeLines = new ArrayList<>();
    }



    public void reset() {
        codeLines.clear(); // Limpa a lista de linhas de código
    }

    public void declareVariable(String name, String declaration) {
        codeLines.add("VariableType " + name + ": " + declaration);
    }

    public void addInstruction(String operation, String token) {
        codeLines.add("Instruction " + operation + token);
    }

    public void generateAssembly(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String line : codeLines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getCodeLines() {
        return codeLines;
    }
}
