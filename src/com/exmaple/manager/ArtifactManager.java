package com.exmaple.manager;

import java.util.ArrayList;
import java.util.Random;

import com.example.ScreenObjects.Artifact;
import com.example.objects.Triangle;
import com.example.util.Graphics;

public class ArtifactManager {
	ArrayList<Artifact> artifacts;
	double ratio;
	
	Random gen = new Random();
	
	ArtifactManager(double ratio) { this.ratio = ratio; }

	void update() {
		for (Artifact art : artifacts) {
			if (art.isDone()) {
				// TODO
				/*delete (*it);
				it = artifacts.erase(it);*/
			}
			else { 
				art.update();
			}
		}
		//cout << "There are " << artifacts.size() << " artifacts." << endl;
	}

	void draw(Graphics g) {
		for (Artifact art : artifacts) {
			art.draw(g);
		}
	}


	void newArtifact() {
		artifacts.add(new Triangle(0, 0, 300, 0, 0, 300));
	}

	void addArtifact(Artifact a) {
		artifacts.add(a);
	}

	void splitTris(int x, int y, int w, int h) {
			
			int topRandomx = gen.nextInt()%w + x;
			int bottomRandomx = gen.nextInt()%w + x;
			int xi = (topRandomx - bottomRandomx) / 4;
			int yi = h/4;
			int angle = 45;
			int change = 46;
			for (int i = 0; i < 4; i++) {
				angle += gen.nextInt()%change - change/2;
				Triangle tri = new Triangle(x, y+(yi*i), topRandomx+(xi*i), y+(yi*i), x, y+(yi*(i+1)), angle*.6, 10);
				artifacts.add(tri);

				angle += gen.nextInt()%change - change/2;
				Triangle tri2 = new Triangle(x+w, y+(yi*i), topRandomx+(xi*i), y+(yi*i), x+w, y+(yi*(i+1)), angle*.6, 10);
				artifacts.add(tri2);

				angle += gen.nextInt()%change - change/2;
				Triangle tri3 = new Triangle(x, y+(yi*(i+1)), topRandomx+(xi*i), y+(yi*i), x+w, y+(yi*(i+1)), angle*.6, 10);
				artifacts.add(tri3);
				
				if (angle < 0) { angle = 20;}
				if (angle > 90) { angle = 70; }
			}
		}
}
