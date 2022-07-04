class SummernoteManager {
    constructor(content) {
        var self = this;
        this.options = {
            lineHeights: ['0.2', '0.3', '0.4', '0.5', '0.6', '0.8', '1.0', '1.2', '1.4', '1.5', '2.0', '3.0'],
            fontNames: ['Arial', 'Comic Sans MS',  'sans-serif', 'Noto Sans CJK Kr', '나눔바른고딕', '나눔고딕', '나눔손글씨체', '한돋움체', '나눔명조', '나눔명조에코', '마루부리'],
            fontNamesIgnoreCheck : ['Arial', 'Comic Sans MS',  'sans-serif', 'Noto Sans CJK Kr', '나눔바른고딕', '나눔고딕', '나눔손글씨체', '한돋움체', '나눔명조', '나눔명조에코', '마루부리'],
            toolbar: [
                ['font', ['fontname', 'fontsize', 'fontsizeunit']],
                ['height',['height']],
                ['style', ['style']],
                ['font', ['bold', 'underline', 'clear']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['table', ['table']],
                ['insert', ['link', 'picture']],
                ['view', ['fullscreen', 'codeview', 'help']],
            ],
            callbacks: {
            },
            lang: 'ko-KR',
            height: 400,
        };
        this.summernote = $('#summernote').summernote(this.options);
        if (content != 'null') {
            $('#summernote').summernote('code', this.htmlDecode(content));
        }
        this.init();
    }

    init = () => {
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

    htmlDecode = (input) => {
        const e = document.createElement('textarea');
        e.innerHTML = input;
        return e.childNodes.length === 0 ? "" : e.childNodes[0].nodeValue;
    }
}