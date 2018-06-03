/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.agents.examples.games.reversi.sis20181.RexeSiReversi;
import unalcol.agents.examples.games.reversi.*;
import java.util.ArrayList;
/**
 *
 * @author Usuario
 */
public class MiniMax {
    // turn impar es para jugadas del max
    // retorna indice del movimiento a hacer y su score según la heuristica
    
    public int[] bestMove(Board t, String color, int turn, int limTurnos) {

        if (turn > limTurnos || t.posWhite.isEmpty() || t.posBlack.isEmpty()) {
            if((t.posWhite.size()/2)+(t.posBlack.size()/2)+10 >= (t.size*t.size)){ //ENDGAME??
                int score = scoreWhenFull(t, color);
                return new int[]{0,score};
            }
            int score = score(t, color);
            return new int[]{0,score};
        }
        
        ArrayList<Move> moves = t.movimientos(color);
        int best = (turn % 2 == 1) ? 0 : Integer.MAX_VALUE;
        int bestMoveIndex = -1;
        int id = 0;
        for (int i = 0; i < moves.size(); i++) {
            if(id>4) break;
            id++;
            Board tHijo = t.makeMove(moves.get(i), (turn%2==1)?color:invert(color));
            tHijo.identifyPieces();
            int score[] = bestMove(tHijo, color, turn + 1, limTurnos);
            
            
            // MAX play
            if (turn % 2 == 1) {
                if (best < score[1]) {
                    best = score[1];
                    bestMoveIndex = i;
                }
            } //MIN play
            else {
                if (best > score[1]) {
                    best = score[1];
                    bestMoveIndex = i;
                }
            }
        }
        
        return new int[]{bestMoveIndex,best};
    }

    private String invert(String color) {
        if (color.equals(Reversi.WHITE)) {
            return Reversi.BLACK;
        } else {
            return Reversi.WHITE;
        }
    }

    private int score(Board tHijo, String color) {
        return tHijo.movimientos(color).size();
    }
    private int scoreWhenFull(Board tHijo, String color) {
        if (color.equals(Reversi.WHITE)) {
            return tHijo.posWhite.size() / 2;
        } else {
            return tHijo.posBlack.size() / 2;
        }
    }
}

