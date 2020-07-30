package Servlet;

import Model.MemStore;
import Model.PsqlStore;
import Model.Store;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class CandidateServletTest {

    @Test
    public void addCandidate() throws ServletException, IOException {
        Store memStore = MemStore.instOf();
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.instOf()).thenReturn(memStore);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        when(req.getParameter("name")).thenReturn("user1");
        when(req.getParameter("id")).thenReturn("0");

        new CandidateServlet().doPost(req, resp);

        assertThat(memStore.findAllCandidate().stream().filter(s ->
                s.getName().equals("user1")).findFirst().isEmpty(), is(false));
    }

    @Test
    public void deleteCandidate() throws ServletException, IOException {
        Store memStore = MemStore.instOf();
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.instOf()).thenReturn(memStore);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(req.getRequestDispatcher(eq("candidates.jsp"))).thenReturn(rd);

        when(req.getParameter("delete")).thenReturn("1");
        when(req.getAttribute("candidates")).thenReturn(memStore.findAllCandidate());

        new CandidateServlet().doGet(req, resp);

        assertThat(memStore.findByIdCandidate(1), is(nullValue()));
    }
}
