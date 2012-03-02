package neuralNets;

import java.util.ArrayList;

public class Neuron implements Comparable<Neuron>{
	protected double bias = Math.random()/10.0;
	protected double output;
	protected double delta;
	protected ArrayList<NeuronConnection> inputs = new ArrayList<NeuronConnection>();
	protected ArrayList<NeuronConnection> outputs = new ArrayList<NeuronConnection>();
	
	private double alpha = 0.35;

	public Neuron()
	{
	}
	
	private double sigmoid(double x)
	{
		return 1.0/(1.0 + Math.exp(-x));
	}
	
	public void updateOutput()
	{
		double output = 0.0;
		for(NeuronConnection inputNeuron : inputs)
			output += inputNeuron.getInputNeuron().getOutput() * inputNeuron.getWeight();
		
		output += this.bias;
		
		this.output = sigmoid(output);
	}
	
	public void updateDelta()
	{
		double delta = 0.0;
		for( NeuronConnection outputNeuron : outputs )
			delta += outputNeuron.getWeight() * outputNeuron.getOutputNeuron().getDelta();

		delta *= this.output * ( 1.0 - this.output ); 
		
		this.delta = delta;
	}
	
	public void updateBias()
	{
		this.bias += alpha * delta;
	}
	
	public void updateWeights()
	{
		for(NeuronConnection inputNeuron : inputs)
			inputNeuron.addWeight(alpha * inputNeuron.getInputNeuron().getOutput() * delta);
	}
	
	public void updateAll()
	{
		updateDelta();
		updateBias();
		updateWeights();
	}
	
	public double getOutput()
	{
		return this.output;
	}
	
	public double getDelta()
	{
		return this.delta;
	}
	
	
	public void setInputs(ArrayList<NeuronConnection> inputs) {
		this.inputs = inputs;
	}
	
	public ArrayList<NeuronConnection> getInputs()
	{
		return inputs;
	}

	public void setOutputs(ArrayList<NeuronConnection> outputs) {
		this.outputs = outputs;
	}
	
	public String toString()
	{
		return "Output: " + this.output + "\nBias: " + this.bias + "\nDelta: " + this.delta ;
		
	}

	public void addInputConnection(NeuronConnection neuronConnection) {
		inputs.add(neuronConnection);
	}

	public void addOutputConnection(NeuronConnection neuronConnection) {
		outputs.add(neuronConnection);
	}

	@Override
	public int compareTo(Neuron neuron) 
	{
		if(neuron.getOutput() > this.output)
			return 1;
		else
			return -1;
	}
}
