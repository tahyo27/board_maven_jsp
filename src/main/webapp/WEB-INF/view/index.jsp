<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Board</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/list.css">
</head>
<body>
    <h1>제목</h1>
    <main>
     <div class="board-header">
                <div>번호</div>
                <div>제목</div>
                <div>작성자</div>
                <div>작성일</div>
                <div>조회수</div>
     </div>
            <c:forEach var="board" items="${boardList}">
                <div class="board-item">
                    <div class="board-id">${board.id}</div>
                    <div class="board-title">${board.title}</div>
                    <div class="board-author">${board.author}</div>
                    <div class="board-created-at">${board.createAt}</div>
                    <div class="board-count">${board.count}</div>
                </div>
            </c:forEach>
    </main>
</body>
</html>