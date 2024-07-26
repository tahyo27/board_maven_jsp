<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${board.title}</title>
</head>
<body>
    <main>
        <div>
              <h1>셀렉트 페이지</h1>
              <div>${board.title}</div>
              <div>${board.author}</div>

              <div>
                <div>${board.content}</div>
              </div>



        </div>
        </main>


</body>
</html>