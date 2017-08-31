import java.util.ArrayList;
import java.util.Collections;

public class GreedyMethod extends Solution{

    @Override
    public Edge getNextMove(final Board board, int color, ArrayList<Win> wins, ArrayList<Edge> actionMove){
        ArrayList<Edge> moveOptions = board.getAttain();
        Collections.shuffle(moveOptions);
        colorAlt = color;
        
        int count = moveOptions.size();
        int array[] = new int[count];
        
        for(int i = 0; i < count; i++)
        {
            Board next = board.getFresh(moveOptions.get(i), color);
        	array[i] = heuristic(next, (next.getPlayerScore(color) > board.getPlayerScore(color) ? color : Board.colorChange(color)));
        }

        int maxIndexNum = 0;
        for(int i = 1; i < count; i++)
        {
        	if(array[i] > array[maxIndexNum])
        		maxIndexNum = i;
        }
        
        return moveOptions.get(maxIndexNum);
    }

}