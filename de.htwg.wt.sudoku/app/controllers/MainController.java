package controllers;

import com.sun.javafx.scene.control.skin.CellSkinBase;
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

    public static Result json() {
        IGrid grid = controller.getGrid();
        int x = grid.getCellsPerEdge();
        ICell[][] cells = new ICell[x][x];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < x; j++) {
                cells[i][j] = grid.getICell(i,j);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("meta", controller.getGrid());
        map.put("grid", cells);

        return ok(Json.stringify(Json.toJson(map)));
    }

}
