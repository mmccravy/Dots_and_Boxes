import java.util.ArrayList;
import java.util.Random;

public class MonteTree {
	
	private  ArrayList<MonteTree> children;
	
	private final static Random random = new Random(System.nanoTime());
    private final static int maxLevel = 1;
    private final static double epsilon = 1e-6;
    static int nodeCount = 0, turnCount = 0;
    private int turn, level;
    private double visitNumber, total;
    private Board board;
    private Edge edge;
    
    public void expand(){
        if(level == maxLevel)
        {
            children = null;
            return;
        }

        ArrayList<Edge> moves = board.getAttain() ;

        if(moves.size()==0) {
            children = null;
            return;
        }

        children = new ArrayList<MonteTree>() ;
        for(Edge edge: moves) {
            Board nextBoard = board.getFresh(edge, turn) ;
            children.add(new MonteTree(nextBoard, (board.getPlayerScore(turn)<nextBoard.getPlayerScore(turn)) ? turn : Board.colorChange(turn), edge, (level + 1) ));
            nodeCount++ ;
        }
    }

    public double getValue(double doub){
        double mid = Math.sqrt(Math.log(doub + 1) / (visitNumber + epsilon));
        return  total / (visitNumber + epsilon) +  mid + random.nextDouble() * epsilon;
    }

    public Edge getMove(){
        double bestValue = Double.NEGATIVE_INFINITY;
        Edge bestEdge = null;
        
        for(MonteTree child: children)
        {
            double currentValue = child.getAverageValue();
            if(currentValue > bestValue)
            {
                bestValue = currentValue;
                bestEdge = child.getEdge();
            }
        }
        return bestEdge;
    }

    public void updateStats(double value){
    	visitNumber++;
    	total += value;
    }

    public double getAverageValue(){
        return total/visitNumber ;
    }

    public Edge getEdge(){
        return edge ;
    }

    public Board getBoard(){
        return board ;
    }

    public int getTurn(){
        return turn ;
    }

    public double getVisitNum(){
        return visitNumber ;
    }

    public ArrayList<MonteTree> getChildren(){
        return children ;
    }
  
    MonteTree(Board board, int turn, Edge edge, int level){
        this.board = board;
        this.turn = turn;
        this.edge = edge;
        this.level = level;
    }
}