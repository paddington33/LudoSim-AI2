package neuralNets;

public class FinalLayerNeuron extends Neuron {
	private double desiredOutput;
	
	public FinalLayerNeuron() 
	{
	}
	
	public FinalLayerNeuron(double desiredOutput) 
	{
		this.desiredOutput = desiredOutput;
	}
	
	public void updateDelta()
	{			
		this.delta = this.output * ( 1.0 - this.output ) * (desiredOutput - this.output); 
	}
	
	public void setDesiredOutput(double desiredOutput)
	{
		this.desiredOutput = desiredOutput;
	}
	
	public double getDesiredOutput()
	{
		return desiredOutput;
	}
	
}
