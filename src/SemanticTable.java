/*
 * Template to help verify type compatibility in a Semantic Analyzer.
 * Available to Computer Science course at UNIVALI.
 * Professor Eduardo Alves da Silva.
 */


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

        return (expTable[newTP1][newTP2][newOP]);
    }

    static int atribType(String TP1, String TP2) {
        int newTP1 = convertTypeToNumber(TP1);
        int newTP2 = convertTypeToNumber(TP2);

        return (atribTable[newTP1][newTP2]);
    }

    private static final int convertTypeToNumber(String TP1) {
        switch (TP1) {
            case "int":
                return INT;
            case "float":
                return FLO;
            case "char":
                return CHA;
            case "string":
                return STR;
            case "bool":
                return BOO;
            default:
                return ERR;
        }
    }
    
    private static final int convertOperationToNumber(String TP1) {

        switch (TP1) {
            case "REL":
                return REL;
            case "SUM":
                return SUM;
            case "SUB":
                return SUB;
            case "MUL":
                return MUL;
            case "DIV":
                return DIV;
            default:
                return ERR;
        }
    }
}
