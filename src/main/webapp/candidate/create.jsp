<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import="Model.Candidate" %>
<%@ page import="Model.PsqlStore" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>

    <title>Работа мечты</title>
</head>
<body>
<%
    String id = request.getParameter("id");
    Candidate candidate = new Candidate(0, "", "");
    if (id != null) {
        candidate = PsqlStore.instOf().findByIdCandidate(Integer.valueOf(id));
    }
%>

<div class="container">
    <div class="row">
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/posts.do">Вакансии</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/candidates.do">Кандидаты</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%= request.getContextPath()%>/post/edit.jsp">Добавить вакансию</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%= request.getContextPath()%>/candidate/create.jsp">Добавить кандидата</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%= request.getContextPath()%>/upload.do">Изображения</a>
            </li>
            <c:choose>
                <c:when test="${sessionScope.user.name != null}">
                    <li class="nav-item">
                        <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp"><c:out
                                value="${sessionScope.user.name}"/> | Выйти</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="nav-item">
                        <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp">Войти
                        </a>
                    </li>
                </c:otherwise>

            </c:choose>
        </ul>
    </div>
    <div class="container pt-3">
        <div class="row">
            <div class="card" style="width: 100%">
                <div class="card-header">
                    <% if (id == null) { %>
                    Новый кандидат.
                    <% } else { %>
                    Редактировать кандидата.
                    <% } %>
                </div>
                <div class="card-body">
                    <form action="<%=request.getContextPath()%>/candidates.do?id=<%=candidate.getId()%>" method="post">
                        <div class="form-group">
                            <label>Имя</label>
                            <input type="text" class="form-control" name="name" value="<%=candidate.getName()%>"
                                   style="width: 25%">
                        </div>
                        <button type="submit" class="btn btn-primary">Сохранить</button>
                    </form>

                    <% if (id != null) { %>
                    <div style="margin-top: 5%">
                        <img src="<c:url value='/download.do?photoId=${param.photoId}' />" width="50px"
                             height="50px"/>
                        <form action="<c:url value="/upload.do?photoId=${param.photoId}"/>" method="post"
                              enctype="multipart/form-data">
                            <label>Загрузить фотографию</label>
                            <div class="checkbox">
                                <input type="file" name="file">
                            </div>
                            <button type="submit" class="btn btn-primary">Загрузить</button>
                        </form>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
