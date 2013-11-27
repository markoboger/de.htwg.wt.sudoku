package controllers;

import models.GridObserver;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import de.htwg.sudoku.Sudoku;
import de.htwg.sudoku.controller.ISudokuController;

public class MainController extends Controller {
	static ISudokuController controller = Sudoku.getInstance().getController();

    public static Result index() {
        return ok(views.html.index.render("HTWG Sudoku", controller));
    }

    public static Result commandline(String command) {
    	Sudoku.getInstance().getTUI().processInputLine(command);
        return ok(views.html.index.render("Got your command "+ command, controller));
    }

    public static Result jsonCommand(String command) {
        Sudoku.getInstance().getTUI().processInputLine(command);
        return ok(controller.getGrid().toJson());
    }
    
    public static WebSocket<String> connectWebSocket() {
        return new WebSocket<String>() {
       
            public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
            	new GridObserver(controller,out);
            }

        };
    }
    


}
