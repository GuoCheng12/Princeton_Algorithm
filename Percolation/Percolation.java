import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class Percolation {
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufForFull;
    private boolean[][] ifOpen;
    private int gridwidth;
    private int opensites;
    private boolean isPercolated;
    private int[] dx = {-1,0,0,1};
    private int[] dy = {0,-1,1,0};

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        if(n <= 0){
            throw new IllegalArgumentException("n must be >= 0");
        }
        uf = new WeightedQuickUnionUF(n * n + 2); // make two top virtual sites
        ufForFull = new WeightedQuickUnionUF(n * n + 1);
        gridwidth = n;
        isPercolated = false;
        ifOpen = new boolean[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                ifOpen[i][j] = false; // initialization -> block
            }
        }
    }
    // judge the given point is in the grid
    private boolean ifInGrid(int row,int col){
        if(row <= 0 || row > gridwidth || col <= 0 || col > gridwidth){
            return false;
        }
        return true;
    }
    private void connectNeighbours(int row,int col){
        int newRow, newCol;
        int currIndex = calcIndex(row,col);
        for(int i = 0; i < 4; i++){
            newRow = row + dx[i];
            newCol = col + dy[i];
            if(ifInGrid(newRow,newCol) && isOpen(newRow,newCol)){
                uf.union(calcIndex(newRow,newCol),currIndex);
                ufForFull.union(calcIndex(newRow,newCol),currIndex);
            }
        }
    }
    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        if(!ifInGrid(row,col)){
            throw new IllegalArgumentException("the given site must in the n-by-n gird");
        }
        int currIndex = calcIndex(row,col);
        if(!ifOpen[row - 1][col - 1]){
            ifOpen[row - 1][col - 1] = true;
            if(row == 1){ // Let the virtual site connect with this point
                uf.union(0,currIndex);
                ufForFull.union(0,currIndex);
            }
            if(row == gridwidth){ //
                uf.union(gridwidth * gridwidth + 1,currIndex);
            }
            connectNeighbours(row,col);
            opensites++;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        if(!ifInGrid(row,col)){
            throw new IllegalArgumentException("the given site must in the n-by-n gird");
        }
        return ifOpen[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        if(!ifInGrid(row,col)){
            throw new IllegalArgumentException("the given site must in the n-by-n grid");
        }
        return ufForFull.find(calcIndex(row,col)) ==  ufForFull.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return opensites;
    }

    // does the system percolate?
    public boolean percolates(){
        return uf.find(0) == uf.find(gridwidth * gridwidth + 1);

    }

    // calculate the index for the given site(row,col),where the index starts at 1.
    private int calcIndex(int row,int col){
        return (row - 1) * gridwidth + col;
    }

    // test client (optional)
    public static void main(String[] args){
        Percolation p = new Percolation(3);
        p.open(1,3);
        p.open(2,3);
        p.open(3,2);
        p.open(3,1);
        System.out.println(p.percolates());
    }
}
