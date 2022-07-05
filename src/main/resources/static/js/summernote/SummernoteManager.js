class SummernoteManager {
    init = (content) => {
        if (content == 'null') {

        }
        $('#summernote').summernote('code', this.htmlDecode(content));

        $("#summernote-form").on("submit", function () {
            var markupStr = $('#summernote').summernote('code');
            $("#summernote").val(markupStr);
        });

        $('.note-codable').on('blur', function() {
            if ($('#summernote').summernote('codeview.isActivated')) {
                $('[name="content"]').val($('#summernote').summernote('code'));
            }
        });
    }

    sendImg = (files, editor) => {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        var data = new FormData();
        var self = this;
        for (var i=0; i<files.length; i++) {
            data.append("multipartFiles", files[i]);
        }
        console.log(data);
        $.ajax({
            data: data,
            beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            type: "POST",
            url: '/files/summernote',
            enctype: 'multipart/form-data',
            cache: false,
            contentType: false,
            processData: false,
            success: function(data) {
                // editor.summernote('insertImage', '/upload' + data.relativePath);
                for (var i=0; i<data.length; i++) {
                    editor.summernote('insertImage', '/upload' + data[i].relativePath);
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log(errorThrown)
            }
        });
    }

    htmlDecode = (input) => {
        const e = document.createElement('textarea');
        e.innerHTML = input;
        return e.childNodes.length === 0 ? "" : e.childNodes[0].nodeValue;
    }
}