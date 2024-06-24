import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
    public boolean isPlusPlus = false;

    public boolean isRead = false;
    public String loopType = "";
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
    public String tempDir;
    public String oprel;
    public String rotFim;
    public String rotIni;
    public Stack<Integer> escopo = new Stack<>();
    public Stack<String> operacoes = new Stack<>();
    public Stack<String> operarit = new Stack<>();
    public Stack<String> labelDo = new Stack<>();
    public Stack<String> labelGeneric = new Stack<>();
    public List<String> warningList = new ArrayList<>();
    public List<String> vetorStrings = new ArrayList<>();
    public int contadorCallParam;
    public int valorTemporario = 1001;
    public int contadorEscopo;
    public int contadorParam;
    public int contPrint = 0;
    public String variableIncrement; 

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
        operarit.clear();
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
        this.labelGeneric.clear();
        this.escopo.clear();
        this.operacoes.clear();
        this.operarit.clear();
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
        isRead = false;

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
            if (variableAux != null && variableAux.isVet() && !isAttribution) {

                codeGenerator.addInstruction("STO ", Integer.toString(valorTemporario));
                codeGenerator.addInstruction("LDI ", token);
                valorTemporario++;

            } else if (operacoes.peek() == "SUM") {
                System.out.println("processImmediateOperation: ADDI " + token);
                if (lastIsVec) {
                    codeGenerator.addInstruction("STO ", Integer.toString(valorTemporario));
                    valorTemporario++;
                }
                codeGenerator.addInstruction("ADDI ", token);
            } else if (operacoes.peek() == "SUB") {
                System.out.println("processImmediateOperation: SUBI " + token);
                operarit.pop();
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
            // System.out.println("tipo 1: " + tipo1 + "\top: " + op + "\ttipo 2:" + tipo2);
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

    public void translateRelationalOperator(String oprel, String rotIni) {
        switch (oprel) {
            case ">":
                codeGenerator.addInstruction("BLE ", rotIni);
                break;
            case "<":
                codeGenerator.addInstruction("BGE ", rotIni);
                break;
            case "==":
                codeGenerator.addInstruction("BNE ", rotIni);
                break;
            case "!=":
                codeGenerator.addInstruction("BEQ ", rotIni);
                break;
            case ">=":
                codeGenerator.addInstruction("BLT ", rotIni);
                break;
            case "<=":
                codeGenerator.addInstruction("BGT ", rotIni);
                break;
            default:
                throw new IllegalArgumentException("Operador relacional desconhecido: " + oprel);
        }
    }

    public void addInstruction(String operation, String token){
        codeGenerator.addInstruction(operation, token);
    }
    

    public void executeAction(int action, Token token) throws SemanticError {

        switch (action) {
            case 1:
                System.out.println("Case 1");
                // Abre um novo escopo
                contadorEscopo++;
                contadorParam = 0;
                if(!labelGeneric.isEmpty())
                {
                    if(labelGeneric.peek().equals("if"))
                    {
                        String rotIf = codeGenerator.generateLabel("R");

                        labelDo.push(rotIf);
                        translateRelationalOperator(oprel, rotIf);
                    }
                }
                escopo.push(contadorEscopo);
                break;

            case 2:
                System.out.println("Case 2");
                // Fecha o escopo atual e remove os símbolos associados a ele
                int escopoDesejado = escopo.pop();
                contadorEscopo--;
                if(!labelGeneric.isEmpty())
                {
                    if(labelGeneric.peek().equals("if") || labelGeneric.peek().equals("else"))
                    {
                        rotFim = labelDo.pop();
                        codeGenerator.addLabel(rotFim);                       
                    } 
                    if(labelGeneric.peek().equals("for"))
                    {
                        variableIncrement = labelDo.pop();
                        if(isPlusPlus){
                            addInstruction("LD ", variableIncrement);
                            addInstruction("ADDI ", "1");
                            addInstruction("STO ", variableIncrement);
                        } else {
                            addInstruction("LD ", variableIncrement);
                            addInstruction("SUBI ", "1");
                            addInstruction("STO ", variableIncrement);
                        }
                        String fimFor = labelDo.pop();
                        String iniFor = labelDo.pop();
                        System.out.println("fimFor: " + fimFor);
                        System.out.println("fimFor: " + fimFor);

                        addInstruction("JMP ", iniFor);
                        codeGenerator.addLabel(fimFor);
                    }
                    escopo.pop();
                    labelGeneric.pop();
                }

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
                tipo = token.getLexeme();
                // Define a variável como uma função
                break;
            case 7:
                System.out.println("Case 7");
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

                if (variable.isVet()) {
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
                    while (valorTemporario > 1001) {
                        addInstruction("STO ", Integer.toString(valorTemporario));
                        valorTemporario--;
                        addInstruction("LD ", Integer.toString(valorTemporario));
                        valorTemporario++;
                        System.out.println("operarit.peek()" + operarit.peek());
                        if (operarit.peek().equals("SUM")) {
                            System.out.println("ENtrei SOma");
                            operarit.pop();
                            if (operarit.peek().equals("SUB")) {
                                addInstruction("SUB ", Integer.toString(valorTemporario));
                            } else {
                                addInstruction("ADD ", Integer.toString(valorTemporario));
                            }
                        } else if (operarit.peek().equals("SUB")) {
                            addInstruction("SUB ", Integer.toString(valorTemporario));
                            operarit.pop();
                        }
                        valorTemporario--;
                    }
                    if (!attribIsVector) {
                        if (symbolTable.getSymbol(attributionValue).isVet()) {
                            addInstruction("STOV ", attributionValue);
                        } else {
                            addInstruction("STO ", attributionValue);
                        }
                    }
                }

                if (lastIsVec && valorTemporario > 1001) {
                    while (valorTemporario > 1001) {
                        addInstruction("STO ", Integer.toString(valorTemporario));
                        valorTemporario--;
                        addInstruction("LD ", Integer.toString(valorTemporario));
                        valorTemporario++;
                        System.out.println("operarit.peek()" + operarit.peek());
                        if (operarit.peek().equals("SUM")) {
                            System.out.println("ENtrei SOma");
                            operarit.pop();
                            if (operarit.peek().equals("SUB")) {
                                addInstruction("SUB ", Integer.toString(valorTemporario));
                            } else {
                                addInstruction("ADD ", Integer.toString(valorTemporario));
                            }
                        } else if (operarit.peek().equals("SUB")) {
                            System.out.println("ENtrei sub");
                            addInstruction("SUB ", Integer.toString(valorTemporario));
                            operarit.pop();

                        }
                        valorTemporario--;
                    }
                    addInstruction("STO ", Integer.toString(valorTemporario));
                    valorTemporario--;
                    addInstruction("LD ", Integer.toString(valorTemporario));
                    valorTemporario++;
                    System.out.println("operarit.peek()" + operarit.peek());
                    if (operarit.peek().equals("SUM")) {
                        System.out.println("ENtrei SOma");
                        operarit.pop();
                        if (operarit.peek().equals("SUB")) {
                            addInstruction("SUB ", Integer.toString(valorTemporario));
                        } else {
                            addInstruction("ADD ", Integer.toString(valorTemporario));
                        }
                    } else if (operarit.peek().equals("SUB")) {
                        System.out.println("ENtrei sub");
                        addInstruction("SUB ", Integer.toString(valorTemporario));
                        operarit.pop();

                    }
                    System.out.println("STO " + tokenAux);
                    if (!attribIsVector) {
                        if (symbolTable.getSymbol(attributionValue).isVet()) {
                            addInstruction("STOV ", attributionValue);
                        } else {
                            addInstruction("STO ", attributionValue);
                        }
                    }
                }

                if (attribIsVector) {
                    addInstruction("STO ", Integer.toString(valorTemporario));
                    addInstruction("LD ", "1000");
                    addInstruction("STO ", "$indr");
                    addInstruction("LD ", Integer.toString(valorTemporario));
                    addInstruction("STOV ", attributionValue);
                    valorTemporario++;
                }

                startBooleans();
                operacoes.clear();

                break;

            case 12:
                System.out.println("Case 12");
                // Verifica se o identificador foi declarado no escopo atual
                if (!symbolTable.symbolExists(tokenAux, escopo.peek())) {
                    semanticError("Prezado desenvolvedor, o identificador \"%s\" não foi declarado neste escopo.",
                            tokenAux);
                }
                labelDo.push(tokenAux);
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
                    if (symbolTable.getSymbol(tokenAux).isVet()) {
                        addInstruction("LDV ", tokenAux); // Carrega vetor
                    } else {
                        addInstruction("LD ", tokenAux); // Carrega variável normal
                    }

                } 
                else {    
                    if (operacoes.peek() == "SUM") {
                        addInstruction("ADD ", tokenAux);
                    }
                    if (operacoes.peek() == "SUB") {
                        addInstruction("SUB ", tokenAux);
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
                System.out.println("Case 19: " + token.getLexeme());
                operacoes.push("REL");
                addInstruction("STO ", "1001");
                oprel = token.getLexeme();
                isOperation = true;
                break;

            case 20:
                System.out.println("Case 20");
                if (variableAux.isVet()) {
                    lastIsVec = true;
                }
                if ((token.getLexeme()).equals("+")) {
                    variableAux = new Symbol();
                    operarit.push("SUM");
                    operacoes.push("SUM");
                }
                if ((token.getLexeme()).equals("-")) {
                    variableAux = new Symbol();
                    operarit.push("SUB");
                    operacoes.push("SUB");
                }
                if ((token.getLexeme()).equals("*")) {
                    operacoes.push("MUL");
                }
                if ((token.getLexeme()).equals("/")) {
                    operacoes.push("DIV");
                }

                isOperation = true;
                break;

            case 21:
                System.out.println("Case 21");
                // Finaliza uma operação de atribuição e verifica os tipos
                if (isOperation) {
                    operationIsCompatible(token);
                    isOperation = false;
                    operacoes.clear();

                }

                if (!variable.isVet()) {
                    if (isFunctionCall) {
                        contadorCallParam++;
                        return;
                    }
                    if (isPrint) {
                        addInstruction("STO ", "$out_port");
                        isPrint = false;
                        return;
                    }
                } 
                
                if(variable.isVet()){
                    System.out.println("Entrei : if(variable.isVet())" + isAttribution);
                    if (isPrint) {
                        if (contPrint == 0) {
                            addInstruction("STO ", "$indr");
                            contPrint++;
                        } else {
                            addInstruction("STO ", "$out_port");
                            contPrint = 0;
                            isPrint = false;
                        }
                        return;

                    } 
                    if(!isRead){
                        if (isAttribution) {
                            addInstruction("STO ", "1000");
                        } else if (!isAttribution) {
                            addInstruction("STO ", "$indr");
                        }
                        return;

                    }
                    //return;
                } 
                
                if(!labelGeneric.isEmpty()){
                    if(labelGeneric.peek().equals("do"))
                    {
                        String IniDo = labelDo.pop();
                        translateRelationalOperator(oprel, IniDo);
                    } 
                    else if(labelGeneric.peek().equals("while"))
                    {
                        System.out.println("Entrei : else if(labelGeneric.peek().equals(\"while\"))" + isAttribution);
                        String fimWhile = codeGenerator.generateLabel("W");
                        labelDo.push(fimWhile);
                        translateRelationalOperator(oprel, fimWhile);
                    } 
                    else if(labelGeneric.peek().equals("for"))
                    {
                        String fimFor = codeGenerator.generateLabel("F");
                        labelDo.push(fimFor);
                        translateRelationalOperator(oprel, fimFor);
                    }
                }

                break;

            case 22:
                System.out.println("Case 22");
                if (!symbolTable.functionExists(variable)) {
                    semanticError("Caro programador, a função de nome \"%s\" não foi designada", variable.getId());
                }

                functionSymbol = new Symbol(variable); // Criar uma cópia do objeto variable
                functionSymbol.setFunc(true);
                break;

            case 23:
                System.out.println("Case 23");
                if (symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                    variable.setParam(true);
                    contadorParam++;
                    variable.setPos(contadorParam);
                }
                break;

            case 24:
                System.out.println("Case 24");
                Symbol lastFunction = symbolTable.getLastFunction();
                lastFunction.setQuantparam(contadorParam);
                break;

            case 25:
                System.out.println("Case 25");
                isFunctionCall = true;
                contadorCallParam = 0;
                break;
            case 26:
                System.out.println("Case 26");
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

            case 27:
                System.out.println("Case 27");
                ///////////////////////////////////////
                // For
                ///////////////////////////////////////
                //contadorParam = 0;
                labelGeneric.push("for");
                contadorEscopo++;
                escopo.push(contadorEscopo);

                break;

            case 28:
                System.out.println("Case 28");
                isPrint = true;
                break;

            case 29:
                System.out.println("Case 29");
                if (!symbolTable.symbolExists(variable.getId(), escopo.peek())) {
                    semanticError("Caro programador, a variavel \"%s\" não foi designada", variable.getId());
                }
                if (variable.isVet()) {
                    
                    addInstruction("STO ", "$indr");
                    addInstruction("LD ", "$in_port");
                    addInstruction("STOV ", variable.getId());

                    isRead = false;
                } else {
                    addInstruction("LD ", "$in_port");
                    addInstruction("STO ", variable.getId());
                }

                break;

            case 30:
                System.out.println("Case 30");
                if (isOperation) {
                    operationIsCompatible(token);
                }


                addInstruction("STO ", "1000");
                addInstruction("LDI ", Integer.toString(vetorStrings.size()));
                addInstruction("STO ", "$indr");
                addInstruction("LD ", "1000");
                addInstruction("STOV ", variable.getId());

                vetorStrings.add(token.getLexeme());
                break;

            case 31:
                System.out.println("Case 31");
                if (!variable.isVet()) {
                    semanticError("Não é possivel adicionar um vetor a variável \"%s\"", dotData);
                }
                if (variable.isIni()) {
                    semanticError("A variavel \"%s\" já foi inicializada", variable.getId());
                }
                break;
            case 32:
                System.out.println("Case 32");
                codeGenerator.declareArray(variable.getId(), vetorStrings);
                vetorStrings.clear();
                variable.setIni(true);
                declaracao = false;
                operacoes.clear();
                isOperation = false;
                isAttribution = false;
                break;
            case 33:
                System.out.println("Case 33");
                variableAux = new Symbol();
                isAttribution = false;
                break;
            case 34:
                System.out.println("Case 34");
                tempDir = token.getLexeme();
                if (symbolTable.symbolExists(token.getLexeme(), escopo.peek())) {
                    addInstruction("LD ", tempDir);
                } else {
                    addInstruction("LDI ", tempDir);

                }

                addInstruction("STO ", "1002");
                addInstruction("LD ", "1001");
                addInstruction("SUB ", "1002");

                break;

            case 35:
                ///////////////////////////////////////
                // KEYWORD_IF#35
                ///////////////////////////////////////
                System.out.println("Case 35");
                labelGeneric.push("if");
                break;

            case 36:
                ///////////////////////////////////////
                // KEYWORD_ELSE#36
                ///////////////////////////////////////
                System.out.println("Case 36");
                labelGeneric.push("else");
                codeGenerator.popInstruction();
                String rotIf = codeGenerator.generateLabel("R");
                String rotFim = codeGenerator.generateLabel("R");
                addInstruction("JMP ", rotFim);
                System.out.println("addlabel:\t" + rotIf);
                codeGenerator.addLabel(rotIf);
                break;

            case 37:
                System.out.println("Case 37");
                labelGeneric.push("do");
                /////////////////////////////////////////
                // KEYWORD_DO#38
                /////////////////////////////////////////
                rotIni = codeGenerator.generateLabel("D");
                labelDo.push(rotIni);
                codeGenerator.addLabel(rotIni);
                break;
            case 38:
                /////////////////////////////////////////
                // KEYWORD_WHILE#37
                /////////////////////////////////////////
                //labelGeneric.push("do");
                break;
            case 39:
                System.out.println("Case 39");
                labelGeneric.push("while");
                /////////////////////////////////////////
                // KEYWORD_WHILE#39
                ////////////////////////////////////////
                String IniDo = codeGenerator.generateLabel("W");
                labelDo.push(IniDo);
                codeGenerator.addLabel(IniDo);
                break;
            case 40:
                System.out.println("Case 40");
                ////////////////////////////////////////////
                // WHILE
                ////////////////////////////////////////////
                String fimWhile = labelDo.pop();
                String IniWhile = labelDo.pop();
                addInstruction("JMP ", IniWhile);
                codeGenerator.addLabel(fimWhile);
                break;  
            
            case 41:
                ////////////////////////////////////////////
                // SEMICOLON#41
                ////////////////////////////////////////////
                String iniFor = codeGenerator.generateLabel("F");
                labelDo.push(iniFor);
                codeGenerator.addLabel(iniFor);

                break;
            case 42:
                ////////////////////////////////////////////
                // <incremento>#42
                ////////////////////////////////////////////
                //labelDo.pop();
                break;
            case 43:
                if(token.getLexeme().equals("++")){
                    isPlusPlus = true;
                } else {
                    isPlusPlus = false;
                }
                break;
            case 44:
                isRead = true;
                break;


        }

    }

}
