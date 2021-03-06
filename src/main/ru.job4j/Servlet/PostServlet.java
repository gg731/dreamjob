package Servlet;

import Model.Post;
import Model.MemStore;
import Model.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class PostServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("posts", PsqlStore.instOf().findAllPost());
        req.getRequestDispatcher("posts.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        PsqlStore.instOf().savePost(
                new Post(Integer.valueOf(req.getParameter("id")),
                        req.getParameter("name"),
                        "О вакансии",
                        new Date()));

        resp.sendRedirect(req.getContextPath() + "/posts.do");
    }
}
