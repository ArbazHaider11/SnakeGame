import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;

        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    //snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food
    Tile food;
    Random random;
    
    //game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this. boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
        
        snakeHead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();
        
        food = new Tile(10,10); 
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;
        
        gameLoop = new Timer(100, this);
        gameLoop.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //food
        g.setColor(Color.yellow);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        //snake head
        g.setColor(Color.red);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        //snake body
        for(int i=0; i<snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);

        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over. Score : " + String.valueOf(snakeBody.size()), tileSize-16, tileSize);
        } 
        else {
            g.drawString("Score : " + String.valueOf(snakeBody.size()), tileSize-16, tileSize );
        }

    }
    
    public void placeFood() {
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;

    }
    
    
    public void move() {
        if (gameOver) return; 
    
        int previousX = snakeHead.x;
        int previousY = snakeHead.y;
        
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;
    
        // Check for food collision
        if (collision(snakeHead, food)) {
        
            placeFood();
    
            snakeBody.add(new Tile(previousX, previousY));
        }
    
        // Move the snake's body
        for (int i = snakeBody.size() - 1; i > 0; i--) {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }
    
        if (!snakeBody.isEmpty()) {
            snakeBody.get(0).x = previousX;
            snakeBody.get(0).y = previousY;
        }
    
        for (Tile snakePart : snakeBody) {
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
    
        if (snakeHead.x < 0 || snakeHead.x >= boardWidth / tileSize || 
            snakeHead.y < 0 || snakeHead.y >= boardHeight / tileSize) {
            gameOver = true;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    
    }

}
