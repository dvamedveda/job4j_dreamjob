<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" tagdir="/WEB-INF/tags/content" %>
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
    <script src="https://code.jquery.com/jquery-3.4.1.min.js" ></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>

    <title>Работа мечты</title>
</head>
<body>

<script type="text/javascript" src='<c:url value="/scripts/editCandidate.js"/>' defer></script>
<%!
    Candidate candidate;
%>
<%
    String id = request.getParameter("id");
    candidate = new Candidate(0, "", 0);
    if (id != null && !id.equals("")) {
        if (Integer.parseInt(id) != 0) {
            candidate = PsqlStore.getInst().findCandidateById(Integer.parseInt(id));
        } else {
            candidate.setUserPhotos(PsqlStore.getInst().getUserPhotos(Integer.parseInt(id)));
        }
    }
    request.setAttribute("candidate", candidate);
    request.setAttribute("user", request.getSession().getAttribute("user"));
%>
<div class="container pt-3">
    <c:if test="${requestScope['user'] != null}">
        <content:navbar auth="${requestScope['user'].name} | Выйти"/>
    </c:if>
    <c:if test="${requestScope['user'] == null}">
        <content:navbar auth="Войти"/>
    </c:if>
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
                <c:if test="${candidate.userPhotos.size() != 0}">
                    <div class="container" style="width:150px">
                        <div id="carouselExampleControls" class="carousel slide" data-ride="carousel">
                            <div class="carousel-inner">
                                <c:forEach var="carusel_id" items="${candidate.userPhotos}">
                                    <div class="carousel-item<c:if test="${carusel_id == candidate.userPhotos[0]}"><c:out value=" active"/></c:if>">
                                        <img src="${pageContext.request.contextPath}/download.do?image_id=${carusel_id}"
                                             height="150" class="d-block w-100" alt="...">
                                    </div>
                                </c:forEach>
                            </div>
                            <a class="carousel-control-prev" href="#carouselExampleControls" role="button"
                               data-slide="prev">
                                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                <span class="sr-only">Previous</span>
                            </a>
                            <a class="carousel-control-next" href="#carouselExampleControls" role="button"
                               data-slide="next">
                                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                <span class="sr-only">Next</span>
                            </a>
                        </div>
                    </div>
                </c:if>
                <form id="candidateInput" action="<%=request.getContextPath()%>/candidates.do?id=<%=candidate.getId()%>" method="post">
                    <div id="nameInput" class="form-group">
                        <label>Имя</label>
                        <input type="text" class="form-control" name="name" value="${candidate.name}">
                    </div>
                    <div id="cityInput" class="form-group">
                        <label for="selectedCity">Выберите город</label>
                        <div id="currentCity" class="d-none">${candidate.city}</div>
                        <div id="ajaxToken" class="d-none">${sessionScope.ajaxToken}</div>
                        <select class="custom-select" id="selectedCity" name="city">
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">Сохранить</button>
                </form>
            </div>
            <div class="card-body">
                <form action="<c:url value='/upload?id=${candidate.id}'/>" method="post" enctype="multipart/form-data">
                    <c:if test='${param["image"] == -1}'>
                        <c:out value="Нужно выбрать файл!"/>
                    </c:if>
                    <div class="checkbox">
                        <input type="file" name="file" multiple>
                    </div>
                    <button type="submit" class="btn btn-primary">Загрузить фотографии</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
