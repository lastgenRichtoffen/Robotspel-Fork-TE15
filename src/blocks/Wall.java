package blocks;
import javafx.scene.paint.Color;

public class Wall extends Block{
	
	public Wall(double square_size){
		super(square_size);
		getBackground().setFill(Color.KHAKI);
	}
	
}
