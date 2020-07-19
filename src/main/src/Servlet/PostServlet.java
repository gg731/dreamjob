package Servlet;

import Model.Post;
import Model.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class PostServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Store.instOf().savePost(
                new Post(Integer.valueOf(req.getParameter("id")),
                        req.getParameter("name"),
                        "О вакансии",
                        new Date()));

        resp.sendRedirect(req.getContextPath() + "/posts.jsp");
    }
}
