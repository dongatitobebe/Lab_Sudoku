
package lab3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Sudoku extends JFrame implements ActionListener {

    private JButton[][] botones;

    public Sudoku() {
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel sudokuPanel = new JPanel();
        sudokuPanel.setLayout(new GridLayout(3, 3));

        botones = new JButton[9][9];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JPanel subGridPanel = new JPanel(new GridLayout(3, 3));
                subGridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); 

                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        JButton button = new JButton();
                        button.addActionListener(this);
                        Dimension dimensionBotones = new Dimension(60, 60);
                        button.setPreferredSize(dimensionBotones);
                        botones[i * 3 + k][j * 3 + l] = button;
                        subGridPanel.add(button);
                    }
                }

                sudokuPanel.add(subGridPanel);
            }
        }

        add(sudokuPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        ponerNumeros();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Sudoku::new);
    }

    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        int row = -1;
        int col = -1;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (botones[i][j] == clickedButton) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        System.out.println("Posicion Boton:\nFila:" + row + ", Columna:" + col);
        String numBoton = botones[row][col].getText();
        String numPorPoner = "";
        int numeroIngresado = 0;
        boolean numeroValido = false;

        while (!numeroValido) {
            try {
                numeroIngresado = 0;
                numPorPoner = JOptionPane.showInputDialog("Ingrese un numero de 1 al 9 para la posicion (" + row + ", " + col + ")");
                System.out.println(numPorPoner);
                if (numPorPoner != null) {
                    numeroIngresado = Integer.valueOf(numPorPoner);
                    if (numeroIngresado >= 1 && numeroIngresado <= 9) {
                        numeroValido = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "INGRESE UN NUMERO VÁLIDO (1 al 9)");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "INGRESE UN NUMERO ");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "INGRESE UN NUMERO VÁLIDO");
            }
        }

        botones[row][col].setText(numPorPoner);
        boolean estaEnFilaOColumna = BuscarNumero(row, col);
        boolean estaEnSubcuadricula = apareceEnSubcuadricula(row, col);
        if (estaEnFilaOColumna || estaEnSubcuadricula) {
            botones[row][col].setBackground(Color.red);
            JOptionPane.showMessageDialog(null, "INGRESE UN NUMERO VALIDO");
        } else {
            botones[row][col].setBackground(Color.white);
        }
        boolean finPartida = TerminoPartido();
        if (finPartida) {
            JOptionPane.showMessageDialog(null, "Has ganado!");
            this.dispose();
        }
    }

    public void ponerNumeros() {
        Random random = new Random();
        boolean[][][] used = new boolean[9][9][10];

        for (int i = 0; i < 10; i++) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);
            int num;
            do {
                num = random.nextInt(9) + 1;
            } while (used[row][col][num]);

            for (int j = 0; j < 9; j++) {
                used[row][j][num] = true;
                used[j][col][num] = true;
            }

            botones[row][col].setText(Integer.toString(num));
            botones[row][col].setEnabled(false);
        }
    }

    public boolean BuscarNumero(int row, int col) {
        boolean apareceEnFila = false, apareceEnColumna = false;

        String numBoton = botones[row][col].getText();

        for (int i = 0; i < 9; i++) {
            if (botones[row][i].getText().equals(numBoton) && i != col) {
                apareceEnFila = true;
            }
            if (botones[i][col].getText().equals(numBoton) && i != row) {
                apareceEnColumna = true;
            }
        }

        return apareceEnFila || apareceEnColumna;
    }

    public boolean apareceEnSubcuadricula(int row, int col) {
        int subcuadriculaRow = row / 3 * 3; 
        int subcuadriculaCol = col / 3 * 3; 

        String numBoton = botones[row][col].getText();

        for (int i = subcuadriculaRow; i < subcuadriculaRow + 3; i++) {
            for (int j = subcuadriculaCol; j < subcuadriculaCol + 3; j++) {
                if (i != row && j != col && botones[i][j].getText().equals(numBoton)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean TerminoPartido() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (botones[i][j].getText().equals("") || botones[i][j].getBackground().equals(Color.red)) {
                    return false;
                }
            }
        }
        return true;
    }
}
