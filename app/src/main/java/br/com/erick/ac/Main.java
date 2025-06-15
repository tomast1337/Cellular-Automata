
package br.com.erick.ac;

import br.com.erick.ac.view.MainWindow;

public class Main {
    public static void main(String[] args) {
        // Ponto de entrada da aplicação
        int initialSize = 100; // Tamanho padrão
        if (args.length > 0) {
            try {
                initialSize = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Argumento inválido. Usando tamanho padrão: 100");
            }
        }
        new MainWindow(initialSize);
    }
}