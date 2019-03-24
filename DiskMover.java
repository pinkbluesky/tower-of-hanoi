package project02;

public class DiskMover
{

    private DiskMover mini;
    private int num;
    private MoverState state;

    private Peg start;
    private Peg end;
    private Peg helper;

    /**
     * Constructs a new DiskMover. A DiskMover moves the given number of disks
     * from the start peg to the end peg.<br>
     * Precondition: The start peg must contain the given number of disks.
     * 
     * @param num
     * @param start
     * @param end
     */
    public DiskMover(int num, Peg start, Peg end, Peg helper)
    {
	this.num = num;
	this.start = start;
	this.end = end;
	this.helper = helper;

	state = MoverState.BEFORE_LARGEST;

	if (num > 1)
	{
	    // mini starts as moving
	    mini = new DiskMover(num - 1, start, helper, end);
	} else
	{
	    mini = null;
	}

    }

    /**
     * Checks if there are more moves to be done.
     * 
     * @return true if there are more moves, false if not
     */
    public boolean hasMoreMoves()
    {
	if (state == MoverState.DONE)
	{
	    return false;
	}

	return true;
    }

    /**
     * Calculates and returns the next move.<br>
     * ***If there are no more moves to be done, returns null.
     * 
     * @return the next move
     */
    public Move nextMove()
    {
	if (num <= 1)
	{
	    finish();
	    return new Move(start, end);
	}

	// if mini is DONE
	// update the current state of the main DiskMover
	if (!mini.hasMoreMoves())
	{
	    nextState();

	    // if this new state is after largest
	    if (state == MoverState.AFTER_LARGEST)
	    {
		// update mini's start and end
		mini = new DiskMover(num - 1, helper, end, start);
	    }
	}

	// now, return the correct move corresponding to the state
	// if this object is done with moving everything
	if (state == MoverState.DONE)
	{
	    return null;
	}

	// if state is before largest
	if (state == MoverState.BEFORE_LARGEST || state == MoverState.AFTER_LARGEST)
	{
	    // use the mini object to move the mini disks to the helper peg
	    Move next = mini.nextMove();

	    // if the state is After largest and mini is done, then this is
	    // done too
	    if (state == MoverState.AFTER_LARGEST && !mini.hasMoreMoves())
	    {
		finish();
	    }

	    return next;
	}

	// if (state == MoverState.LARGEST)
	// move the largest from start to end
	return new Move(start, end);

    }

    /**
     * Updates the current state to the next state.<br>
     * ***If the current state is DONE, this method does nothing.<br>
     * ***If the current state is null, this method does nothing.
     * 
     */
    public void nextState()
    {
	if (state == MoverState.BEFORE_LARGEST)
	{
	    state = MoverState.LARGEST;
	} else if (state == MoverState.LARGEST)
	{
	    state = MoverState.AFTER_LARGEST;
	} else if (state == MoverState.AFTER_LARGEST)
	{
	    state = MoverState.DONE;
	}
    }

    public void finish()
    {
	state = MoverState.DONE;
    }

    /**
     * Returns the current state of the Mover.
     * 
     * @return the current state
     */
    public MoverState getState()
    {
	return state;
    }

    public int getNum()
    {
	return num;
    }
}
