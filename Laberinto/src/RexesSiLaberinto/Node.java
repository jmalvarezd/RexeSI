/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RexesSiLaberinto;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

/**
 *
 * @author Usuario
 */
public class Node implements Cloneable {
    public static final int[][] MOVES = {{0,1},{1,0},{0,-1},{-1,0}}; //up,right,down,left
    int[] position = new int[2];
    int type;
    Node parent;
    boolean marked;
    Node childUP;
    Node childRIGHT;
    Node childDOWN;
    Node childLEFT;
    
    public Node(boolean marked, int x, int y, int type,  Node parent){
        this.position[0] = x;
        this.position[1] = y;
        this.type = type;
        this.marked = marked;
        this.parent = parent;
    }
    public Node(boolean marked, int[] coords, int type,  Node parent){
        this.position[0] = coords[0];
        this.position[1] = coords[1];
        this.type = type;
        this.marked = marked;
        this.parent = parent;
    }
    public boolean samePosition(int[] coords){
        return Arrays.equals(position, coords);
    }
    public void setPosition(int[] coords){
        this.position = coords.clone();
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public void addAllChilds(boolean PF, boolean PD, boolean PI, int orientation) {
        int globalOrientation;
        //Si no pared al frente.
        if(!PF){
            globalOrientation = orientation;
            switch (globalOrientation){
                case 0:
                    childUP = new Node(false, new int[]{position[0]-1 , position[1]},0,this);
                    break;
                case 1:
                    childRIGHT = new Node(false, new int[]{position[0] , position[1]+1},0,this);
                    break;
                case 2:
                    childDOWN  = new Node(false, new int[]{position[0]+1 , position[1]},0,this);
                    break;
                case 3:
                    childLEFT  = new Node(false, new int[]{position[0] , position[1]-1},0,this);
                    break;
            }
        }
           
           //Si no pared a la derecha.
        if(!PD){
            globalOrientation = (orientation + 2)%4;
            switch (globalOrientation){
               case 0:
                    childUP = new Node(false, new int[]{position[0]-1 , position[1]},0,this);
                    break;
                case 1:
                    childRIGHT = new Node(false, new int[]{position[0] , position[1]+1},0,this);
                    break;
                case 2:
                    childDOWN  = new Node(false, new int[]{position[0]+1 , position[1]},0,this);
                    break;
                case 3:
                    childLEFT  = new Node(false, new int[]{position[0] , position[1]-1},0,this);
                    break;
            }
        }
        
        //Si no pared a la izquierda.
        if(!PI){
            globalOrientation = (orientation + 3)%4;
            switch (globalOrientation){
                case 0:
                    childUP = new Node(false, new int[]{position[0]-1 , position[1]},0,this);
                    break;
                case 1:
                    childRIGHT = new Node(false, new int[]{position[0] , position[1]+1},0,this);
                    break;
                case 2:
                    childDOWN  = new Node(false, new int[]{position[0]+1 , position[1]},0,this);
                    break;
                case 3:
                    childLEFT  = new Node(false, new int[]{position[0] , position[1]-1},0,this);
                    break;
            }    
        }
    }
    
    public int getAction(int[] targetPosition, int orientation){
        int rotations = 0;
        int targeOrientation=0;
        
        if(childUP != null && childUP.samePosition(targetPosition)){
            targeOrientation = 0;
        }
        if(childRIGHT != null && childRIGHT.samePosition(targetPosition)){
            targeOrientation = 1;
        }
        if(childDOWN != null && childDOWN.samePosition(targetPosition)){
            targeOrientation = 2;
        }
        if(childLEFT != null && childLEFT.samePosition(targetPosition)){
            targeOrientation = 3;
        }
        
        while(targeOrientation != (orientation+rotations)%4 ){
            rotations++;
        }
        
        return rotations;  
    }    
    
    public int getRandomChildAction(int agentOrientation, TreeMap<Integer,TreeMap<Integer,Node>> marked) {
        
        ArrayList<Node> childs = new ArrayList<>();
        
        if(childUP != null && !(marked.containsKey(childUP.position[0])&& marked.get(childUP.position[0]).containsKey(childUP.position[1])))
            childs.add(childUP);
        if(childDOWN != null && !(marked.containsKey(childDOWN.position[0])&& marked.get(childDOWN.position[0]).containsKey(childDOWN.position[1])))
            childs.add(childDOWN);
        if(childLEFT != null && !(marked.containsKey(childLEFT.position[0])&& marked.get(childLEFT.position[0]).containsKey(childLEFT.position[1])))
            childs.add(childLEFT);
        if(childRIGHT != null && !(marked.containsKey(childRIGHT.position[0])&& marked.get(childRIGHT.position[0]).containsKey(childRIGHT.position[1])))
            childs.add(childRIGHT);
        
        int myRand = (int)(Math.random() * (childs.size() - 0) + 0);
        
        return getAction(childs.get(myRand).position, agentOrientation);
    }
}
