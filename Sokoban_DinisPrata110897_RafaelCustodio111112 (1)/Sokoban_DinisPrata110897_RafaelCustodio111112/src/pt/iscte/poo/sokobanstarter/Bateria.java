package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Bateria extends GameElement implements Absorvable{

	Bateria(Point2D point2d) {
		super(point2d);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Bateria";
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void purpose( Empilhadora bobcat, Direction direcao) {
		// TODO Auto-generated method stub
//		bobcat.move(direcao,50,false,1);
		bobcat.gainFuel(50);
		remove(this);
	}
	

}
