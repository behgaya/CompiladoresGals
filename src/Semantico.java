import java.util.List;

public class Semantico implements Constants
{
    public final SymbolTable symbolTable;

    public Semantico() {
        this.symbolTable = new SymbolTable();
    }

    public void executeAction(int action, Token token) throws SemanticError {
        Symbol variable = new Symbol();

        switch (action) {

            case 1:
                //System.out.println("Tipo:" + token.getLexeme());
                variable.setTipo(token.getLexeme());            
                break;
            case 2:
                //System.out.println("Nome:" + token.getLexeme());
                String id = token.getLexeme();
                // Verifica se o símbolo já existe na tabela
                variable.setId(id);
                variable.setIni(false);
                variable.setUsada(false);
                variable.setEscopo(null);
                variable.setParam(false);
                variable.setPos(0);
                variable.setVet(false);
                variable.setMatriz(false);
                variable.setRef(false);
                variable.setFunc(false);
                symbolTable.addSymbol(variable);
                // Após adicionar o símbolo, imprima a tabela de símbolos para verificar
                //symbolTable.printTable();

                break;
            case 3:
                
                System.out.println("Vetor:" + token.getLexeme());
                variable.setVet(true);
                break;
            case 4:
                variable.setFunc(true);
                break;
            case 5:

                break;
        }

    }
}
 