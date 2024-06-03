import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.lang.model.element.VariableElement;

public class Semantico implements Constants {
    public final SymbolTable symbolTable;
    public SemanticTable semanticTable;
    public SymbolTable symbolTableShow;
    public boolean isDeclarationNotOperation = false;
    public boolean declaracao = false;
    public boolean isFunctionCall = false;
    public boolean isOperation = false;
    public boolean isPrint = false;
    public boolean isRead = false;

    public boolean isAttribution = true;
    public boolean attribIsVector = false;
    public boolean lastIsVec = false;
    public Symbol variable = new Symbol();
    public Symbol variableAux = new Symbol();
    public Symbol functionSymbol = new Symbol();
    public String attributionValue;
    public String tokenAux;
    public String dotData;
    public String tipo;
    public Stack<Integer> escopo = new Stack<>();
    public Stack<String> operacoes = new Stack<>();
    public List<String> warningList = new ArrayList<>();
    public List<String> vetorStrings = new ArrayList<>();
    public int contadorCallParam;
    public int valorTemporario = 1001;
    public int contadorEscopo;
    public int contadorParam;
    public int tamahoVetor = 0;
    public int contPrint = 0;


    private CodeGenerator codeGenerator = new CodeGenerator();

    public Semantico() {
        this.symbolTable = new SymbolTable();
        this.symbolTableShow = new SymbolTable();
        contadorEscopo = 0;
        contadorParam = 0;
        contadorCallParam = 0;
        valorTemporario = 1001;
        escopo.clear();
        operacoes.clear();
        codeGenerator.reset();
        startBooleans();
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
    public void resetAll() {
        this.symbolTable.clearTable();
        this.symbolTableShow.clearTable();
        this.variable = new Symbol();
        this.variableAux = new Symbol();
        this.functionSymbol = new Symbol();
        // this.tokenAux = "";
        // this.dotData = "";
        // this.tipo = "";
        this.escopo.clear();
        this.operacoes.clear();
        this.warningList.clear();
        this.vetorStrings.clear();
        this.valorTemporario = 1001;
        this.contadorEscopo = 0;
        this.contadorParam = 0;
        this.contadorCallParam = 0;
        this.codeGenerator.reset();
        startBooleans();
        escopo.push(contadorEscopo);
    }
    
    public void startBooleans() {
        isDeclarationNotOperation = false;
        declaracao = false;
        isFunctionCall = false;
        isOperation = false;
        isPrint = false;
        isAttribution = true;
        attribIsVector = false;
        lastIsVec = false;
    }

    public void processImmediateOperation(String token) {
        if (!isOperation) {
            codeGenerator.addInstruction("LDI ", token);
            System.out.println("processImmediateOperation: LDI " + token);
        }
        if (isOperation) {
            System.out.println("TESTETESTE: " + variableAux.getId());
            if (variableAux != null && variableAux.isVet() && !isAttribution) {
                System.out.println(variableAux.getId() + "\tAAAAAAAAAAAAAAAAAAAAAAA");
                codeGenerator.addInstruction("STO ", Integer.toString(valorTemporario));
                codeGenerator.addInstruction("LDI ", token);
                valorTemporario++;
            
            } else if (operacoes.peek() == "SUM") {
                System.out.println("processImmediateOperation: ADDI " + token);
                if(lastIsVec){
                    codeGenerator.addInstruction("STO ", Integer.toString(valorTemporario));
                    valorTemporario++;
                }
                codeGenerator.addInstruction("ADDI ", token);
            } else if (operacoes.peek() == "SUB") {
                System.out.println("processImmediateOperation: SUBI " + token);
                codeGenerator.addInstruction("SUBI ", token);
            }
        }
    }

    public void operationIsCompatible(Token token) throws SemanticError {
        System.out.println("operationIsCompatible");
        while (operacoes.size() > 3) {
            String tipo1 = operacoes.pop();
            String op = operacoes.pop();
            String tipo2 = operacoes.pop();
            System.out.println("tipo 1: " + tipo1 + "\top: " + op + "\ttipo 2:" + tipo2);
            int resultadoAtribuicao = SemanticTable.resultType(tipo1, tipo2, op);
            if (resultadoAtribuicao == SemanticTable.ERR) {
                throw new SemanticError(
                        String.format("Prezado desenvolvedor, a soma das variáveis " + tipo1 + " e " + tipo2
                                + " não é possível"),
                        token.getPosition());
            }
            if (resultadoAtribuicao == SemanticTable.WAR) {
                warningList.add("\nA operação \"" + op + " entre \"" + tipo1 + "\" e \"" + tipo2
                        + "\" pode causar perca de precisão");
            }
            operacoes.push(tipo1);
        }

    }

    public void semanticError(String msg, String id) throws SemanticError {
        startBooleans();
        operacoes.clear();
        throw new SemanticError(String.format(msg, id));
    }

    public void executeAction(int action, Token token) throws SemanticError {

        switch (action) {
            case 1:
                System.out.println("Case 1");
                // Abre um novo escopo
                contadorEscopo++;
                contadorParam = 0;
                escopo.push(contadorEscopo);
                break;

            case 2:
                System.out.println("Case 2");
                // Fecha o escopo atual e remove os símbolos associados a ele
                int escopoDesejado = escopo.pop();
                contadorEscopo--;
                symbolTable.removeSymbolsByScope(escopoDesejado);
                break;

            case 3:
                System.out.println("Case 3");
                // Define o tipo da variável
                tipo = token.getLexeme();
                break;

            case 4:
                System.out.println("Case 4");
                if (!symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                    variable = new Symbol(token.getLexeme(), tipo, false, false, escopo.peek(), false, 0, false, false,
                            false, false, false, 0, 0);
                } else {
                    variable = symbolTable.getSymbol(token.getLexeme());
                    //variableAux = symbolTable.getSymbol(token.getLexeme());
                }
                System.out.println("Case 4\tvariable: " + variable.getId() + "\ttoken: " + token.getLexeme());

                tokenAux = token.getLexeme();
                break;

            case 5:
                System.out.println("Case 5\tvariable: " + variable.getId() + "\ttoken: " + token.getLexeme());
                variable.setVet(true);
                tokenAux = token.getLexeme();
                if (symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                    variableAux = symbolTable.getSymbol(token.getLexeme());
                }

                break;

            case 6:
                System.out.println("Case 6");
                // Declaração de uma função
                if (symbolTable.functionExists(variable)) {
                    semanticError(
                            "Caro programador, a função de nome \"%s\" já foi previamente designada ao propósito em questão",
                            variable.getId());
                }

                contadorEscopo++;
                escopo.push(contadorEscopo);
                variable.setFunc(true);
                symbolTable.addSymbol(variable);
                symbolTableShow.addSymbol(variable);
                variable.setEscopo(contadorEscopo);
                break;

            case 7:
                System.out.println("Case 7");
                // Define a variável como uma função
                variable.setFunc(true);
                break;

            case 8:
                System.out.println("Case 8");
                // Inicia uma declaração de variável
                declaracao = true;
                break;

            case 9:
                System.out.println("Case 9");
                // Finaliza uma declaração de variável e a adiciona à tabela de símbolos
                if (symbolTable.symbolExists(variable.getId(), escopo.peek())) {
                    semanticError("Prezado desenvolvedor, a variável \"%s\" já foi declarada", variable.getId());
                }

                if (!variable.isFunc()) {
                    symbolTable.addSymbol(variable);
                    symbolTableShow.addSymbol(variable);
                    codeGenerator.declareVariable(variable.getId(), "0");
                }

                declaracao = false;
                break;
            case 10:
                System.out.println("Case 10");
                if (declaracao) {
                    if (symbolTable.symbolExists(variable.getId(), escopo.peek())) {
                        semanticError("Prezado desenvolvedor, a variável \"%s\" já foi declarada", variable.getId());
                    }

                    symbolTable.addSymbol(variable);
                    symbolTableShow.addSymbol(variable);
                    dotData = tokenAux;

                } else {
                    if (!symbolTable.symbolExists(variable.getId(), escopo.peek())) {
                        semanticError("Prezado desenvolvedor, a variável \"%s\" não foi declarada.", variable.getId());
                    }

                    dotData = tokenAux;
                }

                if(variable.isVet()){
                    attribIsVector = true;
                    isOperation = false;
                } else {
                    attribIsVector = false;
                }

                attributionValue = tokenAux;
                valorTemporario = 1001;
                isAttribution = true;
                break;
            case 11:
                System.out.println("Case 11");
                // Define a variável como inicializada e realiza operações de atribuição se
                // houver
                if (variable != null) {
                    variable.setIni(true);
                }

                // int x = 1; <==== Exemplo
                if (!isOperation && declaracao) {
                    codeGenerator.popInstruction();
                    codeGenerator.declareVariable(dotData, token);
                    isDeclarationNotOperation = true;
                }
                // int x = 1 + 1; <==== Exemplo
                if (isOperation && declaracao) {
                    codeGenerator.declareVariable(dotData, "0");
                }

                // x = 1 + 1; <==== Exemplo
                if (isOperation) {
                    operationIsCompatible(token);
                    isOperation = false;
                }
                // Verifica o tipo da variável atribuída
                if (!operacoes.isEmpty() && SemanticTable.atribType(variable.getTipo(), operacoes.peek()) == -1) {
                    throw new SemanticError(String.format("Prezado desenvolvedor, a variável " + variable.getId()
                            + " de tipo " + variable.getTipo() + " não pode ser declarada como " + operacoes.pop()));
                }
                if (!operacoes.isEmpty() && SemanticTable.atribType(variable.getTipo(), operacoes.peek()) == 1) {
                    warningList.add("\nA atribuição de variavel de tipo \"" + variable.getTipo() + "\" como \""
                            + operacoes.peek() + "\" pode causar perca de precisão");
                }

                if (!isDeclarationNotOperation) {
                    while(valorTemporario > 1001){
                        codeGenerator.addInstruction("STO ", Integer.toString(valorTemporario));
                        valorTemporario--;
                        codeGenerator.addInstruction("LD ", Integer.toString(valorTemporario));
                        valorTemporario++;
                        codeGenerator.addInstruction("ADD ", Integer.toString(valorTemporario));
                        valorTemporario--;
                    }
                    if(!attribIsVector){
                        if(symbolTable.getSymbol(attributionValue).isVet()){
                            codeGenerator.addInstruction("STOV ", attributionValue);
                        } else {
                            codeGenerator.addInstruction("STO ", attributionValue);
                        }    
                    }
                }

                if(lastIsVec && valorTemporario > 1001){
                    while(valorTemporario > 1001){
                        codeGenerator.addInstruction("STO ", Integer.toString(valorTemporario));
                        valorTemporario--;
                        codeGenerator.addInstruction("LD ", Integer.toString(valorTemporario));
                        valorTemporario++;
                        codeGenerator.addInstruction("ADD ", Integer.toString(valorTemporario));
                        valorTemporario--;
                    }
                    codeGenerator.addInstruction("STO ", Integer.toString(valorTemporario));
                    valorTemporario--;
                    codeGenerator.addInstruction("LD ", Integer.toString(valorTemporario));
                    valorTemporario++;
                    codeGenerator.addInstruction("ADD ", Integer.toString(valorTemporario));
                    System.out.println("STO " + tokenAux);
                    if(!attribIsVector){
                        if(symbolTable.getSymbol(attributionValue).isVet()){
                            codeGenerator.addInstruction("STOV ", attributionValue);
                        } else {
                            codeGenerator.addInstruction("STO ", attributionValue);
                        }
                    }
                }



                if (attribIsVector) {
                    codeGenerator.addInstruction("STO ", Integer.toString(valorTemporario));
                    codeGenerator.addInstruction("LD ", "1000");
                    codeGenerator.addInstruction("STO ", "$indr");
                    codeGenerator.addInstruction("LD ", Integer.toString(valorTemporario));
                    codeGenerator.addInstruction("STOV ", tokenAux);
                    valorTemporario++;
                }

                
                startBooleans();
                operacoes.clear();

                // attribIsVector = false;
                // isDeclarationNotOperation = false;
                // isAttribution = true;
                // declaracao = false;
                // isOperation = false;
                break;

            case 12:
                System.out.println("Case 12");
                // Verifica se o identificador foi declarado no escopo atual
                if (!symbolTable.symbolExists(tokenAux, escopo.peek())) {
                    semanticError("Prezado desenvolvedor, o identificador \"%s\" não foi declarado neste escopo.",
                            tokenAux);
                }
                break;

            case 13:
                System.out.println("Case 13");
                // Verifica o tipo da variável na expressão
                if (!symbolTable.symbolExists(tokenAux, escopo.peek())) {
                    semanticError("Prezado desenvolvedor, o identificador \"%s\" não foi declarado neste escopo.",
                            tokenAux);
                }
                variableAux = symbolTable.getSymbol(tokenAux);
                if (!isOperation) {
                    if (isAttribution) {
                        if (symbolTable.getSymbol(tokenAux).isVet()) {
                            //System.out.println("LDV: " + tokenAux);
                            codeGenerator.addInstruction("LDV ", tokenAux); // Carrega vetor
                            // valorTemporario++;
                        } else {
                            codeGenerator.addInstruction("LD ", tokenAux); // Carrega variável normal
                        }
                    }

                } else {
                    if (operacoes.peek() == "SUM") {
                        codeGenerator.addInstruction("ADD ", tokenAux);
                    }
                    if (operacoes.peek() == "SUB") {
                        codeGenerator.addInstruction("SUB ", tokenAux);
                    }
                }

                Symbol variableExp = symbolTable.getSymbol(variableAux.getId());
                variableExp.setUsada(true);
                operacoes.push(variableExp.getTipo());
                isOperation = true;
                break;

            case 14:
                System.out.println("Case 14");
                operacoes.push("string");
                break;

            case 15:
                System.out.println("Case 15");
                processImmediateOperation(token.getLexeme());
                operacoes.push("int");

                break;

            case 16:
                System.out.println("Case 16");
                processImmediateOperation(token.getLexeme());
                operacoes.push("float");
                break;

            case 17:
                System.out.println("Case 17");
                operacoes.push("bool");
                break;

            case 18:
                System.out.println("Case 18");
                operacoes.push("char");
                break;

            case 19:
                System.out.println("Case 19");
                operacoes.push("REL");
                isOperation = true;
                break;

            case 20:
                System.out.println("Case 20");
                if(variableAux.isVet()){
                    lastIsVec = true;
                }
                variableAux = new Symbol();

                operacoes.push("SUM");
                isOperation = true;
                break;

            case 21:
                System.out.println("Case 21");
                if(variableAux.isVet()){
                    lastIsVec = true;
                }
                variableAux = new Symbol();
                // Define o tipo da operação como SUB (subtração) e indica que uma operação está
                // sendo realizada
                operacoes.push("SUB");
                isOperation = true;
                break;

            case 22:
                System.out.println("Case 22");
                // Define o tipo da operação como MUL (multiplicação) e indica que uma operação
                // está sendo realizada
                operacoes.push("MUL");
                isOperation = true;
                break;

            case 23:
                System.out.println("Case 23");
                // Define o tipo da operação como DIV (divisão) e indica que uma operação está
                // sendo realizada
                operacoes.push("DIV");
                isOperation = true;
                break;

            case 24:
                System.out.println("Case 24");
                // Finaliza uma operação de atribuição e verifica os tipos
                if (isOperation) {
                    operationIsCompatible(token);
                }

                if (!variable.isVet()) {
                    if (isFunctionCall) {
                        contadorCallParam++;
                    }
                    if (isPrint) {
                        codeGenerator.addInstruction("STO ", "$out_port");
                        isPrint = false;
                    }
                } else {
                    //System.out.println("isAttribution: " + isAttribution);
                    if (isPrint) {
                        if(contPrint == 0){
                            codeGenerator.addInstruction("STO ", "$indr");
                            contPrint++;
                        } else {
                            codeGenerator.addInstruction("STO ", "$out_port");
                            contPrint = 0;
                        }
                    } else if (isAttribution) {
                        codeGenerator.addInstruction("STO ", "1000");
                    } else if (!isAttribution) {
                        codeGenerator.addInstruction("STO ", "$indr");
                        codeGenerator.addInstruction("LDV ", tokenAux);
                    }
                }

                operacoes.clear();
                isOperation = false;
                break;

            case 25:
                System.out.println("Case 25");
                if (!symbolTable.functionExists(variable)) {
                    semanticError("Caro programador, a função de nome \"%s\" não foi designada", variable.getId());
                }

                functionSymbol = new Symbol(variable); // Criar uma cópia do objeto variable
                functionSymbol.setFunc(true);
                break;
            case 26:
                System.out.println("Case 26");
                if (symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                    variable.setParam(true);
                    contadorParam++;
                    variable.setPos(contadorParam);
                }
                break;

            case 27:
                System.out.println("Case 27");
                Symbol lastFunction = symbolTable.getLastFunction();
                lastFunction.setQuantparam(contadorParam);
                break;

            case 28:
                System.out.println("Case 28");
                isFunctionCall = true;
                contadorCallParam = 0;
                break;

            case 29:
                System.out.println("Case 29");
                functionSymbol = symbolTable.getFunctionById(functionSymbol.getId());

                if (functionSymbol.getQuantparam() > contadorCallParam) {
                    semanticError("Caro programador, a função de nome \"%s\" Necessita de mais parametros",
                            variable.getId());
                }

                if (functionSymbol.getQuantparam() < contadorCallParam) {
                    semanticError("Caro programador, a função de nome \"%s\" Possui parametros que não foram expostos",
                            variable.getId());
                }

                isFunctionCall = false;
                contadorCallParam = 0;
                break;

            case 30:
                System.out.println("Case 30");
                contadorParam = 0;
                escopo.push(contadorEscopo);
                break;

            case 31:
                System.out.println("Case 31");
                isPrint = true;
                break;
            case 32:
                System.out.println("Case 32");
                if (!symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                    semanticError("Caro programador, a variavel \"%s\" não foi designada", variable.getId());
                }

                codeGenerator.addInstruction("LD ", "$in_port");
                codeGenerator.addInstruction("STO ", token.getLexeme());
                break;
            case 33:
                System.out.println("Case 33");
                if (isOperation) {
                    operationIsCompatible(token);
                }

                //codeGenerator.popInstruction();
                codeGenerator.addInstruction("STO ", "1000");
                codeGenerator.addInstruction("LDI ", Integer.toString(vetorStrings.size()));
                codeGenerator.addInstruction("STO ", "$indr");
                codeGenerator.addInstruction("LD ", "1000");

                codeGenerator.addInstruction("STOV ", variable.getId());

                vetorStrings.add(token.getLexeme());
                break;
            case 34:
                System.out.println("Case 34");
                if (!variable.isVet()) {
                    semanticError("Não é possivel adicionar um vetor a variável \"%s\"", dotData);
                }
                if (variable.isIni()) {
                    semanticError("A variavel \"%s\" já foi inicializada", variable.getId());
                }
                break;

            case 35:
                System.out.println("Case 35");

                codeGenerator.declareArray(variable.getId(), vetorStrings);
                vetorStrings.clear();
                variable.setIni(true);
                declaracao = false;
                operacoes.clear();
                isOperation = false;
                break;

            case 36:

                break;

            case 37:
                variableAux = new Symbol();
                isAttribution = false;
                break;

        }

    }

}