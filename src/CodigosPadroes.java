import java.util.ArrayList;
import java.util.List;

public class CodigosPadroes {
        private List<String> codigosPadroes = new ArrayList<>();

        public CodigosPadroes() {
                codigosPadroes.add(
                                "int main(){\n" +
                                        "\tint a = 2;\n" +
                                        "\tint x = 1 + 1 + 1;\n" +
                                "}");

                codigosPadroes.add("int main() {\n" +
                                "    int x = 5;\n" +
                                "    return 0;\n" +
                                "}");

                codigosPadroes.add("int main() {\n" +
                                "    float num = 3.14;\n" +
                                "    return 0;\n" +
                                "}");

                codigosPadroes.add("int main() {\n" +
                                "    int x = 0;\n" +
                                "    if (x == 0) {\n" +
                                "        x = 1;\n" +
                                "    }\n" +
                                "    return 0;\n" +
                                "}");

                codigosPadroes.add("int main() {\n" +
                                "    int x = 0;\n" +
                                "    while (x < 5) {\n" +
                                "        x++;\n" +
                                "    }\n" +
                                "    return 0;\n" +
                                "}");

                codigosPadroes.add(
                                "int main() {\n" +
                                                "       int x = 5;\n" +
                                                "       int y = 10;\n" +
                                                "       if (x > y) {\n" +
                                                "               x = x + 1;\n" +
                                                "       } else {\n" +
                                                "               y = y - 1;\n" +
                                                "       }\n" +
                                                "       return 0;\n" +
                                                "}");
                codigosPadroes.add(
                                "int main() {\n" +
                                                "       int a, b, x;\n" +
                                                "       float c = 0.5;\n" +
                                                "       char d = '1';\n" +
                                                "       bool h = true;\n" +
                                                "       x = 1;\n" +
                                                "       a = 1;\n" +
                                                "       x = x + a + 1 + 1 + 1 + 1 / 2.0;\n" +
                                                "       a = 1 + 2 + 3 + c;\n" +
                                                "       b = 1 + 1 * d;\n" +
                                                "       if (c > 0) {\n" +
                                                "           c = 1;\n" +
                                                "       }\n" +
                                                "       return 0;\n" +
                                                "}");

                // Adicione mais códigos padrões conforme necessário
        }

        public List<String> getCodigosPadroes() {
                return codigosPadroes;
        }

        public void adicionarCodigoPadrao(String codigo) {
                codigosPadroes.add(codigo);
        }

        public void removerCodigoPadrao(String codigo) {
                codigosPadroes.remove(codigo);
        }

}
