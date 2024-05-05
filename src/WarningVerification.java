public class WarningVerification {
    public static String verificarVariaveisNaoInicializadas(SymbolTable symbolTable) {
        StringBuilder variaveisNaoInicializadas = new StringBuilder("Variáveis não inicializadas: ");
        for (Symbol symbol : symbolTable.getSymbols()) {
            if (!symbol.isIni() && !symbol.isFunc()) {
                variaveisNaoInicializadas.append(symbol.getId()).append(", ");
            }
        }
        if (variaveisNaoInicializadas.toString().equals("Variáveis não inicializadas: ")) {
            return "Compilado com sucesso!";
        } else {
            return variaveisNaoInicializadas.toString();
        }
    }

}
