<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import="Model.Candidate" %>
<%@ page import="Model.PsqlStore" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script>
        function check() {
            var name = $('#name').val();

            if (name == '') {
                alert('Заполните все поля.');
                return false;
            }
            return true;
        }
    </script>

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
                                   style="width: 25%" id="name">
                        </div>
                        <div>
                            <label> Выберите город: </label>
                            <select name="city">
                                <option selected disabled>Выберите город:</option>
                                <c:forEach items="${cities}" var="city">
                                    <option value="<c:out value="${city.key}"/>">
                                        <c:out value="${city.value}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary" onclick="return check()">Сохранить</button>
                    </form>

                    <% if (id != null) { %>
                    <div style="margin-top: 5%">
                        <img src="<c:url value='/download.do?photoId=${param.photoId}' />" width="50px"
                             height="50px"/>
                        <form action="<c:url value="/upload.do?photoId=${param.photoId}"/>" method="post"
                              enctype="multipart/form-data">
                            <label>Загрузить фотографию</label>
                            <div class="checkbox">
                                <input type="file" name="file" required>
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
