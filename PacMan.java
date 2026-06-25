import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

import javax.swing.*;
public class PacMan extends JPanel implements ActionListener, KeyListener
{
    class block 
    {
        int x,y,width,height;
        Image image;
        int startX,startY;
        char direction='U';
        int velocityX=0;
        int velocityY=0;
        block(Image image, int x,int y, int width, int height)
        {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }
        void updateDirection(char direction)
        {
            char prevDirection=this.direction;
            this.direction=direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for (block otherwall : walls) 
            {
                if (collision(this, otherwall)) 
                {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction =prevDirection;
                    updateVelocity();
                    this.x += this.velocityX;
                    this.y += this.velocityY;
                    for (block wall: walls) 
                    {
                        if (collision(this, wall)) 
                        {
                            this.x -= this.velocityX;
                            this.y -= this.velocityY;
                            this.direction = prevDirection;
                            updateVelocity();
                        }
                    }
            
                }
            }
        }
        void updateVelocity()
        {
            if(this.direction=='U')
            {
                this.velocityX=0;
                this.velocityY=-tileSize/4;
            }
            else if (this.direction == 'D') 
            {
                this.velocityX = 0;
                this.velocityY = tileSize/4;
            }
            else if (this.direction == 'L') 
            {
                this.velocityX = -tileSize/4;
                this.velocityY = 0;
            }
            else if (this.direction == 'R') 
            {
                this.velocityX = tileSize/4;
                this.velocityY = 0;
            }
        }
         void reset() 
        {
            this.x = this.startX;
            this.y = this.startY;
        }
    }
    int rowCount=21;
    int cloumnCount=19;
    int tileSize=32;
    int boardWidth= cloumnCount* tileSize;
    int boardHeight= rowCount*tileSize;
    Image wallImg;
    Image blueGhost;
    Image orangeGhost;
    Image pinkGhost;
    Image redGhost;
    Image up;
    Image down;
    Image left;
    Image right;
    Image foodImg;
    //X = wall, O = skip, P = pac man, ' ' = foodImg
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };
    HashSet<block> walls;
    HashSet<block> foods;
    HashSet<block> ghosts;
    block pacman;
    Timer gameLoop;
    char[] directions={'U','D','L','R'};
    Random random=new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;
    PacMan()
    {
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        wallImg=new ImageIcon(getClass().getResource("wall.png")).getImage();
        blueGhost=new ImageIcon(getClass().getResource("blueGhost.png")).getImage();
        orangeGhost=new ImageIcon(getClass().getResource("orangeGhost.png")).getImage();
        pinkGhost=new ImageIcon(getClass().getResource("pinkGhost.png")).getImage();
        redGhost=new ImageIcon(getClass().getResource("redGhost.png")).getImage();
        up=new ImageIcon(getClass().getResource("pacmanUp.png")).getImage();
        down=new ImageIcon(getClass().getResource("pacmanDown.png")).getImage();
        left=new ImageIcon(getClass().getResource("pacmanLeft.png")).getImage();
        right=new ImageIcon(getClass().getResource("pacmanRight.png")).getImage();
        foodImg=new ImageIcon(getClass().getResource("powerFood.png")).getImage();
        loadMap();
        for (block ghost : ghosts) {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
        gameLoop=new Timer(50,this);
        gameLoop.start();
    }
    public void loadMap()
    {
        walls = new HashSet<block>();
        foods = new HashSet<block>();
        ghosts = new HashSet<block>();
         for (int r = 0; r < rowCount; r++) 
        {
            for (int c = 0; c < cloumnCount; c++) 
            {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;

                if (tileMapChar == 'X') 
                { //block wall
                    block wall = new block(wallImg, x, y,tileSize, tileSize);
                    walls.add(wall);
                }
                else if (tileMapChar == 'b') 
                { //blue ghost
                    block ghost = new block(blueGhost, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'o') 
                { //orange ghost
                    block ghost = new block(orangeGhost, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'p') 
                { //pink ghost
                    block ghost = new block(pinkGhost, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'r') 
                { //red ghost
                    block ghost = new block(redGhost, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'P') 
                { //pacman
                    pacman = new block(right, x, y, tileSize, tileSize);
                }
                else if (tileMapChar == ' ') 
                { //food
                    block food = new block(foodImg, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }
            }
        }
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g)
    {
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width ,pacman.height, null);
        for (block ghost : ghosts) 
        {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for (block wall : walls) 
        {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.WHITE);
        for (block food : foods) 
        {
            g.fillRect(food.x, food.y, food.width, food.height);
        }
        //score
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) 
        {
            g.drawString("Game Over: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
        else 
        {
            g.drawString("x" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
    }
    public void move()
    {
        pacman.x+=pacman.velocityX;
        pacman.y+=pacman.velocityY;
        for (block wall : walls) 
        {
            if (collision(pacman, wall)) 
            {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        //check ghost collisions
        for (block ghost : ghosts) {
            if (collision(ghost, pacman)) {
                lives -= 1;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }

            if (ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D') {
                ghost.updateDirection('U');
            }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            for (block wall : walls) {
                if (collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= boardWidth) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
        }
        block foodEaten = null;
        for (block food : foods) 
        {
            if (collision(pacman, food)) 
            {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);

        if (foods.isEmpty()) 
        {
            loadMap();
            resetPositions();
        }
    }

    public boolean collision(block a, block b)
    {
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }
    public void resetPositions() 
    {
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for (block ghost : ghosts) 
        {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        move();
        repaint();
        if (gameOver) 
        {
            gameLoop.stop();
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) 
    {
        System.out.println("keyEvent: "+e.getKeyCode());
         if (gameOver) 
        {
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }
        // System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP) 
        {
            pacman.updateDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) 
        {
            pacman.updateDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) 
        {
            pacman.updateDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) 
        {
            pacman.updateDirection('R');
        }

        if (pacman.direction == 'U') 
        {
            pacman.image = up;
        }  
        else if (pacman.direction == 'D') 
        {
            pacman.image = down;
        }
        else if (pacman.direction == 'L') 
        {
            pacman.image = left;
        }
        else if (pacman.direction == 'R') 
        {
            pacman.image = right;
        }
    }
}