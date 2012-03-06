package LUDOSimulator;

import java.util.ArrayList;

import neuralNets.FinalLayerNeuron;
import neuralNets.InputLayerNeuron;
import neuralNets.NeuralNetwork;
import neuralNets.NeuralNetworkFactory;
import neuralNets.Neuron;

public class teachingPlayerUsingANN {

	/**
	 * @param args
	 */
	static LUDOBoard board = new LUDOBoard();
	
	public static void main(String[] args) {
		
		RecordInputAndOutputListener eventListener = new RecordInputAndOutputListener();
		
		board.addGameEventListener(eventListener);
		
		LUDOPlayer player1 = new MySemiSmartLUDOPlayer(board);
		LUDOPlayer player2 = new MySemiSmartLUDOPlayer(board);
		LUDOPlayer player3 = new MySemiSmartLUDOPlayer(board);
		LUDOPlayer player4 = new MySemiSmartLUDOPlayer(board);
		
		board.setPlayer(player1, LUDOBoard.BLUE);
		board.setPlayer(player2, LUDOBoard.GREEN);
		board.setPlayer(player3, LUDOBoard.RED);
		board.setPlayer(player4, LUDOBoard.YELLOW);
		
		int noGames = 100;
		try {
			for(int game = 0;game<noGames;game++)
			{
				board.play();
				board.kill();
				board.reset();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// {28,55,4} -> 82%
		// {28,40,4} -> 82%
		
		int[] layerStructure = {28,4};
		NeuralNetwork network = null;
		try {
			network = NeuralNetworkFactory.makeAllConnections(layerStructure);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<int[][]> allInputs = eventListener.getInputs();
		ArrayList<int[]> allOutputs = eventListener.getOutputs();
		
		int totalMoves = allInputs.size();
		System.out.println("Total moves: " + totalMoves );
		
		removeAllOnePieceOnly(allInputs,allOutputs);
		
		int s = 0, s2 = 0;
		
		for(int i = 0;i<allInputs.size();i++)
		{
			int[][] inputs = allInputs.get(i);
			int[] outputs = allOutputs.get(i);
			
			int j = 0;
			for(Neuron neuron : network.getInputLayer())
			{
				((InputLayerNeuron)neuron).setValue(inputs[j/7][j%7]);
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

		}
		
		
		for(int k = 0; k < allInputs.size(); k++)
		{
			int[][] inputs = allInputs.get(k);
			int[] outputs = allOutputs.get(k);
			
			int j = 0;
			for(Neuron neuron : network.getInputLayer())
			{
				((InputLayerNeuron)neuron).setValue(inputs[j/7][j%7]);
				j++;
			}
			
			network.forwardPropagation();
			
			int choice = 0;
			double tempMax = 0.0;
			int tempIndex = 0;
			for(int i = 0 ;i<4;i++)
			{
				if(outputs[i] == 1 && network.getFinalLayer().get(i).getOutput() > .5)
				{
					s++;
				}
				if(outputs[i] == 0 && network.getFinalLayer().get(i).getOutput() > .5)
					s--;
				
				if(outputs[i] == 1 )
					choice = i;
				
				if(network.getFinalLayer().get(i).getOutput() > tempMax)
				{
					tempMax = network.getFinalLayer().get(i).getOutput();
					tempIndex = i;
				}
				
//				System.out.print(outputs[i] + " ");
//				System.out.println(network.getFinalLayer().get(i).getOutput());
			}
			
			if(tempIndex == choice)
				s2++;
		
		}
		
		System.out.println(s + " out of " + allInputs.size() + " " + (double)s/allInputs.size() * 100.0 + "% " 
						+ (double)(totalMoves-allInputs.size()+s)/totalMoves * 100.0 + "%");
		System.out.println(s2 + " out of " + allInputs.size() + " " + (double)s2/allInputs.size() * 100.0 + "% "
						+ (double)(totalMoves-allInputs.size()+s2)/totalMoves * 100.0 + "%");
		
	}

	private static void removeAllOnePieceOnly(ArrayList<int[][]> allInputs,	ArrayList<int[]> allOutputs) {
//		ArrayList<int[][]> inputs = new ArrayList<int[][]>();
//		ArrayList<int[]> outputs = new ArrayList<int[]>();
		
		int choiceCnt = 0;
				
		for(int j = allInputs.size() -1 ;j>-1;j--)
		{
			for(int i = 0;i < 4;i++)
			{
				choiceCnt += allInputs.get(j)[i][6];
			}
			
			if(choiceCnt < 2)
			{
				allOutputs.remove(j);
				allInputs.remove(j);
			}
			choiceCnt = 0;
		}
	}
	
	


}
