package hw4.puzzle;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Solver {

    private PriorityQueue<Node> pq;

    private class Node {
        private WorldState ws;
        private int numOfMoves;
        private Node preNode;

        public Node(WorldState ws, int numOfMoves, Node preNode) {
            this.preNode = preNode;
            this.ws = ws;
            this.numOfMoves = numOfMoves;
        }
    }

    private class NodeComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            return o1.numOfMoves - o2.numOfMoves;
        }
    }

    public Solver(WorldState initial) {
        pq = new PriorityQueue<>(new NodeComparator());
        pq.add(new Node(initial, 0, null));
    }

    public int moves() {

    }

    public Iterable<WorldState> solution() {
        LinkedList<WorldState> lst = new LinkedList<>();
        lst.add(pq.peek().ws);


    }
}
