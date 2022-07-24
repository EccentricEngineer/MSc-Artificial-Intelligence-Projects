// This is the java file that encapsulates the Bicycle Production Environment and its behaviour

import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.logging.Logger;

public class ProductionEnv extends Environment {

	// Constants
    public static final int GRow = 6;  	     // Number of rows in the grid
    public static final int GCol = 10;		 // Number of columns in the grid
    public static final int BIN = 16;    	 // BIN code in grid model
    public static final int BIKE = 32;   	 // BIKE code in grid model
    public static final int RA = 64;     	 // Robotic Arm Agent code in grid model
    public static final int HA = 63;     	 // Holding Agent code in grid model
    public static final int WA = 62;     	 // Welding Agent code in grid model
    public static final int MA = 61;     	 // Moving Agent code in grid model
	public static final int HEAD = 60;	 	 // Head Tube
	public static final int TOP = 59;	 	 // Top Tube
	public static final int DOWN = 58;	 	 // Top Tube
	public static final int SEAT = 57;	 	 // Top Tube
	public static final int SEATSTAY = 56;	 // Top Tube
	public static final int CHAIN = 55;	 	 // Top Tube
	
    /* Percepts and actions */	
	//// ADD AND REMOVE AGENTS FROM THE ENVIRONMENT
	// Robotic Arm Agent
	public static final Term    ara = Literal.parseLiteral("add(ra)");
	public static final Term    rra = Literal.parseLiteral("remove(ra)");
	
	// Holding Agent
	public static final Term    ah1 = Literal.parseLiteral("add(h1)");
	public static final Term    rh1 = Literal.parseLiteral("remove(h1)");
	public static final Term    ah2 = Literal.parseLiteral("add(h2)");
	public static final Term    rh2 = Literal.parseLiteral("remove(h2)");
	public static final Term    ah3 = Literal.parseLiteral("add(h3)");
	public static final Term    rh3 = Literal.parseLiteral("remove(h3)");
	public static final Term    ah4 = Literal.parseLiteral("add(h4)");
	public static final Term    rh4 = Literal.parseLiteral("remove(h4)");
	public static final Term    ah5 = Literal.parseLiteral("add(h5)");
	public static final Term    rh5 = Literal.parseLiteral("remove(h5)");
	public static final Term    ah6 = Literal.parseLiteral("add(h6)");
	public static final Term    rh6 = Literal.parseLiteral("remove(h6)");
	
	// Welding Agent
	public static final Term    aw1 = Literal.parseLiteral("add(w1)");
	public static final Term    rw1 = Literal.parseLiteral("remove(w1)");
	public static final Term    aw2 = Literal.parseLiteral("add(w2)");
	public static final Term    rw2 = Literal.parseLiteral("remove(w2)");
	public static final Term    aw3 = Literal.parseLiteral("add(w3)");
	public static final Term    rw3 = Literal.parseLiteral("remove(w3)");
	public static final Term    aw4 = Literal.parseLiteral("add(w4)");
	public static final Term    rw4 = Literal.parseLiteral("remove(w4)");
	public static final Term    aw5 = Literal.parseLiteral("add(w5)");
	public static final Term    rw5 = Literal.parseLiteral("remove(w5)");
	
	// Moving Agent
	public static final Term    ama = Literal.parseLiteral("add(ma)");
	public static final Term    rma = Literal.parseLiteral("remove(ma)");
	
	//// PICKING UP AND DROPPING OFF PIECES
	public static final Term    ph = Literal.parseLiteral("pick(head)");
	public static final Term    pt = Literal.parseLiteral("pick(top)");
	public static final Term    pd = Literal.parseLiteral("pick(down)");
	public static final Term    ps = Literal.parseLiteral("pick(seat)");
	public static final Term    pss = Literal.parseLiteral("pick(seatstay)");
	public static final Term    pc = Literal.parseLiteral("pick(chain)");
	public static final Term    dh = Literal.parseLiteral("drop(head)");
	public static final Term    dt = Literal.parseLiteral("drop(top)");
	public static final Term    dd = Literal.parseLiteral("drop(down)");
	public static final Term    ds = Literal.parseLiteral("drop(seat)");
	public static final Term    dss = Literal.parseLiteral("drop(seatstay)");
	public static final Term    dc = Literal.parseLiteral("drop(chain)");
	
	//// WELDING OF PIECES	
	// Seat Stay
	public static final Term    wss = Literal.parseLiteral("weld(seatstay)");
	
	// MOVING AGENT ACTIONS
	public static final Term    pb = Literal.parseLiteral("pick(bike)");
    public static final Term    sb = Literal.parseLiteral("stash(bike)");
    public static final Literal cb = Literal.parseLiteral("complete(bike)");
    
	public static final Literal eb = Literal.parseLiteral("empty(bin)");

    static Logger logger = Logger.getLogger(ProductionEnv.class.getName());

    private ProductionModel model;
    private ProductionView  view;
	
	private int bikeNo = 0;
	private int noOfPieces = 6;
    
    @Override
    public void init(String[] args) {
        model = new ProductionModel();
        view  = new ProductionView(model);
        model.setView(view);
        updatePercepts();
    }

    /* Actions */
    @Override
    public boolean executeAction(String ag, Structure action) {
        logger.info(ag+" doing: "+ action);
        try {
            if (action.getFunctor().equals("move_towards")) {
                int x = (int)((NumberTerm)action.getTerm(0)).solve();
                int y = (int)((NumberTerm)action.getTerm(1)).solve();
                int id = (int)((NumberTerm)action.getTerm(2)).solve();
                model.moveTowards(x,y, id);
			} else if (action.equals(ara)) {
				model.addRoboticAgent();
			} else if (action.equals(rra)) {
				model.removeRoboticAgent();
			} else if (action.equals(ah1)) {
				model.addHolderHead();
			} else if (action.equals(ah2)) {
				model.addHolderTop();
			} else if (action.equals(ah3)) {
				model.addHolderDown();
			} else if (action.equals(ah4)) {
				model.addHolderSeat();
			} else if (action.equals(ah5)) {
				model.addHolderSeatStay();
			} else if (action.equals(ah6)) {
				model.addHolderChain();
			} else if (action.equals(rh1)) {
				model.removeHolderHead();
			} else if (action.equals(rh2)) {
				model.removeHolderTop();
			} else if (action.equals(rh3)) {
				model.removeHolderDown();
			} else if (action.equals(rh4)) {
				model.removeHolderSeat();
			} else if (action.equals(rh5)) {
				model.removeHolderSeatStay();
			} else if (action.equals(rh6)) {
				model.removeHolderChain();
			} else if (action.equals(aw1)) {
				model.addWelder1();
			} else if (action.equals(rw1)) {
				model.removeWelder1();
			} else if (action.equals(aw2)) {
				model.addWelder2();
			} else if (action.equals(rw2)) {
				model.removeWelder2();
			} else if (action.equals(aw3)) {
				model.addWelder3();
			} else if (action.equals(rw3)) {
				model.removeWelder3();
			} else if (action.equals(aw4)) {
				model.addWelder4();
			} else if (action.equals(rw4)) {
				model.removeWelder4();
			} else if (action.equals(aw5)) {
				model.addWelder5();
			} else if (action.equals(rw5)) {
				model.removeWelder5();
			} else if (action.equals(ama)) {
				model.addMovingAgent();
			} else if (action.equals(rma)) {
				model.removeMovingAgent();
			} else if (action.equals(wss)) {
				model.weldSeatStay();
			} else if (action.equals(ph) || action.equals(pt) || action.equals(pd) || action.equals(ps) || action.equals(pss) || action.equals(pc)) {
				model.pickPiece();
			} else if (action.equals(dh)) {
				model.dropHead();
			} else if (action.equals(dt)) {
				model.dropTop();
			} else if (action.equals(dd)) {
				model.dropDown();
			} else if (action.equals(ds)) {
				model.dropSeat();
			} else if (action.equals(dss)) {
				model.dropSeatStay();
			} else if (action.equals(dc)) {
				model.dropChain();
			} else if (action.equals(pb)) {
				model.pickBike();
			} else if (action.equals(sb)) {
				model.stashBike();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        updatePercepts();

        try {
            Thread.sleep(100);
        } catch (Exception e) {}
        informAgsEnvironmentChanged();
        return true;
    }
    
    /*
    * LOOK HERE to see how the percepts change in the environment
    */
    void updatePercepts() {
        clearPercepts();
        
		// Position of the RA agent
        Location RALoc = model.getAgPos(0);
		RALoc = model.getAgPos(0);
        Literal RAPos = Literal.parseLiteral("pos(robot," + RALoc.x + "," + RALoc.y + ")");
        addPercept(RAPos);
		
		// Position of the MA agent
        Location MALoc = model.getAgPos(1);
		MALoc = model.getAgPos(1);
        Literal MAPos = Literal.parseLiteral("pos(moving," + MALoc.x + "," + MALoc.y + ")");
        addPercept(MAPos);
		
		// Percept to signify that assembly of bike frame has been completed
		if (model.bikeFrameCompleted) {
			addPercept(cb);
		}
		
		// Percept to signify whether bin is empty
		if (model.binIsEmpty) {
			addPercept(eb);
		}		
    }

    class ProductionModel extends GridWorldModel {
		
		public boolean bikeFrameCompleted = false; 	// whether all pieces of the bike have been assembled
		public boolean binIsEmpty = false; 			// whether all pieces have been taken from bin
		
        private ProductionModel() {
            super(GCol, GRow, 2);
			
			// initial location of agents
            try {
                // Set location of RA agent to the bin's location and moving agent to the bike frame
				setAgPos(0, 0, 0);
				setAgPos(1, 4, 5);
				
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Objects are placed in the environment
			add(BIN, 0, 0);
        }
		
		// Change location of the RA/MA agent
        void moveTowards(int x, int y, int id) throws Exception {
			Location Loc;
			
			if (id == 0) {				// Robotic Arm agent is moving
				Loc = getAgPos(0);
			} else {					// Moving agent is moving
				Loc = getAgPos(1);
			}
			
            if (Loc.x < x)
                Loc.x++;
            else if (Loc.x > x)
                Loc.x--;
            if (Loc.y < y)
                Loc.y++;
            else if (Loc.y > y)
                Loc.y--;
			
			if (id == 0) {
				setAgPos(0, Loc);
				setAgPos(1, getAgPos(1));
			} else {
				setAgPos(0, getAgPos(0));
				setAgPos(1, Loc);
			}
        }
				
		//// Add or remove agents in the environment
		void addRoboticAgent() {
			add(RA, 0, 0);
		}
		
		void removeRoboticAgent() {
			remove(RA, getAgPos(0));
		}
		
		void addHolderHead() {
			add(HA, 8, 0);
		}
		
		void addHolderTop() {
			add(HA, 7, 0);
		}
		
		void addHolderDown() {
			add(HA, 7, 3);
		}
		
		void addHolderSeat() {
			add(HA, 3, 3);
		}
		
		void addHolderSeatStay() {
			add(HA, 1, 3);
		}
		
		void addHolderChain() {
			add(HA, 3, 5);
		}
		
		void removeHolderHead() {
			remove(HA, 8, 0);
		}
		
		void removeHolderTop() {
			remove(HA, 5, 0);
		}
		
		void removeHolderDown() {
			remove(HA, 7, 3);
		}
		
		void removeHolderSeat() {
			remove(HA, 3, 3);
		}
		
		void removeHolderSeatStay() {
			remove(HA, 1, 3);
		}
		
		void removeHolderChain() {
			remove(HA, 3, 5);
		}
		
		void addWelder1() {
			add(WA, 8, 2);
		}
		
		void addWelder2() {
			add(WA, 5, 5);
		}
		
		void addWelder3() {
			add(WA, 2, 0);
		}
		
		void addWelder4() {
			add(WA, 1, 5);
		}
		
		void addWelder5() {
			add(WA, 2, 2);
		}
		
		void removeWelder1() {
			remove(WA, 8, 2);
		}
		
		void removeWelder2() {
			remove(WA, 5, 5);
		}
		
		void removeWelder3() {
			remove(WA, 2, 0);
		}
		
		void removeWelder4() {
			remove(WA, 1, 5);
		}
		
		void removeWelder5() {
			remove(WA, 2, 2);
		}
		
		void addMovingAgent() {
			add(MA, 4, 5);
		}
		
		void removeMovingAgent() {
			remove(MA, getAgPos(1));
		}
		
		void weldSeatStay() {
			bikeFrameCompleted = true;
		}
		
		//// RA places bike parts on the assembly surface
		void dropHead() {
			add(HEAD, 8, 1);
		}
		
		void dropTop() {
			add(TOP, 7, 1);
			add(TOP, 6, 1);
			add(TOP, 5, 1);
			add(TOP, 4, 1);
			add(TOP, 3, 1);
		}
		
		void dropDown() {
			add(DOWN, 7, 2);
			add(DOWN, 6, 3);
			add(DOWN, 5, 4);
		}
		
		void dropSeat() {
			add(SEAT, 2, 1);
			add(SEAT, 3, 2);
			add(SEAT, 4, 3);
		}
		
		void dropSeatStay() {
			add(SEATSTAY, 2, 3);
			add(SEATSTAY, 1, 4);
		}
		
		void dropChain() {
			add(CHAIN, 2, 4);
			add(CHAIN, 3, 4);
			add(CHAIN, 4, 4);
		}
		
		// MA places bike frame away from assembly surface
		void pickBike() {
			remove(HEAD, 8, 1);
			remove(TOP, 7, 1);
			remove(TOP, 6, 1);
			remove(TOP, 5, 1);
			remove(TOP, 4, 1);
			remove(TOP, 3, 1);
			remove(DOWN, 7, 2);
			remove(DOWN, 6, 3);
			remove(DOWN, 5, 4);
			remove(SEAT, 2, 1);
			remove(SEAT, 3, 2);
			remove(SEAT, 4, 3);
			remove(SEATSTAY, 2, 3);
			remove(SEATSTAY, 1, 4);
			remove(CHAIN, 2, 4);
			remove(CHAIN, 3, 4);
			remove(CHAIN, 4, 4);
		}
		
        void stashBike() {
            add(BIKE, 9, 0);
			bikeFrameCompleted = false;				// Reset flag for new frame being assembled
        }
		
		// RA picks a bike part from the bin
		void pickPiece() {
			noOfPieces--;
			
			if (noOfPieces == 0) {
				binIsEmpty = true;
			}
		}
    }
    
    class ProductionView extends GridWorldView {

        public ProductionView(ProductionModel model) {
            super(model, "Bicycle Assembly", 600);
            defaultFont = new Font("Arial", Font.BOLD, 18); // change default font
            setVisible(true);
            repaint();
        }

        /** draw application objects */
        @Override
        public void draw(Graphics g, int x, int y, int object) {
            switch (object) {
                case ProductionEnv.BIN: drawBin(g, 0, 0); break;
                case ProductionEnv.BIKE: drawBicycle(g, 9, 0); break;
				case ProductionEnv.RA: drawAgent(g, x, y, Color.yellow, 0); break;
				case ProductionEnv.HA: drawAgent(g, x, y, Color.magenta, 1); break;
				case ProductionEnv.WA: drawAgent(g, x, y, Color.cyan, 2); break;
				case ProductionEnv.MA: drawAgent(g, x, y, Color.green, 3); break;
				case ProductionEnv.HEAD: drawPiece(g, x, y, "|"); break;
				case ProductionEnv.TOP: drawPiece(g, x, y, "--"); break;
				case ProductionEnv.DOWN: drawPiece(g, x, y, "/"); break;
				case ProductionEnv.SEAT: drawPiece(g, x, y, "\\"); break;
				case ProductionEnv.SEATSTAY: drawPiece(g, x, y, "/"); break;
				case ProductionEnv.CHAIN: drawPiece(g, x, y, "--"); break;
            }			
        }
		
		@Override
		public void drawAgent(Graphics g, int x, int y, Color c, int id) {
			String label = "";
			
			switch(id) {
				case 0: label = "RA"; break;
				case 1: label = "HA"; break;
				case 2: label = "WA"; break;
				case 3: label = "MA"; break;
			}
			
			super.drawAgent(g, x, y, c, -1);
            g.setColor(Color.black);
            drawString(g, x, y, defaultFont, label);
		}

        public void drawBin(Graphics g, int x, int y) {
            super.drawObstacle(g, x, y);
            g.setColor(Color.white);
            drawString(g, x, y, defaultFont, "~ BIN ~");
        }

        public void drawBicycle(Graphics g, int x, int y) {
			String label = bikeNo++ + " bikes";
			
           // super.drawObstacle(g, x, y);
           // g.setColor(Color.orange);
            //drawString(g, x, y, defaultFont, label);
        }
		
		public void drawPiece(Graphics g, int x, int y, String label) {
			super.drawObstacle(g, x, y);
			g.setColor(Color.white);
			drawString(g, x, y, defaultFont, label);
		}
    }    
}
