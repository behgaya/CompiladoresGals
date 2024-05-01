public class Semantico implements Constants
{
    private SymbolTable symbolTable;

    public Semantico() {
        this.symbolTable = new SymbolTable();
    }

    public void executeAction(int action, Token token) throws SemanticError {
        System.out.println("A��o #"+action+", Token: "+token);

        switch (action) {
            case 1:
                // Obter o tipo e nome do token
                
                break;
            case 2:

                break;
            case 3:
                // Implementar a lógica para outras ações conforme necessário
                break;
            case 4:
                // Implementar a lógica para outras ações conforme necessário
                break;
            case 5:
                // Implementar a lógica para outras ações conforme necessário
                break;
        }
    }
}
 