import java.util.ArrayList;

public abstract class Solution {

    protected int colorAlt;
    
    private final static int score = 20;
    private final static int primary = 15;
    private final static int secondary = 1;
    private final static int chain = 10;
    
    protected int heuristic(final Board board, int color){
        int total;
        if(colorAlt == Board.RED)
            total = score * board.getPlayerScoreRed() - score * board.getPlayerScoreBlue();
        else
            total = score * board.getPlayerScoreBlue() - score * board.getPlayerScoreRed();
        
        if(colorAlt == Board.BLUE)
            total += chain * board.chainCount();
        
        if(colorAlt == color)
            total += primary * board.boxTally(3) - secondary * board.boxTally(2);
        else
            total -= primary * board.boxTally(3) - secondary * board.boxTally(2);
     
        return total;
    }

    public abstract Edge getNextMove(final Board board, int color , ArrayList<Win> Win, ArrayList<Edge> actionMove);
}