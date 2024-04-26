$(function () {
    $("#sendBtn").click(send_letter);
});

function send_letter() {
    //发送AJAX请求之前，将CSRF令牌设置到请求头中
    var token = $("meta[name = '_csrf']").attr("content");
    var header = $("meta[name = '_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    })

    $("#sendModal").modal("hide");

    var toUsername = $("#recipient-name").val();
    var content = $("#message-text").val();

    $.post(
        CONTEXT_PATH + "/message/letter/send",
        {
            "toUsername": toUsername,
            "content": content
        },
        function (data) {
            data = $.parseJSON(data);

            $("#hintBody").text(data.msg);

            $("#hintModal").modal("show");
            setTimeout(function () {
                $("#hintModal").modal("hide");
                if (data.code === 0) {
                    window.location.reload();
                }
            }, 2000);
        }
    );
}

function delete_msg(messageId) {
    //发送AJAX请求之前，将CSRF令牌设置到请求头中
    var token = $("meta[name = '_csrf']").attr("content");
    var header = $("meta[name = '_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    })

    // AJAX发送请求，将message状态改为DELETE
    $.post(
        CONTEXT_PATH + "/message/delete",
        {
            "messageId": messageId
        },
        function (responseVo) {
            try {
                responseVo = $.parseJSON(responseVo);
            } catch (e) {
                location.href = CONTEXT_PATH + "/login";
                return;
            }

            if (responseVo.code === 0) {
                window.location.reload();
            } else {//统一异常管理器返回该消息
                alert(responseVo.msg);
            }
        }
    )
    $(this).parents(".media").remove();
}