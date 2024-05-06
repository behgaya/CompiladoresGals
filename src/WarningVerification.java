import java.util.HashSet;
import java.util.Set;

public class WarningVerification {
    
    public static String verificarVariaveisNaoUtilizadas(SymbolTable symbolTable) {
        StringBuilder variaveisNaoUtilizadas = new StringBuilder("Avisos: Identificadores declarados e não utilizados: ");
        Set<String> utilizados = new HashSet<>();
        
        for (Symbol symbol : symbolTable.getSymbols()) {
            if (!symbol.isUsada() && !symbol.isFunc()) {
                variaveisNaoUtilizadas.append(symbol.getId()).append(", ");
            } else {
                utilizados.add(symbol.getId());
            }
        }
        
        if (variaveisNaoUtilizadas.toString().equals("Avisos: Identificadores declarados e não utilizados: ")) {
            return "";
        } else {
            return variaveisNaoUtilizadas.toString();
        }
    }
    
    public static String verificarVariaveisNaoInicializadas(SymbolTable symbolTable) {
        StringBuilder variaveisNaoInicializadas = new StringBuilder("Avisos: Identificadores que não estão inicializados: ");
        
        for (Symbol symbol : symbolTable.getSymbols()) {
            if (!symbol.isIni() && !symbol.isFunc()) {
                variaveisNaoInicializadas.append(symbol.getId()).append(", ");
            }
        }
        
        if (variaveisNaoInicializadas.toString().equals("Avisos: Identificadores que não estão inicializados: ")) {
            return "";
        } else {
            return variaveisNaoInicializadas.toString();
        }
    }
}
