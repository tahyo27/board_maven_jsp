<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>INSERT</title>
    <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.css" />
</head>
<body>


    <main>
    <div>
          <h1>인서트페이지</h1>
          <form>
          <div id="editor"></div>
          <!--
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
          -->


    </div>
    </main>

    <script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>

    <script>
        const editor = new toastui.Editor({
        	el: document.querySelector('#editor'),
            previewStyle: 'vertical',
            height: '500px',
            initialEditType: 'wysiwyg',

             hooks: {
                addImageBlobHook: async (blob, callback) => {
                    console.log(blob);
                    console.log(callback);
                    const formData = new FormData();
                    formData.append('file', blob, blob.name);

                   try {
                        const response = await fetch('/image/temp', {
                            method: 'POST',
                            body: formData
                        });

                   if (!response.ok) {
                        throw new Error('Upload failed');
                   }

                   const data = await response.json();
                   console.log(data);
                   callback(data.url);
                   } catch (error) {
                        console.error('Error:', error);
                   }
                }
             }

        });
    </script>
</body>
</html>