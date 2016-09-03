import java.awt.Color;

public class Genetic {
	
	public Color Base;
	private Color Target;
	public Color Child[] = new Color[10];
	private short Fitness[] = new short[10];
	private double Mutationrate;
	public short Generation = 0;
	
	public Genetic(Color target, Color in, double MutationRate) {
		Base = in;
		Mutationrate = MutationRate/100;
		Target = target;
		GenerateOffspring();
	}

	public void GenerateOffspring() {
		for(int i=0; i<Child.length; i++) {
			int Red = Base.getRed(), Green = Base.getGreen(), Blue = Base.getBlue();
			if (Math.random() < 0.5) {
				Red -= Math.random()*255 * this.Mutationrate;
			} else {
				Red += Math.random()*255 * this.Mutationrate;
			}
			if (Math.random() < 0.5) {
				Blue -= Math.random()*255 * this.Mutationrate;
			} else {
				Blue += Math.random()*255 * this.Mutationrate;
			}
			if (Math.random() < 0.5) {
				Green -= Math.random()*255 * this.Mutationrate;
			} else {
				Green += Math.random()*255 * this.Mutationrate;
			}
			// 50-50 chance of increasing or decreasing the Red, Green, or Blue values. math limits the change to the mutation percentage
			// Notice no mention of target value (we're not cheating :))
			if (Red >= 255) { Red = 255; } else if (Red < 0) { Red = 0; }
			if (Blue >= 255) { Blue = 255; } else if (Blue < 0) { Blue = 0; }
			if (Green >= 255) { Green = 255; } else if (Green < 0) { Green = 0; }
			// bit of validation
			Child[i] = new Color(Red, Green, Blue);
			// OK apply
		}
	}
	
	public void JudgeFitnesses() {
		for(int i=0; i<Child.length; i++) {
			// Want to find the difference no matter if one target is less then given value.		
			int Red = Math.abs(Target.getRed() - Child[i].getRed());
			int Green = Math.abs(Target.getGreen() - Child[i].getGreen());
			int Blue = Math.abs(Target.getBlue() - Child[i].getBlue());
			// Simple math FTW
			Fitness[i] = (short)(Red + Green + Blue); // add them up to see who has the least difference to the target.
		}
	}

	public void newGeneration() {
		JudgeFitnesses();
		Generation++;
		// Bubble sort Algorithm to sort their fitnesses
		boolean swapped = false;
		Color colortemp;
		short temp;
		while(swapped == false) {
			swapped = true;
			for(int i=1; i<Child.length; i++) {
				if (Fitness[i-1] > Fitness[i]) {
					colortemp = Child[i-1];
					Child[i-1] = Child[i];
					Child[i] = colortemp;
					
					temp = Fitness[i];
					Fitness[i-1] = Fitness[i];
					Fitness[i] = temp;
					swapped = false;
				}
			}
		}
		// New parent being the average of the two best children
		Base = new Color(
					(Child[0].getRed()+Child[1].getRed())/2,
					(Child[0].getGreen()+Child[1].getGreen())/2,
					(Child[0].getBlue()+Child[1].getBlue())/2
				);
	}
}
