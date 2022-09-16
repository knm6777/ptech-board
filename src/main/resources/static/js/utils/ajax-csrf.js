let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");
$.ajaxSetup({
    beforeSend: function (xhr){
        xhr.setRequestHeader(header, token);
    }
});