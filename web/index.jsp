<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="code.lab4.SearchServlet.Result" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Поиск слов</title>
</head>
<body>
<h2>Поиск по слову</h2>

<form action="search" method="get">
    <input type="text" name="word" placeholder="Введите слово" required />
    <button type="submit">Искать</button>
</form>

<%
    String query = (String) request.getAttribute("query");
    List<Result> results = (List<Result>) request.getAttribute("results");

    if (results != null && !results.isEmpty()) {
%>
<h3>Результаты для: "<%= query %>"</h3>
<table border="1">
    <tr><th>Файл</th><th>Количество</th><th>Скачать</th></tr>
    <% for (Result r : results) { %>
    <tr>
        <td><%= r.filename %></td>
        <td><%= r.count %></td>
        <td><a href="download?file=<%= r.filename %>">Скачать</a></td>
    </tr>
    <% } %>
</table>
<% } else if (query != null) { %>
<p>Слово "<%= query %>" не найдено в базе.</p>
<% } %>
</body>
</html>
