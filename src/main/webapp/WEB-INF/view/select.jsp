<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${board.title}</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/select.css">
</head>
<body>
    <h1>셀렉트 페이지</h1>
    <main>
        <div class="select-header">
            <div class="select-title"><c:out value="${board.title}" escapeXml="true" /></div>
            <div class="select-detail-container">
                 <div class="left-group">
                        <div><c:out value="${board.author}" escapeXml="true" /></div>
                        <div><c:out value="${board.createAt}" escapeXml="true" /></div>
                 </div>
                 <div class="right-group">
                        <div><c:out value="${board.count}" escapeXml="true" /></div>
                 </div>
            </div>

        </div>

        <div class="select-content-container">
            <div>
                 <c:out value="${board.content}" escapeXml="false" />
            </div>
        </div>
    </main>


</body>
</html>