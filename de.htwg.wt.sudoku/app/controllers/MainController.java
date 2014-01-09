package controllers;

import models.GridObserver;
import play.data.*;
import static play.data.Form.*;
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
    
    public static Result login() {
        return ok( views.html.login.render(Form.form(Login.class)));
    }
    
    public static Result authenticate() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(views.html.login.render(loginForm));
        } else {
            session().clear();
            session("email", loginForm.get().email);
            return redirect(
                routes.MainController.index()
            );
        } 
    }
    
    public String validate(String email, String password) {
        if (new User().authenticate(email, password) == null) {
          return "Invalid user or password";
        }
        return null;
    }
    
    public static class Login {

        public String email;
        public String password;

    }
    
    public class User {
    	public String authenticate( String email, String password){return null;}
    }

}
