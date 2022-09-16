class SummernoteManager {
    constructor() {
        this.imgIds = [];
        let self = this;
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
                }
                // ,
                // onMediaDelete : function(target) {
                //     self.deleteFile(target[0].src);
                // }
        },
            lang: 'ko-KR',
            height: 400,
        };
        this.summernote = $('#summernote').summernote(this.options);
    }


    sendImg = (files, editor) => {
        let data = new FormData();
        for (let i = 0; i < files.length; i++) {
            data.append("multipartFiles", files[i]);
        }
        let self = this;
        $.ajax({
            data: data,
            type: "POST",
            url: '/files/summernote',
            enctype: 'multipart/form-data',
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                for (let i = 0; i < data.length; i++) {
                    editor.summernote('insertImage', '/upload/summernote' + data[i].relativePath);
                    self.imgIds.push(data[i].id);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert("이미지 저장에 실패했습니다.")
                console.log(errorThrown)
            }
        });
    }

    // deleteFile = (src) => {
    //     let token = $("meta[name='_csrf']").attr("content");
    //     let header = $("meta[name='_csrf_header']").attr("content");
    //
    //     $.ajax({
    //         data: {src : src},
    //         beforeSend: function (xhr) {
    //             xhr.setRequestHeader(header, token);
    //         },
    //         type: "POST",
    //         url: base_url+"dropzone/delete_file", // replace with your url
    //         cache: false,
    //         success: function(resp) {
    //             console.log(resp);
    //         }
    //     });
    // }


}