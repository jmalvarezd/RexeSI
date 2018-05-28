/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.agents.examples.games.reversi.sis20181.RexeSiReversi;

import unalcol.agents.examples.games.reversi.*;
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
    protected Tablero tablero;
    protected int K = 10;
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
            tablero = new Tablero(tam);
        }

        if (p.getAttribute(Reversi.TURN).equals(color)) {
            leerTablero(p);                                                     // Lee el tablero actual
            String strArray[] = p.getAttribute(color+"_"+Reversi.TIME).toString().split(":");
            tablero.identificarFichas();                                        // Identifica posiciones fichas negras y blancas
            ArrayList<Move> posibleMov = tablero.movimientos(color);            // Movimientos válidos a partir de mis posiciones actuales        
            // No se puede ejecutar movimiento alguno, pasa turno
            if ( posibleMov.isEmpty() ) {
                return new Action(Reversi.PASS);
            }
            if(strArray[2].equals("0")  && strArray[1].equals("0")  && strArray[0].equals("0")){ // Si nos estamos quedando sin tiempo
                return new Action(posibleMov.get(0).hasta[0]+":"+posibleMov.get(0).hasta[1]+":"+color);
            }
            int bestMoveIndesx[] = new MiniMax().bestMove(tablero, color, 1, 3);
            //Otras condiciones donde no hay mas jugadas por hacer
            
            if( bestMoveIndesx.length>=1 && bestMoveIndesx[0]>=0)
                return new Action(posibleMov.get(bestMoveIndesx[0]).hasta[0] + ":" + posibleMov.get(bestMoveIndesx[0]).hasta[1] + ":" + color);
            else
                return new Action(Reversi.PASS);
        }

        System.out.println("Stealing turn " + color);
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

    @Override
    public void init() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}