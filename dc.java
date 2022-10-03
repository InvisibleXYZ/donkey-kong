import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class dc {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                Controller::new
        );
    }
}

class Model extends Thread {
    private final List<PlayerPosition> playerPositionList = new ArrayList<>();
    private final Player player;
    private int pPosition = 0;

    private final List<Position> barrelPositionList = new ArrayList<>();
    private final List<Barrel> barrels = new ArrayList<>();

    private final List<Position> balkPositionList = new ArrayList<>();
    private int bPosition = 0;

    private int score;

    public Model() {
        playerPositionList.add(new PlayerPosition(0, true, Direction.RIGHT_FORWARD));
        playerPositionList.add(new PlayerPosition(1, false, Direction.RIGHT_FORWARD));
        playerPositionList.add(new PlayerPosition(2, false, Direction.RIGHT_FORWARD));
        playerPositionList.add(new PlayerPosition(3, true, Direction.RIGHT_FORWARD));
        playerPositionList.add(new PlayerPosition(4, false, Direction.UP));
        playerPositionList.add(new PlayerPosition(5, false, Direction.DOWN));
        playerPositionList.add(new PlayerPosition(6, true, Direction.LEFT_FORWARD));
        playerPositionList.add(new PlayerPosition(7, true, Direction.LEFT_FORWARD));
        playerPositionList.add(new PlayerPosition(8, false, Direction.LEFT_FORWARD));
        playerPositionList.add(new PlayerPosition(9, true, Direction.UP));

        barrelPositionList.add(new Position(-1));
        barrelPositionList.add(new Position(0));
        barrelPositionList.add(new Position(1));
        barrelPositionList.add(new Position(2));
        barrelPositionList.add(new Position(3));
        barrelPositionList.add(new Position(4));
        barrelPositionList.add(new Position(-4));
        barrelPositionList.add(new Position(5));
        barrelPositionList.add(new Position(6));
        barrelPositionList.add(new Position(7));
        barrelPositionList.add(new Position(8));
        barrelPositionList.add(new Position(9));
        barrelPositionList.add(new Position(10));

        balkPositionList.add(new Position(-6));
        balkPositionList.add(new Position(6));
        balkPositionList.add(new Position(7));
        balkPositionList.add(new Position(-7));
        balkPositionList.add(new Position(9));

        player = new Player(playerPositionList.get(pPosition));
    }

    public Player getPlayer() {
        return player;
    }

    public void move(KeyEvent e) {
        boolean collision = false;
        for(Barrel barrel : barrels){
            if (barrel.getBarrelPosition().getNumber() == player.getPlayerPosition().getNumber()) {
                collision = true;
                break;
            }
        }
        if (!player.isJump()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    if(player.getPlayerPosition().getDirection() == Direction.RIGHT_FORWARD && collision){
                        player.damage();
                        player.setPlayerPosition(playerPositionList.get(0));
                        pPosition = 0;
                    }
                    else if (player.getPlayerPosition().getDirection() == Direction.RIGHT_FORWARD) {
                        player.setPlayerPosition(playerPositionList.get(++pPosition));
                    } else if (player.getPlayerPosition().getDirection() == Direction.LEFT_FORWARD || pPosition == 9) {
                        player.setPlayerPosition(playerPositionList.get(--pPosition));
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (player.getPlayerPosition().getDirection() == Direction.LEFT_FORWARD  && collision) {
                        player.damage();
                        player.setPlayerPosition(playerPositionList.get(0));
                        pPosition = 0;
                    }
                    if (player.getPlayerPosition().getDirection() == Direction.LEFT_FORWARD || player.getPlayerPosition().getDirection() == Direction.DOWN) {
                        player.setPlayerPosition(playerPositionList.get(++pPosition));
                    } else if (((player.getPlayerPosition().getDirection() == Direction.RIGHT_FORWARD || player.getPlayerPosition().getDirection() == Direction.UP) && pPosition!=9) && pPosition!=0) {
                        player.setPlayerPosition(playerPositionList.get(--pPosition));
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (player.getPlayerPosition().isJumping() && balkPositionList.get(bPosition).getNumber() == player.getPlayerPosition().getNumber()) {
                        player.damage();
                        player.setPlayerPosition(playerPositionList.get(0));
                        pPosition = 0;
                    }
                    else if (pPosition == 9) {
                        player.setPlayerPosition(playerPositionList.get(0));
                        pPosition = 0;
                        score+=10;
                    } else if (player.getPlayerPosition().getDirection() == Direction.UP) {
                        player.setPlayerPosition(playerPositionList.get(++pPosition));
                    }
                    else if (player.getPlayerPosition().isJumping()){
                        jump();
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (player.getPlayerPosition().getDirection() == Direction.DOWN) {
                        player.setPlayerPosition(playerPositionList.get(--pPosition));
                    }
                    break;
            }
        }


    }

    private void jump() {
        player.setJump(true);

    }

    public void setpPosition(int pPosition) {
        this.pPosition = pPosition;
    }

    public List<Position> getBarrelPositionList() {
        return barrelPositionList;
    }

    public List<Barrel> getBarrels() {
        return barrels;
    }

    public List<PlayerPosition> getPlayerPositionList() {
        return playerPositionList;
    }

    public int getbPosition() {
        return bPosition;
    }

    public void setbPosition(int bPosition) {
        this.bPosition = bPosition;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    static class Position {
        private final int number;

        public Position(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }
    enum Direction{
        RIGHT_FORWARD, UP, LEFT_FORWARD, DOWN
    }

    static class PlayerPosition extends Position {
        private final boolean isJumping;
        private final Direction direction;

        public PlayerPosition(int number, boolean isJump, Direction direction) {
            super(number);
            this.direction = direction;
            this.isJumping = isJump;
        }

        public boolean isJumping() {
            return isJumping;
        }

        public Direction getDirection() {
            return direction;
        }
    }

    static class Player {
        private PlayerPosition playerPosition;
        private int lives = 3;
        private boolean isJump = false;

        public Player(PlayerPosition playerPosition) {
            this.playerPosition = playerPosition;
        }

        public PlayerPosition getPlayerPosition() {
            return playerPosition;
        }

        public void setPlayerPosition(PlayerPosition playerPosition) {
            this.playerPosition = playerPosition;
        }

        public void damage() {
            lives -= 1;
        }

        public boolean isJump() {
            return isJump;
        }

        public void setJump(boolean jump) {
            isJump = jump;
        }

        public int getLives() {
            return lives;
        }

        public void setLives(int lives) {
            this.lives = lives;
        }
    }

    static class Barrel {
        private Position barrelPosition;
        private int bPosition = 12;

        public Barrel(Position barrelPosition) {
            this.barrelPosition = barrelPosition;
        }

        public Position getBarrelPosition() {
            return barrelPosition;
        }

        public void setBarrelPosition(Position barrelPosition) {
            this.barrelPosition = barrelPosition;
        }

        public int getbPosition() {
            return bPosition;
        }

        public void setbPosition(int bPosition) {
            this.bPosition = bPosition;
        }
    }
}

class View extends JFrame{
    private int playerPosition;
    private final List<PlayerPositionView> playerPositionsList = new ArrayList<>();

    private final List<BarrelPosition> barrelPositionsList = new ArrayList<>();
    private List<BarrelPosition> barrelPositions = new ArrayList<>();

    private final List<BalkPosition> balkPositionList = new ArrayList<>();
    private int balkPosition;

    private final List<LivesPosition> playerLives = new ArrayList<>();
    private int lives;

    private int score;
    private boolean gameOver = false;

    public View(){
        this.setSize(680, 380);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Donkey Kong (Part 1)");

        playerPositionsList.add(new PlayerPositionView(100, 257));
        playerPositionsList.add(new PlayerPositionView(200, 253));
        playerPositionsList.add(new PlayerPositionView(300, 250));
        playerPositionsList.add(new PlayerPositionView(400, 247));
        playerPositionsList.add(new PlayerPositionView(500, 253));
        playerPositionsList.add(new PlayerPositionView(500, 137));
        playerPositionsList.add(new PlayerPositionView(400, 135));
        playerPositionsList.add(new PlayerPositionView(300, 131));
        playerPositionsList.add(new PlayerPositionView(200, 128));
        playerPositionsList.add(new PlayerPositionView(100, 125));

        barrelPositionsList.add(new BarrelPosition(50, 300));
        barrelPositionsList.add(new BarrelPosition(150, 275));
        barrelPositionsList.add(new BarrelPosition(250, 272));
        barrelPositionsList.add(new BarrelPosition(350, 268));
        barrelPositionsList.add(new BarrelPosition(450, 265));
        barrelPositionsList.add(new BarrelPosition(530, 261));
        barrelPositionsList.add(new BarrelPosition(520, 161));
        barrelPositionsList.add(new BarrelPosition(450, 156));
        barrelPositionsList.add(new BarrelPosition(350, 153));
        barrelPositionsList.add(new BarrelPosition(250, 149));
        barrelPositionsList.add(new BarrelPosition(150, 146));
        barrelPositionsList.add(new BarrelPosition(65, 143));
        barrelPositionsList.add(new BarrelPosition(65, 55));

        for (int i = 485; i >= 85; i -= 100) {
            balkPositionList.add(new BalkPosition(i, 75));
        }

        for (int i = 300; i <= 350; i += 25) {
            playerLives.add(new LivesPosition(i, 325));
        }
    }

    @Override
    public void paint(Graphics g) {
        AffineTransform af = new AffineTransform();
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.RED);
        af.rotate(Math.toRadians(-2));
        g2.fill(af.createTransformedShape(new Rectangle(70, 300, 480, 20)));

        af.rotate(Math.toRadians(4));
        g2.fill(af.createTransformedShape(new Rectangle(60, 160, 470, 20)));
        g2.fill(af.createTransformedShape(new Rectangle(60, 30, 550, 20)));

        g2.fillRect(175, 180, 200, 40);
        g2.fillRect(550, 70, 150, 60);
        g2.fillRect(500, 100, 150, 30);
        g2.fillRect(175, 90, 70, 30);

        g2.setColor(Color.green);
        g2.fillRect(495, 176, 30, 127);
        g2.fillRect(95, 35, 30, 150);

        clear(g2, true, true);

        g2.setColor(Color.BLACK);

        switch (playerPosition){
            case 0 -> g2.fill(playerPositionsList.get(0).getModel());
            case 1 -> g2.fill(playerPositionsList.get(1).getModel());
            case 2 -> g2.fill(playerPositionsList.get(2).getModel());
            case 3 -> g2.fill(playerPositionsList.get(3).getModel());
            case 4 -> g2.fill(playerPositionsList.get(4).getModel());
            case 5 -> g2.fill(playerPositionsList.get(5).getModel());
            case 6 -> g2.fill(playerPositionsList.get(6).getModel());
            case 7 -> g2.fill(playerPositionsList.get(7).getModel());
            case 8 -> g2.fill(playerPositionsList.get(8).getModel());
            case 9 -> g2.fill(playerPositionsList.get(9).getModel());
        }

        for(BarrelPosition barrel: barrelPositions){
            switch (barrel.getNumber()){
                case -1 -> g2.fill(barrelPositionsList.get(0).getModel());
                case 0 ->  g2.fill(barrelPositionsList.get(1).getModel());
                case 1 ->  g2.fill(barrelPositionsList.get(2).getModel());
                case 2 ->  g2.fill(barrelPositionsList.get(3).getModel());
                case 3 ->  g2.fill(barrelPositionsList.get(4).getModel());
                case 4 ->  g2.fill(barrelPositionsList.get(5).getModel());
                case -4 -> g2.fill(barrelPositionsList.get(6).getModel());
                case 5 ->  g2.fill(barrelPositionsList.get(7).getModel());
                case 6 ->  g2.fill(barrelPositionsList.get(8).getModel());
                case 7 ->  g2.fill(barrelPositionsList.get(9).getModel());
                case 8 ->  g2.fill(barrelPositionsList.get(10).getModel());
                case 9 ->  g2.fill(barrelPositionsList.get(11).getModel());
                case 10 -> g2.fill(barrelPositionsList.get(12).getModel());
            }
        }

        switch (balkPosition){
            case 0 -> g2.fill(balkPositionList.get(0).getModel());
            case 1 -> g2.fill(balkPositionList.get(1).getModel());
            case 2 -> g2.fill(balkPositionList.get(2).getModel());
            case 3 -> g2.fill(balkPositionList.get(3).getModel());
            case 4 -> g2.fill(balkPositionList.get(4).getModel());
        }

        switch (lives){
            case 3 :
                g2.fill(playerLives.get(2).getModel());

            case 2 :
                g2.fill(playerLives.get(1).getModel());

            case 1 :
                g2.fill(playerLives.get(0).getModel());
                break;
        }

        g2.setFont(new Font(this.getFont().getName(), Font.BOLD, 30));
        g2.drawString(Integer.toString(score), 550, 350);

        if(gameOver){
            g2.setFont(new Font(this.getFont().getName(), Font.BOLD, 50));
            g2.setColor(Color.BLUE);
            g2.drawString("GAME OVER", getWidth()/4, getHeight()/2);
            g2.setFont(new Font(this.getFont().getName(), Font.BOLD, 30));
            g2.drawString("PRESS 'SPACE' TO RESTART", getWidth()/6, (int)(getHeight()/1.5));
        }
    }

    public void clear(Graphics2D g2, boolean player, boolean other){
        if(player) {
            g2.setColor(Color.WHITE);
            g2.fill(playerPositionsList.get(0).getModel());
            g2.fill(playerPositionsList.get(1).getModel());
            g2.fill(playerPositionsList.get(2).getModel());
            g2.fill(playerPositionsList.get(3).getModel());
            g2.fill(playerPositionsList.get(5).getModel());
            g2.fill(playerPositionsList.get(6).getModel());
            g2.fill(playerPositionsList.get(7).getModel());
            g2.fill(playerPositionsList.get(8).getModel());

            g2.setColor(Color.GREEN);
            g2.fill(playerPositionsList.get(4).getModel());
            g2.fill(playerPositionsList.get(9).getModel());
        }

        if(other) {
            g2.setColor(Color.WHITE);
            for (int j = 0; j < 13; j++) {
                g2.fill(barrelPositionsList.get(j).getModel());
            }

            for (int i = 0; i < 5; i++) {
                g2.fill(balkPositionList.get(i).getModel());
            }

            for (int i = 2; i > -1; i--) {
                g2.fill(playerLives.get(i).getModel());
            }

            g2.fillRect(500, 320, 200, 100);

        }
    }

    public void setPlayerPosition(int playerPosition) {
        this.playerPosition = playerPosition;
    }

    public void addKeyListenerMethod(KeyListener keyListener){
        this.addKeyListener(keyListener);
    }

    public void removeKeyListenerMethod(){
        this.removeKeyListener(this.getKeyListeners()[0]);
    }

    public void gameOver(){
        gameOver=true;
    }

    public void restart(Graphics2D g2){
        gameOver=false;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    public void jump(Graphics2D g2, int position) {
        clear(g2, true, false);
        g2.setColor(Color.BLACK);
        playerPositionsList.get(position).jump();
        g2.fill(playerPositionsList.get(position).getModel());

        try {
            Thread.sleep(700);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        clear(g2, true, false);
        g2.setColor(Color.BLACK);
        playerPositionsList.get(position).down();
        g2.fill(playerPositionsList.get(position).getModel());
    }

    public void setBarrelPositions(List<BarrelPosition> barrelPositions) {
        this.barrelPositions = barrelPositions;
    }

    public void setBalkPosition(int balkPosition) {
        this.balkPosition = balkPosition;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setScore(int score) {
        this.score = score;
    }

    static class PlayerPositionView{
        private final int x;
        private int y;
        private Rectangle model;

        public PlayerPositionView(int x, int y) {
            this.x = x;
            this.y = y;
            model = new Rectangle(x, y, 20, 40);
        }

        public Rectangle getModel() {
            return model;
        }

        public void jump(){
            y-=40;
            model = new Rectangle(x, y, 20, 40);
        }

        public void down(){
            y+=40;
            model = new Rectangle(x, y, 20, 40);
        }
    }

    static class BarrelPosition{
        private Ellipse2D model;
        private int number;

        public BarrelPosition(int x, int y) {
            model = new Ellipse2D.Float(x, y, 20, 20);
        }

        public BarrelPosition(int number) {
            this.number = number;
        }

        public Ellipse2D getModel() {
            return model;
        }

        public int getNumber() {
            return number;
        }
    }

    static class BalkPosition {
        private final Rectangle model;

        public BalkPosition(int x, int y) {

            model = new Rectangle(x, y, 50, 10);
        }

        public Rectangle getModel() {
            return model;
        }
    }

    static class LivesPosition {
        private final Rectangle model;

        public LivesPosition(int x, int y) {

            model = new Rectangle(x, y, 20, 40);
        }

        public Rectangle getModel() {
            return model;
        }
    }
}

class Controller {
    private final Model model = new Model();
    private final View view = new View();
    private final KeyListenerClass myKeyListener = new KeyListenerClass();
    private final RestartKeyListener restartKeyListener = new RestartKeyListener();
    private final BarrelThread barrelThread = new BarrelThread();

    public Controller() {
        view.setVisible(true);
        view.addKeyListenerMethod(myKeyListener);

        barrelThread.start();
    }

    class KeyListenerClass implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if(!model.getPlayer().isJump()) {
                model.move(e);
                if (model.getPlayer().isJump()) {
                    view.jump((Graphics2D) view.getGraphics(), model.getPlayer().getPlayerPosition().getNumber());
                    SwingUtilities.invokeLater(
                            () ->model.getPlayer().setJump(false)
                    );
                } else {
                    view.setPlayerPosition(model.getPlayer().getPlayerPosition().getNumber());
                }
                view.repaint();
            }
        }
    }

    class RestartKeyListener implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                barrelThread.resumeThread();
                model.getPlayer().setLives(3);
                model.setScore(0);
                view.setScore(0);
                view.removeKeyListenerMethod();
                view.addKeyListenerMethod(myKeyListener);
                view.restart((Graphics2D) view.getGraphics());
            }
        }
    }

    class BarrelThread extends Thread{
        private volatile boolean paused = false;
        private final Object pauseLock = new Object();

        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                synchronized (pauseLock) {
                    if (paused) {
                        try {
                            synchronized (pauseLock) {
                                pauseLock.wait();
                            }
                        } catch (InterruptedException ex) {
                            break;
                        }
                    }
                }
                Model.Barrel toRemove = null;
                for(Model.Barrel barrel : model.getBarrels()){
                    if(barrel.getbPosition() > 0) {
                        if(barrel.getBarrelPosition().getNumber() == model.getPlayer().getPlayerPosition().getNumber() && !model.getPlayer().isJump()){
                            model.getPlayer().damage();
                            model.getPlayer().setPlayerPosition(model.getPlayerPositionList().get(0));
                            model.setpPosition(0);
                            view.setPlayerPosition(model.getPlayer().getPlayerPosition().getNumber());
                            view.repaint();
                        }
                        else if(barrel.getBarrelPosition().getNumber() == model.getPlayer().getPlayerPosition().getNumber() && model.getPlayer().isJump()){
                            model.setScore(model.getScore()+2);
                        }
                        barrel.setBarrelPosition(model.getBarrelPositionList().get(barrel.getbPosition() - 1));
                        barrel.setbPosition(barrel.getbPosition() - 1);
                    }
                    else {
                        toRemove = barrel;
                    }
                }
                if(toRemove != null) {
                    model.getBarrels().remove(toRemove);
                }

                int percent = (int)(Math.random()*100);
                if(percent > 60){
                    model.getBarrels().add(new Model.Barrel(model.getBarrelPositionList().get(12)));
                }

                List<View.BarrelPosition> barrelPositions = new ArrayList<>();
                for(Model.Barrel barrel : model.getBarrels()){
                    barrelPositions.add(new View.BarrelPosition(barrel.getBarrelPosition().getNumber()));
                }
                view.setBarrelPositions(barrelPositions);

                if(model.getbPosition() < 4){
                    view.setBalkPosition(model.getbPosition() + 1);
                    model.setbPosition(model.getbPosition()+1);
                }
                else {
                    model.setbPosition(0);
                    view.setBalkPosition(0);
                }



                view.setLives(model.getPlayer().getLives());
                view.setScore(model.getScore());
                view.repaint();
                if(model.getPlayer().getLives() < 0){
                    view.removeKeyListenerMethod();
                    view.addKeyListenerMethod(restartKeyListener);
                    view.gameOver();
                    model.getBarrels().removeAll(model.getBarrels());
                    model.setbPosition(0);
                    view.setBalkPosition(0);
                    pauseThread();
                    view.repaint();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }
        public void pauseThread() {
            paused = true;
        }

        public void resumeThread() {
            synchronized (pauseLock) {
                paused = false;
                pauseLock.notifyAll();
            }
        }
    }
}