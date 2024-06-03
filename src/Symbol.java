public class Symbol {
    private String id;       // Nome do identificador
    private String tipo;     // Tipo do identificador (inteiro, real, etc.)
    private boolean ini;     // Indica se o identificador foi inicializado
    private boolean usada;   // Indica se o identificador foi usado
    private int escopo;   // Escopo do identificador (global, local, etc.)
    private boolean param;   // Indica se o identificador é um parâmetro de função
    private int pos;         // Posição do identificador em relação aos parâmetros da função
    private boolean vet;     // Indica se o identificador é um vetor
    private boolean matriz;  // Indica se o identificador é uma matriz
    private boolean ref;     // Indica se o identificador é passado por referência
    private boolean func;    // Indica se o identificador é uma função
    private boolean proc;    // Indica se o identificador é um procedimento
    private int quantparam;         // Posição do identificador em relação aos parâmetros da função


    public Symbol(){}

    public Symbol(Symbol symbol) {
        this.id = symbol.getId();
        this.tipo = symbol.getTipo();
        this.ini = symbol.isIni();
        this.usada = symbol.isUsada();
        this.escopo = symbol.getEscopo();
        this.param = symbol.isParam();
        this.pos = symbol.getPos();
        this.vet = symbol.isVet();
        this.matriz = symbol.isMatriz();
        this.ref = symbol.isRef();
        this.func = symbol.isFunc();
        this.proc = symbol.isProc();
        this.quantparam = symbol.getQuantparam();

    }
    
    // Construtor
    public Symbol(String id, String tipo, boolean ini, boolean usada, int escopo, 
                  boolean param, int pos, boolean vet, boolean matriz, boolean ref, boolean func, boolean proc, int quantparam, int tamvet) {
        this.id = id;
        this.tipo = tipo;
        this.ini = ini;
        this.usada = usada;
        this.escopo = escopo;
        this.param = param;
        this.pos = pos;
        this.vet = vet;
        this.matriz = matriz;
        this.ref = ref;
        this.func = func;
        this.proc = proc;
        this.quantparam = quantparam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isIni() {
        return ini;
    }

    public void setIni(boolean ini) {
        this.ini = ini;
    }

    public boolean isUsada() {
        return usada;
    }

    public void setUsada(boolean usada) {
        this.usada = usada;
    }

    public int getEscopo() {
        return escopo;
    }

    public void setEscopo(int escopo) {
        this.escopo = escopo;
    }

    public boolean isParam() {
        return param;
    }

    public void setParam(boolean param) {
        this.param = param;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public boolean isVet() {
        return vet;
    }

    public void setVet(boolean vet) {
        this.vet = vet;
    }

    public boolean isMatriz() {
        return matriz;
    }

    public void setMatriz(boolean matriz) {
        this.matriz = matriz;
    }

    public boolean isRef() {
        return ref;
    }

    public void setRef(boolean ref) {
        this.ref = ref;
    }

    public boolean isFunc() {
        return func;
    }

    public void setFunc(boolean func) {
        this.func = func;
    }


    public void printParameters() {
        System.out.println("ID: " + id);
        System.out.println("Tipo: " + tipo);
        System.out.println("Inicializado: " + ini);
        System.out.println("Usada: " + usada);
        System.out.println("Escopo: " + escopo);
        System.out.println("Parâmetro: " + param);
        System.out.println("Posição: " + pos);
        System.out.println("Vetor: " + vet);
        System.out.println("Matriz: " + matriz);
        System.out.println("Referência: " + ref);
        System.out.println("Função: " + func);

    }

    public void clear() {
        // Reset all attributes to their default values
        this.tipo = null;
        this.id = null;
        this.ini = false;
        this.usada = false;
        this.escopo = 0;
        this.param = false;
        this.pos = 0;
        this.vet = false;
        this.matriz = false;
        this.ref = false;
        this.func = false;

    }

    public boolean isProc() {
        return proc;
    }

    public void setProc(boolean proc) {
        this.proc = proc;
    }
 
    public boolean isEmpty() {
        return id == null;// Supondo que id, tipo e valor sejam os campos relevantes
    }
    
    public int getQuantparam() {
        return quantparam;
    }

    public void setQuantparam(int quantparam) {
        this.quantparam = quantparam;
    }


}
