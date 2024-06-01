import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Semantico implements Constants {
    public final SymbolTable symbolTable;
    public SemanticTable semanticTable;
    public SymbolTable symbolTableShow;
    public boolean auxDeclaracao = false;
    public boolean declaracao = false;    
    public boolean isInit = false;
    public boolean isOperation = false;
    public boolean isFunctionCall = false;
    public Symbol variable = new Symbol();
    public Symbol functionVariable = new Symbol();
    public Symbol functionSymbol = new Symbol();
    public String operation;
    public String dotData;
    public String tipo;
    public Stack<Integer> escopo = new Stack<>();
    public Stack<String> operacoes = new Stack<>();
    public List<String> warningList = new ArrayList<>();
    public int contadorEscopo;
    public int contadorParam;
    public int contadorCallParam;
    private CodeGenerator codeGenerator = new CodeGenerator();

    public Semantico() {
        this.symbolTable = new SymbolTable();
        this.symbolTableShow = new SymbolTable();
        contadorEscopo = 0;
        contadorParam = 0;
        contadorCallParam = 0;
        escopo.clear();
        escopo.push(contadorEscopo);

    }

    public void resetScope() {
        contadorEscopo = 0;
        while (escopo.size() > 1) {
            escopo.pop();
        }
        warningList.clear();
    }

    public void resetCodeGenerator() {
        this.codeGenerator.reset();
    }

    public CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }

    public void executeAction(int action, Token token) throws SemanticError {

        switch (action) {
            case 1:
                // Abre um novo escopo
                contadorEscopo++;
                contadorParam = 0;
                escopo.push(contadorEscopo);
                break;

            case 2:
                // Fecha o escopo atual e remove os símbolos associados a ele
                int escopoDesejado = escopo.pop();
                contadorEscopo--;
                symbolTable.removeSymbolsByScope(escopoDesejado);
                break;

            case 3:
                // Define o tipo da variável
                tipo = token.getLexeme();
                break;

            case 4:
                // Cria uma nova variável com o tipo definido
                if (!symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                    variable = new Symbol(token.getLexeme(), tipo, false, false, escopo.peek(), false, 0, false, false,
                            false, false, false, 0);
                }
                break;

            case 5:
                // Define a variável como um vetor
                variable.setVet(true);
                break;

            case 6:
                // Declaração de uma função
                if (!symbolTable.functionExists(variable)) {
                    contadorEscopo++;
                    escopo.push(contadorEscopo);
                    variable.setFunc(true);
                    symbolTable.addSymbol(variable);
                    symbolTableShow.addSymbol(variable);
                    variable.setEscopo(contadorEscopo);

                } else {
                    throw new SemanticError(String.format(
                            "Caro programador, a função de nome \"%s\" já foi previamente designada ao propósito em questão",
                            variable.getId()));
                }
                break;

            case 7:
                // Define a variável como uma função
                variable.setFunc(true);
                break;

            case 8:
                // Inicia uma declaração de variável
                declaracao = true;
                break;

            case 9:
                // Finaliza uma declaração de variável e a adiciona à tabela de símbolos
                if (symbolTable.symbolExists(variable.getId(), escopo.peek())) {
                    System.out.println("ENTREI CASE 9");
                    throw new SemanticError(
                            String.format("Prezado desenvolvedor, a variável \"%s\" já foi declarada",
                                variable.getId()));
                } else {
                    if (!variable.isFunc()) {
                        symbolTable.addSymbol(variable);
                        symbolTableShow.addSymbol(variable);
                    }
                    codeGenerator.declareVariable(variable.getId(), "0");
                    declaracao = false;
                }
                break;

            case 10:
                // Verifica se a variável está sendo declarada ou usada e adiciona à tabela de símbolos
                if (declaracao) {
                    if (symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                        System.out.println("ENTREI CASE 10");
                        throw new SemanticError(
                                String.format("Prezado desenvolvedor, a variável \"%s\" já foi declarada",
                                        token.getLexeme()));
                    }
                               
                    //declaracao = false;
                    symbolTable.addSymbol(variable);
                    symbolTableShow.addSymbol(variable);
                    dotData = token.getLexeme();

                } else {
                    variable = symbolTable.getSymbol(token.getLexeme());
                    if (!symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                        throw new SemanticError(
                                String.format("Prezado desenvolvedor, a variável \"%s\" não foi declarada.",
                                        token.getLexeme()),
                                token.getPosition());
                    }
                    dotData = token.getLexeme();
                }
                break;

            case 11:
                // Define a variável como inicializada e realiza operações de atribuição se houver
                if (variable != null) {
                    variable.setIni(true);
                }
                if(!isOperation && declaracao){
                    codeGenerator.declareVariable(dotData, token.getLexeme());
                    auxDeclaracao = true;
                    declaracao = false;
                }

                if (isOperation) {
                    codeGenerator.declareVariable(dotData, "0");
                    while (operacoes.size() > 3) {
                        String tipo1 = operacoes.pop();
                        String op = operacoes.pop();
                        String tipo2 = operacoes.pop();
                        int resultadoAtribuicao = SemanticTable.resultType(tipo1, tipo2, op);
                        if (Integer.valueOf(resultadoAtribuicao).equals(SemanticTable.ERR)) {
                            throw new SemanticError(
                                    String.format("Prezado desenvolvedor, a soma das variáveis " + tipo1 + " e " + tipo2
                                            + " não é possível"),
                                    token.getPosition());
                        } else if (resultadoAtribuicao == SemanticTable.WAR) {
                            warningList.add("\nA operação \"" + op + " entre \"" + tipo1 + "\" e \"" + tipo2
                                    + "\" pode causar perca de precisão");
                        }
                        operacoes.push(tipo1);
                    }  
                    isOperation = false;
                } 

                // Verifica o tipo da variável atribuída
                if (!operacoes.isEmpty() && SemanticTable.atribType(variable.getTipo(), operacoes.peek()) == -1) {
                    throw new SemanticError(
                            String.format(
                                    "Prezado desenvolvedor, a variável " + variable.getId() + " de tipo "
                                            + variable.getTipo() + " não pode ser declarada como " + operacoes.pop()));
                } else if (!operacoes.isEmpty() && SemanticTable.atribType(variable.getTipo(), operacoes.peek()) == 1) {
                    warningList.add("\nA atribuição de variavel de tipo \"" + variable.getTipo() + "\" como \""
                            + operacoes.peek() + "\" pode causar perca de precisão");
                }

                if(!auxDeclaracao){
                    codeGenerator.addInstruction("STO ", variable.getId());
                }
                operacoes.clear();
                break;

            case 12:
                // Verifica se o identificador foi declarado no escopo atual
                if (!symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                    throw new SemanticError(
                            String.format(
                                    "Prezado desenvolvedor, o identificador \"%s\" não foi declarado neste escopo.",
                                    token.getLexeme()),
                            token.getPosition());
                }
                break;

            case 13:
                // Verifica o tipo da variável na expressão
                if (!symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                    throw new SemanticError(
                            String.format(
                                    "Prezado desenvolvedor, o identificador \"%s\" não foi declarado neste escopo.",
                                    token.getLexeme()),
                            token.getPosition());
                }

                if(!isOperation){
                    System.out.println("ENTREI if 1");
                    codeGenerator.addInstruction("LD ", token.getLexeme());
                } else {
                    System.out.println("ENTREI if 2");

                    if(operacoes.peek() == "SUM"){
                        codeGenerator.addInstruction("ADD ", token.getLexeme());
                    } 
                    if(operacoes.peek() == "SUB"){
                        codeGenerator.addInstruction("SUB ", token.getLexeme());
                    } 
                }

                Symbol variableExp = symbolTable.getSymbol(token.getLexeme());
                variableExp.setUsada(true);
                operacoes.push(variableExp.getTipo());
                isOperation = false;

                break;

            case 14:
                operacoes.push("string");
                break;

            case 15:
                if(!isOperation && auxDeclaracao){
                    codeGenerator.addInstruction("LDI ", token.getLexeme());
                    auxDeclaracao = false;
                } else if(isOperation) {
                    if(operacoes.peek() == "SUM"){
                        codeGenerator.addInstruction("ADDI ", token.getLexeme());
                    } 
                    if(operacoes.peek() == "SUB"){
                        codeGenerator.addInstruction("SUBI ", token.getLexeme());
                    } 
                }

                operacoes.push("int");
                break;

            case 16:
                operacoes.push("float");
                break;

            case 17:
                operacoes.push("bool");
                break;

            case 18:
                operacoes.push("char");
                break;

            case 19:
                operacoes.push("REL");
                isOperation = true;
                break;

            case 20:
                operacoes.push("SUM");
                isOperation = true;

                break;

            case 21:
                // Define o tipo da operação como SUB (subtração) e indica que uma operação está
                // sendo realizada
                operacoes.push("SUB");
                isOperation = true;
                break;

            case 22:
                // Define o tipo da operação como MUL (multiplicação) e indica que uma operação
                // está sendo realizada
                operacoes.push("MUL");
                isOperation = true;
                break;

            case 23:
                // Define o tipo da operação como DIV (divisão) e indica que uma operação está
                // sendo realizada
                operacoes.push("DIV");
                isOperation = true;
                break;

            case 24:
                // Finaliza uma operação de atribuição e verifica os tipos
                if (isFunctionCall) {
                    contadorCallParam++;
                }

                if (isOperation) {
                    while (operacoes.size() > 3) {
                        String tipo1 = operacoes.pop();
                        String op = operacoes.pop();
                        String tipo2 = operacoes.pop();
                        int resultadoAtribuicao = SemanticTable.resultType(tipo1, tipo2, op);
                        if (resultadoAtribuicao == SemanticTable.ERR) {
                            throw new SemanticError(
                                    String.format("Prezado desenvolvedor, a soma das variáveis " + tipo1 + " e " + tipo2
                                            + " não é possível"),
                                    token.getPosition());
                        } else if (resultadoAtribuicao == SemanticTable.WAR) {
                            warningList.add("\nA operação \"" + op + " entre \"" + tipo1 + "\" e \"" + tipo2
                                    + "\" pode causar perca de precisão");
                        } else {
                            operacoes.push(tipo1);
                        }
                    }
                }

                operacoes.clear();
                isOperation = false;
                break;

            case 25:
                if (!symbolTable.functionExists(variable)) {
                    throw new SemanticError(String.format(
                            "Caro programador, a função de nome \"%s\" não foi designada",
                            variable.getId()));

                } else {
                    functionSymbol = new Symbol(variable); // Criar uma cópia do objeto variable
                    functionSymbol.setFunc(true);
                }
                break;
            case 26:
                if (symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                    variable.setParam(true);
                    contadorParam++;
                    variable.setPos(contadorParam);
                }

                break;

            case 27:
                Symbol lastFunction = symbolTable.getLastFunction();
                lastFunction.setQuantparam(contadorParam);

                break;

            case 28:
                isFunctionCall = true;
                contadorCallParam = 0;
                break;

            case 29:
                functionSymbol = symbolTable.getFunctionById(functionSymbol.getId());

                if (functionSymbol.getQuantparam() > contadorCallParam) {
                    throw new SemanticError(String.format(
                            "Caro programador, a função de nome \"%s\" Necessita de mais parametros",
                            variable.getId()));
                } else if (functionSymbol.getQuantparam() < contadorCallParam) {
                    throw new SemanticError(String.format(
                            "Caro programador, a função de nome \"%s\" Possui parametros que não foram expostos",
                            variable.getId()));
                }
                isFunctionCall = false;
                contadorCallParam = 0;
                break;

            case 30:
                contadorParam = 0;
                escopo.push(contadorEscopo);
                break;

        }

    }

}