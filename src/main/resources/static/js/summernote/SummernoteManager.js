class SummernoteManager {
    constructor() {
        this.token = $("meta[name='_csrf']").attr("content");
        this.header = $("meta[name='_csrf_header']").attr("content");
        this.imgIds = [];
        var self = this;
        this.options = {
            lineHeights: ['0.2', '0.3', '0.4', '0.5', '0.6', '0.8', '1.0', '1.2', '1.4', '1.5', '2.0', '3.0'],
            fontNames: ['Arial', 'Comic Sans MS', 'sans-serif', 'Noto Sans CJK Kr', '나눔바른고딕', '나눔고딕', '나눔손글씨체', '한돋움체', '나눔명조', '나눔명조에코', '마루부리'],
            fontNamesIgnoreCheck: ['Arial', 'Comic Sans MS', 'sans-serif', 'Noto Sans CJK Kr', '나눔바른고딕', '나눔고딕', '나눔손글씨체', '한돋움체', '나눔명조', '나눔명조에코', '마루부리'],
            toolbar: [
                ['font', ['fontname', 'fontsize', 'fontsizeunit']],
                ['height', ['height']],
                ['style', ['style']],
                ['font', ['bold', 'underline', 'clear']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['table', ['table']],
                ['insert', ['link', 'picture']],
                ['view', ['fullscreen', 'codeview', 'help']],
            ],
            callbacks: {
                onImageUpload: function (files) {
                    self.sendImg(files, $(this));
                },
                onMediaDelete : function(target) {
                    self.deleteFile(target[0].src);
                }
        },
            lang: 'ko-KR',
            height: 400,
        };
        this.summernote = $('#summernote').summernote(this.options);
    }


    sendImg = (files, editor) => {
        var data = new FormData();
        for (var i = 0; i < files.length; i++) {
            data.append("multipartFiles", files[i]);
        }
        var self = this;
        console.log(data);
        $.ajax({
            data: data,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(self.header, self.token);
            },
            type: "POST",
            url: '/files/summernote',
            enctype: 'multipart/form-data',
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                for (var i = 0; i < data.length; i++) {
                    editor.summernote('insertImage', '/upload/summernote' + data[i].relativePath);
                    self.imgIds.push(data[i].id);
                    console.log(self.imgIds);
                }

                var formdata = new FormData($("#post-form")[0]);
                formdata.forEach(el=>{console.log(el)})
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(errorThrown)
            }
        });
    }

    deleteFile = (src) => {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            data: {src : src},
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            type: "POST",
            url: base_url+"dropzone/delete_file", // replace with your url
            cache: false,
            success: function(resp) {
                console.log(resp);
            }
        });
    }


}