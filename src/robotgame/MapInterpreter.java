package robotgame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import blocks.Block;
import blocks.Coin;
import blocks.Exit;
import blocks.Gate;
import blocks.Key;
import blocks.Pit;
import blocks.Wall;
import blocks.Sliding;
import blocks.SlidingX;
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
			int step = 0;
			int corr = 0;
			for (int x = 0; x < blocks.length; x+=(step+1)) {
				corr += step;
				char block = blocks[x];
				Block b = null;
				step = 0;
				switch (block) {

				case '#':
					b = new Wall(SQUARE_SIZE);
					break;
				
				case 'X':
					b = new SlidingX(SQUARE_SIZE);
					step++;
					char dir = blocks[x+step];
					((SlidingX)b).setDir(dir);
					step++;
					char len = blocks[x+step];
					((SlidingX)b).setLen(len);
					step++;
					char con = blocks[x+step];
					
					break;
					
				case 'Y':
					b = new SlidingY(SQUARE_SIZE);
					step++;
					dir = blocks[x+step];
					step++;
					len = blocks[x+step];
					step++;
					con = blocks[x+step];
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
					b = new Pit(SQUARE_SIZE);
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
					b.setTranslateX((x-corr) * SQUARE_SIZE - 1);				//corrects the jumps perfomed in sliding
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