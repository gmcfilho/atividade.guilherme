package ui;

import java.util.Scanner;

public class Console {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static String lerTexto(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    public static String lerTextoOpcional(String prompt) {
        System.out.print(prompt + " (Enter para pular): ");
        return SCANNER.nextLine().trim();
    }

    public static int lerInteiro(String prompt) {
        while (true) {
            System.out.print(prompt);
            String entrada = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                erro("Valor inválido. Digite um número inteiro.");
            }
        }
    }

    public static int lerOpcaoMenu(int min, int max) {
        while (true) {
            int valor = lerInteiro("  Opção: ");
            if (valor >= min && valor <= max) return valor;
            erro("Opção inválida. Escolha entre " + min + " e " + max + ".");
        }
    }


    private static final String LINHA   = "═".repeat(70);
    private static final String DIVISOR = "─".repeat(70);

    public static void titulo(String texto) {
        System.out.println("\n" + LINHA);
        System.out.printf("  📚  %s%n", texto.toUpperCase());
        System.out.println(LINHA);
    }

    public static void subtitulo(String texto) {
        System.out.println("\n" + DIVISOR);
        System.out.println("  " + texto);
        System.out.println(DIVISOR);
    }

    public static void sucesso(String msg) {
        System.out.println("\n  ✅  " + msg);
    }

    public static void erro(String msg) {
        System.out.println("\n  ❌  " + msg);
    }

    public static void aviso(String msg) {
        System.out.println("\n  ⚠️   " + msg);
    }

    public static void info(String msg) {
        System.out.println("  ℹ️   " + msg);
    }

    public static void linha() {
        System.out.println(DIVISOR);
    }

    public static void pausar() {
        System.out.print("\n  Pressione Enter para continuar...");
        SCANNER.nextLine();
    }
}
