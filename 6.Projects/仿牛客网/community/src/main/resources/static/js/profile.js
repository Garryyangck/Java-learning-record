function follow(btn, entityId) {
    //发送AJAX请求之前，将CSRF令牌设置到请求头中
    var token = $("meta[name = '_csrf']").attr("content");
    var header = $("meta[name = '_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    })

    if ($(btn).hasClass("btn-info")) {
        // 关注TA
        $(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
        $.post(
            CONTEXT_PATH + "/follow",
            {
                "entityType": 3,
                "entityId": entityId
            },
            function (responseVo) {
                try {
                    responseVo = $.parseJSON(responseVo);
                } catch (e) {
                    location.href = CONTEXT_PATH + "/login";
                    return;
                }

                if (responseVo.code === 0) {
                    $("#follower").text(responseVo.data.followerCount);
                } else {//统一异常管理器返回该消息
                    alert(responseVo.msg);
                }
            }
        )
    } else {
        // 取消关注
        $(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
        $.post(
            CONTEXT_PATH + "/unfollow",
            {
                "entityType": 3,
                "entityId": entityId
            },
            function (responseVo) {
                try {
                    responseVo = $.parseJSON(responseVo);
                } catch (e) {
                    location.href = CONTEXT_PATH + "/login";
                    return;
                }

                if (responseVo.code === 0) {
                    $("#follower").text(responseVo.data.followerCount);
                } else {//统一异常管理器返回该消息
                    alert(responseVo.msg);
                }
            }
        )
    }
}