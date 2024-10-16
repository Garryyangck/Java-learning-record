<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Garry售票系统</title>
<style>
  body {
    background-color: #f7f7f7;
    color: #333;
  }
  .text-center {
    text-align: center;
  }
  .logo {
    font-size: 4em;
    color: #3498db;
    font-weight: bold;
  }
  .mission {
    background-color: #ecf0f1;
    padding: 20px;
    border-radius: 5px;
    margin: 20px 0;
  }
  .feature-list {
    list-style: none;
    padding: 0;
  }
  .feature-list li {
    background-color: #fff;
    border: 1px solid #ddd;
    padding: 10px;
    margin-bottom: -1px; /* Remove double space between list items */
    border-radius: 5px;
    transition: background-color 0.3s ease;
  }
  .feature-list li:hover {
    background-color: #ecf0f1;
  }
  .step-list {
    list-style-type: none;
    counter-reset: step;
    padding-left: 0;
  }
  .step-list li {
    margin-bottom: 10px;
    padding-left: 50px;
    position: relative;
  }
  .step-list li::before {
    content: counter(step);
    counter-increment: step;
    position: absolute;
    left: 10px;
    top: 0;
    background-color: #3498db;
    color: #fff;
    width: 25px;
    height: 25px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    font-weight: bold;
  }
  blockquote {
    background-color: #fff;
    border-left: 5px solid #3498db;
    padding: 10px;
    margin: 20px 0;
    quotes: "\201C""\201D""\2018""\2019";
  }
  blockquote p {
    display: inline;
  }
  .contact-info, .social-links {
    font-size: 1.2em;
  }
  .social-links a {
    margin: 0 10px;
    color: #3498db;
    text-decoration: none;
  }
  .footer {
    margin-top: 20px;
    color: #aaa;
    font-size: 0.8em;
  }
</style>
</head>
<body>

<div class="container">
  <div class="text-center">
    <h1 class="logo">GARRY</h1>
  </div>

  <h2 style="font-size: 1.5em; color: #3498db; font-weight: bold; border-left: 5px solid #3498db; padding-left: 15px; margin-top: 20px;">
    欢迎来到 Garry 售票系统 🚄✈️🚌
  </h2>

  <hr>

  <div class="mission">
    <p>
      在Garry售票系统，我们致力于为您提供最便捷、最高效的车票预订体验。无论是火车票、飞机票还是长途汽车票，您都可以在这里一站式解决您的出行需求。
    </p>
  </div>

  <h3 style="font-size: 1.2em; color: #333; font-weight: bold; margin-top: 20px;">
    特色功能
  </h3>

  <ul class="feature-list">
    <li>实时查询 - 随时随地查看最新的班次和票价。</li>
    <li>快速预订 - 简单几步即可完成订票流程。</li>
    <li>多种支付方式 - 支持信用卡、借记卡及第三方支付平台。</li>
    <li>电子票务 - 订单确认后立即获取电子票据。</li>
    <li>客户支持 - 24/7在线客服为您解答疑问。</li>
  </ul>

  <h3 style="font-size: 1.2em; color: #333; font-weight: bold; margin-top: 20px;">
    如何使用
  </h3>

  <ol class="step-list">
    <li>注册/登录 - 创建您的个人账户或直接登录。</li>
    <li>选择目的地 - 输入出发地和目的地，并选择出行日期。</li>
    <li>筛选结果 - 根据您的偏好筛选出最适合的班次。</li>
    <li>填写信息 - 完善乘客信息并选择座位。</li>
    <li>支付订单 - 通过安全的支付网关完成付款。</li>
    <li>获取票据 - 查收邮件中的电子票据，准备愉快的旅程！</li>
  </ol>

  <h3 style="font-size: 1.2em; color: #333; font-weight: bold; margin-top: 20px;">
    用户评价
  </h3>

  <blockquote>
    “非常方便快捷！界面友好，操作简单。强烈推荐给经常需要出行的朋友。” —— 张先生
  </blockquote>

  <blockquote>
    “服务态度非常好，有一次我遇到了问题，客服很快就帮我解决了。” —— 李小姐
  </blockquote>

  <div class="contact-info text-center">
    <h3 style="font-size: 1.2em; color: #333; font-weight: bold;">
      联系我们
    </h3>
    <p>电话: 123-456-7890</p>
    <p>邮箱: support@garryticketing.com</p>
    <p>地址: 中国某市某区某路123号</p>
  </div>

  <div class="social-links text-center">
    <h3 style="font-size: 1.2em; color: #333; font-weight: bold;">
      关注我们
    </h3>
    <a href="#">Facebook</a>
    <a href="#">Twitter</a>
    <a href="#">Instagram</a>
  </div>

  <div class="footer text-center">
    感谢您选择Garry售票系统，祝您旅途愉快！
    <br>
    © 2024 Garry售票系统. 保留所有权利.
  </div>
</div>

</body>
</html>