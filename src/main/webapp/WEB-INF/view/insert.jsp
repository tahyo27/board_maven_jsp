<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>INSERT</title>
</head>
<body>
    <main>
    <div>
          <h1>INSERT TEST</h1>
          <form action="/boards" method="post" enctype="multipart/form-data">
              <label for="title">Title:</label>
              <input type="text" id="title" name="title" required>
              <br><br>

              <label for="content">Content:</label>
              <textarea id="content" name="content" rows="4" cols="50" required></textarea>
              <br><br>

              <label for="author">Author:</label>
              <input type="text" id="author" name="author" required>
              <br><br>

              <label for="file">Choose an image to upload:</label>
              <input type="file" id="file" name="images" accept="image/*" multiple>
              <br><br>

              <button type="submit">Upload Image</button>
          </form>
    </div>
    </main>
</body>
</html>