/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RexesSiLaberinto;

/**
 *
 * @author Usuario
 */
public class Node {
    public static final int[][] MOVES = {{0,1},{1,0},{0,-1},{-1,0}}; //up,right,down,left
    int x;
    int y;
    int type;
    Node[] children = new Node[4];
    Node parent;
    int marked;
    
    public Node(int x, int y, int type, int marked, Node parent){
        this.x = x;
        this.y = y;
        this.type = type;
        this.marked = marked;
        this.parent = parent;
    }
    @Override
    public boolean equals(Object obj){
        return ((Node)obj).x == this.x && ((Node)obj).y == this.y;
    }
}
