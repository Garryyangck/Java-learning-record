$(function () {
    $("#topBtn").click(setTop);
    $("#wonderfulBtn").click(setWonderful);
    $("#blacklistBtn").click(setBlacklist);
})

function like(btn, entityType, entityId, receiverId, postId) {
    //发送AJAX请求之前，将CSRF令牌设置到请求头中
    var token = $("meta[name = '_csrf']").attr("content");
    var header = $("meta[name = '_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    })

    $.post(
        CONTEXT_PATH + "/like",
        {
            "entityType": entityType,
            "entityId": entityId,
            "receiverId": receiverId,
            "postId": postId
        },
        function (responseVo) {
            try {
                responseVo = $.parseJSON(responseVo);
            } catch (e) {
                //如果用户非登录状态，则登录限制拦截器会返回login的html页面
                //该页面不能转换为JSON格式，于是跳转至login页面，并return，防止执行下面的alert
                location.href = CONTEXT_PATH + "/login";
                return;
            }

            if (responseVo.code === 0) {
                $(btn).children("i").text(responseVo.data.likeCount);
                $(btn).children("b").text(responseVo.data.likeStatus === 1 ? '已赞' : '赞');
            } else {//统一异常管理器返回该消息
                alert(responseVo.msg);
            }
        }
    )
}

function setTop() {
    //发送AJAX请求之前，将CSRF令牌设置到请求头中
    var token = $("meta[name = '_csrf']").attr("content");
    var header = $("meta[name = '_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    })

    var postId = $("#postId").text();
    $.post(
        CONTEXT_PATH + "/post/top",
        {
            "postId": postId
        },
        function (responseVo) {
            try {
                responseVo = $.parseJSON(responseVo);
            } catch (e) {
                //如果用户非登录状态，则登录限制拦截器会返回login的html页面
                //该页面不能转换为JSON格式，于是跳转至login页面，并return，防止执行下面的alert
                location.href = CONTEXT_PATH + "/login";
                return;
            }

            if (responseVo.code === 0) {
                $("#topBtn").attr("disabled", "disabled");
            } else {//统一异常管理器返回该消息
                alert(responseVo.msg);
            }
        }
    )
}

function setWonderful() {
    //发送AJAX请求之前，将CSRF令牌设置到请求头中
    var token = $("meta[name = '_csrf']").attr("content");
    var header = $("meta[name = '_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    })

    var postId = $("#postId").text();
    $.post(
        CONTEXT_PATH + "/post/wonderful",
        {
            "postId": postId
        },
        function (responseVo) {
            try {
                responseVo = $.parseJSON(responseVo);
            } catch (e) {
                //如果用户非登录状态，则登录限制拦截器会返回login的html页面
                //该页面不能转换为JSON格式，于是跳转至login页面，并return，防止执行下面的alert
                location.href = CONTEXT_PATH + "/login";
                return;
            }

            if (responseVo.code === 0) {
                $("#wonderfulBtn").attr("disabled", "disabled");
            } else {//统一异常管理器返回该消息
                alert(responseVo.msg);
            }
        }
    )
}

function setBlacklist() {
    //发送AJAX请求之前，将CSRF令牌设置到请求头中
    var token = $("meta[name = '_csrf']").attr("content");
    var header = $("meta[name = '_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    })

    var postId = $("#postId").text();
    $.post(
        CONTEXT_PATH + "/post/blacklist",
        {
            "postId": postId
        },
        function (responseVo) {
            try {
                responseVo = $.parseJSON(responseVo);
            } catch (e) {
                //如果用户非登录状态，则登录限制拦截器会返回login的html页面
                //该页面不能转换为JSON格式，于是跳转至login页面，并return，防止执行下面的alert
                location.href = CONTEXT_PATH + "/login";
                return;
            }

            if (responseVo.code === 0) {
                location.href = CONTEXT_PATH + "/index";
            } else {//统一异常管理器返回该消息
                alert(responseVo.msg);
            }
        }
    )
}