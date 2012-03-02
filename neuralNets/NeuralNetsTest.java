package neuralNets;

import java.util.ArrayList;

public class NeuralNetsTest {

	/**
	 * @param args
	 */
	
	public static double randomOfTwo()
	{
		if(Math.random() > .5)
			return 0.0;
		else
			return 1.0;
	}
	
	public static double randomValue()
	{
		return Math.random();
	}
	
	public static int[] randomPattern(int size)
	{
		int[] pattern = new int[size];
		for(int i = 0;i<size;i++)
		{
			if(Math.random() > .5)
				pattern[i] = 1;
			else
				pattern[i] = 0;
		}
		return pattern;
	}
	
	
	
	public static int[] invertPattern(int[] pattern)
	{
		int[] invertedPattern = new int[pattern.length];
		for(int i = 0;i<pattern.length;i++)
			if(pattern[i] == 0)
				invertedPattern[i] = 1;
			else
				invertedPattern[i] = 0;
		return invertedPattern;
	}
	
	public static int[] shiftPattern(int[] pattern)
	{
		int shiftedPattern[] = new int[pattern.length];
		
		for(int i = 0 ; i < pattern.length ; i++)
		{
			shiftedPattern[(i+1)%(pattern.length)] = pattern[i];			
		}
		
		return shiftedPattern;
	}
	
	public static double[] halfPattern(int[] pattern)
	{
		double halfPattern[] = new double[pattern.length];
		
		for(int i = 0 ; i < pattern.length ; i++)
		{
			halfPattern[i] = (double)pattern[i]*.5;			
		}
		
		return halfPattern;
	}
	
	public static void test1()
	{
		//Generate neural network
		ArrayList<ArrayList<Neuron>> network = new ArrayList<ArrayList<Neuron>>();
		
		ArrayList<Neuron> layer1 = new ArrayList<Neuron>();
		Neuron n11 = new InputLayerNeuron(.4);
		layer1.add(n11);
		Neuron n12 = new InputLayerNeuron(.1);
		layer1.add(n12);
		network.add(layer1);
		
		ArrayList<Neuron> layer2 = new ArrayList<Neuron>();
		Neuron n21 = new Neuron();
		layer2.add(n21);	
		network.add(layer2);
		
		ArrayList<NeuronConnection> connections12 = new ArrayList<NeuronConnection>();
		NeuronConnection cN11N21 = new NeuronConnection(n11, n21, .4);
		connections12.add(cN11N21);
		NeuronConnection cN12N21 = new NeuronConnection(n12, n21, .3);
		connections12.add(cN12N21);
		
		n21.setInputs(connections12);
		
		ArrayList<Neuron> layer3 = new ArrayList<Neuron>();
		Neuron n31 = new FinalLayerNeuron(.5);
		layer3.add(n31);
		Neuron n32 = new FinalLayerNeuron(.56);
		layer3.add(n32);
		network.add(layer3);
		
		ArrayList<NeuronConnection> connections21 = new ArrayList<NeuronConnection>();
		NeuronConnection cN21N31 = new NeuronConnection(n21, n31, .5);
		connections21.add(cN21N31);
		NeuronConnection cN21N32 = new NeuronConnection(n21, n32, .7);
		connections21.add(cN21N32);
		
		ArrayList<NeuronConnection> connections31 = new ArrayList<NeuronConnection>();
		connections31.add(cN21N31);
		
		ArrayList<NeuronConnection> connections32 = new ArrayList<NeuronConnection>();
		connections32.add(cN21N32);
		
		n21.setOutputs(connections21);
		n31.setInputs(connections31);
		n32.setInputs(connections32);
		
		NeuralNetwork neuralNetwork = new NeuralNetwork(network);
		
		double ran = 0.0;
		
		for(int i = 0;i<100;i++)
		{
			ran = randomValue();
			
			((InputLayerNeuron)n11).setValue(ran);
			((FinalLayerNeuron)n31).setDesiredOutput(ran + .3);
			
			
			neuralNetwork.forwardPropagation();
			neuralNetwork.backwardPropagation();
			
			System.out.println(ran);
			neuralNetwork.printAllOutputs();
		}
		
		
		((InputLayerNeuron)n12).setValue(0.0);
		neuralNetwork.forwardPropagation();
		neuralNetwork.printAllOutputs();
		
		neuralNetwork.printNeuralNetwork();
	}
	
	public static void main(String[] args) 
	{
		int[] nNeuronsInLayers = {4,7,4};
		
		NeuralNetwork neuralNetwork = null;
		try {
			neuralNetwork = NeuralNetworkFactory.makeAllConnections(nNeuronsInLayers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		int nTraining = 10000;
		
		
		
		
		for(int i = 0;i < nTraining ;i++)
		{
			int[] randomPattern = randomPattern(neuralNetwork.getInputLayer().size());
			int[] invertedPattern = invertPattern(randomPattern);
			int[] shiftedPattern = shiftPattern(randomPattern);
			double[] halfPattern = halfPattern(randomPattern);
			int[] resultPattern;
			
			if(randomPattern[0] == 1)
				resultPattern = shiftedPattern;
			else
				resultPattern = invertedPattern;
			
//			resultPattern = halfPattern;
			
			
			int j = 0;
			for(Neuron neuron : neuralNetwork.getInputLayer())
			{
				((InputLayerNeuron)neuron).setValue(randomPattern[j]);
				j++;
			}
			
			j = 0;
			for(Neuron neuron : neuralNetwork.getFinalLayer())
			{
				((FinalLayerNeuron)neuron).setDesiredOutput(resultPattern[j]);
				j++;
			}
			
			neuralNetwork.forwardPropagation();
			neuralNetwork.backwardPropagation();
			
			neuralNetwork.printAllInputs();
			neuralNetwork.printAllOutputs();
		}
		
		neuralNetwork.printNeuralNetwork();
		
		
		for(ArrayList<Neuron> layer : neuralNetwork)
		{
			if(layer != neuralNetwork.getInputLayer())
			{
				System.out.println(".----------new layer------------.");
				for(Neuron neuron : layer)
				{
					System.out.println(".----------new neuron------------.");
					for(NeuronConnection neuronConnection : neuron.getInputs())
					{
						System.out.println(neuronConnection.getWeight());
					}
				}
			}
		}
		
	}
	


}
