import processing.core.*;
import peasy.*;

public class libraries extends PApplet {
	
	public static void main(String[] args) {
		PApplet.main("libraries");
	}

	PeasyCam cam;
	
	public void setup() {
		  cam = new PeasyCam(this, 100);
		  cam.setMinimumDistance(50);
		  cam.setMaximumDistance(500);
		}
	
	public void settings() {
		size(1000,1000,P3D);
	}
	
	public void draw() {
		  background(0);
		  fill(255,0,0);
		  box(30);
		  pushMatrix();
		  translate(0,0,20);
		  fill(0,0,255);
		  box(5);
		  popMatrix();
	}
}