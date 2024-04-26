$(function () {
    bsCustomFileInput.init();
    $("input").focus(clear_error);
    $("#uploadForm").submit(upload);
});

function clear_error() {
    $(this).removeClass("is-invalid");
}

document.getElementById("update-password-commit").onclick = function () {
    var pwd1 = $("#new-password").val();
    var pwd2 = $("#confirm-password").val();
    if (pwd1 != pwd2) {
        $("#confirm-password").addClass("is-invalid");
        return false;
    }
    return true;
}

document.getElementById("confirm-password").onfocus = function () {
    $(this).removeClass("is-invalid");
}

function upload() {
    var fileName = $("#fileName").val();
    console.log(fileName);
    $.ajax({
        url: "http://upload-z1.qiniup.com",
        method: "post",
        processData: false,
        contentType: false,
        data: new FormData($("#uploadForm")[0]),
        success: function (data) {
            if (data && data.code === 0) {
                // 更新头像访问路径
                updateHeader(fileName);
            } else {
                alert("上传失败!");
            }
        }
    });
    return false;//该表单已经异步提交了，return false让该表单不再自己提交！
}

function updateHeader(fileName) {
    console.log("进入updateHeader");
    console.log(fileName);

    //发送AJAX请求之前，将CSRF令牌设置到请求头中
    var token = $("meta[name = '_csrf']").attr("content");
    var header = $("meta[name = '_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    })

    $.post(
        CONTEXT_PATH + "/user/update/header",
        {
            "fileName": fileName
        },
        function (data) {
            console.log("收到回复");
            data = $.parseJSON(data);
            if (data.code === 0) {
                window.location.reload();
            } else {
                alert(data.msg);
            }
        }
    );
}