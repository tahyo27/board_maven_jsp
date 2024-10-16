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
           <form id="hiddenForm" action="/boards" method="post">
               <label for="title">Title:</label>
               <input type="text" id="title" name="title" required>
               <br><br>
               <label for="author">Author:</label>
               <input type="text" id="author" name="author" required>
               <br><br>

               <input type="hidden" name="content" id="hiddenContent">
           </form>

          <div id="editor"></div>
          <div>
            <button type="button" id="saveButton">저장</button>
          </div>


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

        const saveBoard = async () => {
            if(editor.getMarkdown().length < 1) {
                alert('내용을 입력하세요');
                throw new Error('에디터에 내용이 필요합니다');
            }

            const content = editor.getHTML();

             const hiddenContentElement = document.querySelector('#hiddenContent');
             if (hiddenContentElement) {
                 hiddenContentElement.value = content;
             }
             else {
                console.error('히든 컨텐트의 엘리멘트 찾을 수 없습니다');
                return;
             }
             const hiddenFormElement = document.querySelector('#hiddenForm');
             if (hiddenFormElement) {
                hiddenFormElement.submit();
             } else {
                console.error('히든 폼을 찾을 수 없습니다');
             }

        };

        document.querySelector('#saveButton').onclick = saveBoard;

    </script>
</body>
</html>