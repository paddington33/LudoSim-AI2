package LUDOSimulator;

import java.util.ArrayList;

public class RecordInputAndOutputListener implements GameEventListener {

	
	ArrayList<int[][]> 	inputs 	= new ArrayList<int[][]>();
	ArrayList<int[]> 	outputs = new ArrayList<int[]>();
	
	
	@Override
	public void gameEvent(LUDOBoard board, EventType eventType, Object pieceMoved) 
	{
		int diceValue = board.getDice();
		int color = board.getMyColor();
		
		int[][] currentBoard = board.getBoardState();
		int[][] nextBoard;
		int index;
		boolean atHome, isStar, isGlobe, hitMyselfHome, hitOpponentHome, moveOut, moveAble;
		
		int[][] inputs = new int[4][7];
		
		for(int piece = 0;piece < 4;piece++)
		{
			nextBoard = board.getNewBoardState(piece, color, diceValue);
			index = nextBoard[color][piece];
			
			atHome = board.atHome(index, color);
			isStar = board.isStar(index);
			isGlobe = board.isGlobe(index);
			hitMyselfHome = hitMySelfHome(board, currentBoard, nextBoard);
			hitOpponentHome = hitOpponentHome(board, currentBoard, nextBoard);
			moveOut = moveOut(board, currentBoard, nextBoard);
			moveAble = board.moveable(piece);
			
			inputs[piece][0] = atHome 			? 1 : 0;
			inputs[piece][1] = isStar 			? 1 : 0;
			inputs[piece][2] = isGlobe 			? 1 : 0;
			inputs[piece][3] = hitMyselfHome 	? 1 : 0;
			inputs[piece][4] = hitOpponentHome 	? 1 : 0;
			inputs[piece][5] = moveOut 			? 1 : 0;
			inputs[piece][6] = moveAble			? 1 : 0;
			
		}
		this.inputs.add(inputs);
		
		
		int[] outputs = new int[4];
		
		for(int i = 0;i<4;i++)
			outputs[i] = ((Integer)pieceMoved == i) ? 1 : 0;
		
		if(!this.inputs.isEmpty())
			this.outputs.add(outputs);
	}
	
	public void printInputs()
	{
		for(int[][] inputs : this.inputs)
		{
			for(int i = 0;i< inputs.length;i++)
			{
				for(int j = 0;j< inputs[i].length;j++)
					System.out.print(inputs[i][j] + " ");
				
				System.out.println("");
			}
			System.out.println("- - - - - - -");
		}
		
	}
	
	public ArrayList<int[][]> getInputs() {
		return inputs;
	}

	public void setInputs(ArrayList<int[][]> inputs) {
		this.inputs = inputs;
	}

	public ArrayList<int[]> getOutputs() {
		return outputs;
	}

	public void setOutputs(ArrayList<int[]> outputs) {
		this.outputs = outputs;
	}

	public void printOutputs()
	{
		for(int[] outputs : this.outputs)
		{
			for(int i = 0;i<4;i++)
				System.out.print(outputs[i] + " ");
			System.out.println("");
		}
	}
	
	public void printInputAndOutputs()
	{
		int k = 0;
		for(int[][] inputs : this.inputs)
		{
			for(int i = 0;i< inputs.length;i++)
			{
				for(int j = 0;j< inputs[i].length;j++)
					System.out.print(inputs[i][j] + " ");
				
				System.out.print("-> " + this.outputs.get(k)[i] + " ");
				
				System.out.println("");
			}
			System.out.println("- - - - - - -");
			k++;
		}
	}
	
	private boolean moveOut(LUDOBoard board, int[][] current_board, int[][] new_board) {
		for(int i=0;i<4;i++) {
			if(board.inStartArea(current_board[board.getMyColor()][i],board.getMyColor())&&!board.inStartArea(new_board[board.getMyColor()][i],board.getMyColor())) {
				return true;
			}
		}
		return false;
	}

	private boolean hitOpponentHome(LUDOBoard board, int[][] current_board, int[][] new_board) {
		for(int i=0;i<4;i++) {
			for(int j=0;j<4;j++) {
				if(board.getMyColor()!=i) {
					if(board.atField(current_board[i][j])&&!board.atField(new_board[i][j])) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean hitMySelfHome(LUDOBoard board, int[][] current_board, int[][] new_board) {
		for(int i=0;i<4;i++) {
			if(!board.inStartArea(current_board[board.getMyColor()][i],board.getMyColor())&&board.inStartArea(new_board[board.getMyColor()][i],board.getMyColor())) {
				return true;
			}
		}
		return false;
	}
}
