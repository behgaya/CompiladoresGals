import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class SymbolTable {
    private List<Symbol> symbolTable = new ArrayList<Symbol>();

    public List<Symbol> getSymbols() {
        return symbolTable;
    }

    // Method to add a symbol to the table
    public void addSymbol(Symbol symbol) {
        symbolTable.add(symbol);
    }

    public boolean symbolExists(Symbol addedSymbol) {
        for (Symbol symbol : symbolTable) {
            if (symbol.getId().equals(addedSymbol.getId()) && !symbol.isFunc()) {
                return true;
            }
        }
        return false;
    }

    public boolean functionExists(Symbol addedFunction) {
        for (Symbol symbol : symbolTable) {
            if (symbol.isFunc() && symbol.getId().equals(addedFunction.getId()) && symbol.getTipo().equals(addedFunction.getTipo())) {
                // Verifica se o símbolo é uma função e se possui a mesma assinatura (nome, tipo e parâmetros)
                return true;
            }
        }
        return false;
    }


    public void printTable() {
        System.out.println("ID\tTipo\tInicializado\tUsado\tEscopo\tParâmetro\tPosição\tVetor\tMatriz\tReferência\tFunção");
        for (Symbol symbol : symbolTable) {
            System.out.println(symbol.getId() + "\t" + symbol.getTipo() + "\t" + symbol.isIni() + "\t" + symbol.isUsada()
                + "\t" + symbol.getEscopo() + "\t" + symbol.isParam() + "\t" + symbol.getPos() + "\t" + symbol.isVet()
                + "\t" + symbol.isMatriz() + "\t" + symbol.isRef() + "\t" + symbol.isFunc());
        }
    }

    public void clearTable() {
        symbolTable.clear();
    }

    public boolean updateSymbol(Symbol updatedSymbol) {
        for (Symbol symbol : symbolTable) {
            if (symbol.getId().equals(updatedSymbol.getId())) {
                // Atualizar os atributos do símbolo existente com base no símbolo atualizado
                symbol.setTipo(updatedSymbol.getTipo());
                symbol.setIni(updatedSymbol.isIni());
                symbol.setUsada(updatedSymbol.isUsada());
                symbol.setEscopo(updatedSymbol.getEscopo());
                symbol.setParam(updatedSymbol.isParam());
                symbol.setPos(updatedSymbol.getPos());
                symbol.setVet(updatedSymbol.isVet());
                symbol.setMatriz(updatedSymbol.isMatriz());
                symbol.setRef(updatedSymbol.isRef());
                symbol.setFunc(updatedSymbol.isFunc());
                return true; // Indica que o símbolo foi atualizado com sucesso
            }
        }
        return false; // Indica que o símbolo com o ID fornecido não foi encontrado na tabela
    }
    // Outros métodos conforme necessário
}