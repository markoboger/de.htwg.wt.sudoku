package controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
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
    
}
