package controllers;

import de.htwg.sudoku.Sudoku;
import de.htwg.sudoku.controller.ISudokuController;
import de.htwg.sudoku.model.IGrid;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;

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
        return json();
    }

    public static Result json() {
        IGrid grid = controller.getGrid();
        int x = grid.getCellsPerEdge();
        Map<String, Object> mapMatrix[][] = new HashMap[x][x];
        for (int row = 0; row < x; row++) {
            for (int col= 0; col < x; col++) {
                mapMatrix[row][col] = new HashMap<String, Object>();
                mapMatrix[row][col].put("cell", grid.getICell(row,col));
                boolean[] candidates = new boolean[x];
                for (int candidate = 0; candidate < x; candidate++) {
                    candidates[candidate] = controller.isCandidate(row, col, candidate + 1);
                }
                mapMatrix[row][col].put("candidates", candidates);
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("meta", controller.getGrid());
        map.put("grid", mapMatrix);

        return ok(Json.stringify(Json.toJson(map)));
    }

}
