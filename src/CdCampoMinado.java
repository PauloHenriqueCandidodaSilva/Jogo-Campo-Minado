
import java.util.Random;
import java.util.Scanner;

public class CdCampoMinado {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int escolha;
        boolean continuar = true;

        while (continuar) {
            System.out.println();
            System.out.println("                    *   *   *");
            System.out.println("                 *   BOOM!   *");
            System.out.println("                    *   *   *");
            System.out.println();
            System.out.println("╔══════════════════════════════════════════════════╗");
            System.out.println("║               BEM-VINDO AO CAMPO MINADO          ║");
            System.out.println("╚══════════════════════════════════════════════════╝");
            System.out.println("1 - JOGAR");
            System.out.println("2 - COMO JOGAR");
            System.out.println("3 - CRIADORES DO JOGO");
            System.out.println("4 - SAIR");
            System.out.println("=======================================");
            System.out.print("Escolha uma opção: ");

            if (input.hasNextInt()) {
                escolha = input.nextInt();
                System.out.println("=======================================");

                switch (escolha) {
                    case 1:
                        jogarCampoMinado(input);
                        break;
                    case 2:
                        System.out.println("O objetivo do CAMPO MINADO é abrir todas as células sem acionar uma mina.");
                        System.out.println("Se você abrir uma célula com bomba, o jogo termina!");
                        break;
                    case 3:
                        System.out.println("Criadores do Jogo:");
                        System.out.println(" - GUILHERME SANTOS");
                        System.out.println(" - PEDRO HENRIQUE");
                        System.out.println(" - PAULO CANDIDO");
                        System.out.println(" - ROBERT OLIVEIRA");
                        break;
                    case 4:
                        System.out.println("Que pena, até a próxima!");
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                        break;
                }
            } else {
                System.out.println("Entrada inválida! Digite um número.");
                input.next();
            }
        }

        input.close();
    }

    public static void jogarCampoMinado(Scanner scanner) {
        CampoMinado jogo = new CampoMinado();
        boolean jogando = true;

        System.out.println("\n🪨 Iniciando uma nova partida de Campo Minado!");
        System.out.println("Objetivo: Revele todas as células sem acionar uma bomba.\n");

        while (jogando) {
            jogo.mostrarTabuleiro();

            int linha = solicitarEntrada(scanner, "linha", CampoMinado.LINHAS);
            int coluna = solicitarEntrada(scanner, "coluna", CampoMinado.COLUNAS);

            boolean jogadaValida = jogo.revelarCelula(linha, coluna);

            if (!jogadaValida) {
                jogo.revelarTudo();
                jogo.mostrarTabuleiro();
                System.out.println("\n💥 BOOM! Você pisou numa bomba. Fim de jogo.");
                jogando = false;
            } else {
                System.out.println("Boa jogada!");
                if (jogo.venceu()) {
                    jogo.revelarTudo();
                    jogo.mostrarTabuleiro();
                    System.out.println("\n🎉 Parabéns! Você revelou todas as células com sucesso!");
                    jogando = false;
                }
            }
        }

        System.out.print("\nPressione ENTER para voltar ao menu principal...");
        scanner.nextLine();
        scanner.nextLine();
    }

    public static int solicitarEntrada(Scanner scanner, String tipo, int limiteMax) {
        int valor = -1;

        while (true) {
            System.out.print("Digite a " + tipo + " (0-" + (limiteMax - 1) + "): ");
            if (scanner.hasNextInt()) {
                valor = scanner.nextInt();
                if (valor >= 0 && valor < limiteMax) {
                    break;
                } else {
                    System.out.println("⚠️ Valor fora do intervalo permitido. Tente novamente.");
                }
            } else {
                System.out.println("⚠️ Entrada inválida! Por favor, digite um número.");
                scanner.next();
            }
        }

        return valor;
    }
}

class CampoMinado {

    public static final int LINHAS = 5;
    public static final int COLUNAS = 5;
    public static final int NUM_BOMBAS = 3;
    public static final char CELULA_OCULTA = '-';
    public static final char BOMBA = '*';

    private char[][] tabuleiro = new char[LINHAS][COLUNAS];
    private boolean[][] revelado = new boolean[LINHAS][COLUNAS];
    private int celulasParaRevelar;

    public CampoMinado() {
        inicializarTabuleiro();
    }

    public void inicializarTabuleiro() {
        celulasParaRevelar = LINHAS * COLUNAS - NUM_BOMBAS;
        for (int l = 0; l < LINHAS; l++) {
            for (int c = 0; c < COLUNAS; c++) {
                tabuleiro[l][c] = '0';
                revelado[l][c] = false;
            }
        }

        Random rand = new Random();
        int bombasColocadas = 0;
        while (bombasColocadas < NUM_BOMBAS) {
            int linha = rand.nextInt(LINHAS);
            int coluna = rand.nextInt(COLUNAS);
            if (tabuleiro[linha][coluna] != BOMBA) {
                tabuleiro[linha][coluna] = BOMBA;
                bombasColocadas++;
            }
        }

        for (int l = 0; l < LINHAS; l++) {
            for (int c = 0; c < COLUNAS; c++) {
                if (tabuleiro[l][c] != BOMBA) {
                    int bombasAdjacentes = contarBombasAdjacentes(l, c);
                    tabuleiro[l][c] = (char) ('0' + bombasAdjacentes);
                }
            }
        }
    }

    public int contarBombasAdjacentes(int linha, int coluna) {
        int total = 0;
        for (int l = linha - 1; l <= linha + 1; l++) {
            for (int c = coluna - 1; c <= coluna + 1; c++) {
                if (l >= 0 && l < LINHAS && c >= 0 && c < COLUNAS) {
                    if (tabuleiro[l][c] == BOMBA) {
                        total++;
                    }
                }
            }
        }
        return total;
    }

    public void mostrarTabuleiro() {
        System.out.print("  ");
        for (int c = 0; c < COLUNAS; c++) {
            System.out.print(c + " ");
        }
        System.out.println();

        for (int l = 0; l < LINHAS; l++) {
            System.out.print(l + " ");
            for (int c = 0; c < COLUNAS; c++) {
                if (revelado[l][c]) {
                    System.out.print(tabuleiro[l][c] + " ");
                } else {
                    System.out.print(CELULA_OCULTA + " ");
                }
            }
            System.out.println();
        }
    }

    public boolean revelarCelula(int linha, int coluna) {
        if (linha < 0 || linha >= LINHAS || coluna < 0 || coluna >= COLUNAS) {
            System.out.println("Coordenadas inválidas. Tente novamente.");
            return true;
        }

        if (revelado[linha][coluna]) {
            System.out.println("Célula já revelada. Escolha outra.");
            return true;
        }

        revelado[linha][coluna] = true;

        if (tabuleiro[linha][coluna] == BOMBA) {
            return false;
        }

        celulasParaRevelar--;

        if (tabuleiro[linha][coluna] == '0') {
            for (int l = linha - 1; l <= linha + 1; l++) {
                for (int c = coluna - 1; c <= coluna + 1; c++) {
                    if (l >= 0 && l < LINHAS && c >= 0 && c < COLUNAS && !revelado[l][c]) {
                        revelarCelula(l, c);
                    }
                }
            }
        }

        return true;
    }

    public boolean venceu() {
        return celulasParaRevelar == 0;
    }

    public void revelarTudo() {
        for (int l = 0; l < LINHAS; l++) {
            for (int c = 0; c < COLUNAS; c++) {
                revelado[l][c] = true;
            }
        }
    }
}
