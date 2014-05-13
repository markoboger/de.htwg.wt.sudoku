package models;

import play.mvc.WebSocket;
import play.mvc.WebSocket.Out;
import de.htwg.sudoku.controller.ISudokuController;
import de.htwg.util.observer.Event;
import de.htwg.util.observer.IObserver;

public class GridObserver implements IObserver {
	
	private Out<String> out;
	private ISudokuController controller;


	public GridObserver(ISudokuController controller,WebSocket.Out<String> out) {
		controller.addObserver(this);
		this.controller = controller;
		this.out = out;
	}

	@Override
	public void update(Event arg0) {
//		out.write(controller.getGrid().toJson());	
		System.out.println("WUI was updated");
	}

}
