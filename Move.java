package project02;

public class Move
{

    private Peg start;
    private Peg end;

    /**
     * Constructs a new Move with a given start and end peg.
     * 
     * @param start
     *            the start peg
     * @param end
     *            the end peg
     */
    public Move(Peg start, Peg end)
    {
	this.start = start;
	this.end = end;
    }

    /**
     * Returns the start peg.
     * 
     * @return the start peg
     */
    public Peg getStart()
    {
	return start;
    }

    /**
     * Returns the end peg.
     * 
     * @return the end peg
     */
    public Peg getEnd()
    {
	return end;
    }

    @Override
    public String toString()
    {
	return "Move disk from " + (start.ordinal() + 1) + " to " + (end.ordinal() + 1);
    }
}
