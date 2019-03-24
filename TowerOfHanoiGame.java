package project02;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class TowerOfHanoiGame
{
    private ArrayList<Integer> start;
    private ArrayList<Integer> helper;
    private ArrayList<Integer> end;

    public static final double DISK_WIDTH = 30;
    public static final double DISK_HEIGHT = 30;
    public static final double PILE_SPACING = 80;

    public static final int MAX_DISKS = 10;
    public static final int MIN_DISKS = 3;

    private double x;
    private double y;
    private Color[] colors;

    private GameState state;

    private DiskMover mover;

    /**
     * Constructs a new TowerOfHanoiGame.
     * 
     * @param x
     *            the top left x value
     * @param y
     *            the top left y value
     * @param colors
     *            the array of colors (index 0 = smallest disk color)
     */
    public TowerOfHanoiGame(double x, double y, Color[] colors)
    {

	start = new ArrayList<Integer>();
	helper = new ArrayList<Integer>();
	end = new ArrayList<Integer>();

	this.x = x;
	this.y = y;

	this.colors = colors;

	this.state = GameState.BEGINNING;

	this.mover = null;
    }

    /**
     * Draws the 3 pile of disks.
     * 
     * @param g2
     *            the Graphics2D object
     */
    public void draw(Graphics2D g2)
    {
	if (state == GameState.PLAY || state == GameState.SOLVE)
	{
	    // draw the disks
	    drawPeg(g2, Peg.ONE);
	    drawPeg(g2, Peg.TWO);
	    drawPeg(g2, Peg.THREE);
	}

	// draw the 3 brackets showing where the 3 piles are
	g2.setColor(Color.BLACK);
	Peg[] pegs = new Peg[] { Peg.ONE, Peg.TWO, Peg.THREE };
	for (int i = 0; i < 3; i++)
	{
	    // get a rectangle that is on the current peg, the largest possible,
	    // and at the very bottom of the peg
	    Rectangle2D.Double rect = calculateDisk(pegs[i], MAX_DISKS - 1, 0);

	    // use the rectangle to calculate the 3 bracket lines
	    Line2D.Double leftLine = new Line2D.Double(rect.getX(), rect.getY(), rect.getX(),
		    rect.getY() + rect.getHeight());
	    Line2D.Double rightLine = new Line2D.Double(rect.getX() + rect.getWidth(), rect.getY(),
		    rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
	    Line2D.Double bottomLine = new Line2D.Double(leftLine.getP2(), rightLine.getP2());

	    g2.draw(leftLine);
	    g2.draw(rightLine);
	    g2.draw(bottomLine);
	}
    }

    /**
     * Draws a whole peg.
     * 
     * @param g2
     * @param p
     */
    private void drawPeg(Graphics2D g2, Peg p)
    {
	ArrayList<Integer> arr = getArrayFromPeg(p);

	for (int i = 0; i < arr.size(); i++)
	{
	    // set color based on number of the disk
	    g2.setColor(colors[arr.get(i)]);

	    Rectangle2D.Double rect = calculateDisk(p, arr.get(i), i);

	    // draw disk
	    g2.fill(rect);
	    g2.draw(rect);
	}
    }

    /**
     * Calculates and returns the Rectangle2D.Double object that represents the
     * given peg and index of peg. <br>
     * Precondition: peg must not be null, index must be in bounds.
     * 
     * @param peg
     *            the Peg the disk is on
     * @param size
     *            the size of the disk (0=smallest, MAX_DISK - 1=largest)
     * @param positionIndex
     *            the position of the disk on the peg (0=lowest, MAX_DISK - 1 =
     *            highest)
     * @return the Rectangle2D.double object representing the disk
     */
    public Rectangle2D.Double calculateDisk(Peg peg, int size, int positionIndex)
    {
	// check that peg is not null and the current state is not beginning
	if (peg != null)
	{
	    if (positionIndex >= 0 && positionIndex < MAX_DISKS)
	    {
		if (size >= 0 && size < MAX_DISKS)
		{
		    double rectX = this.x + (MAX_DISKS - size) * DISK_WIDTH
			    + peg.ordinal() * (DISK_WIDTH * (MAX_DISKS * 2 - 1) + PILE_SPACING);
		    double rectY = this.y + ((MAX_DISKS - 1) - (positionIndex)) * DISK_HEIGHT;

		    return new Rectangle2D.Double(rectX, rectY, DISK_WIDTH * (2 * size + 1), DISK_HEIGHT);
		} else
		{
		    throw new IllegalArgumentException("Size must be between 0 and MAXDISKS - 1.");
		}
	    } else
	    {
		throw new IllegalArgumentException("Position index must be in bounds.");
	    }
	} else
	{
	    throw new IllegalArgumentException("Peg cannot be null.");
	}
    }

    /**
     * This method returns if there are more moves or not.<br>
     * 
     * @return true if there are more moves, false if not or if the state is not
     *         "solve"
     */
    public boolean hasMoreMoves()
    {
	if (state == GameState.SOLVE)
	{
	    return mover.hasMoreMoves();
	}
	return false;
    }

    /**
     * If the current state is "solve" and if there are more moves, this method
     * does the next move.<br>
     * Precondition: current state must be SOLVE.<br>
     * If there are no more moves, this does nothing.
     */
    public void nextMove()
    {
	// if the state is solve and the mover has more moves
	if (state == GameState.SOLVE)
	{
	    // if the mover has more moves
	    if (mover.hasMoreMoves())
	    {
		doMove(mover.nextMove());
	    }
	} else
	{
	    throw new IllegalStateException("Current state must be SOLVE.");
	}
    }

    /**
     * Moves a disk according to the move.<br>
     * Precondition: The current state must be PLAY, the move must not be null,
     * and the move has to be valid (the starting peg has to contain a disk).
     *
     * 
     * @param m
     *            the Move object describing the move
     */
    public void movePlayer(Move m)
    {
	if (state == GameState.PLAY)
	{
	    doMove(m);
	} else
	{
	    throw new IllegalStateException("The current state must be PLAY.");
	}

    }

    /**
     * Checks if the game is done in the "play" state. <br>
     * Precondition: The game must be in "play" state.
     * 
     * @return true if the game is done, false if not
     */
    public boolean isPlayDone()
    {
	if (state == GameState.PLAY)
	{
	    // checks if the size is correct
	    if (end.size() == mover.getNum())
	    {
		// for each number, check if it has (0, 1, ..., numOfDisks)
		for (int i = 0; i < mover.getNum(); i++)
		{
		    // if they don't match, return false
		    if (end.get(i) != mover.getNum() - i - 1)
		    {
			return false;
		    }
		}
		return true;
	    }
	    return false;
	} else
	{
	    throw new IllegalStateException("CurrentState must be PLAY.");
	}
    }

    /**
     * Does the given move.<br>
     * Precondition: the move cannot be null.<br>
     * ***If the move is invalid(starting peg does not contain a disk to move
     * away or moves a larger on top of a smaller), this does nothing.
     * 
     * @param m
     *            the move to do
     */
    private void doMove(Move m)
    {
	// initialize peg arrays
	ArrayList<Integer> s = getArrayFromPeg(m.getStart());
	ArrayList<Integer> e = getArrayFromPeg(m.getEnd());

	// check if the starting peg has at least one disk to move
	if (s.size() >= 1)
	{
	    // check if the move doesn't move a larger disk on a smaller one
	    if (e.size() == 0 || s.get(s.size() - 1) < e.get(e.size() - 1))
	    {
		int removed = s.remove(s.size() - 1);
		e.add(removed);
	    }
	}

    }

    /**
     * Sets the number of disks with the new state.<br>
     * Precondition: the number must be between (inclusive) MIN_DISKS and
     * MAX_DISKS and the state must not be null or "beginning". <br>
     * 
     * @param num
     *            the number of disks
     * @param state
     *            the new state (either "play" or "solve")
     */
    public void setState(int num, GameState state)
    {
	// check that the number of disks is in bounds and the state is not
	// "beginning"
	if (num >= MIN_DISKS && num <= MAX_DISKS)
	{
	    if (state != GameState.BEGINNING || state != null)
	    {
		// initialize the starting peg
		start = new ArrayList<Integer>();
		for (int i = num - 1; i >= 0; i--)
		{
		    start.add(i);
		}

		// initialize the DiskMover even if the state is play
		mover = new DiskMover(num, Peg.ONE, Peg.THREE, Peg.TWO);

		this.state = state;
	    } else
	    {
		throw new IllegalArgumentException("New state should not be BEGINNING or null.");
	    }
	} else
	{
	    throw new IllegalArgumentException("Number of disks is not between MIN DISKS and MAX DISKS.");
	}
    }

    /**
     * Returns the current GameState.
     * 
     * @return the current GameState
     */
    public GameState getState()
    {
	return state;
    }

    /**
     * Resets the game to "beginning" and clears the pegs.
     */
    public void reset()
    {
	start = new ArrayList<Integer>();
	helper = new ArrayList<Integer>();
	end = new ArrayList<Integer>();

	state = GameState.BEGINNING;
	mover = null;

    }

    /**
     * Returns a cloned version of the Array containing the peg's
     * information.<br>
     * Precondition: the peg must not be null
     * 
     * @param peg
     *            the peg to get the array of
     * @return the Array
     */
    public ArrayList<Integer> getArrayCopyFromPeg(Peg peg)
    {
	return new ArrayList<Integer>(getArrayFromPeg(peg));
    }

    /**
     * Returns the corresponding ArrayList for the given peg.<br>
     * Precondition: peg must not be null.
     * 
     * @param peg
     *            the Peg to find the ArrayList of
     * @return the ArrayList representing the Peg
     */
    private ArrayList<Integer> getArrayFromPeg(Peg peg)
    {
	if (peg != null)
	{
	    if (peg == Peg.ONE)
	    {
		return start;
	    } else if (peg == Peg.TWO)
	    {
		return helper;
	    } else
	    {
		return end;
	    }
	} else
	{
	    throw new IllegalArgumentException("Peg cannot be null.");
	}
    }

}
