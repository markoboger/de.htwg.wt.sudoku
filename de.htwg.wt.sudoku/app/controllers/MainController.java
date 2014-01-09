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

    @play.mvc.Security.Authenticated(Secured.class)
    public static Result index() {
        String email = session("email");
        System.out.println(email);
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
        Form<Person> loginform = DynamicForm.form(Person.class).bindFromRequest();
        
        User user = User.authenticate(loginform.get());

        if (loginForm.hasErrors() || account == null) {

            return badRequest(views.html.login.render(loginForm));
        } else {
            session().clear();
            session("email", user.email);
            return redirect(
                routes.MainController.index()
            );
        } 
    }
    
    public static class User {
        
        private final static String defaultUser = "test@123.de";
        private final static String defaultPasswort = "nsa";

        public String email;
        public String password;
        
        private User(final String email, final String password) {
            this.email = email;
            this.password = password;
        }

     	public static User authenticate(String email, String password){
     	    if (email.equals(defaultUser) && password.equals(defaultPasswort)) {
     	        return new User(email, password);
     	    }
     	    
    	    return null;
    	}
   }
   
    public static class Secured extends Security.Authenticator {

        @Override
        public String getUsername(Context ctx) {
            return ctx.session().get("email");
        }

        @Override
        public Result onUnauthorized(Context ctx) {
            return redirect(routes.Application.login());
        }
    }

}
