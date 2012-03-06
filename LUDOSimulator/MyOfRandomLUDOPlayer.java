package LUDOSimulator;
import java.util.Random;
/**
 * Example of automatic LUDO player
 * @author David Johan Christensen
 * 
 * @version 0.9
 *
 */
public class MyOfRandomLUDOPlayer implements LUDOPlayer{
	LUDOBoard board;
	Random rand;
	public MyOfRandomLUDOPlayer(LUDOBoard board)
	{
		this.board = board;
		rand = new Random();
	}
	public void play() {
		board.rollDice();
		
		for(int i=0;i<4;i++) // move first piece
		{			
			if(board.moveable(i))
			{
				board.moveBrick(i);
			}
		}
		
	}
	public synchronized void delay() {
		try {
			wait(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
