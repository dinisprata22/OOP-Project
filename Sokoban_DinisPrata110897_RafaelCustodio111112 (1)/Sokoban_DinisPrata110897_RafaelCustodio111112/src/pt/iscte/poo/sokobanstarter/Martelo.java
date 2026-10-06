package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Martelo extends GameElement implements Absorvable {

	public Martelo(Point2D point2d) {
		super(point2d);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Martelo";
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void purpose( Empilhadora bobcat, Direction direcao) {
		// TODO Auto-generated method stub
		//bobcat.move(direcao, -1 ,true,1);
		bobcat.setHasHammer(true);
		remove(this);
		
	}
	
	
	
	

}
