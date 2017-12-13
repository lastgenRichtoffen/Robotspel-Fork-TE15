package robotgame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import blocks.Block;
import blocks.ExampleBlock;
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
					b = new ExampleBlock(SQUARE_SIZE);
					break;

				case 'P':
					b = new ExampleBlock(SQUARE_SIZE);
					break;

				case 'X':
					b = new ExampleBlock(SQUARE_SIZE);
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
