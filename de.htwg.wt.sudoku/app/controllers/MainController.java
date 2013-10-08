package controllers;

import models.CommandLine;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import de.htwg.sudoku.Sudoku;
import de.htwg.sudoku.controller.ISudokuController;

public class MainController extends Controller {
	static ISudokuController controller = Sudoku.getInstance().getController();
	static Form<CommandLine> command = Form.form(CommandLine.class);
    
    public static Result index() {
        return ok(views.html.index.render("HTWG Sudoku", controller));
    }
    
    public static Result commandline(){
    	CommandLine commandline = command.bindFromRequest().get();
    	return ok(views.html.index.render("Got your command "+ commandline.command, controller));
    }
    
}
