import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class GamePlay{

    private final static int size = 8;
    private final static int dist = 40;
    ArrayList<Edge> action_A = new ArrayList<Edge>();
    ArrayList<Edge> action_B = new ArrayList<Edge>();
    ArrayList<Edge> actionTurnA = new ArrayList<Edge>();
    ArrayList<Edge> actionTurnB = new ArrayList<Edge>();
    ArrayList<Win> wins = new ArrayList<Win>();
    boolean isTurn;
    private int num, turn;
    private Board board;
    private boolean mouseEnabled, goBack;
    private boolean[][] isSetHorizontal, isSetVertical;
    private JLabel[][] horizontal, vertical, box;
    private JLabel redScoreLabel, blueScoreLabel, statusLabel;
    private JFrame frame;
    Solution solutionRed, solutionBlue, solver;
    String nameOfRed, nameOfBlue;
    Starter parent;

    private MouseListener mouseListener = new MouseListener(){
        @Override
        public void mouseClicked(MouseEvent mouseEvent){
            if(!mouseEnabled) 
            	return;
            generate(origin(mouseEvent.getSource()));
        }
        @Override
        public void mousePressed(MouseEvent mouseEvent){}
        @Override
        public void mouseReleased(MouseEvent mouseEvent){}
        @Override
        public void mouseEntered(MouseEvent mouseEvent){
            if(!mouseEnabled) return;
            Edge location = origin(mouseEvent.getSource());
            
            int x = location.getH(), y = location.getV();
            if(location.isHorizontal())
            {
                if(isSetHorizontal[x][y]) 
                	return;
                horizontal[x][y].setBackground((turn == Board.RED) ? Color.RED : Color.BLUE);
            }
            else
            {
                if(isSetVertical[x][y]) 
                	return;
                vertical[x][y].setBackground((turn == Board.RED) ? Color.RED : Color.BLUE);
            }
        }
        @Override
        public void mouseExited(MouseEvent mouseEvent){
            if(!mouseEnabled) 
            	return;
            Edge location = origin(mouseEvent.getSource());
            int x = location.getH(), y = location.getV();
            if(location.isHorizontal())
            {
                if(isSetHorizontal[x][y]) 
                	return;
                horizontal[x][y].setBackground(Color.WHITE);
            }
            else
            {
                if(isSetVertical[x][y]) 
                	return;
                vertical[x][y].setBackground(Color.WHITE);
            }
        }
    };

    private void generate(Edge location){
        if(isTurn)
        {
            action_A.add(location);
            isTurn = false;
            if(solver != null)
                actionTurnA.add(location);
        }
        else
        {
            isTurn = true;
            action_B.add(location);
            if(solver != null)
                actionTurnB.add(location);
        }
                
        int x = location.getH(), y = location.getV();
        ArrayList<Point> variable;
        
        if(location.isHorizontal())
        {
            if(isSetHorizontal[x][y]) 
            	return;
            variable = board.setHorizontal(x, y, turn);
            horizontal[x][y].setBackground(Color.BLACK);
            isSetHorizontal[x][y] = true;
        }
        else
        {
            if(isSetVertical[x][y]) return;
            variable = board.setVertical(x, y, turn);
            vertical[x][y].setBackground(Color.BLACK);
            isSetVertical[x][y] = true;
        }

        for(Point p : variable)
            box[p.x][p.y].setBackground((turn == Board.RED) ? Color.RED : Color.BLUE);

        redScoreLabel.setText(String.valueOf(board.getPlayerScoreRed()));
        blueScoreLabel.setText(String.valueOf(board.getPlayerScoreBlue()));

        if(board.isFull()){
            int winner = board.getGameWinner();
            board.zeroRound();
            
            if(winner == Board.RED){
                statusLabel.setText("Player-1 is the winner!");
                statusLabel.setForeground(Color.RED);
                
                if( board.getSize() == 4 && board.getPlayerScoreRed() >= 5 ){
                    System.out.println("Red wins on 4 by 4 grid");
                }
                else if( board.getSize() == 5 && board.getPlayerScoreRed() > 11 ){
                    System.out.println("Red wins on 5 by 5 grid");
                }
                else if( board.getSize() == 6 && board.getPlayerScoreRed() >= 17 ){
                    System.out.println("Red wins on 6 by 6 grid");
                }
            }
            else if(winner == Board.BLUE) {
                System.out.println("BOARD SIZE "+board.getSize());
                statusLabel.setText("Player-2 is the winner!");
                statusLabel.setForeground(Color.BLUE);
               
                if( board.getSize() == 4 && board.getPlayerScoreBlue()>= 6 ){
                    System.out.println("Blue wins on 4 by 4 grid");
                }
                else if( board.getSize() == 5 && board.getPlayerScoreBlue() > 10 ){
                    System.out.println("Blue wins on 4 by 4 grid");
                }
                else if( board.getSize() == 6 && board.getPlayerScoreBlue() >= 17 ){
                    System.out.println("Bue wins on 4 by 4 grid");
                }
            }
            else {
                statusLabel.setText("The game has been tied.....");
                statusLabel.setForeground(Color.BLACK);
            }
        }

        if(variable.isEmpty()){
            if(turn == Board.RED)
            {
                turn = Board.BLUE;
                solver = solutionBlue;
                statusLabel.setText("Player-2 (Blue)");
                statusLabel.setForeground(Color.BLUE);
            }
            else {
                turn = Board.RED;
                solver = solutionRed;
                statusLabel.setText("Player-1 (Red) Turn");
                statusLabel.setForeground(Color.RED);
            }
        }

    }

    private void gamingProcess() {
       
        while(!board.isFull()) {
            
            if(goBack)
            	return;
            if(solver == null) 
                mouseEnabled = true;
            else
            {
            	mouseEnabled = false;       
                if(isTurn == false)
                {
                    generate(solver.getNextMove(board, turn, wins, actionTurnB));
                    board.newRound();
                }
                else
                	generate(solver.getNextMove(board, turn, wins, actionTurnA)); 
            }
            try
            {
                Thread.sleep(100);
            } 
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private Edge origin(Object object){
        for(int i = 0; i < (num - 1); i++)
        {
            for(int j = 0; j < num; j++)
            {
                if(horizontal[i][j] == object)
                    return new Edge(i, j, true);
            }
        }
        for(int i = 0; i < num; i++)
        {
            for(int j = 0; j < (num -1); j++)
            {
                if(vertical[i][j] == object)
                    return new Edge(i, j, false);
            }
        }
        return new Edge();
    }

    private JLabel getXAxis(){
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(dist, size));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setOpaque(true);
        label.addMouseListener(mouseListener);
        return label;
    }

    private JLabel getYAxis(){
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(size, dist));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setOpaque(true);
        label.addMouseListener(mouseListener);
        return label;
    }

    private JLabel getDot(){
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(size, size));
        label.setBackground(Color.BLACK);
        label.setOpaque(true);
        return label;
    }

    private JLabel getBox(){
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(dist, dist));
        label.setOpaque(true);
        return label;
    }

    private JLabel getBlank(Dimension dimen){
        JLabel label = new JLabel();
        label.setPreferredSize(dimen);
        return label;
    }

    public GamePlay(Starter parent, JFrame frame, int num, Solution gameRed, Solution gameBlue, String nameOfRed, String nameOfBlue) {
        this.parent = parent;
        this.frame = frame;
        this.num = num;
        this.solutionRed = gameRed;
        this.solutionBlue = gameBlue;
        this.nameOfRed = nameOfRed;
        this.nameOfBlue = nameOfBlue;
        this.isTurn = true;
        
        initialize();
    }

    private ActionListener backListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            goBack = true;
        }
    };

    private void initialize() {
            
        board = new Board(num);
        board.zeroRound();
        int boardWidth = num * size + (num-1) * dist;
        turn = Board.RED;
        solver = solutionRed;

        JPanel grid = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        grid.add(getBlank(new Dimension(2 * boardWidth, 10)), gbc);        
        JPanel playerPanel = new JPanel(new GridLayout(2, 2));
       
        if(num > 3) 
        	playerPanel.setPreferredSize(new Dimension(2 * boardWidth, dist));
        else 
        	playerPanel.setPreferredSize(new Dimension(2 * boardWidth, 2 * dist));
        
        playerPanel.add(new JLabel("<html><font color='red'>Player-1:", SwingConstants.CENTER));
        playerPanel.add(new JLabel("<html><font color='blue'>Player-2:", SwingConstants.CENTER));
        playerPanel.add(new JLabel("<html><font color='red'>" + nameOfRed, SwingConstants.CENTER));
        playerPanel.add(new JLabel("<html><font color='blue'>" + nameOfBlue, SwingConstants.CENTER));
        
        ++gbc.gridy;
        grid.add(playerPanel, gbc);
        ++gbc.gridy;
        grid.add(getBlank(new Dimension(2 * boardWidth, 10)), gbc);

        JPanel scorePanel = new JPanel(new GridLayout(2, 2));
        scorePanel.setPreferredSize(new Dimension(2 * boardWidth, dist));
        scorePanel.add(new JLabel("<html><font color='red'>Score:", SwingConstants.CENTER));
        scorePanel.add(new JLabel("<html><font color='blue'>Score:", SwingConstants.CENTER));
        
        redScoreLabel = new JLabel("0", SwingConstants.CENTER);
        redScoreLabel.setForeground(Color.RED);
        scorePanel.add(redScoreLabel);
        blueScoreLabel = new JLabel("0", SwingConstants.CENTER);
        blueScoreLabel.setForeground(Color.BLUE);
        
        scorePanel.add(blueScoreLabel);
        ++gbc.gridy;
        grid.add(scorePanel, gbc);
        ++gbc.gridy;
        grid.add(getBlank(new Dimension(2 * boardWidth, 10)), gbc);

        horizontal = new JLabel[num-1][num];
        isSetHorizontal = new boolean[num-1][num];
        vertical = new JLabel[num][num-1];
        isSetVertical = new boolean[num][num-1];

        box = new JLabel[num - 1][num - 1];

        for(int i = 0; i < (2 * num - 1); i++)
        {
            JPanel flowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            if(i % 2 == 0)
            {
            	flowPanel.add(getDot());
                
            	for(int j = 0; j < (num - 1); j++)
                {
                    horizontal[j][i / 2] = getXAxis();
                    flowPanel.add(horizontal[j][i / 2]);
                    flowPanel.add(getDot());
                }
            }
            else
            {
                for(int j = 0; j < (num - 1); j++)
                {
                    vertical[j][i / 2] = getYAxis();
                    flowPanel.add(vertical[j][i / 2]);
                    box[j][i / 2] = getBox();
                    flowPanel.add(box[j][i / 2]);
                }
                vertical[num - 1][i / 2] = getYAxis();
                flowPanel.add(vertical[num - 1][i / 2]);
            }
            ++gbc.gridy;
            grid.add(flowPanel, gbc);
        }

        ++gbc.gridy;
        grid.add(getBlank(new Dimension(2 * boardWidth, 10)), gbc);

        statusLabel = new JLabel("Player-1's Turn...", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
        statusLabel.setPreferredSize(new Dimension(2 * boardWidth, dist));
        
        ++gbc.gridy;
        grid.add(statusLabel, gbc);
        ++gbc.gridy;
        grid.add(getBlank(new Dimension(2 * boardWidth, 10)), gbc);

        JButton buttonReturn = new JButton("Return to Main Menu");
        buttonReturn.setPreferredSize(new Dimension(boardWidth, dist));
        buttonReturn.addActionListener(backListener);
       
        ++gbc.gridy;
        grid.add(buttonReturn, gbc);

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        frame.setContentPane(grid);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        goBack = false;
        gamingProcess();

        while(!goBack)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        parent.initializeGUI();
    }

 }
