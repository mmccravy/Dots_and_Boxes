import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MonteCarloMethod extends Solution{
    
	final Random r = new Random(System.nanoTime());
    @Override
    public Edge getNextMove(Board board, int color, ArrayList<Win> Win,ArrayList<Edge> actionMove) {
    	colorAlt = color;
    	MonteTree.nodeCount = 0;
    	MonteTree.turnCount = 0;
        MonteTree root = new MonteTree(board, color, null, 0) ;
        long timeVar = System.nanoTime();
        long maxTime = 1000000000;
        
        while(System.nanoTime() - timeVar < maxTime)
            selectAction(root);

        return root.getMove();
    }

    public void selectAction(MonteTree root){
        List<MonteTree> past = new LinkedList<MonteTree>();
        MonteTree limb = root;
        past.add(root);
        while(true)
        {
            MonteTree child = select(limb);
            if(child == null)
            	break;
            limb=child;
            past.add(limb);
        }
        limb.expand();
        MonteTree newNode = select(limb);
        
        if(newNode == null)
            newNode = limb ;
        past.add(newNode);
       
        int value = simulateFromState(newNode.getBoard(), newNode.getTurn());
       
        for (MonteTree node : past)
        {
            node.updateStats(value);
        }
    }
    
    private int simulateFromState(Board board, int turn)
    {
        int champ;
        if(board.isFull())
        {
            MonteTree.turnCount++;
            return(board.getPlayerScore(colorAlt) - board.getPlayerScore(Board.colorChange(colorAlt)));
        }
        else
        {
            Edge move = (new GreedyMethod().getNextMove(board, turn, null, null));
            Board nextBoard = board.getFresh(move, turn) ;
            champ = simulateFromState(nextBoard, (nextBoard.getPlayerScore(turn) > board.getPlayerScore(turn) ? turn : Board.colorChange(turn)));
            return champ;
        }
    }
   
    private MonteTree select(MonteTree limb){
        ArrayList<MonteTree> branches = limb.getChildren();
        
        if(branches == null)
            return null;
        
        MonteTree value = null;
        double bestValue = Double.NEGATIVE_INFINITY;

        if(limb.getVisitNum() > 3)
        {
            for(MonteTree leaf : branches)
            {
                double transferValue = leaf.getValue(limb.getVisitNum());
                
                if (transferValue > bestValue)
                {
                    value = leaf;
                    bestValue = transferValue;
                }
            }
        }
        else 
        {
            for(MonteTree leaf: branches)
            {
                double currentValue = heuristic(leaf.getBoard(), leaf.getTurn()) ;
                if(currentValue > bestValue) {
                    bestValue = currentValue;
                    value = leaf;
                }
            }
        }
        
        return value;
    }
}