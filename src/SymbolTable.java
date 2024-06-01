import java.util.ArrayList;
import java.util.List;


public class SymbolTable {
    private List<Symbol> symbolTable = new ArrayList<Symbol>();

    public List<Symbol> getSymbols() {
        return symbolTable;
    }

    public void addSymbol(Symbol symbol) {
        symbolTable.add(symbol);
    }

    public boolean symbolExists(String id, int escopo) {
        for (int i = symbolTable.size() - 1; i >= 0; i--) {
            Symbol symbol = symbolTable.get(i);
            if (symbol.getId().equals(id) && symbol.getEscopo() <= escopo && !symbol.isFunc()) {
                return true;
            }
        }
        return false;
    }
    
    
    public boolean variableExists(Symbol addedSymbol, int escopo) {
        for (Symbol symbol : symbolTable) {
            if (symbol.getEscopo() <= escopo && symbol.getId().equals(addedSymbol.getId()) && !symbol.isFunc()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean functionExists(String id) {
        for (Symbol symbol : symbolTable) {
            if (symbol.isFunc() && symbol.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean functionExists(Symbol addedFunction) {
        for (Symbol symbol : symbolTable) {
            if (symbol.isFunc() && symbol.getId().equals(addedFunction.getId())) {
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
                return true; 
            }
        }
        return false; 
    }

    public Symbol getSymbol(String id) {
        for (Symbol symbol : symbolTable) {
            if (symbol.getId().equals(id)) {
                return symbol;
            }
        }
        return null; 
    }

    public void removeSymbolsByScope(int scope) {
        List<Symbol> symbolsToRemove = new ArrayList<>();
        for (Symbol symbol : symbolTable) {
            if (symbol.getEscopo() == scope && !symbol.isFunc()) {
                symbolsToRemove.add(symbol);
            }
        }
        symbolTable.removeAll(symbolsToRemove);
    }

    public List<Symbol> getSymbolsByScope(int scope) {
        List<Symbol> symbolsInScope = new ArrayList<>();

        for (Symbol symbol : symbolTable) {
            if (symbol.getEscopo() == scope) {
                symbolsInScope.add(symbol);
            }
        }

        
        return symbolsInScope;
    }

    public Symbol getLastFunction() {
        for (int i = symbolTable.size() - 1; i >= 0; i--) {
            Symbol symbol = symbolTable.get(i);
            if (symbol.isFunc()) {
                return symbol;
            }
        }
        return null; 
    }

    public Symbol getFunctionById(String id) {
        for (Symbol symbol : symbolTable) {
            if (symbol.isFunc() && symbol.getId().equals(id)) {
                return symbol;
            }
        }
        return null;
    }

    

}