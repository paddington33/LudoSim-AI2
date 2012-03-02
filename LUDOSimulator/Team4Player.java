package LUDOSimulator;

import java.util.Arrays;
import java.util.Random;

import neuralNets.FinalLayerNeuron;
import neuralNets.InputLayerNeuron;
import neuralNets.NeuralNetwork;
import neuralNets.NeuralNetworkFactory;
import neuralNets.Neuron;

public class Team4Player implements LUDOPlayer {

	NeuralNetwork network;
	
	LUDOBoard board;
	public Team4Player(LUDOBoard board)
	{
		this.board = board;
		
		//10 loose - 87% agree
		//20 loose - 87% agree
		//30 loose - 87% agree
		//40 tie   - 87% agree
		//50 wins  - 87% agree
		//60 loose - 85% agree
		//130 wins - 90% agree
		int[] layerStructure = {24,130,4};
		try {
			network = NeuralNetworkFactory.makeAllConnections(layerStructure);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		trainNeuralNetworkToBeSemiSmart();
		
	}
	
	private boolean moveOut(int[][] current_board, int[][] new_board) {
		for(int i=0;i<4;i++) {
			if(board.inStartArea(current_board[board.getMyColor()][i],board.getMyColor())&&!board.inStartArea(new_board[board.getMyColor()][i],board.getMyColor())) {
				return true;
			}
		}
		return false;
	}

	private boolean hitOpponentHome(int[][] current_board, int[][] new_board) {
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
	
	private boolean hitMySelfHome(int[][] current_board, int[][] new_board) {
		for(int i=0;i<4;i++) {
			if(!board.inStartArea(current_board[board.getMyColor()][i],board.getMyColor())&&board.inStartArea(new_board[board.getMyColor()][i],board.getMyColor())) {
				return true;
			}
		}
		return false;
	}
	
	
	int tempMove = 0;
	private void trainNeuralNetworkToBeSemiSmart()
	{
		LUDOPlayer semi = new MySemiSmartLUDOPlayer(board);
		
		int[] inputs = new int[24];
		
		int[][] current_board = board.getBoardState();
		
		for(int i = 0; i < 4;i++)
		{
			int[][] new_board = board.getNewBoardState(i, board.getMyColor(), board.getDice());	

			if(hitMySelfHome(current_board, new_board))
				inputs[i*6 + 0] = 1;
			else
				inputs[i*6 + 0] = 0;
			
			if(hitOpponentHome(current_board, new_board))
				inputs[i*6 + 1] = 1;
			else
				inputs[i*6 + 1] = 0;
			
			if(moveOut(current_board, new_board))
				inputs[i*6 + 2] = 1;
			else
				inputs[i*6 + 2] = 0;
			
			if(board.isGlobe(new_board[board.getMyColor()][i]))
				inputs[i*6 + 3] = 1;
			else
				inputs[i*6 + 3] = 0;
			
			if(board.isStar(new_board[board.getMyColor()][i]))
				inputs[i*6 + 4] = 1;
			else
				inputs[i*6 + 4] = 0;
			
			if(board.atHome(new_board[board.getMyColor()][i],board.getMyColor()))
				inputs[i*6 + 5] = 1;
			else
				inputs[i*6 + 5] = 0;
		}
		
		int move = ((MySemiSmartLUDOPlayer)semi).getMove(board.getDice());
		tempMove = move;
		
		int outputs[] = new int[4];
		
		for(int i = 0;i<4;i++)
		{
			if(i == move)
				outputs[i] = 1;
			else
				outputs[i] = 0;			
		}
		
		int j = 0;
		for(Neuron neuron : network.getInputLayer())
		{
			((InputLayerNeuron)neuron).setValue(inputs[j]);
			j++;
		}
		
		j = 0;
		for(Neuron neuron : network.getFinalLayer())
		{
			((FinalLayerNeuron)neuron).setDesiredOutput(outputs[j]);
			j++;
		}
		
		network.forwardPropagation();
		network.backwardPropagation();
		
//		network.printAllOutputs();
//		System.out.println(move);
		
	}

	
	public int cnt = 0;
	public int moveCnt = 0;
	
	@Override
	public void play() 
	{
//		System.out.println("New turn");
		
		board.rollDice();
		
		trainNeuralNetworkToBeSemiSmart();
		
		FinalLayerNeuron[] outputs = new FinalLayerNeuron[network.getFinalLayer().size()];
		
		for(int i = 0;i < outputs.length;i++)
			outputs[i] = (FinalLayerNeuron) network.getFinalLayer().get(i);
		
		Arrays.sort(outputs);
		
//		for(int i = 0;i<outputs.length;i++)
//			System.out.println(network.getFinalLayer().indexOf(outputs[i]));
			
		Random rand = new Random();
		for(int i=0;i<4;i++) // find a random moveable brick
		{
			int move = network.getFinalLayer().indexOf(outputs[i]);
			
//			move = rand.nextInt(3);
			
			if(board.moveable(move))
			{
				
				if(move == tempMove)
					cnt++;
				
				moveCnt++;
				
//				System.out.println(move);
				board.moveBrick(move);
				break;
			}
		}
		
		for(int i=0;i<4;i++) // move first piece
		{			
			if(board.moveable(i))
			{
				board.moveBrick(i);
			}
		}
		
	}
	
	public int choosePieceToMove()
	{
		int[][] current_board = board.getBoardState();
		int[] inputs = new int[4];
		for(int i = 0;i<4;i++)
		{
			int[][] new_board = board.getNewBoardState(i, board.getMyColor(), board.getDice());
			if(board.isStar(new_board[board.getMyColor()][i]))
				inputs[i] = 1;
			else
				inputs[i] = 0;
		}
		
		int neuronIndex = 0;
		for(Neuron neuron : network.getInputLayer())
		{
			((InputLayerNeuron)neuron).setValue(inputs[neuronIndex]);
			neuronIndex++;
		}
		
		network.forwardPropagation();
		
		double valueOfBestMove = 0.0;
		int indexOfBestMove = 0;
		int i = 0;
		
		for(Neuron neuron : network.getFinalLayer())
		{
			if(((FinalLayerNeuron)neuron).getOutput() > valueOfBestMove)
			{
				valueOfBestMove = ((FinalLayerNeuron)neuron).getOutput();
				indexOfBestMove = i;
			}
			i++;
		}
		
		return indexOfBestMove;
	}

}
