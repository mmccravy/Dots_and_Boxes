public class Edge {

    private int H, V;
    private boolean zone;

    Edge(){
        H = V = -1;
        zone = false;
    }

    Edge(int H, int V, boolean zone){
        this.H = H;
        this.V = V;
        this.zone = zone;
    }

    public boolean isHorizontal(){
        return zone;
    }

    public int getH(){
        return H;
    }

    public int getV(){
        return V;
    }
    
    public boolean getHorizontal(){
        return zone;
    }

    @Override
    public String toString(){
        return ((zone ? "H " : "V ") + H + " " + V);
    }

}
