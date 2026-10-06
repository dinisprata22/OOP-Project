package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Buraco extends GameElement {

	Buraco(Point2D point2d) {
		super(point2d);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Buraco";
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 1;
	}
	
	public void swallow( GameElement e) {
		if (e instanceof Palete) {
			remove(e);
			remove(this);
		} else
			remove(e);
	}

}
