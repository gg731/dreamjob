<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">


    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <title>Job dreams</title>
</head>
<body>
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
                    Кандидаты
                </div>
                <div class="card-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Названия</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${candidates}" var="candidate">
                            <tr>
                                <td width="10%">
                                    <img src="<c:url value='/download.do?photoId=${candidate.photoId}' />" width="50px"
                                         height="50px"/>
                                    <a href="<c:url value='/download.do?photoId=${candidate.photoId}'/>"
                                       style="font-size: 1.1rem">Download</a>
                                </td>
                                <td style="vertical-align: middle" width="20%">
                                    <c:out value="${candidate.name}"/>
                                    <a href="<c:url value='candidate/create.jsp?id=${candidate.id}&photoId=${candidate.photoId}'/>">
                                        <i class=" fa fa-edit mr-3"></i>
                                    </a>
                                </td>
                                <td style="vertical-align: middle">
                                    <a href="<c:url value='/candidates.do?delete=${candidate.id}&photoId=${candidate.photoId}'/>"
                                       style="color: darkred">Удалить</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
