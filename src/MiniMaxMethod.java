import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class MiniMaxMethod extends Solution{
    @Override
    public Edge getNextMove(Board board, int color, ArrayList<Win> wins, ArrayList<Edge> actionMove){
    	
        colorAlt = color;
    	LinkedList<TreeNode> queue =  new LinkedList<TreeNode>();
        LinkedList<TreeNode> stack = new LinkedList<TreeNode>() ;
        TreeNode root = new TreeNode(board, color, null, null);
        TreeNode temp = null;
        ArrayList<Edge> moves;
        Board currentBoard;
        
        int currentColor, currentScore;
        long timeVar = System.nanoTime();
        long maxTime = 1000000000;
        queue.add(root);
        queue.add(temp);
        
        do
        {
        	if((System.nanoTime() - timeVar) > maxTime) 
        		break;
        	
            TreeNode current = queue.remove() ;
            
            if(current != temp)
            {
                stack.add(current);
                currentBoard = current.getBoard();
                currentColor = current.getColor();
                currentScore = currentBoard.getPlayerScore(currentColor);
                moves = currentBoard.getAttain();
                Collections.shuffle(moves);

                for (Edge a : moves)
                {
                    Board child = currentBoard.getFresh(a, currentColor);
                    int newScore = child.getPlayerScore(currentColor);
                    if (newScore == currentScore)
                        queue.add(new TreeNode(child, Board.colorChange(currentColor), current, a));
                    else
                        queue.add(new TreeNode(child, currentColor, current,a));
                }

            }
            else
            {
            	queue.add(temp);
            }
        } while(queue.size() != 0);

        while(queue.size() != 0)
        {
            TreeNode current = queue.remove();
            if(current != temp)
                stack.add(current);
        }

        do
        {
            TreeNode current = stack.removeLast();
            TreeNode parent = current.getParent();
            int currentUtil= current.getUtil();

            if(TreeNode.MIN == currentUtil)
                current.setUtil(heuristic(current.getBoard(), current.getColor()));
            
            currentUtil = current.getUtil();

            if(parent.getColor() == colorAlt)
            {
                if(parent.getUtil() < currentUtil)
                {
                    parent.setUtil(currentUtil);
                    if (parent == root)
                        root.setEdge(current.getEdge());
                }
            }
            else
            {
                if(parent.getUtil() > currentUtil)
                    parent.setUtil(currentUtil);
            }

        }while(stack.size() != 1);
        
        return root.getEdge();
    }


}