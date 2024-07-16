<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Board</title>
</head>
<body>
    <h1>제목</h1>
    <main>
    <div>
          <div>번호<div>
          <div>제목<div>
          <div>작성자<div>
          <div>작성일<div>
          <div>조회수<div>
    <div>
    <c:forEach var="board" items="${boardList}">
    <div>
          <div>${board.id}<div>
          <div>${board.title}<div>
          <div>${board.author}<div>
          <div>${board.createAt}<div>
          <div>${board.count}<div>
    <div>
    </c:forEach>
    </main>
</body>
</html>