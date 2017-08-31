import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Starter{

    private int num;
    private boolean startGame;
    String[] players = {"Select player", "Human", "Greedy-Method", "Monte Carlo-Method", "Minimax-Method"};
    
    private Solution solutionRed, solutionBlue;
    private String nameOfRed, nameOfBlue;
    private JFrame frame;
    private JLabel playerError, sizeError;
    private JRadioButton[] gridSizeButton;

    JComboBox<String> redDropDown, blueDropDown;
    ButtonGroup sizeGroup;

    public Starter(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        redDropDown = new JComboBox<String>(players);
        blueDropDown = new JComboBox<String>(players);
        gridSizeButton = new JRadioButton[5];
        sizeGroup = new ButtonGroup();
        
        for(int i = 0; i < 5; i++)
        {
            String size = String.valueOf(i + 3);//setting grid size
            gridSizeButton[i] = new JRadioButton(size + " x " + size + " grid");
            sizeGroup.add(gridSizeButton[i]);
        }
    }
   
    private JLabel getFreshLabel(Dimension dim){
        JLabel label = new JLabel();
        label.setPreferredSize(dim);
        return label;
    }

    private Solution gameReturn(int method){
        if(method == 1) 
        	return new GreedyMethod();
        else if(method == 2) 
        	return new MonteCarloMethod();
        else if(method == 3) 
        	return new MiniMaxMethod();
        else 
        	return null;
    }

    private ActionListener submitListener = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            int redActionNum = redDropDown.getSelectedIndex();
            int blueActionNum = blueDropDown.getSelectedIndex();
            if(redActionNum == 0 || blueActionNum == 0)
            {
                playerError.setText("Please enter the player selections");
                return;
            }
            else
            {
                playerError.setText("");
                nameOfRed = players[redActionNum];
                nameOfBlue = players[blueActionNum];
                
                if(redActionNum > 1)
                	solutionRed = gameReturn(redActionNum - 1);
                if(blueActionNum > 1)
                	solutionBlue = gameReturn(blueActionNum - 1);
            }
            for(int i = 0; i < 5; i++)
            {
                if(gridSizeButton[i].isSelected())
                {
                    num = i + 3;
                    startGame = true;
                    return;
                }
            }
            sizeError.setText("Please enter the game board size");
        }
    };

    public void initializeGUI() {
    	solutionRed = null;
    	solutionBlue = null;

        JPanel guiOutput = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
       
        JLabel titleLabel = new JLabel(new ImageIcon(getClass().getResource("title.jpg")));
        guiOutput.add(titleLabel, constraints);
        ++constraints.gridy;
        guiOutput.add(getFreshLabel(new Dimension(500,25)), constraints);

        playerError = new JLabel("", SwingConstants.CENTER);
        playerError.setForeground(Color.RED);
        playerError.setPreferredSize(new Dimension(500, 25));
        ++constraints.gridy;
        guiOutput.add(playerError, constraints);

        JPanel gameMethodPanel = new JPanel(new GridLayout(2, 2));
        gameMethodPanel.setPreferredSize(new Dimension(400, 50));
        gameMethodPanel.add(new JLabel("<html><font color='red'>Player-1:", SwingConstants.CENTER));
        gameMethodPanel.add(new JLabel("<html><font color='blue'>Player-2:", SwingConstants.CENTER));
        gameMethodPanel.add(redDropDown);
        gameMethodPanel.add(blueDropDown);
        
        redDropDown.setSelectedIndex(0);
        blueDropDown.setSelectedIndex(0);
        
        ++constraints.gridy;
        guiOutput.add(gameMethodPanel, constraints);
        ++constraints.gridy;
        guiOutput.add(getFreshLabel(new Dimension(500,25)), constraints);

        sizeError = new JLabel("", SwingConstants.CENTER);
        sizeError.setForeground(Color.RED);
        sizeError.setPreferredSize(new Dimension(500, 25));
       
        ++constraints.gridy;
        guiOutput.add(sizeError, constraints);
        ++constraints.gridy;
       
        JLabel messageLabel = new JLabel("Select the size of the board:");
        messageLabel.setPreferredSize(new Dimension(400, 50));
        guiOutput.add(messageLabel, constraints);

        JPanel sizePanel = new JPanel(new GridLayout(4, 2));
        sizePanel.setPreferredSize(new Dimension(400, 100));
        for(int i=1; i<4; i++)
        {
            sizePanel.add(gridSizeButton[i]);
        }
        sizeGroup.clearSelection();
        ++constraints.gridy;
        guiOutput.add(sizePanel, constraints);

        ++constraints.gridy;
        guiOutput.add(getFreshLabel(new Dimension(500, 25)), constraints);

        JButton submitButton = new JButton("Start Game");
        submitButton.addActionListener(submitListener);
        ++constraints.gridy;
        guiOutput.add(submitButton, constraints);

        ++constraints.gridy;
        guiOutput.add(getFreshLabel(new Dimension(500, 25)), constraints);

        frame.setContentPane(guiOutput);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        startGame = false;
        while(!startGame) {
            try
            {
                Thread.sleep(100);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }
        
        new GamePlay(this, frame, num, solutionRed, solutionBlue, nameOfRed, nameOfBlue);
    }

    public static void main(String[] args) {
        new Starter().initializeGUI();
    }

}
