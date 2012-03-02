package neuralNets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class NeuralNetwork implements Iterable<ArrayList<Neuron>>{
	protected ArrayList<ArrayList<Neuron>> network;
	
	public NeuralNetwork(ArrayList<ArrayList<Neuron>> network)
	{
		this.network = network;
	}
	
	public ArrayList<Neuron> getLayer(int layerNumber)
	{
		return this.network.get(layerNumber);
	}
	
	public ArrayList<Neuron> getInputLayer()
	{
		return getLayer(0);
	}
	
	public ArrayList<Neuron> getFinalLayer()
	{
		return getLayer(network.size() - 1);
	}
	
	public void forwardPropagation()
	{
		for(ArrayList<Neuron> layer : network)
			for(Neuron neuron : layer)
				neuron.updateOutput();
	}
	
	public void backwardPropagation()
	{
		for(ListIterator<ArrayList<Neuron>> layerIterator = network.listIterator(network.size()) ; layerIterator.hasPrevious() ; )
			for(Neuron neuron : layerIterator.previous())
				neuron.updateAll();	
	}
	
	public void printAllOutputs()
	{
		ArrayList<Neuron> layer = getFinalLayer();
		
		for(Neuron neuron : layer)
		{	
			System.out.println(neuron.getOutput());
		}
		System.out.println("-");
	}
	
	public void printAllDesiredOutputs()
	{
		ArrayList<Neuron> layer = getFinalLayer();
		
		for(Neuron neuron : layer)
		{	
			System.out.println(((FinalLayerNeuron)neuron).getDesiredOutput());
		}
		System.out.println("-");
	}
	
	public void printNeuralNetwork()
	{
		int layerNumber = 0;
		for(ArrayList<Neuron> layer : network)
		{
			System.out.println("----------------------------");
			System.out.println("Layer number: " + layerNumber);
			int neuronNumber = 0;
			for(Neuron neuron : layer)
			{
				System.out.println("Neuron number: " + neuronNumber);
				System.out.println(neuron);
				neuronNumber++;
			}
			layerNumber++;
		}
	}

	@Override
	public Iterator<ArrayList<Neuron>> iterator() 
	{
		return this.network.iterator();
	}

	public void printAllInputs() {
		ArrayList<Neuron> layer = getInputLayer();
		for(Neuron neuron : layer)
		{	
			System.out.println(neuron.getOutput());
		}
		System.out.println("-");
	}
}

