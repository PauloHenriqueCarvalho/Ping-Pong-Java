
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

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


	private int paddle1Y = altura / 2 - paredeAltura / 2;
	private int paddle2Y = altura / 2 - paredeAltura / 2;
        
	private int ballX = largura / 2 - ballTamanho / 2;
	private int ballY = altura / 2 - ballTamanho / 2;
        
	private int ballXSpeed = 7;
	private int ballYSpeed = 7;


	private int jogadorScore1 = 0;
	private int jogadorScore2 = 0;
	private int level = 1;

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
				ballYSpeed = -ballYSpeed;
			}

			// Ball collisions with paddles
			if (ballX <= paredeLargura && ballY + ballTamanho >= paddle1Y && 
					ballY <= paddle1Y + paredeAltura) {
				ballXSpeed = -ballXSpeed;
			} else if (ballX >= largura - paredeLargura - ballTamanho && 
					ballY + ballTamanho >= paddle2Y
					&& ballY <= paddle2Y + paredeAltura) {
				ballXSpeed = -ballXSpeed;
			}
		}

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
		JOptionPane.showMessageDialog(this, message + "\nPlayer 1: " + 
	jogadorScore1 + " | Player 2: " + jogadorScore2,
				"Game Over", JOptionPane.INFORMATION_MESSAGE);
		start();
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
                
                //P1
		if (keyCode == KeyEvent.VK_W && paddle1Y > 0) {
			paddle1Y -= 10;
		} else if (keyCode == KeyEvent.VK_S && paddle1Y < altura - paredeAltura) {
			paddle1Y += 10;
		}

		//P2
		if (keyCode == KeyEvent.VK_UP && paddle2Y > 0) {
			paddle2Y -= 10;
		} else if (keyCode == KeyEvent.VK_DOWN && paddle2Y < altura - paredeAltura) {
			paddle2Y += 10;
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void paint(Graphics g) {
		Graphics2D g2d = buffer.createGraphics();

		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, largura, altura);

		g2d.setColor(Color.WHITE);
		g2d.fillRect(paredeLargura, paddle1Y, paredeLargura, paredeAltura);
		g2d.fillRect(largura - 2 * paredeLargura, paddle2Y, paredeLargura, paredeAltura);

		g2d.setColor(Color.RED);
		g2d.fillOval(ballX, ballY, ballTamanho, ballTamanho);

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
}
