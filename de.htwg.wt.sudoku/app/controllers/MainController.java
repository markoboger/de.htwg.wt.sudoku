package controllers;

import de.htwg.sudoku.Sudoku;
import de.htwg.sudoku.controller.ISudokuController;
import de.htwg.sudoku.model.ICell;
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
        Map<String, Object> obj[][] = new HashMap[x][x];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < x; j++) {
                obj[i][j] = new HashMap<>();
                obj[i][j].put("cell", grid.getICell(i,j));
                boolean[] candidates = new boolean[x];
                for (int ii = 0; ii < x; ii++) {
                    candidates[ii] = controller.isCandidate(i, j, ii + 1);
                }
                obj[i][j].put("candidates", candidates);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("meta", controller.getGrid());
        map.put("grid", obj);

        return ok(Json.stringify(Json.toJson(map)));
    }

}
