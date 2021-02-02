<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="auth" required="true" rtexprvalue="true" %>
<%@ tag pageEncoding="UTF-8" isELIgnored="false" %>

<div class="row">
    <ul class="nav">
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/index.do">Главная</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/posts.do">Вакансии</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/candidates.do">Кандидаты</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/editPost.do">Добавить вакансию</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/editCandidate.do">Добавить кандидата</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/auth.do">${auth}</a>
        </li>
    </ul>
</div>