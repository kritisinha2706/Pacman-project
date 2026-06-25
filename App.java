import javax.swing.*;
public class App 
{
    public static void main(String[] args) throws Exception 
    {
        int rowCount=21;
        int cloumnCount=19;
        int tileSize=32;
        int boardWidth= cloumnCount* tileSize;
        int boardHeight= rowCount*tileSize;
        
        JFrame frame=new JFrame("Pacman");
        frame.setSize(boardWidth,boardHeight);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PacMan pacmanGame= new PacMan();
        frame.add(pacmanGame);
        pacmanGame.requestFocus();
        frame.setVisible(true);
    }
}
