package Servlet;

import Model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if ("root@local".equals(email) && "root".equals(password)) {
            HttpSession session = req.getSession();
            User admin = new User();
            admin.setId(1);
            admin.setName("admin");
            admin.setPassword(password);
            admin.setEmail(email);
            session.setAttribute("user", admin);
            resp.sendRedirect(req.getContextPath() + "/posts.do");
        } else {
            req.setAttribute("authError", "Не верная почта или пароль");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
