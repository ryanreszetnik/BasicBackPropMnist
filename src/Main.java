import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) throws IOException {
		MnistMatrix[] mnistMatrix = new MnistDataReader().readData(
				"/Users/rreszetnik/Documents/workspaceAI/Mnist/src/data/train-images.idx3-ubyte",
				"/Users/rreszetnik/Documents/workspaceAI/Mnist/src/data/train-labels.idx1-ubyte");
		MnistMatrix[] mnistMatrix2= new MnistDataReader().readData(
				"/Users/rreszetnik/Documents/workspaceAI/Mnist/src/data/t10k-images.idx3-ubyte",
				"/Users/rreszetnik/Documents/workspaceAI/Mnist/src/data/t10k-labels.idx1-ubyte");
		
		BufferedImage image = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_GRAY);
//		printMnistMatrix(mnistMatrix[mnistMatrix.length - 1], image);
		int[] size = {784, 16, 16, 10};
		double[] eoutput = new double[10];
		Network net = new Network(size);
		double[] input = new double[784];
		int runthroughs = 0;
		double error = 1;
		while (error>0.02) {//up to 98%
			runthroughs++;
			int mainCount = 0;
			for (int round = 0; round < 60000; round++) {
				for (int i = 0; i < 28; i++) {
					for (int p = 0; p < 28; p++) {
						int place = i * 28 + p;
						input[place] = mnistMatrix[round].getValue(i, p)/255.0;
					}
				}
				for (int i = 0; i < 10; i++) {
					eoutput[i] = 0;
				}
				eoutput[mnistMatrix[round].getLabel()] = 1;
				double[] output = net.run(input, eoutput);
				double max = output[0];
				int guess = 0;
				for (int count = 0; count < 10; count++) {
					if (output[count] > max) {
						max = output[count];
						guess = count;
					}
				}
				if(guess==mnistMatrix[round].getLabel()){
					mainCount++;
				}
				// System.out.println((mnistMatrix[round].getLabel() == guess) +
				// " Round%: "
				// + Math.round(round / 600.0) / 100.0 + " Expected: " +
				// mnistMatrix[round].getLabel() + " Guess: "
				// + guess + " " + net.cost(output,
				// eoutput)+"-->"+net.cost(net.forwards(input), eoutput) + " "+
				// Arrays.toString(output));

			}
			System.out.println("Main Round: "+mainCount);
			int correct = 0;
			for (int round = 0; round < 10000; round++) {
				for (int i = 0; i < 28; i++) {
					for (int p = 0; p < 28; p++) {
						int place = i * 28 + p;
						input[place] = mnistMatrix2[round].getValue(i, p)/255.0;
					}
				}

				double[] output = net.forwards(input);
				double max = output[0];
				int guess = 0;
				for (int count = 0; count < 10; count++) {
					if (output[count] > max) {
						max = output[count];
						guess = count;
					}
				}
				if (mnistMatrix2[round].getLabel() == guess) {
					correct++;
				}
				// System.out.println(Math.round(round / 100.0));

			}
			error=1-correct/10000.0;
			System.out.println("Pass: " + runthroughs +" " + correct + " = " + correct / 100.0 + " error: " +error);
			
		}
		File outputfile = new File("image.jpg");
		ImageIO.write(image, "jpg", outputfile);

		// printMnistMatrix(mnistMatrix[0]);
	}

	private static void printMnistMatrix(final MnistMatrix matrix, BufferedImage im) {
		System.out.println("label: " + matrix.getLabel());

		for (int c = 0; c < matrix.getNumberOfColumns(); c++) {
			System.out.print("{");
			for (int r = 0; r < matrix.getNumberOfRows(); r++) {
				int value = matrix.getValue(r, c);
				Color col = new Color(value, value, value);
				im.setRGB(c, r, col.getRGB());
				System.out.print(value / 255.0 + (r == matrix.getNumberOfRows() - 1 ? "" : ", "));
			}
			System.out.println("},");
		}
	}
}
