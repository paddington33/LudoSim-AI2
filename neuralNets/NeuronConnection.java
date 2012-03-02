package neuralNets;

public class NeuronConnection {
	private Neuron inputNeuron,outputNeuron;
	private double weight;
	
	private final double defaultWeight = 1.0;
	
	public NeuronConnection(Neuron input, Neuron output)
	{
		this.inputNeuron = input;
		this.outputNeuron = output;
		this.weight = defaultWeight;
	}
	
	public NeuronConnection(Neuron input, Neuron output, double weight)
	{
		this.inputNeuron = input;
		this.outputNeuron = output;
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Neuron getInputNeuron() {
		return inputNeuron;
	}

	public Neuron getOutputNeuron() {
		return outputNeuron;
	}

	public void addWeight(double deltaWeight) {
		this.weight += deltaWeight;
	}
	
}
