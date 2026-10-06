package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class ParedeRachada extends GameElement implements Unmovable{

	public ParedeRachada(Point2D point2d) {
		super(point2d);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ParedeRachada";
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
