package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.RexeSiLaberinto;

import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.types.collection.vector.*;
import unalcol.agents.Action;
import unalcol.agents.examples.labyrinth.LabyrinthUtil;

/**
 * <p>
 * Title: </p>
 *
 * <p>
 * Description: </p>
 *
 * <p>
 * Copyright: Copyright (c) 2007</p>
 *
 * <p>
 * Company: Universidad Nacional de Colombia</p>
 *
 * @author Jonatan Gómez
 * @version 1.0
 */
public class MyAgent implements AgentProgram {

    protected SimpleLanguage language;
    protected Vector<String> cmd = new Vector<String>();
    protected int energy;
    protected int lastEnergy = 0;
    protected Node current;
    protected int direction;
    protected int[] position = new int[]{0,0};
    protected int[] lastPosition = new int[]{0,0};
    protected TreeMap<Integer,TreeMap<Integer,Node>> marked = new TreeMap<>();
    protected Vector<Node> posibles = new Vector<Node>();

    public MyAgent() {
    }

    public MyAgent(SimpleLanguage _language) {
        language = _language;
        current = new Node(1,position,0,null);
        direction = 0;
    }

    public void setLanguage(SimpleLanguage _language) {
        language = _language;
    }

    public void init() {
        cmd.clear();
    }

    public int accion(boolean PF, boolean PD, boolean PA, boolean PI, boolean MT, boolean FAIL, int energy, boolean FOOD) throws CloneNotSupportedException {
        if (MT) {
            return -1;
        }
        
        //BACKWARDS
        if(PF&&PD&&PI){
            updateDirection(2);
            updatePosition();
            return 2;
        }
        //LEFT
        if(PD&&PF){
            updateDirection(3);
            updatePosition();
            return 3;
        }
        //RIGHT
        if(PI&&PF){
            updateDirection(1);
            updatePosition();
            return 1;
        }
        //FORWARD
        if(PI&&PD&&!PF){
            updatePosition();
            return 0;
        }
        if(FOOD && energy < 39){
            lastEnergy = energy;
            return 4;
        }
        posibles.clear();
        current.setPosition(lastPosition);
        if(!isMarked(marked, lastPosition[0], lastPosition[1])){
            mark(lastPosition[0], lastPosition[1], current);
        }
        Node posible = (Node)current.clone();
        if(!PD){
            switch(direction){
                case(0):
                    posible.setPosition(position[0]+1,position[1]);
                    break;
                case(1):
                    posible.setPosition(position[0],position[1]-1);
                    break;
                case(2):
                    posible.setPosition(position[0]-1,position[1]);
                    break;
                case(3):
                    posible.setPosition(position[0],position[1]+1);
                    break;
            }
            if(!isMarked(marked, posible.position[0], posible.position[1])){
                mark(posible.position[0], posible.position[1], posible);
                updateDirection(1);
                updatePosition();
                return 1;
            }
        }
        if(!PF){
            switch(direction){
                case(0):
                    posible.setPosition(position[0],position[1]+1);
                    break;
                case(1):
                    posible.setPosition(position[0]+1,position[1]);
                    break;
                case(2):
                    posible.setPosition(position[0],position[1]-1);
                    break;
                case(3):
                    posible.setPosition(position[0]-1,position[1]);
                    break;
            }
            if(!isMarked(marked, posible.position[0], posible.position[1])){
                mark(posible.position[0], posible.position[1], posible);
                updatePosition();
                return 0;
            }
        }
        if(!PI){
            switch(direction){
                case(0):
                    posible.setPosition(position[0]-1,position[1]);
                    break;
                case(1):
                    posible.setPosition(position[0],position[1]+1);
                    break;
                case(2):
                    posible.setPosition(position[0]+1,position[1]);
                    break;
                case(3):
                    posible.setPosition(position[0],position[1]-1);
                    break;
            }
            if(!isMarked(marked, posible.position[0], posible.position[1])){
                mark(posible.position[0], posible.position[1], posible);
                updateDirection(3);
                updatePosition();
                return 3;
            }
        }
        
//        current.setPosition(lastPosition);
//        if(marked.containsKey(lastPosition[0])){
//            if(marked.get(lastPosition[0]).containsKey(lastPosition[1])){
//                marked.get(lastPosition[0]).get(lastPosition[1]).marked++;
//            }else{
//                Node clone = (Node) current.clone();
//                marked.get(lastPosition[0]).put(lastPosition[1], clone);
//            }
//        }else{
//            marked.put(lastPosition[0], new TreeMap<>());
//            Node clone = (Node) current.clone();
//            marked.get(lastPosition[0]).put(lastPosition[1], clone);
//        }
//        //Escojemos una direccion
//        current.setPosition(position);
//        current.addAllChilds(PF, PD, PI, PA, direction);
//        int action = current.getRandomChildAction(direction,marked);
//        //updateDirection(action);
//        System.out.println("direction: " + direction);
//        updatePosition();
//        current.setPosition(position);
//        if(marked.containsKey(position[0])){
//            if(marked.get(position[0]).containsKey(position[1])){
//                marked.get(position[0]).get(position[1]).marked++;
//            }else{
//                Node clone = (Node) current.clone();
//                marked.get(position[0]).put(position[1], clone);
//            }
//        }else{
//            marked.put(position[0], new TreeMap<>());
//            Node clone = (Node) current.clone();
//            marked.get(position[0]).put(position[1], clone);
//        }
        if(!PF){
            updateDirection(0);
            updatePosition();
            return 0;
        }
        if(!PD){
            updateDirection(1);
            updatePosition();
            return 1;
        }
        
        if(!PI){
            updateDirection(3);
            updatePosition();
            return 3;
        }
        
        
        return 0;
        
        
//        if(!map.containsKey(current.x))
//            map.put(current.x, new TreeMap<>());
//        if(!map.get(current.x).containsKey(current.y))
//            map.get(current.x).put(current.y, current);
//        if (FOOD){
//            current.type = 1;
//        }
        
//        if(!PF && !PA && PD && PI){
//            if(map.containsKey(current.x + Node.MOVES[direction][0]) && map.get(current.x + Node.MOVES[direction][0]).containsKey(current.y + Node.MOVES[direction][1]) ){
//                current.children[direction] = map.get(current.x + Node.MOVES[direction][0]).get(current.y + Node.MOVES[direction][1]);
//            }else{
//                current.children[direction] = new Node(current.x + Node.MOVES[direction][0], current.y + Node.MOVES[direction][1], 0, 0, current);
//            }
//            current = current.children[direction]; 
//            return 0;
//        }else{
//            if(!PA && PF && PD && PI){
//                direction = ( direction+2 )%4;
//                current = current.parent;
//                return 2;
//            }else{
//                System.out.println("Ultimo caso");
//                if(current.parent != null){
//                    current.parent.marked++;
//                }
//                boolean[] candidates = {false,false,false};
//                if(!PF && 
//                        ((map.containsKey(current.x + Node.MOVES[0][0]) 
//                        && map.get(current.x + Node.MOVES[0][0]).containsKey(current.y + Node.MOVES[0][1]) 
//                        && map.get(current.x + Node.MOVES[0][0]).get(current.y + Node.MOVES[0][1]).marked == 0) ||
//                        (!map.containsKey(current.x + Node.MOVES[0][0])) ||
//                        (map.containsKey(current.x + Node.MOVES[0][0]) 
//                        && !map.get(current.x + Node.MOVES[0][0]).containsKey(current.y + Node.MOVES[0][1])))) 
//                        candidates[0] = true;
//                if(!PD && 
//                        ((map.containsKey(current.x + Node.MOVES[1][0])
//                        && map.get(current.x + Node.MOVES[1][0]).containsKey(current.y + Node.MOVES[1][1])
//                        && map.get(current.x + Node.MOVES[1][0]).get(current.y + Node.MOVES[1][1]).marked == 0)||
//                        (!map.containsKey(current.x + Node.MOVES[1][0]))||
//                        (map.containsKey(current.x + Node.MOVES[1][0])
//                        && !map.get(current.x + Node.MOVES[1][0]).containsKey(current.y + Node.MOVES[1][1]))
//                        )
//                        ) 
//                        candidates[1] = true;
//                if(!PI && 
//                        ((map.containsKey(current.x + Node.MOVES[3][0])
//                        && map.get(current.x + Node.MOVES[3][0]).containsKey(current.y + Node.MOVES[3][1])
//                        && map.get(current.x + Node.MOVES[3][0]).get(current.y + Node.MOVES[3][1]).marked == 0)||
//                        (!map.containsKey(current.x + Node.MOVES[3][0]))||
//                        (map.containsKey(current.x + Node.MOVES[3][0])
//                        && !map.get(current.x + Node.MOVES[3][0]).containsKey(current.y + Node.MOVES[3][1]))
//                        )) 
//                    candidates[2] = true;
//                if(!candidates[0] && !candidates[1] && !candidates[2]){
//                    System.out.println("Todos falsos");
//                    direction = ( direction+2 )%4;
//                    current = current.parent;
//                    return 2;
//                }else{
//                    boolean flag = true;
//                    while (flag) {
//                        k = (int) (Math.random() * 3);
//                        System.out.println("k" + k);
//                        switch (k) {
//                            case 0:
//                                flag = !candidates[0];
//                                break;
//                            case 1:
//                                flag = !candidates[1];
//                                break;
//                            default:
//                                flag = !candidates[2];
//                                break;
//                        }
//                    }
//                    if(k == 0){
//                        if(map.containsKey(current.x + Node.MOVES[direction][0]) && map.get(current.x + Node.MOVES[direction][0]).containsKey(current.y + Node.MOVES[direction][1]) ){
//                            current.children[direction] = map.get(current.x + Node.MOVES[direction][0]).get(current.y + Node.MOVES[direction][1]);
//                        }else{
//                            current.children[direction] = new Node(current.x + Node.MOVES[direction][0], current.y + Node.MOVES[direction][1], 0, 0, current);
//                        }
//                        current = current.children[direction]; 
//                    }
//                    if(k == 1){
//                        direction = (direction+1)%4;
//                        if(map.containsKey(current.x + Node.MOVES[direction][0]) && map.get(current.x + Node.MOVES[direction][0]).containsKey(current.y + Node.MOVES[direction][1]) ){
//                            current.children[direction] = map.get(current.x + Node.MOVES[direction][0]).get(current.y + Node.MOVES[direction][1]);
//                        }else{
//                            current.children[direction] = new Node(current.x + Node.MOVES[direction][0], current.y + Node.MOVES[direction][1], 0, 0, current);
//                        }
//                        current = current.children[direction]; 
//                    }
//                    if(k == 2){
//                        direction = (direction+3)%4;
//                        if(map.containsKey(current.x + Node.MOVES[direction][0]) && map.get(current.x + Node.MOVES[direction][0]).containsKey(current.y + Node.MOVES[direction][1]) ){
//                            current.children[direction] = map.get(current.x + Node.MOVES[direction][0]).get(current.y + Node.MOVES[direction][1]);
//                        }else{
//                            current.children[direction] = new Node(current.x + Node.MOVES[direction][0], current.y + Node.MOVES[direction][1], 0, 0, current);
//                        }
//                        current = current.children[direction]; 
//                    }
//                    return k;
//                }
//                
//            }
            
//        return 0;
    }
    

    /**
     * execute
     *
     * @param perception Perception
     * @return Action[]
     */
    public Action compute(Percept p) {
        if (cmd.size() == 0) {
            boolean PF = ((Boolean) p.getAttribute(language.getPercept(0)));
            boolean PD = ((Boolean) p.getAttribute(language.getPercept(1)));
            boolean PA = ((Boolean) p.getAttribute(language.getPercept(2)));
            boolean PI = ((Boolean) p.getAttribute(language.getPercept(3)));
            boolean MT = ((Boolean) p.getAttribute(language.getPercept(4)));
            boolean FAIL = ((Boolean) p.getAttribute(language.getPercept(5)));
            boolean FOOD = ((Boolean) p.getAttribute(language.getPercept(10)));
            energy = (int) (p.getAttribute(LabyrinthUtil.ENERGY));
            
            int d = 0;
            try {
                d = accion(PF, PD, PA, PI, MT, FAIL, energy, FOOD);
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(MyAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (0 <= d && d < 4) {
                for (int i = 1; i <= d; i++) {
                    cmd.add(language.getAction(3)); //rotate
                }
                cmd.add(language.getAction(2)); // advance
            }  
            if(d == 4){ cmd.add(language.getAction(4)); } //eat
            if(d == -1) {
                cmd.add(language.getAction(0)); // die
            }
        }
        String x = cmd.get(0);
        if(x == language.getAction(2)&&((Boolean) p.getAttribute(language.getPercept(6)))){
            return new Action(language.getAction(0));
        }
        cmd.remove(0);
        return new Action(x);
    }
    private void updateDirection(int i){
        this.direction = (this.direction + i)%4;
    }
    private void updatePosition(){
        lastPosition = position.clone();
        if(this.direction == 0)
            this.position[1] +=1;
        if(this.direction == 2)
            this.position[1] -=1;
        
        if(this.direction == 1)
            this.position[0] +=1;
        if(this.direction == 3)
            this.position[0] -=1;
    }
    public boolean isMarked(TreeMap<Integer, TreeMap<Integer, Node>> map, int positionx, int positiony){
        if(map.containsKey(positionx)){
            if(map.get(positionx).containsKey(positiony)){
                return true;
            }
        }
        return false;
    }
    public void mark(int positionx, int positiony, Node current){
        if(!marked.containsKey(positionx)){
            marked.put(positionx, new TreeMap<>());
        }
        marked.get(positionx).put(positiony, current);
    }
}
