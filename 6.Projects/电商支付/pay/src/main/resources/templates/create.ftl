<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>支付</title>
</head>
<body>

<div id="myQrcode"></div>
<div id="orderId" hidden>${orderId}</div>
<div id="returnUrl" hidden>${returnUrl}</div>

<script src="https://cdn.bootcdn.net/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
<script>
    jQuery/*此处不能写jquery!*/('#myQrcode').qrcode({
        text: "${codeUrl}"
    });
    $(function () {
        console.log("开始查询支付状态")
        setInterval(function () {
            $.ajax({
                url: '/pay/queryByOrderId',//向这里发送get请求
                data: {
                    'orderId': $('#orderId').text()//当前页面二维码的orderId
                },
                success: function (result) {
                    console.log(result)
                    if (result.platformStatus != null
                        && result.platformStatus === '支付成功') {
                        location.href = $('#returnUrl').text()//跳转
                    }
                },
                error: function (result) {
                    alert(result)
                }
            })
        }, 2000)
    })
</script>
</body>
</html>