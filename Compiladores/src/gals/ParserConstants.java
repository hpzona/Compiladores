package gals;

public interface ParserConstants {

    int START_SYMBOL = 58;

    int FIRST_NON_TERMINAL    = 58;
    int FIRST_SEMANTIC_ACTION = 92;

    int[][] PARSER_TABLE =
    {
        { -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1,  1,  1, -1, -1, -1,  1,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  1, -1, -1,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 77, 78, 78, 78, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 78, 78, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 30, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 32, -1, 37, 34, 35, -1, -1, -1, -1, -1, -1, -1, -1, -1, 33, -1, -1, -1, 36, -1, 37, -1, -1, -1, -1, -1, -1, -1, -1, -1, 31, 37, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 64, 64, -1, -1, 64, 63, -1, -1, -1, -1, 64, -1, -1, -1, -1, -1, -1, -1, 63, 64, 64, -1, 64, 64, 64, -1, 64, -1, 64, -1, 64, 64, 64, 63, 63, -1, -1, -1, 64, 64, 64 },
        { -1, 72, 73, 73, 73, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 69, 73, 73, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 71, -1, -1, -1, -1, -1, -1, 70, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1,  2,  2, -1, -1, -1,  2,  2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  2, -1, -1,  3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 18, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 27, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1,  7,  8, -1, -1, -1,  4,  6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 15, -1, -1, -1, -1, 14, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 13, 12, -1, -1, -1, 13, -1, 13, -1, -1, -1, -1, -1, -1, -1, -1, 13, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1, 19, -1, -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 24, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 23, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 21, -1, -1, -1, -1, -1, -1, 22, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 45, -1, -1, -1, -1, -1, 46, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 25, 26, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 28, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 29, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 43, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 43, -1, -1, -1, -1, -1, 42, -1, 41, -1, -1, 43, -1, -1, -1, -1, -1, 40, -1, -1, -1, -1 },
        { -1, 47, 47, 47, 47, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 47, 47, 47, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 47, -1, -1, -1, -1, -1, -1, 47, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 38, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 39, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 39, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 44, 44, 44, 44, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 44, 44, 44, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 44, -1, -1, -1, -1, -1, -1, 44, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 56, 56, 56, 56, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 56, 56, 56, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 56, -1, -1, -1, -1, -1, -1, 56, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 49, 49, -1, -1, -1, -1, -1, -1, -1, -1, 49, -1, -1, -1, -1, -1, -1, -1, -1, 49, 49, -1, 48, 48, 48, -1, 49, -1, 49, -1, 49, -1, -1, -1, -1, -1, -1, -1, 48, 48, 48 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 52, 51, 50, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 55, 54, 53 },
        { -1, 62, 62, 62, 62, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, 62, 62, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 58, 58, -1, -1, 57, -1, -1, -1, -1, -1, 58, -1, -1, -1, -1, -1, -1, -1, -1, 58, 58, -1, 58, 58, 58, -1, 58, -1, 58, -1, 58, 57, 57, -1, -1, -1, -1, -1, 58, 58, 58 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 61, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 59, 60, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 68, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 67, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 65, 66, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 76, 76, -1, -1, 76, 76, -1, -1, -1, -1, 76, -1, -1, -1, -1, -1, -1, -1, 76, 76, 76, -1, 76, 76, 76, 74, 76, 75, 76, -1, 76, 76, 76, 76, 76, -1, -1, -1, 76, 76, 76 },
        { -1, -1, 83, 79, 80, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 81, 82, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }
    };

    int[][] PRODUCTIONS = 
    {
        {  6,  2, 36, 59, 38 },
        { 64, 72, 66 },
        { 67, 68, 69, 70, 36, 64 },
        {  0 },
        { 13 },
        { 28 },
        { 14 },
        {  8 },
        {  9, 44, 60, 45 },
        { 44, 60, 45 },
        {  0 },
        {  2, 71 },
        { 37, 69 },
        {  0 },
        { 41, 60 },
        {  0 },
        { 65, 36, 72 },
        {  0 },
        { 31,  2, 73, 74, 36, 59 },
        { 42, 77, 69, 52, 67, 75, 43 },
        {  0 },
        { 36, 77, 69, 52, 67, 75 },
        {  0 },
        { 52, 67 },
        {  0 },
        { 32 },
        { 33 },
        { 46, 61, 78, 47 },
        { 36, 61, 78 },
        {  0 },
        {  2, 79 },
        { 66 },
        { 16, 80, 17, 61, 81 },
        { 30, 80, 27, 61 },
        { 19, 42, 69, 43 },
        { 20, 42, 82, 43 },
        { 34, 80 },
        {  0 },
        { 18, 61 },
        {  0 },
        { 53, 80 },
        { 44, 80, 45, 53, 80 },
        { 42, 82, 43 },
        {  0 },
        { 80, 76 },
        { 37, 80, 76 },
        {  0 },
        { 83, 84 },
        { 85, 83 },
        {  0 },
        { 41 },
        { 40 },
        { 39 },
        { 57 },
        { 56 },
        { 55 },
        { 86, 87 },
        { 88, 86, 87 },
        {  0 },
        { 48 },
        { 49 },
        { 21 },
        { 63, 62 },
        { 89, 63, 62 },
        {  0 },
        { 50 },
        { 51 },
        { 35 },
        { 22 },
        { 23, 63 },
        { 49, 63 },
        { 42, 80, 43 },
        {  2, 90 },
        { 91 },
        { 42, 80, 76, 43 },
        { 44, 80, 45 },
        {  0 },
        {  2 },
        { 91 },
        {  4 },
        {  5 },
        { 24 },
        { 25 },
        {  3 }
    };

    String[] PARSER_ERROR
            = {
                "",
                "Era esperado fim de programa",
                "Era esperado id",
                "Era esperado um numero inteiro",
                "Era esperado um numero real",
                "Era esperado identificador",
                "Era esperado número inteiro",
                "Era esperado número real",
                "Era esperado literal",
                "Era esperado programa",
                "Era esperado variável",
                "Era esperado caracter",
                "Era esperado cadeia",
                "Era esperado um procedimento",
                "Era esperado inicio",
                "Era esperado fim",
                "Era esperado inteiro",
                "Era esperado booleano",
                "Era esperado funcao",
                "Era esperado se",
                "Era esperado entao",
                "Era esperado senao",
                "Era esperado leia",
                "Era esperado escreva",
                "Era esperado ou",
                "Era esperado e",
                "Era esperado nao",
                "Era esperado falso",
                "Era esperado verdadeiro",
                "Era esperado de",
                "Era esperado faca",
                "Era esperado real",
                "Era esperado vetor",
                "Era esperado enquanto",
                "Era esperado metodo",
                "Era esperado ref",
                "Era esperado val",
                "Era esperado retorne",
                "Era esperado div",
                "Era esperado \";\"",
                "Era esperado \",\"",
                "Era esperado \".\"",
                "Era esperado \">\"",
                "Era esperado \"<\"",
                "Era esperado \"=\"",
                "Era esperado \"(\"",
                "Era esperado \")\"",
                "Era esperado \"[\"",
                "Era esperado \"]\"",
                "Era esperado \"{\"",
                "Era esperado \"}\"",
                "Era esperado \"+\"",
                "Era esperado \"-\"",
                "Era esperado \"*\"",
                "Era esperado \"/\"",
                "Era esperado \":\"",
                "Era esperado \":=\"",
                "Era esperado \"..\"",
                "Era esperado \"<>\"",
                "Era esperado \"<=\"",
                "Era esperado \">=\"",
                "Estrutura de programa inválida",
                "Bloco de código inválido",
                "Constante inválida",
                "Comando inválido",
                "Repetição de termo inválido",
                "Fator inválido",
                "Declaração de variável ou constante inválida",
                "Declaração de método inválida",
                "Comando composto inválido",
                "Tipo inválido",
                "Dimensão inválida",
                "Lista de identificadores inválida",
                "Fator constante inválido",
                "Repetição lista identificador inválido",
                "Declaração de métodos inválido",
                "Parâmetro formal inválido",
                "Tipo de método inválido",
                "Repetição de parâmetro inválida",
                "Repetição de lista de expressão inválida",
                "Parametro inválido, deve ser ref ou val",
                "Repetição de lista de comando inválida",
                "Comando inválido",
                "Expressão inválida",
                "senao inválido",
                "Lista de expressões inválida",
                "Expressão simples inválida",
                "Expressão inválida",
                "Operador relacional inválido",
                "Termo inválido",
                "Repetição de expressão simples inválida",
                "Operador de adição inválido",
                "Operador de multiplicação inválido",
                "Variável inválida",
                "Constante explícita inválido"
            };
}
