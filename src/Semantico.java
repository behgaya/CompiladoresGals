import java.util.List;

public class Semantico implements Constants
{
    public final SymbolTable symbolTable;
    Symbol variable = new Symbol();
    public boolean declaracao = false;

    public String tipo;

    public Semantico() {
        this.symbolTable = new SymbolTable();
    }

    public void executeAction(int action, Token token) throws SemanticError {

        switch (action) {
            // Tipo
            case 1:
                System.out.println("Tipo:" + token.getLexeme());
                tipo = token.getLexeme();
                break;

            // Nome e criação variavel
            case 2:
                //System.out.println("Nome:" + token.getLexeme());
                variable = new Symbol(token.getLexeme(), tipo, false, false, null, false, 0, false, false, false, false, false);
                break;

            // Vetor
            case 3:
                //System.out.println("Vetor:" + token.getLexeme());
                variable.setVet(true);
                break;

            // Função
            case 4:
                //System.out.println("Funcao:" + token.getLexeme());
                if(!symbolTable.functionExists(variable)){
                    variable.setFunc(true);
                    symbolTable.addSymbol(variable);
                } else {
                    throw new SemanticError(
                        String.format("Função %s já declarada", variable.getId()),
                        token.getPosition()); 
                }


                break;
            
            // Procedimento
            case 5:
                //System.out.println("Procedimento:" + token.getLexeme());
                variable.setFunc(true);
                break;

            case 6:
                declaracao = true;
                break; 
            case 7:
                if(symbolTable.symbolExists(variable)){
                    throw new SemanticError(
                        String.format("Variavel %s já declarada", variable.getId()),
                        token.getPosition());
                } else {
                    System.out.println("variavel declarada:" + variable.getId());
                    symbolTable.addSymbol(variable);
                    declaracao = false;
                }
  
                break;
            case 8:
                if(!declaracao){
                    if(!symbolTable.symbolExists(variable)){
                        throw new SemanticError(
                            String.format("Variavel %s não declarada", variable.getId()),
                            token.getPosition()); 
                    }
                } else {
                    if(symbolTable.symbolExists(variable)){
                        throw new SemanticError(
                            String.format("Variavel %s já declarada", variable.getId()),
                            token.getPosition());
                    } else {
                        System.out.println("variavel declarada:" + variable.getId());
                        symbolTable.addSymbol(variable);
                        declaracao = false;

                    }
                }
 
                break;

            case 9:
                variable.setIni(true);
                break;

        }

    }
}