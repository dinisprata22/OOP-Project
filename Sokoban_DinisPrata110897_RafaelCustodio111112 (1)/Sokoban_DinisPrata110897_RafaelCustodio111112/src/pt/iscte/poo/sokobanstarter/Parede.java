package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Parede extends GameElement implements Unmovable{
	
	
	public Parede(Point2D Point2D){
		super(Point2D);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Parede";
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public boolean standStill() {
		// TODO Auto-generated method stub
		return true;
	}

}
