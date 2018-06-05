/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.agents.examples.games.reversi.sis20181.RexeSiReversi;

import unalcol.agents.examples.games.reversi.Reversi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;

/**
 *
 * @author Rexes
 */
public class RexesAgent implements AgentProgram {

    protected String color;
    protected int tam;
    protected int fvez = 0;
    protected Board tablero;
    protected int K = 3;
    Map<String, Integer> valor = new HashMap<>();

    public RexesAgent(String color) {
        this.color = color;
        valor.put("space", 0);
        valor.put("white", 1);
        valor.put("black", -1);
    }

    @Override
    public Action compute(Percept p) {
        if (fvez == 0) {
            fvez = 1;
            tam = Integer.parseInt(p.getAttribute(Reversi.SIZE).toString());
            //System.out.println("tamaño:" +tam);
            tablero = new Board(tam);
        }

        if (p.getAttribute(Reversi.TURN).equals(color)) {
            leerTablero(p);                                                   
            String strArray[] = p.getAttribute(color+"_"+Reversi.TIME).toString().split(":");
            tablero.identifyPieces();                                     
            ArrayList<Move> posibleMov = tablero.movimientos(color);      

            if ( posibleMov.isEmpty() ) {
                fvez = 0;
                return new Action(Reversi.PASS);
            }
            for(Move mov : posibleMov){
                if(isCornerMove(mov,tam)){
                    return new Action(mov.to[0]+":"+mov.to[1]+":"+color);
                }
            }
            if((strArray[2].equals("0") || strArray[2].equals("1")) && strArray[1].equals("0")  && strArray[0].equals("0")){ // Si nos estamos quedando sin tiempo
                return new Action(posibleMov.get(0).to[0]+":"+posibleMov.get(0).to[1]+":"+color);
            }
            int bestMoveIndex[] = new MiniMax().bestMove(tablero, color, 1, K);

            if( bestMoveIndex.length>=1 && bestMoveIndex[0]>=0)
                return new Action(posibleMov.get(bestMoveIndex[0]).to[0] + ":" + posibleMov.get(bestMoveIndex[0]).to[1] + ":" + color);
            else
                return new Action(Reversi.PASS);
        }

        //System.out.println("Stealing turn " + color);
        return new Action(Reversi.PASS);
    }

    private void leerTablero(Percept p) {
        String pos, casilla;
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                pos = i + ":" + j;
                casilla = p.getAttribute(pos).toString();
                tablero.t[i][j] = valor.get(casilla);
            }
        }
    }
    
    private boolean isCornerMove(Move mov, int tama){
        if(mov.to[0] == 0 && mov.to[1] == 0) return true;
        if(mov.to[0] == 0 && mov.to[1] == tama-1) return true;
        if(mov.to[0] == tama-1 && mov.to[1] == 0) return true;
        if(mov.to[0] == tama-1 && mov.to[1] == tama-1) return true;
        return false;
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}