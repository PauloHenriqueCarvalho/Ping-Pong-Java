
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Main class
 *
 * @author Stavr
 *
 */
public class JogoPingPong extends JFrame implements ActionListener, KeyListener {

    private static final int largura = 1200;
    private static final int altura = 600;
    private static final int paredeLargura = 20;
    private static final int paredeAltura = 120;
    private static final int ballTamanho = 20;

    private boolean player1KeyPressedUp = false;
    private boolean player1KeyPressedDown = false;
    private boolean player2KeyPressedUp = false;
    private boolean player2KeyPressedDown = false;

    private int paddle1Y = altura / 2 - paredeAltura / 2;
    private int paddle2Y = altura / 2 - paredeAltura / 2;

    private int ballX = largura / 2 - ballTamanho / 2;
    private int ballY = altura / 2 - ballTamanho / 2;

    private int ballXSpeed = 7;
    private int ballYSpeed = 7;

    private int jogadorScore1 = 0;
    private int jogadorScore2 = 0;
    private int level = 1;

    private int i = 2;

    private BufferedImage buffer;

    private boolean gameRunning;

    public JogoPingPong() {
        setTitle("Ping Pong");
        setSize(largura, altura);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);

        buffer = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);

        Timer timer = new Timer(10, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);

        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.addActionListener(e -> start());
        playAgainButton.setFocusable(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(playAgainButton);

        add(buttonPanel, BorderLayout.SOUTH);

        start();
    }

    private void start() {
        gameRunning = true;
        jogadorScore1 = 0;
        jogadorScore2 = 0;
        level = 1;
        reiniciarBall();
    }

    private void reiniciarBall() {
        ballX = largura / 2 - ballTamanho / 2;
        ballY = altura / 2 - ballTamanho / 2;
        ballXSpeed = (level > 1) ? level * 7 : 7;
        ballYSpeed = (level > 1) ? level * 7 : 7;
    }

    public void actionPerformed(ActionEvent e) {
        if (gameRunning) {
            ballX += ballXSpeed;
            ballY += ballYSpeed;

            if (ballX <= 2) {
                jogadorScore2++;
                statusJogoPingPong();
                reiniciarBall();
            } else if (ballX >= largura - ballTamanho) {
                jogadorScore1++;
                statusJogoPingPong();
                reiniciarBall();
            }

            if (ballY <= 0 || ballY >= altura - ballTamanho) {
                Random gerador = new Random();
                int random = gerador.nextInt(4);
                while (random == 0) {
                    random = gerador.nextInt(4);
                }
                ballYSpeed = -ballYSpeed;
                ballXSpeed = ballXSpeed + random;
            }

            if (ballX <= paredeLargura && ballY + ballTamanho >= paddle1Y
                    && ballY <= paddle1Y + paredeAltura) {
                ballXSpeed = -ballXSpeed;
            } else if (ballX >= largura - paredeLargura - ballTamanho
                    && ballY + ballTamanho >= paddle2Y
                    && ballY <= paddle2Y + paredeAltura) {
                ballXSpeed = -ballXSpeed;
            }
        }
        update();

        repaint();
    }

    private void statusJogoPingPong() {
        if (jogadorScore1 >= 5) {
            gameRunning = false;
            fimDeJogoPingPong("Player 1 wins!");
        } else if (jogadorScore2 >= 5) {
            gameRunning = false;
            fimDeJogoPingPong("Player 2 wins!");
        }
    }

    private void fimDeJogoPingPong(String message) {
        JOptionPane.showMessageDialog(this, message + "\nPlayer 1: "
                + jogadorScore1 + " | Player 2: " + jogadorScore2,
                "Game Over", JOptionPane.INFORMATION_MESSAGE);
        start();
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        //P1
        if (keyCode == KeyEvent.VK_W) {
            player1KeyPressedUp = true;
        } else if (keyCode == KeyEvent.VK_S) {
            player1KeyPressedDown = true;
        }

        //P2
        if (keyCode == KeyEvent.VK_UP) {
            player2KeyPressedUp = true;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            player2KeyPressedDown = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        //P1
        if (keyCode == KeyEvent.VK_W) {
            player1KeyPressedUp = false;
        } else if (keyCode == KeyEvent.VK_S) {
            player1KeyPressedDown = false;
        }

        //P2
        if (keyCode == KeyEvent.VK_UP) {
            player2KeyPressedUp = false;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            player2KeyPressedDown = false;
        }
    }

    public void update() {
        //P1
        if (player1KeyPressedUp && paddle1Y > 0) {
            paddle1Y -= 10;
        } else if (player1KeyPressedDown && paddle1Y < altura - paredeAltura) {
            paddle1Y += 10;
        }

        //P2
        if (player2KeyPressedUp && paddle2Y > 0) {
            paddle2Y -= 10;
        } else if (player2KeyPressedDown && paddle2Y < altura - paredeAltura) {
            paddle2Y += 10;
        }
    }

    public void paint(Graphics g) {
        draw(g);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = buffer.createGraphics();

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, largura, altura);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(paredeLargura - 20, paddle1Y, paredeLargura, paredeAltura);
        g2d.fillRect(largura - 1 * paredeLargura, paddle2Y, paredeLargura, paredeAltura);

        g2d.setColor(Color.RED);
        g2d.fillOval(ballX + i, ballY + i, ballTamanho, ballTamanho);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.drawString("Player 1: " + jogadorScore1, 20, 70);
        g2d.drawString("Player 2: " + jogadorScore2, largura - 150, 70);

        g2d.drawString("Level: " + level, largura / 2 - 30, 30);

        g.drawImage(buffer, 0, 0, this);

        Toolkit.getDefaultToolkit().sync();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JogoPingPong game = new JogoPingPong();
            game.setVisible(true);
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
