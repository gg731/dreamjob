package Servlet;

import Model.PsqlStore;
import Model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (PsqlStore.instOf().findUserByEmail(req.getParameter("email")) != null) {
            req.setAttribute("authError", "Email занят");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        } else {
            User user = new User(0,
                    req.getParameter("name"),
                    req.getParameter("email"),
                    req.getParameter("password")
            );

            PsqlStore.instOf().saveUser(user);
            req.setAttribute("authError", "Регистрация прошла успешно, авторизируйтесь");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}

