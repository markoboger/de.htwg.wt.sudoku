package controllers;

import models.GridObserver;
import play.data.*;
import static play.data.Form.*;

import play.libs.F;
import play.libs.Json;
import play.libs.OpenID;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.Http.Context;
import play.mvc.WebSocket;
import de.htwg.sudoku.Sudoku;
import de.htwg.sudoku.controller.ISudokuController;

import java.util.HashMap;
import java.util.Map;

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
        return ok( views.html.login.render(Form.form(User.class)));
    }

    public static Result logout() {
    	session().clear();
    	return redirect(routes.MainController.index());
    }

    public static Result authenticate() {
        Form<User> loginform = DynamicForm.form(User.class).bindFromRequest();

        User user = User.authenticate(loginform.get());

        if (loginform.hasErrors() || user == null) {

            return badRequest(views.html.login.render(loginform));
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

        public User() {}

        private User(final String email, final String password) {
            this.email = email;
            this.password = password;
        }

     	public static User authenticate(User user){
     	    if (user.email.equals(defaultUser) && user.password.equals(defaultPasswort)) {
     	        return new User(user.email, user.password);
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
            return redirect(routes.MainController.login());
        }
    }

    public static Result auth() {
        String providerUrl = "https://www.google.com/accounts/o8/id";
        String returnToUrl = "http://localhost:9000/openID/verify";

        Map<String, String> attributes = new HashMap<>();
        attributes.put("Email", "http://schema.openid.net/contact/email");
        attributes.put("FirstName", "http://schema.openid.net/namePerson/first");
        attributes.put("LastName", "http://schema.openid.net/namePerson/last");

        F.Promise<String> redirectUrl = OpenID.redirectURL(providerUrl, returnToUrl, attributes);
        return redirect(redirectUrl.get());
    }

    public Result verify() {
        F.Promise<OpenID.UserInfo> userInfoPromise = OpenID.verifiedId();
        OpenID.UserInfo userInfo = userInfoPromise.get();
        session().clear();
        session("email", userInfo.attributes.get("Email"));
        return redirect(
                routes.MainController.index()
        );
    }
}
