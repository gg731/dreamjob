package Servlet;

import Model.Candidate;
import Model.MemStore;
import Model.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class CandidateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String delete = req.getParameter("delete");
        if (delete != null) {
            PsqlStore.instOf().deleteCandidate(Integer.valueOf(delete));
            File file = new File("images" + File.separator + req.getParameter("photoId"));
            file.delete();
        }

        req.setAttribute("candidates", PsqlStore.instOf().findAllCandidate());
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        PsqlStore.instOf().saveCandidate(
                new Candidate(
                        Integer.valueOf(req.getParameter("id")),
                        req.getParameter("name"),
                        req.getParameter("city")));

        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
