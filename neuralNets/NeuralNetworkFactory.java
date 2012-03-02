package neuralNets;

import java.util.ArrayList;

public class NeuralNetworkFactory 
{
	public static NeuralNetwork makeAllConnections(int[] nNeuronsInLayers) throws Exception
	{

		ArrayList<ArrayList<Neuron>> network = new ArrayList<ArrayList<Neuron>>();
		
		for(int i = 0 ; i < nNeuronsInLayers.length; i++ )
		{
			ArrayList<Neuron> layer = new ArrayList<Neuron>();
			for(int j = 0;j < nNeuronsInLayers[i];j++)
			{
				Neuron neuron;
				if(i == 0)
					neuron = new InputLayerNeuron();
				else if(i == nNeuronsInLayers.length - 1)
					neuron = new FinalLayerNeuron();
				else
					neuron = new Neuron();
				layer.add(neuron);
			}
			
			network.add(layer);
			
			//If not first layer generate connections
			if(i != 0)
			{
				for(int j = 0; j < nNeuronsInLayers[i-1] ; j++)
				{
					for(int k = 0; k < nNeuronsInLayers[i]; k++ )
					{
						NeuronConnection neuronConnection = new NeuronConnection(
								network.get(i-1).get(j), 						//jth neuron in layer i-1
								network.get(i).get(k),							//kth neuron in layer i
								Math.random() / (nNeuronsInLayers[i-1]*nNeuronsInLayers[i]));			//random weight
						
						network.get(i-1).get(j).addOutputConnection(neuronConnection);
						network.get(i).get(k).addInputConnection(neuronConnection);
					}
				}
			}
		}
		
		NeuralNetwork neuralNetwork = new NeuralNetwork(network);
		
		return neuralNetwork;
	}
}
