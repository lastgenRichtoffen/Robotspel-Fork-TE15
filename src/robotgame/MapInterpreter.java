package robotgame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import blocks.Block;
import blocks.Coin;
import blocks.Exit;
import blocks.Gate;
import blocks.Hole;
import blocks.Key;
import blocks.Wall;
import blocks.Sliding;
import blocks.Plate;
import blocks.Pushable;
import javafx.scene.Group;

/**
 * 
 * @author Joakim
 */
public class MapInterpreter extends Group {
	
	public final double SQUARE_SIZE = 45;

	public MapInterpreter() throws FileNotFoundException {

		Scanner fileReader = new Scanner(new File("level1.txt"));

		int y = -1;
		ArrayList<Integer> robotCoords = new ArrayList<Integer>();

		while (fileReader.hasNextLine()) {
			y++;
			String line = fileReader.nextLine();
			char[] blocks = line.toCharArray();

			for (int x = 0; x < blocks.length; x++) {
				char block = blocks[x];
				Block b = null;
				switch (block) {

				case '#':
					b = new Wall(SQUARE_SIZE);
					break;
				
				case 'X':
					b = new Sliding(SQUARE_SIZE);
					break;
					
				case 'Y':
					b = new Sliding(SQUARE_SIZE);
					break;	

				case 'V':
					b = new Plate(SQUARE_SIZE);
					break;
					
				case 'H':
					b = new Plate(SQUARE_SIZE);
					break;
					
				case 'G':
					b = new Gate(SQUARE_SIZE);
					break;

				case 'E':
					b = new Exit(SQUARE_SIZE);
					break;
					
				case 'P':
					b = new Hole(SQUARE_SIZE);
					break;

				case 'C':
					b = new Coin(SQUARE_SIZE);
					break;
					
				case 'K':
					b = new Key(SQUARE_SIZE);
					break;
					
					
				case 'B':
					b = new Pushable(SQUARE_SIZE);
					break;				
					
				case 'R': // ROBOT
					robotCoords.add(x);
					robotCoords.add(y);
					break;

				case ' ':
					b = null;
					break;

				default:
					b = null;
					break;

				}

				if (b != null) {
					b.setTranslateX(x * SQUARE_SIZE - 1);
					b.setTranslateY(y * SQUARE_SIZE - 1);
					this.getChildren().add(b);
				}

			}

		}

		for (int i = 0; i < robotCoords.size(); i += 2) {
			Robot r = new Robot(SQUARE_SIZE);
			r.setTranslateX(robotCoords.get(i) * SQUARE_SIZE);
			r.setTranslateY(robotCoords.get(i + 1) * SQUARE_SIZE);
			this.getChildren().add(r);
		}

	}

}