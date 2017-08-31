public class Pair implements Comparable<Pair>{
    private int util ;
    private Edge edge;

    Pair(Edge edge, int util) {
        this.edge = edge ;
        this.util = util ;
    }

    int getUtil() {
        return util ;
    }

    Edge getEdge() {
        return this.edge ;
    }

    void setEdge(Edge edge) {
        this.edge = edge ;
    }
    void setUtil(int util) {
        this.util = util ;
    }
    @Override
    public int compareTo(Pair pair) {
        return this.util - pair.util;
    }
}
