/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.agents.examples.games.reversi.sis20181.RexeSiReversi;

/**
 *
 * @author Rexes
 */
public class Move {
    public int[] from;
    public int[] to;
    public int direction;
    
    public Move(int x1, int y1, int x2, int y2, int d){
        from = new int[]{x1,y1};
        to = new int[]{x2,y2};
        direction = d;
        
    }
}
