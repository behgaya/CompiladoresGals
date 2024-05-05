import java.util.List;
import java.util.Stack;

public class Semantico implements Constants {
    public final SymbolTable symbolTable;
    public SymbolTable symbolTableShow;
    public SemanticTable semanticTable;

    Symbol variable = new Symbol();

    public boolean declaracao = false;
    public boolean isInit = false;

    Stack<Integer> escopo = new Stack<>();
    Stack<String> operacoes = new Stack<>();

    int contadorEscopo = 0;

    public String tipo;

    public Semantico() {
        this.symbolTable = new SymbolTable();
        this.symbolTableShow = new SymbolTable();
        contadorEscopo = 0;
        escopo.push(contadorEscopo);
    }

    public void executeAction(int action, Token token) throws SemanticError {

        switch (action) {
            case 1:
                contadorEscopo++;
                escopo.push(contadorEscopo);
                // System.out.println("abre escopo");
                break;

            case 2:
                contadorEscopo--;
                int escopoDesejado = escopo.peek(); // Obtém o escopo desejado (o topo da pilha)
                symbolTable.removeSymbolsByScope(escopoDesejado);
                escopo.pop();
                break;
            // Tipo
            case 3:
                // System.out.println("Tipo:" + token.getLexeme());
                tipo = token.getLexeme();
                break;

            // Nome e criação variavel
            case 4:
                // System.out.println("Nome:" + token.getLexeme());

                if (!symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                    variable = new Symbol(token.getLexeme(), tipo, false, false, escopo.peek(), false, 0, false, false,
                            false, false, false);
                }

                break;

            // Vetor
            case 5:
                // System.out.println("Vetor:" + token.getLexeme());
                variable.setVet(true);
                break;

            // Função
            case 6:
                // System.out.println("Funcao:" + token.getLexeme());
                if (!symbolTable.functionExists(variable)) {
                    variable.setFunc(true);
                    symbolTable.addSymbol(variable);
                    symbolTableShow.addSymbol(variable);


                } else {
                    throw new SemanticError(
                            String.format(
                                    "Caro programador, a função de nome %s já foi previamente designada ao propósito em questão",
                                    variable.getId()),
                            token.getPosition());
                }

                break;

            // Procedimento
            case 7:
                // System.out.println("Procedimento:" + token.getLexeme());
                variable.setFunc(true);
                break;

            case 8:
                declaracao = true;
                break;

            case 9:
                if (symbolTable.variableExists(variable, escopo.peek())) {
                    throw new SemanticError(
                            String.format("Prezado desenvolvedor, a variável '%s' já foi declarada", variable.getId()),
                            token.getPosition());
                } else {
                    symbolTable.addSymbol(variable);
                    symbolTableShow.addSymbol(variable);

                    declaracao = false;
                }

                break;
            case 10:
                if (declaracao) { // Verifica se está em modo de declaração
                    if (symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                        throw new SemanticError(
                                String.format("Prezado desenvolvedor, a variável '%s' já foi declarada",
                                        variable.getId()),
                                token.getPosition());
                    } else {
                        // System.out.println("Variável declarada: " + variable.getId());
                        // Atualiza o estado de declaração
                        declaracao = false;
                        // Adiciona o símbolo à tabela
                        symbolTable.addSymbol(variable);
                        symbolTableShow.addSymbol(variable);


                    }

                } else {
                    variable = symbolTable.getSymbol(token.getLexeme());
                    // Verifica a existência da variável apenas quando não está em modo de
                    // declaração
                    if (!symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                        throw new SemanticError(
                                String.format("Prezado desenvolvedor, a variável '%s' não foi declarada.",
                                        token.getLexeme()),
                                token.getPosition());
                    }

                    while(operacoes.size() > 1) {
                        String tipo1 = operacoes.pop();
                        String tipo2 = operacoes.pop();
                        System.out.println("TIPO 1: " + tipo1 + "\t TIPO 2: " + tipo2);

                        int resultadoAtribuicao = SemanticTable.atribType(tipo1, tipo2);
                        if (resultadoAtribuicao == SemanticTable.ERR) {
                            throw new SemanticError(
                                String.format("Prezado desenvolvedor, a soma das variaveis " + tipo1 + " e " + tipo2 + " não é possivel"),
                                token.getPosition()); 

                        } else if (resultadoAtribuicao == SemanticTable.WAR) {

                        } else {
                            operacoes.push(tipo1);
                        }
                    } 

                    //semanticTable.atribType(, escopoDesejado)
                    // System.out.println("Variável já declarada: " + variable.getId());
                }

                break;

            case 11:
                if (variable != null) {
                    // System.out.println("CASE 11: " + variable.getId());
                    variable.setIni(true);

                }

                break;

            case 12:
                if (!symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                    throw new SemanticError(
                            String.format("Prezado desenvolvedor, o identificador '%s' não foi declarado neste escopo.",
                                    token.getLexeme()),
                            token.getPosition());
                }
                break;
            case 13:
                if (!symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                    throw new SemanticError(
                            String.format("Prezado desenvolvedor, o identificador '%s' não foi declarado neste escopo.",
                                    token.getLexeme()),
                            token.getPosition());
                }

                Symbol variableExp = symbolTable.getSymbol(token.getLexeme());
                operacoes.push(variableExp.getTipo());


                break;
            
            case 14:
                //System.out.println(token.getLexeme() + " é uma STRING");
                operacoes.push("string");
                break;
            
            case 15:
                System.out.println(token.getLexeme() + " é um int");
                operacoes.push("int");
                break;
                
            case 16:
                System.out.println(token.getLexeme() + " é um float");

                operacoes.push("float");
                break;

            case 17:
                //System.out.println(token.getLexeme() + " é um bool");

                operacoes.push("bool");
                break;
            
            case 18: 
            
                System.out.println("CASE 18: " + token.getLexeme());
        }

    }
}