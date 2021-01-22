<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="ru.job4j.dream.store.PsqlStore" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page isELIgnored="false" %>
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
<%!
    Candidate candidate;
%>
<%
    String id = request.getParameter("id");
    candidate = new Candidate(0, "");
    if (id != null && !id.equals("")) {
        candidate = PsqlStore.getInst().findCandidateById(Integer.parseInt(id));
    }
    request.setAttribute("candidate", candidate);
%>
<div class="container pt-3">
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) { %>
                Новый кандидат
                <% } else { %>
                Редактирование кандидата
                <% } %>
            </div>
            <div class="card-body">
                <c:if test='${candidate.photoId > 0 and param["image"] == null}'>
                    <img src="${pageContext.request.contextPath}/download.do?image_id=${candidate.photoId}"
                         class="rounded float-start" alt="Фото кандидата"
                         width="150" height="150"
                    >
                </c:if>
                <c:if test='${param["image"] > 0 and param["image"] != null}'>
                    <img src="${pageContext.request.contextPath}/download.do?image_id=${param['image']}"
                         class="rounded float-start" alt="Фото кандидата"
                         width="150" height="150"
                    >
                </c:if>
                <form action="<%=request.getContextPath()%>/candidates.do?id=<%=candidate.getId()%>" method="post">
                    <div class="form-group">
                        <label>Имя</label>
                        <input type="text" class="form-control" name="name" value="${candidate.name}">
                        <c:if test='${param["image"] > 0}'>
                            <input type="hidden" name="new_image" value="${param['image']}">
                        </c:if>
                        <input type="hidden" name="old_image" value="${candidate.photoId}">
                    </div>
                    <button type="submit" class="btn btn-primary">Сохранить</button>
                </form>
            </div>
            <div class="card-body">
                <form action="<c:url value='/upload?id=${param.id}'/>" method="post" enctype="multipart/form-data">
                    <c:if test='${param["image"] == -1}'>
                        <c:out value="Нужно выбрать файл!"/>
                    </c:if>
                    <div class="checkbox">
                        <input type="file" name="file">
                    </div>
                    <button type="submit" class="btn btn-primary">Загрузить аватар</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
