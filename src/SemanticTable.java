/*
 * Template to help verify type compatibility in a Semantic Analyzer.
 * Available to Computer Science course at UNIVALI.
 * Professor Eduardo Alves da Silva.
 */

import java.util.HashMap;
import java.util.Map;

public class SemanticTable {

    public static final int ERR = -1;
    public static final int OK_ = 0;
    public static final int WAR = 1;

    public static final int INT = 0;
    public static final int FLO = 1;
    public static final int CHA = 2;
    public static final int STR = 3;
    public static final int BOO = 4;

    public static final int SUM = 0;
    public static final int SUB = 1;
    public static final int MUL = 2;
    public static final int DIV = 3;
    public static final int REL = 4; // qualquer operador relacional

    // TIPO DE RETORNO DAS EXPRESSOES ENTRE TIPOS
    // 5 x 5 X 5 = TIPO X TIPO X OPER
    static int expTable[][][] = {    /* INT */                    /* FLOAT */                  /* CHAR */                   /* STRING */                /* BOOL */
                        /* INT */ { { INT, INT, INT, FLO, BOO }, { FLO, FLO, FLO, FLO, BOO }, { ERR, ERR, ERR, ERR, ERR }, { ERR, ERR, ERR, ERR, ERR }, { ERR, ERR, ERR, ERR, ERR } },
                      /* FLOAT */ { { FLO, FLO, FLO, FLO, BOO }, { FLO, FLO, FLO, FLO, BOO }, { ERR, ERR, ERR, ERR, ERR }, { ERR, ERR, ERR, ERR, ERR }, { ERR, ERR, ERR, ERR, ERR } },
                       /* CHAR */ { { ERR, ERR, ERR, ERR, ERR }, { ERR, ERR, ERR, ERR, ERR }, { ERR, ERR, ERR, ERR, BOO }, { ERR, ERR, ERR, ERR, ERR }, { ERR, ERR, ERR, ERR, ERR } },
                     /* STRING */ { { ERR, ERR, ERR, ERR, ERR }, { ERR, ERR, ERR, ERR, ERR }, { ERR, ERR, ERR, ERR, ERR }, { STR, ERR, ERR, ERR, BOO }, { ERR, ERR, ERR, ERR, ERR } },
                       /* BOOL */ { { ERR, ERR, ERR, ERR, ERR }, { ERR, ERR, ERR, ERR, ERR }, { ERR, ERR, ERR, ERR, ERR }, { ERR, ERR, ERR, ERR, ERR }, { ERR, ERR, ERR, ERR, BOO } }
    };

    // atribuicoes compativeis
    // 5 x 5 = TIPO X TIPO
    static int atribTable[][] = { /* INT FLO CHA STR BOO */
            /* INT */ { OK_, WAR, ERR, ERR, ERR },
            /* FLO */ { OK_, OK_, ERR, ERR, ERR },
            /* CHA */ { ERR, ERR, OK_, ERR, ERR },
            /* STR */ { ERR, ERR, OK_, OK_, ERR },
            /* BOO */ { ERR, ERR, ERR, ERR, OK_ }
    };

    static int resultType(String TP1, String TP2, String OP) {
        int newTP1 = convertTypeToNumber(TP1);
        int newTP2 = convertTypeToNumber(TP2);
        int newOP = convertOperationToNumber(OP);

        System.out.println("TIPO 1: " + newTP1 + "\t TIPO 2: " + newTP2 + "\t OP : " + newOP);
        return (expTable[newTP1][newTP2][newOP]);
    }

    static int atribType(String TP1, String TP2) {
        int newTP1 = convertTypeToNumber(TP1);
        int newTP2 = convertTypeToNumber(TP2);

        return (atribTable[newTP1][newTP2]);
    }

    private static final Map<String, Integer> typeMap = new HashMap<>();
    static {
        typeMap.put("int", INT);
        typeMap.put("float", FLO);
        typeMap.put("char", CHA);
        typeMap.put("string", STR);
        typeMap.put("bool", BOO);
    }

    private static final Map<String, Integer> operationMap = new HashMap<>();
    static {
        operationMap.put("REL", REL);
        operationMap.put("SUM", SUM);
        operationMap.put("SUB", SUB);
        operationMap.put("MUL", MUL);
        operationMap.put("DIV", DIV);
    }

    // Função para converter o tipo de dados em número
    static int convertTypeToNumber(String TP1) {
        return typeMap.getOrDefault(TP1, ERR);
    }

    // Função para converter o tipo de dados em número
    static int convertOperationToNumber(String TP1) {
        return operationMap.getOrDefault(TP1, ERR);
    }
}
