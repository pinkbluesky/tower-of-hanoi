package project02;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JComponent;

public class TowerOfHanoiComp extends JComponent
{

    public static final Color[] COLORS = { Color.pink, Color.red, Color.orange, Color.yellow, Color.green,
	    new Color(0, 153, 0), Color.cyan, new Color(102, 178, 255), Color.blue, new Color(51, 0, 102) };

    public static final double X = 0;
    public static final double Y = 0;

    private TowerOfHanoiGame game;

    private Peg movingDiskStart;
    private Rectangle2D.Double movingDisk;

    /**
     * Constructs a new TowerOfHanoiComp.
     */
    public TowerOfHanoiComp()
    {
	game = new TowerOfHanoiGame(X, Y, COLORS);
	movingDisk = null;
	movingDiskStart = null;
    }

    public void paintComponent(Graphics g)
    {
	Graphics2D g2 = (Graphics2D) g;

	// draws the game
	game.draw(g2);

	// if moving disk is not null, draw the moving disk
	if (movingDisk != null)
	{
	    g2.draw(movingDisk);
	}

    }

    /**
     * Calculates and returns the Peg that the given position corresponded
     * to.<br>
     * ***If the xy position is in the box of peg1, it returns peg 1. And the
     * same for the other pegs.<br>
     * ***If the position is not valid, return null.
     * 
     * @param posX
     *            the x value of the position
     * @param posY
     *            the y value of the position
     * @return the peg the position is on
     */
    private Peg calculatePegFromPos(double posX, double posY)
    {
	// initialize a maxY variable
	double maxY = Y + TowerOfHanoiGame.DISK_HEIGHT * TowerOfHanoiGame.MAX_DISKS;

	Peg[] pegs = new Peg[] { Peg.ONE, Peg.TWO, Peg.THREE };

	// check each peg
	for (int i = 0; i < 3; i++)
	{
	    Rectangle2D.Double pegRect = game.calculateDisk(pegs[i], TowerOfHanoiGame.MAX_DISKS - 1, 0);

	    // if the posX is within the pegRect and the posY is less than maxY
	    // and greater than this.Y
	    if ((posX >= pegRect.getMinX() && posX <= pegRect.getMaxX()) && (posY >= Y && posY <= maxY))
	    {
		return pegs[i];
	    }
	}

	return null;
    }

    public void setState(int num, GameState state)
    {
	game.setState(num, state);
    }

    /**
     * Returns if there are more moves or not
     * 
     * @return true if there are more moves, false if not
     */
    public boolean hasMoreMoves()
    {
	return game.hasMoreMoves();
    }

    /**
     * Does the next move if there are more.
     */
    public void nextMove()
    {
	if (game.hasMoreMoves())
	{
	    game.nextMove();
	}
    }

    /**
     * Initializes the moving disk with the posX and posY if and only if they
     * are valid positions.<br>
     * This method will run if the state is in "play".<br>
     * ***A valid position is one that is inside or on the rectangle
     * representing the top disk of every peg.<br>
     * ***If the position is not valid, this does nothing.<br>
     * ***If the peg chosen does not contain any disks to move, this method does
     * nothing.
     * 
     * @param posX
     *            the x value of the moving disk
     * @param posY
     *            the y value of the moving disk
     */
    public void startMovingDisk(double posX, double posY)
    {
	if (game.getState() == GameState.PLAY)
	{
	    // calculate which peg the posX, posY is on
	    Peg peg = calculatePegFromPos(posX, posY);

	    // check if the position is valid
	    if (peg != null)
	    {
		ArrayList<Integer> pegArr = game.getArrayCopyFromPeg(peg);

		// check if the peg contains a disk to move
		if (pegArr.size() >= 1)
		{
		    // initialize the moving disk
		    movingDisk = game.calculateDisk(peg, pegArr.get(pegArr.size() - 1), pegArr.size() - 1);
		    // intialize the start peg for the moving disk
		    movingDiskStart = peg;
		}
	    }
	}
    }

    /**
     * Updates the moving disk if and only if the moving disk is initialized.
     * <br>
     * This method will run if the state is in "play" and moving disk is
     * initialized.
     * 
     * 
     * @param posX
     *            the new x value
     * @param posY
     *            the new y value
     */
    public void updateMovingDisk(double posX, double posY)
    {
	if (game.getState() == GameState.PLAY)
	{
	    if (movingDisk != null)
	    {
		movingDisk.setRect(posX, posY, movingDisk.getWidth(), movingDisk.getHeight());
	    }
	}
    }

    /**
     * Stops moving the disk and places it at (posX, posY) if the position is
     * valid.<br>
     * This method also checks if the game is done after the move.<br>
     * This method will run if the state is in "play" and moving disk is
     * initialized.<br>
     * ***If the position is not valid, this method does nothing.<br>
     * An invalid position is one that is not on a peg or that moves a disk on
     * top of a smaller disk.
     * 
     * @param posX
     *            the x value of the placement
     * @param posY
     *            the y value of the placement
     * @return true if the game is now done, false if not
     */
    public boolean stopMovingDisk(double posX, double posY)
    {
	if (game.getState() == GameState.PLAY)
	{
	    Peg movingDiskEnd = calculatePegFromPos(posX, posY);
	    // if the position is valid and moving disk is initialized
	    if (movingDiskEnd != null && movingDisk != null)
	    {
		game.movePlayer(new Move(movingDiskStart, calculatePegFromPos(posX, posY)));
		// reset moving disk
		movingDiskStart = null;
		movingDisk = null;
		// check if the game is done
		return isPlayDone();
	    }
	    // reset moving disk
	    movingDiskStart = null;
	    movingDisk = null;
	} else
	{
	    throw new IllegalStateException("Current state must be PLAY.");
	}

	return false;
    }

    /**
     * Returns if the game was solved.<br>
     * Precondition: the state must be in "play"
     * 
     * @return true if the game was done, false if not
     */
    public boolean isPlayDone()
    {
	// check that the state is "play"
	if (game.getState() == GameState.PLAY)
	{
	    return game.isPlayDone();
	} else
	{
	    throw new IllegalStateException("Current state must be PLAY.");
	}
    }

    public void reset()
    {
	game.reset();
	movingDiskStart = null;
	movingDisk = null;
    }

}
