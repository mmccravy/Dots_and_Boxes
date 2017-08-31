import java.awt.Point;
import java.util.ArrayList;

public class Board implements Cloneable{
	
	ArrayList<Edge> action;
	int round;
	
	final static int RED = 0;
    final static int BLUE = 1;
    final static int BLACK = 2;
    final static int BLANK = 3;
 
    private int[][] box;
    private int[][] vertical;
    private int[][] horizontal;
    private int n, playerScoreRed, playerScoreBlue;

    public Board(int n){
        this.round = 0; 
        this.action = new ArrayList<>();
        horizontal = new int[n - 1][n];
        vertical = new int[n][n - 1];
        box = new int[n - 1][n - 1];
        populate(horizontal,BLANK);
        populate(vertical,BLANK);
        populate(box,BLANK);
        this.n = n;
        playerScoreRed = playerScoreBlue = 0;
    }

    public void setAction(ArrayList<Edge> action){
        this.action = action;
    }
    
    public void newRound(){round++;}
    
    public void zeroRound(){round = 0;}

    public int getRound(){return round;}

    public Board clone(){
        Board cloneBoard = new Board(n);

        for(int i = 0; i < (n-1); i++)
        {
            for(int j = 0; j < n; j++)
            {
                cloneBoard.horizontal[i][j] = horizontal[i][j];
            }
        }
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < (n-1); j++)
            {
                cloneBoard.vertical[i][j] = vertical[i][j];
            }
        }
        for(int i = 0; i < (n-1); i++)
        {
        	for(int j = 0; j < (n-1); j++)
            {
                cloneBoard.box[i][j] = box[i][j];
            }
        }       
        cloneBoard.playerScoreRed = playerScoreRed;
        cloneBoard.playerScoreBlue = playerScoreBlue;

        return cloneBoard;
    }

    private void populate(int[][] array, int temp){
        for(int i = 0; i < array.length; i++)
        {
            for(int j = 0; j < array[i].length; j++)
            {
            	array[i][j]=temp;
            }
        }
    }

    public int getSize(){return n;}

    public int getPlayerScoreRed(){return playerScoreRed;}

    public int getPlayerScoreBlue(){return playerScoreBlue;}

    public int getPlayerScore(int color){
        if(color == RED) 
        	return playerScoreRed;
        else 
        	return playerScoreBlue;
    }

    public static int colorChange(int color){
        if(color == RED)
            return BLUE;
        else
            return RED;
    }

    public ArrayList<Edge> getAttain(){
        ArrayList<Edge> temp = new ArrayList<Edge>();
        for(int i = 0; i < (n - 1); i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(horizontal[i][j] == BLANK)
                    {
                		temp.add(new Edge(i, j, true));
                    }
            }
        }
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < (n - 1); j++)
            {
                if(vertical[i][j] == BLANK)
                    {
                		temp.add(new Edge(i, j, false));
                    }
            }
        }
        
        return temp;
    }
    
    public ArrayList<Point> setHorizontal(int x, int y, int color){
        horizontal[x][y]=BLACK;
        
        ArrayList<Point> temp = new ArrayList<Point>();
        
        if(y < (n - 1) && vertical[x][y] == BLACK && vertical[x + 1][y] == BLACK && horizontal[x][y + 1] == BLACK)
        {
            box[x][y] = color;
            temp.add(new Point(x,y));
            if(color == RED)
            	playerScoreRed++;
            else 
            	playerScoreBlue++;
        }
        if(y > 0 && vertical[x][y-1] == BLACK && vertical[x+1][y-1] == BLACK && horizontal[x][y-1] == BLACK)
        {
            box[x][y - 1] = color;
            temp.add(new Point(x, y - 1));
            if(color == RED)
            	playerScoreRed++;
            else
            	playerScoreBlue++;
        }
        return temp;
    }

    public ArrayList<Point> setVertical(int x, int y, int color){
        vertical[x][y]=BLACK;
        
        ArrayList<Point> temp = new ArrayList<Point>();
        
        if(x < (n - 1) && horizontal[x][y] == BLACK && horizontal[x][y + 1] == BLACK && vertical[x + 1][y] == BLACK)
        {
            box[x][y] = color;
            temp.add(new Point(x,y));
            if(color == RED)
            	playerScoreRed++;
            else
            	playerScoreBlue++;
        }
        if(x > 0 && horizontal[x - 1][y] == BLACK && horizontal[x - 1][y + 1] == BLACK && vertical[x - 1][y] == BLACK)
        {
            box[x - 1][y] = color;
            temp.add(new Point(x - 1,y));
            if(color == RED)
            	playerScoreRed++;
            else
            	playerScoreBlue++;
        }
        return temp;
    }

    public boolean isFull(){
        return (playerScoreRed + playerScoreBlue) == (n - 1) * (n - 1);
    }

    public int getGameWinner(){
        if(playerScoreRed > playerScoreBlue) 
        	return RED;
        else if(playerScoreRed < playerScoreBlue) 
        	return BLUE;
        else 
        	return BLANK;
    }

    public Board getFresh(Edge edge, int color){
        Board temp = clone();
        if(edge.isHorizontal())
            temp.setHorizontal(edge.getH(), edge.getV(), color);
        else
            temp.setVertical(edge.getH(), edge.getV(), color);
        return temp;
    }

    private int getTally(int i, int j){
        int inc = 0;
        if(horizontal[i][j] == BLACK)
        	inc++;
        if(horizontal[i][j + 1] == BLACK)
        	inc++;
        if(vertical[i][j] == BLACK)
        	inc++;
        if(vertical[i + 1][j] == BLACK)
        	inc++;
        return inc;
    }
   
    private int chains(int i){
        int inc = 0; 
        
        for(int j = 0 ; j  < (n - 1); j++ )
        {
            if(horizontal[i][j] == BLANK) 
            {
                if((vertical[i][j] == BLACK ) && (vertical[i + 1][j] == BLACK)) 
                    inc++;
            }
        }
        return inc;
    }
     
    public int chainCount(){      
        int inc = 0;
        for(int i = 0; i < (n - 1); i++)
            {
        		inc += chains(i);
            }

        return inc;
    }

    public int boxTally(int num){   
        int inc = 0;
        
        for(int i = 0; i < (n - 1); i++)
        {
        	for(int j=0; j<(n-1); j++)
            {
                if(getTally(i, j) == num)
                    {
                		inc++;
                    }
            }
        }
        return inc;
    }

}