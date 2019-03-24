package project02;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class TowerOfHanoiViewer
{
    private static TowerOfHanoiComp comp;
    private static JButton restartButton;
    private static JButton nextMoveButton;
    private static JButton okButton;

    public static void main(String[] args)
    {
	// contruct the main game comp
	comp = new TowerOfHanoiComp();
	// TODO: add click listeners timers, etc.

	// construct north panel for holding the user interface
	JPanel northPanel = new JPanel();

	// add a jlabel with instructions
	northPanel.add(new JLabel(
		"<html>Welcome to the Tower of Hanoi! <br>The object of the game is to move all the disks to Peg 3.<br>Enter a number of disks between "
			+ TowerOfHanoiGame.MIN_DISKS + " and " + TowerOfHanoiGame.MAX_DISKS + "."));
	// add a jtextfield for the number of disks
	JTextField diskTextField = new JTextField(4);
	northPanel.add(diskTextField);
	// add 2 radio buttons ("play", "solve")
	JRadioButton playButton = new JRadioButton("Play");
	playButton.setActionCommand("PLAY");
	JRadioButton solveButton = new JRadioButton("Solve");
	solveButton.setActionCommand("SOLVE");
	ButtonGroup stateGroup = new ButtonGroup();
	stateGroup.add(playButton);
	stateGroup.add(solveButton);
	northPanel.add(playButton);
	northPanel.add(solveButton);

	// create the button listener for the "next move" button
	class NextMoveButtonListener implements ActionListener
	{

	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		if (comp.hasMoreMoves())
		{
		    comp.nextMove();
		    comp.repaint();

		    if (!comp.hasMoreMoves())
		    {
			whenGameIsDone();
		    }

		}
	    }

	}
	// add the "next move" button
	nextMoveButton = new JButton("Next Move");
	nextMoveButton.setEnabled(false);
	NextMoveButtonListener nextMoveListener = new NextMoveButtonListener();
	nextMoveButton.addActionListener(nextMoveListener);
	northPanel.add(nextMoveButton);

	// create the button listener for confirming the information
	class OkButtonListener implements ActionListener
	{

	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		String input = diskTextField.getText();
		int num;
		try
		{
		    num = Integer.parseInt(input);

		    if (num < TowerOfHanoiGame.MIN_DISKS || num > TowerOfHanoiGame.MAX_DISKS)
		    {
			JOptionPane.showMessageDialog(null, "Please enter an integer between "
				+ TowerOfHanoiGame.MIN_DISKS + " and " + TowerOfHanoiGame.MAX_DISKS + ".");
			return;
		    }
		} catch (NumberFormatException a)
		{
		    JOptionPane.showMessageDialog(null, "Please enter an integer for the number of disks.");
		    return;
		}

		ButtonModel b = stateGroup.getSelection();
		if (b == null)
		{
		    JOptionPane.showMessageDialog(null, "Please select a mode.");
		    return;
		}

		GameState state = (b.getActionCommand().equalsIgnoreCase("play") ? GameState.PLAY : GameState.SOLVE);

		comp.setState(num, state);

		if (state == GameState.SOLVE)
		{
		    nextMoveButton.setEnabled(true);
		}

		comp.repaint();

		okButton.setEnabled(false);

	    }

	}
	// add the "ok" button
	okButton = new JButton("Ok");
	OkButtonListener okListener = new OkButtonListener();
	okButton.addActionListener(okListener);
	northPanel.add(okButton);

	// create the button listener for the "restart" button
	class RestartButtonListener implements ActionListener
	{

	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		nextMoveButton.setEnabled(false);
		okButton.setEnabled(true);
		diskTextField.setText("");
		stateGroup.clearSelection();
		comp.reset();
		comp.repaint();

	    }

	}

	// add the "restart" button
	restartButton = new JButton("Restart");
	RestartButtonListener restartListener = new RestartButtonListener();
	restartButton.addActionListener(restartListener);
	northPanel.add(restartButton);

	// create mouse listener for play mode
	MoveDiskListener mouseListener = new MoveDiskListener(comp);
	comp.addMouseListener(mouseListener);
	comp.addMouseMotionListener(mouseListener);

	JFrame frame = new JFrame();
	frame.setSize(1000, 1000);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	frame.add(comp);
	frame.add(northPanel, BorderLayout.NORTH);
	frame.setVisible(true);
    }

    public static void whenGameIsDone()
    {
	nextMoveButton.setEnabled(false);
	int input = JOptionPane.showConfirmDialog(null, "The game is done. Play again?", "Confirm",
		JOptionPane.YES_NO_OPTION);

	if (input == JOptionPane.YES_OPTION)
	{
	    restartButton.doClick();
	} else
	{
	    System.exit(0);
	}

    }
}
