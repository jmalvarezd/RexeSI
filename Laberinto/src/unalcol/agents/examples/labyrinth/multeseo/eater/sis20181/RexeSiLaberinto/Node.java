/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.RexeSiLaberinto;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

/**
 *
 * @author Usuario
 */
public class Node implements Cloneable {

    int[] position = new int[2];
    int type;
    Node parent;
    int marked = 0;
    Node childUP;
    Node childRIGHT;
    Node childDOWN;
    Node childLEFT;

    public Node(int marked, int x, int y, int type, Node parent) {
        this.position[0] = x;
        this.position[1] = y;
        this.type = type;
        this.marked = marked;
        this.parent = parent;
    }

    public Node(int marked, int[] coords, int type, Node parent) {
        this.position[0] = coords[0];
        this.position[1] = coords[1];
        this.type = type;
        this.marked = marked;
        this.parent = parent;
    }

    public boolean samePosition(int[] coords) {
        return Arrays.equals(position, coords);
    }

    public void setPosition(int[] coords) {
        this.position = coords.clone();
    }
    
    public void setPosition(int x, int y) {
        this.position[0] = x;
        this.position[1] = y;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void addAllChilds(boolean PF, boolean PD, boolean PI, boolean PA, int orientation) {
        int globalOrientation;
        //Si no pared al frente.
        if (!PF) {
            globalOrientation = orientation;
            switch (globalOrientation) {
                case 0:
                    childUP = new Node(0, new int[]{position[0], position[1]+1}, 0, this);
                    break;
                case 1:
                    childRIGHT = new Node(0, new int[]{position[0]+1, position[1]}, 0, this);
                    break;
                case 2:
                    childDOWN = new Node(0, new int[]{position[0], position[1]-1}, 0, this);
                    break;
                case 3:
                    childLEFT = new Node(0, new int[]{position[0]-1, position[1]}, 0, this);
                    break;
            }
        }

        //Si no pared a la derecha.
        if (!PD) {
            globalOrientation = (orientation + 1) % 4;
            switch (globalOrientation) {
                case 0:
                    childUP = new Node(0, new int[]{position[0], position[1]+1}, 0, this);
                    break;
                case 1:
                    childRIGHT = new Node(0, new int[]{position[0]+1, position[1]}, 0, this);
                    break;
                case 2:
                    childDOWN = new Node(0, new int[]{position[0], position[1]-1}, 0, this);
                    break;
                case 3:
                    childLEFT = new Node(0, new int[]{position[0]-1, position[1]}, 0, this);
                    break;
            }
        }

        //Si no pared a la izquierda.
        if (!PI) {
            globalOrientation = (orientation + 3) % 4;
            switch (globalOrientation) {
                case 0:
                    childUP = new Node(0, new int[]{position[0], position[1]+1}, 0, this);
                    break;
                case 1:
                    childRIGHT = new Node(0, new int[]{position[0]+1, position[1]}, 0, this);
                    break;
                case 2:
                    childDOWN = new Node(0, new int[]{position[0], position[1]-1}, 0, this);
                    break;
                case 3:
                    childLEFT = new Node(0, new int[]{position[0]-1, position[1]}, 0, this);
                    break;
            }
        }
        if (!PA) {
            globalOrientation = (orientation + 2) % 4;
            switch (globalOrientation) {
                case 0:
                    childUP = new Node(0, new int[]{position[0], position[1]+1}, 0, this);
                    break;
                case 1:
                    childRIGHT = new Node(0, new int[]{position[0]+1, position[1]}, 0, this);
                    break;
                case 2:
                    childDOWN = new Node(0, new int[]{position[0], position[1]-1}, 0, this);
                    break;
                case 3:
                    childLEFT = new Node(0, new int[]{position[0]-1, position[1]}, 0, this);
                    break;
            }
        }
    }

    public int getAction(int[] targetPosition, int orientation, boolean rollback) {
        int rotations = 0;
        int targeOrientation = 0;
        
        if (rollback){
            return 2;
            //targeOrientation = (orientation+2)%4;
        }else{
        if (childUP != null && childUP.samePosition(targetPosition)) {
            targeOrientation = 0;
        } else {
            if (childRIGHT != null && childRIGHT.samePosition(targetPosition)) {
                targeOrientation = 1;
            } else {
                if (childDOWN != null && childDOWN.samePosition(targetPosition)) {
                    targeOrientation = 2;
                } else {
                    if (childLEFT != null && childLEFT.samePosition(targetPosition)) {
                        targeOrientation = 3;
                    }
                }
            }
        }
        }

        while (targeOrientation != (orientation + rotations) % 4) {
            rotations++;
        }

        return rotations;
    }

    public boolean isMarked(TreeMap<Integer, TreeMap<Integer, Node>> map, int positionx, int positiony){
        if(map.containsKey(positionx)){
            if(map.get(positionx).containsKey(positiony)){
                return true;
            }
        }
        return false;
    }
    public int getRandomChildAction(int agentOrientation, TreeMap<Integer, TreeMap<Integer, Node>> marked) {

        ArrayList<Node> childs = new ArrayList<>();

        if (childUP != null && !(marked.containsKey(childUP.position[0]) && marked.get(childUP.position[0]).containsKey(childUP.position[1]))) {
            childs.add(childUP);
        } else {
            if (childUP != null && isMarked(marked,childUP.position[0],childUP.position[1])) {
                childs.add(childUP);
            }
        }
        if (childDOWN != null && !(marked.containsKey(childDOWN.position[0]) && marked.get(childDOWN.position[0]).containsKey(childDOWN.position[1]))) {
            childs.add(childDOWN);
        } else if (childDOWN != null && isMarked(marked,childDOWN.position[0],childDOWN.position[1])) {
            childs.add(childDOWN);
        }

        if (childLEFT != null && !(marked.containsKey(childLEFT.position[0]) && marked.get(childLEFT.position[0]).containsKey(childLEFT.position[1]))) {
            childs.add(childLEFT);
        } else if (childLEFT != null && isMarked(marked,childLEFT.position[0],childLEFT.position[1])) {
            childs.add(childLEFT);
        }
        if (childRIGHT != null && !(marked.containsKey(childRIGHT.position[0]) && marked.get(childRIGHT.position[0]).containsKey(childRIGHT.position[1]))) {
            childs.add(childRIGHT);
        } else if (childRIGHT != null && isMarked(marked,childRIGHT.position[0],childRIGHT.position[1])) {
            childs.add(childRIGHT);
        }

        int myRand = (int) (Math.random() * (childs.size() - 0) + 0);
        System.out.println("Size=" + childs.size());
        System.out.println("Rand=" + myRand);
        if (childs.size() != 0 && (isMarked(marked,childs.get(myRand).position[0],childs.get(myRand).position[1]))) {
            childs.get(myRand).marked++;
        }else if(childs.size() == 0){
            int[] aux = new int[]{0,0};
            return getAction(aux,3,true);
        }

        return getAction(childs.get(myRand).position, agentOrientation , false);
    }
}
