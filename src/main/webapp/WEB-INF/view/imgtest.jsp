<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>이미지테스트</title>
</head>
<body>
    <main>
    <div>
          <h1>Upload an Image</h1>
          <form action="/imgtest" method="post" enctype="multipart/form-data">
             <label for="file">Choose an image to upload:</label>
             <input type="file" id="file" name="images" accept="image/*" multiple>
             <br><br>
             <button type="submit">Upload Image</button>
          </form>
    </div>
    </main>
</body>
</html>