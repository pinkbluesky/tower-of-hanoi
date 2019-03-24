package project02;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MoveDiskListener implements MouseListener, MouseMotionListener
{
    private TowerOfHanoiComp comp;

    public MoveDiskListener(TowerOfHanoiComp comp)
    {
	this.comp = comp;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
	// not needed

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
	// TODO
	comp.startMovingDisk(e.getX(), e.getY());
	comp.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
	// TODO Auto-generated method stub
	boolean isGameDone = comp.stopMovingDisk(e.getX(), e.getY());
	comp.repaint();

	if (isGameDone)
	{
	    TowerOfHanoiViewer.whenGameIsDone();
	}
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
	// not needed

    }

    @Override
    public void mouseExited(MouseEvent e)
    {
	// not needed

    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
	comp.updateMovingDisk(e.getX(), e.getY());
	comp.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
	// not needed
    }

}
