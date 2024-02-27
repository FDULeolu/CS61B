package lab11.graphs;

import edu.princeton.cs.algs4.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs(int v) {
        marked[v] = true;
        Queue<Integer> fringe = new Queue<>();
        fringe.enqueue(v);
        while (!fringe.isEmpty()) {
            int thisVertex = fringe.dequeue();
            for (int w : maze.adj(thisVertex)) {
                if (!marked[w]) {
                    marked[w] = true;
                    fringe.enqueue(w);
                    edgeTo[w] = thisVertex;
                    distTo[w] = distTo[thisVertex] + 1;
                    announce();
                    if (w == t) {
                        return;
                    }
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs(s);
    }
}

