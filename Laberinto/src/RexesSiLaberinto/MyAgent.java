package RexesSiLaberinto;

import java.util.TreeMap;
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
    protected Node current;
    protected int direction;
    protected TreeMap<Integer,TreeMap<Integer,Node>> map = new TreeMap<>();

    public MyAgent() {
    }

    public MyAgent(SimpleLanguage _language) {
        language = _language;
        current = new Node(0,0,0,0,null);
        direction = 0;
    }

    public void setLanguage(SimpleLanguage _language) {
        language = _language;
    }

    public void init() {
        cmd.clear();
    }

    public int accion(boolean PF, boolean PD, boolean PA, boolean PI, boolean MT, boolean FAIL, int energy, boolean FOOD) {
        if (MT) {
            return -1;
        }
        int k = 0;
        System.out.println(current.x);
        System.out.println(current.y);
        if(!map.containsKey(current.x))
            map.put(current.x, new TreeMap<>());
        if(!map.get(current.x).containsKey(current.y))
            map.get(current.x).put(current.y, current);
        if (FOOD){
            current.type = 1;
        }
        
        if(!PF && !PA && PD && PI){
            if(map.containsKey(current.x + Node.MOVES[direction][0]) && map.get(current.x + Node.MOVES[direction][0]).containsKey(current.y + Node.MOVES[direction][1]) ){
                current.children[direction] = map.get(current.x + Node.MOVES[direction][0]).get(current.y + Node.MOVES[direction][1]);
            }else{
                current.children[direction] = new Node(current.x + Node.MOVES[direction][0], current.y + Node.MOVES[direction][1], 0, 0, current);
            }
            current = current.children[direction]; 
            return 0;
        }else{
            if(!PA && PF && PD && PI){
                direction = ( direction+2 )%4;
                current = current.parent;
                return 2;
            }else{
                if(current.parent != null){
                    current.parent.marked++;
                }
                boolean[] candidates = {false,false,false};
                if(!PF && 
                        (map.containsKey(current.x + Node.MOVES[0][0]) 
                        && map.get(current.x + Node.MOVES[0][0]).containsKey(current.y + Node.MOVES[0][1]) 
                        && map.get(current.x + Node.MOVES[0][0]).get(current.y + Node.MOVES[0][1]).marked == 0)) 
                        candidates[0] = true;
                if(!PD && 
                        (map.containsKey(current.x + Node.MOVES[1][0])
                        && map.get(current.x + Node.MOVES[1][0]).containsKey(current.y + Node.MOVES[1][1])
                        && map.get(current.x + Node.MOVES[1][0]).get(current.y + Node.MOVES[1][1]).marked == 0)) 
                        candidates[1] = true;
                if(!PI && 
                        (map.containsKey(current.x + Node.MOVES[3][0])
                        && map.get(current.x + Node.MOVES[3][0]).containsKey(current.y + Node.MOVES[3][1])
                        && map.get(current.x + Node.MOVES[3][0]).get(current.y + Node.MOVES[3][1]).marked == 0)) candidates[2] = true;
                if(!candidates[0] && !candidates[1] && !candidates[2]){
                    direction = ( direction+2 )%4;
                    current = current.parent;
                    return 2;
                }else{
                    boolean flag = true;
                    while (flag) {
                        k = (int) (Math.random() * 3);
                        System.out.println("k" + k);
                        switch (k) {
                            case 0:
                                flag = !candidates[0];
                                break;
                            case 1:
                                flag = !candidates[1];
                                break;
                            default:
                                flag = !candidates[2];
                                break;
                        }
                    }
                    if(k == 0){
                        if(map.containsKey(current.x + Node.MOVES[direction][0]) && map.get(current.x + Node.MOVES[direction][0]).containsKey(current.y + Node.MOVES[direction][1]) ){
                            current.children[direction] = map.get(current.x + Node.MOVES[direction][0]).get(current.y + Node.MOVES[direction][1]);
                        }else{
                            current.children[direction] = new Node(current.x + Node.MOVES[direction][0], current.y + Node.MOVES[direction][1], 0, 0, current);
                        }
                        current = current.children[direction]; 
                    }
                    if(k == 1){
                        direction = (direction+1)%4;
                        if(map.containsKey(current.x + Node.MOVES[direction][0]) && map.get(current.x + Node.MOVES[direction][0]).containsKey(current.y + Node.MOVES[direction][1]) ){
                            current.children[direction] = map.get(current.x + Node.MOVES[direction][0]).get(current.y + Node.MOVES[direction][1]);
                        }else{
                            current.children[direction] = new Node(current.x + Node.MOVES[direction][0], current.y + Node.MOVES[direction][1], 0, 0, current);
                        }
                        current = current.children[direction]; 
                    }
                    if(k == 2){
                        direction = (direction+3)%4;
                        if(map.containsKey(current.x + Node.MOVES[direction][0]) && map.get(current.x + Node.MOVES[direction][0]).containsKey(current.y + Node.MOVES[direction][1]) ){
                            current.children[direction] = map.get(current.x + Node.MOVES[direction][0]).get(current.y + Node.MOVES[direction][1]);
                        }else{
                            current.children[direction] = new Node(current.x + Node.MOVES[direction][0], current.y + Node.MOVES[direction][1], 0, 0, current);
                        }
                        current = current.children[direction]; 
                    }
                    return k;
                }
                
            }
            
        }
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
            
            int d = accion(PF, PD, PA, PI, MT, FAIL, energy, FOOD);
            if (0 <= d && d < 4) {
                for (int i = 1; i <= d; i++) {
                    cmd.add(language.getAction(3)); //rotate
                }
                cmd.add(language.getAction(2)); // advance
            } else {
                cmd.add(language.getAction(0)); // die
            }
        }
        String x = cmd.get(0);
        cmd.remove(0);
        return new Action(x);
    }
}
