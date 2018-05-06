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
    protected int lastEnergy;
    protected boolean knownMaxEnergy;
    protected boolean comiAntes;
    protected int maxEnergy = 0;
    protected Node current;
    protected int direction;
    protected int[] position = new int[]{0, 0};
    protected int[] lastPosition = new int[]{0, 0};
    protected TreeMap<Integer, TreeMap<Integer, Node>> marked = new TreeMap<>();
    protected TreeMap<Integer, TreeMap<Integer, Node>> badFood = new TreeMap<>();

    public MyAgent() {
    }

    public MyAgent(SimpleLanguage _language) {
        language = _language;
        current = new Node(1, position, 0, null);
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
       // System.out.println("energia inicio movimiento"+ energy);
        if ( FOOD && comiAntes && !isMarked(badFood, position[0], position[1]) && lastEnergy > energy) {
            //System.out.println("Mala comida :C");
            current.setPosition(position);
            if (!isMarked(badFood, position[0], position[1])) {
                markBadFood(position[0], position[1], current);
            }
        }
        //BACKWARDS
        if (PF && PD && PI) {
            updateDirection(2);
            updatePosition();
            return 2;
        }

        if (!isMarked(badFood, position[0], position[1]) && FOOD && knownMaxEnergy &&maxEnergy - energy > 5) {
            
            return 4;
        }
        if (FOOD && !knownMaxEnergy && !isMarked(badFood, position[0], position[1]) ) {
            if (lastEnergy == energy) {
                maxEnergy = energy;
                knownMaxEnergy = true;
                return 4;
            }
            return 4;
        }
        //LEFT
        if (PD && PF) {
            updateDirection(3);
            updatePosition();
            return 3;
        }
        //RIGHT
        if (PI && PF) {
            updateDirection(1);
            updatePosition();
            return 1;
        }
        //FORWARD
        if (PI && PD && !PF) {
            updatePosition();
            return 0;
        }
        current.setPosition(lastPosition);
        if (!isMarked(marked, lastPosition[0], lastPosition[1])) {
            mark(lastPosition[0], lastPosition[1], current);
        }
        Node posible = (Node) current.clone();
        if (!PD) {
            switch (direction) {
                case (0):
                    posible.setPosition(position[0] + 1, position[1]);
                    break;
                case (1):
                    posible.setPosition(position[0], position[1] - 1);
                    break;
                case (2):
                    posible.setPosition(position[0] - 1, position[1]);
                    break;
                case (3):
                    posible.setPosition(position[0], position[1] + 1);
                    break;
            }
            if (!isMarked(marked, posible.position[0], posible.position[1])) {
                mark(posible.position[0], posible.position[1], posible);
                updateDirection(1);
                updatePosition();
                return 1;
            }
        }
        if (!PF) {
            switch (direction) {
                case (0):
                    posible.setPosition(position[0], position[1] + 1);
                    break;
                case (1):
                    posible.setPosition(position[0] + 1, position[1]);
                    break;
                case (2):
                    posible.setPosition(position[0], position[1] - 1);
                    break;
                case (3):
                    posible.setPosition(position[0] - 1, position[1]);
                    break;
            }
            if (!isMarked(marked, posible.position[0], posible.position[1])) {
                mark(posible.position[0], posible.position[1], posible);
                updatePosition();
                return 0;
            }
        }
        if (!PI) {
            switch (direction) {
                case (0):
                    posible.setPosition(position[0] - 1, position[1]);
                    break;
                case (1):
                    posible.setPosition(position[0], position[1] + 1);
                    break;
                case (2):
                    posible.setPosition(position[0] + 1, position[1]);
                    break;
                case (3):
                    posible.setPosition(position[0], position[1] - 1);
                    break;
            }
            if (!isMarked(marked, posible.position[0], posible.position[1])) {
                mark(posible.position[0], posible.position[1], posible);
                updateDirection(3);
                updatePosition();
                return 3;
            }
        }

        if (!PD) {
            updateDirection(1);
            updatePosition();
            return 1;
        }
        if (!PF) {
            updateDirection(0);
            updatePosition();
            return 0;
        }

        if (!PI) {
            updateDirection(3);
            updatePosition();
            return 3;
        }
        updateDirection(0);
        updatePosition();
        return 0;
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
                    comiAntes = false;
                    cmd.add(language.getAction(3)); //rotate
                }
                comiAntes = false;
                cmd.add(language.getAction(2)); // advance
            }
            if (d == 4) {
                comiAntes = true;
                lastEnergy = energy;
                //System.out.println("energia antes de comer:" + energy);
                cmd.add(language.getAction(4));
            } //eat
            if (d == -1) {
                cmd.add(language.getAction(0)); // die
            }
        }
        String x = cmd.get(0);
        if (x == language.getAction(2) && ((Boolean) p.getAttribute(language.getPercept(6)))) {
            return new Action(language.getAction(0));
        }
        cmd.remove(0);
        return new Action(x);
    }

    private void updateDirection(int i) {
        this.direction = (this.direction + i) % 4;
    }

    private void updatePosition() {
        lastPosition = position.clone();
        if (this.direction == 0) {
            this.position[1] += 1;
        }
        if (this.direction == 2) {
            this.position[1] -= 1;
        }

        if (this.direction == 1) {
            this.position[0] += 1;
        }
        if (this.direction == 3) {
            this.position[0] -= 1;
        }
    }

    public boolean isMarked(TreeMap<Integer, TreeMap<Integer, Node>> map, int positionx, int positiony) {
        if (map.containsKey(positionx)) {
            if (map.get(positionx).containsKey(positiony)) {
                return true;
            }
        }
        return false;
    }

    public void mark(int positionx, int positiony, Node current) {
        if (!marked.containsKey(positionx)) {
            marked.put(positionx, new TreeMap<>());
        }
        marked.get(positionx).put(positiony, current);
    }
    
        public void markBadFood(int positionx, int positiony, Node current) {
        if (!badFood.containsKey(positionx)) {
            badFood.put(positionx, new TreeMap<>());
        }
        badFood.get(positionx).put(positiony, current);
    }
}
