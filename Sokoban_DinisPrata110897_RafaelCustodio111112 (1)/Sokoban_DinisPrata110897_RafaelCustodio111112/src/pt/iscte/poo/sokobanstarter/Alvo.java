package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Alvo extends GameElement{
	
		
	public Alvo(Point2D Point2D){
		super(Point2D);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Alvo";
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
