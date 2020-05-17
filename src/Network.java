
public class Network {

	Layer[] layers;
	public Network(int[]size){
		layers= new Layer[size.length];
		layers[0] = new Layer(Layer.TYPE.input,size[0]);
		for(int i = 1; i < size.length-1; i++){
			layers[i] = new Layer(Layer.TYPE.middle,size[i]);
		}
		layers[layers.length-1] = new Layer(Layer.TYPE.output,size[layers.length-1]);
		for(int i = 1; i < size.length; i++){
			layers[i].addConnections(layers[i-1]);
		}
	}
	public double[] run(double[] in, double eOut[]){
		layers[0].setOutputs(in);
		layers[layers.length-1].setExpectedOut(eOut);
		for(Layer l: layers){
			l.run();
		}
		double[] out = layers[layers.length-1].getOutputs();
		
		for(int i = layers.length-1; i >= 0; i--){
			layers[i].backProp();
		}
//		double cost1 = cost(out,eOut);
//		for(Layer l: layers){
//			l.run();
//		}
//		double[] out2 = layers[layers.length-1].getOutputs();
//		double cost2 = cost(out2,eOut);
//		if(cost1-cost2<0){
//			System.out.println("Not Learning");
//		}
		return out;
	}
	public double[] forwards(double[]in){
		layers[0].setOutputs(in);
		for(Layer l: layers){
			l.run();
		}
		double[] out = layers[layers.length-1].getOutputs();
		return out;
	}
	
	public double cost(double out[], double eOut[]){
		double total = 0;
		for(int i = 0; i < out.length; i++){
			total+=Math.pow(out[i]-eOut[i], 2);
		}
		return total;
	}
}
