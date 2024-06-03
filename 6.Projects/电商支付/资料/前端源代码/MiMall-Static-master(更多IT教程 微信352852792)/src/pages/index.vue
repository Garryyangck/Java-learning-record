<template>
  <div class="index">
    <div class="container">
      <div class="swiper-box">
        <div class="nav-menu">
          <ul class="menu-wrap">
            <li class="menu-item">
              <a href="javascript:;">手机 电话卡</a>
              <div class="children">
                <ul>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米CC9</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-2.png" alt="">小米8 青春版</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-3.jpg" alt="">Redmi K20 Pro</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-4.jpg" alt="">移动4G+专区</a></li>
                  </ul>
                  <ul>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米CC美图定制版</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米8 屏幕指纹版</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">Redmi K20</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米移动 电话卡</a></li>
                  </ul>
                  <ul>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米9</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米9</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-1.jpg" alt="">小米9</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米9</a></li>
                  </ul>
                  <ul>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米9</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米9</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米9</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米9</a></li>
                  </ul>
                  <ul>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米9</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米9</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米9</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米9</a></li>
                  </ul>
                  <ul>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米9</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米9</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米9</a></li>
                    <li><a href="" id=""><img src="/imgs/item-box-1.png" alt="">小米9</a></li>
                  </ul>
              </div>
            </li>
            <li class="menu-item">
              <a href="javascript:;">电视 盒子</a>
            </li>
            <li class="menu-item">
              <a href="javascript:;">笔记本 平板</a>
            </li>
            <li class="menu-item">
              <a href="javascript:;">家电 插线板</a>
            </li>
            <li class="menu-item">
              <a href="javascript:;">出行 穿戴</a>
            </li>
            <li class="menu-item">
              <a href="javascript:;">智能 路由器</a>
            </li>
            <li class="menu-item">
              <a href="javascript:;">电源 配件</a>
            </li>
            <li class="menu-item">
              <a href="javascript:;">生活 箱包</a>
            </li>
          </ul>
        </div>
        <swiper v-bind:options="swiperOption">
          <!-- slides -->
          <swiper-slide><img src="/imgs/slider/slide-1.jpg" alt=""></swiper-slide>
          <swiper-slide><img src="/imgs/slider/slide-2.jpg" alt=""></swiper-slide>
          <swiper-slide><img src="/imgs/slider/slide-3.jpg" alt=""></swiper-slide>
          <swiper-slide><img src="/imgs/slider/slide-4.jpg" alt=""></swiper-slide>
          <swiper-slide><img src="/imgs/slider/slide-5.jpg" alt=""></swiper-slide>
          <!-- Optional controls -->
          <div class="swiper-pagination"  slot="pagination"></div>
          <div class="swiper-button-prev" slot="button-prev"></div>
          <div class="swiper-button-next" slot="button-next"></div>
        </swiper>
      </div>
      <div class="ads-box">
        <a href="" id=""><img src="/imgs/ads/ads-1.png" alt=""></a>
        <a href="" id=""><img src="/imgs/ads/ads-2.jpg" alt=""></a>
        <a href="" id=""><img src="/imgs/ads/ads-3.png" alt=""></a>
        <a href="" id=""><img src="/imgs/ads/ads-4.jpg" alt=""></a>
      </div>
      <div class="banner">
        <a href="/#/product/30">
          <img src="/imgs/banner-1.png" alt="">
        </a>
      </div>
    </div>
    <div class="product-box">
      <div class="container">
        <h2>手机</h2>
        <div class="wrapper">
          <div class="banner-left">
            <a href="/#/product/35"><img src="/imgs/mix-alpha.jpg" alt=""></a>
          </div>
          <div class="list-box">
            <div class="list">
              <div class="item">
                <span class="new-pro">新品</span>
                <div class="item-img"><img src="https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/6f2493e6c6fe8e2485c407e5d75e3651.jpg" alt=""></div>
                <div class="item-info">
                  <h3>Redmi K20 Pro 6GB+128GB</h3>
                  <p>骁龙855， 弹出全面屏</p>
                  <p class="price">2999元</p>
                </div>
              </div>
              <div class="item">
                <span class="kill-pro">新品</span>
                <div class="item-img"><img src="https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/4c87947d104ee5833913e4c520108f16.jpg" alt=""></div>
                <div class="item-info">
                  <h3>Redmi Note 7</h3>
                  <p>4800万拍照千元机</p>
                  <p class="price">1199元</p>
                </div>
              </div>
              <div class="item">
                <span></span>
                <div class="item-img"><img src="https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/8737a33c78a94bc36afb860ab23b3939.jpg" alt=""></div>
                <div class="item-info">
                  <h3>小米9 王源限量版</h3>
                  <p>骁龙855，索尼4800万三摄</p>
                  <p class="price">3599元</p>
                </div>
              </div>
              <div class="item">
                <span></span>
                <div class="item-img"><img src="https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/0ce61b71e2f81df62bd0c05aaa901d22.jpg" alt=""></div>
                <div class="item-info">
                  <h3>小米MIX 3 8GB+128GB</h3>
                  <p>DxO百分拍照手机</p>
                  <p class="price">2599元</p>
                </div>
              </div>
            </div>
            <div class="list">
              <div class="item">
                <span></span>
                <div class="item-img"><img src="https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/9aab8a7fa9005ef918c9aa2d5f17c806.jpg" alt=""></div>
                <div class="item-info">
                  <h3>小米CC9e</h3>
                  <p>3200万自拍，4800万三摄</p>
                  <p class="price">1299元</p>
                </div>
              </div>
              <div class="item">
                <span></span>
                <div class="item-img"><img src="https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/3c1af9783bdc53ed843af5655ca92009.jpg" alt=""></div>
                <div class="item-info">
                  <h3>小米CC9</h3>
                  <p>3200万自拍，4800万三摄</p>
                  <p class="price">1799元</p>
                </div>
              </div>
              <div class="item">
                <span></span>
                <div class="item-img"><img src="https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/bd25cc614a670f4d5546fe82e239ef86.jpg" alt=""></div>
                <div class="item-info">
                  <h3>小米CC9 8GB+256GB 美图定制版</h3>
                  <p>8GB+256GB，100%美图相机</p>
                  <p class="price">2599元</p>
                </div>
              </div>
              <div class="item">
                <span></span>
                <div class="item-img"><img src="https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/ca9b4f81af709935556bef9aa21a90e8.jpg" alt=""></div>
                <div class="item-info">
                  <h3>Redmi Note 7 Pro</h3>
                  <p>索尼4800万超清拍照</p>
                  <p class="price">1399元</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <service-bar></service-bar>
  </div>
</template>
<script>
  import ServiceBar from './../components/ServiceBar'
  import { swiper, swiperSlide } from 'vue-awesome-swiper'
  import 'swiper/dist/css/swiper.css'
  export default{
    name:'index',
    components:{
      swiper,
      swiperSlide,
      ServiceBar,
    },
    data(){
      return {
        swiperOption:{
          autoplay:true,
          loop:true,
          effect:'cube',
          cubeEffect: {
            shadowOffset: 100,
            shadowScale: 0.6
          },
          pagination: {
            el: '.swiper-pagination',
            clickable:true
          },
          navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
          }
        }
      }
    }
  }
</script>
<style lang="scss">
  @import './../assets/scss/config.scss';
  @import './../assets/scss/mixin.scss';
  .index{
    .swiper-box{
      .nav-menu{
        position:absolute;
        width:264px;
        height:451px;
        z-index:9;
        padding:26px 0;
        background-color:#55585a7a;
        box-sizing:border-box;
        .menu-wrap{
          .menu-item{
            height:50px;
            line-height:50px;
            a{
              position:relative;
              display:block;
              font-size:16px;
              color:#ffffff;
              padding-left:30px;
              &:after{
                position:absolute;
                right:30px;
                top:17.5px;
                content:' ';
                @include bgImg(10px,15px,'/imgs/icon-arrow.png');
              }
            }
            &:hover{
              background-color:$colorA;
              .children{
                display:block;
              }
            }
            .children{
              display:none;
              width:962px;
              height:451px;
              background-color:$colorG;
              position:absolute;
              top:0;
              left:264px;
              border:1px solid $colorH;
              ul{
                display:flex;
                justify-content:space-between;
                height:75px;
                li{
                  height:75px;
                  line-height:75px;
                  flex:1;
                  padding-left:23px;
                }
                a{
                  color:$colorB;
                  font-size:14px;
                }
                img{
                  width:42px;
                  height:35px;
                  vertical-align:middle;
                  margin-right:15px;
                }
              }
            }
          }
        }
      }
      .swiper-container {
        height: 451px;
        .swiper-button-prev{
          left:274px;
        }
        img{
          width:100%;
          height:100%;
        }
      }  
    }
    .ads-box{
      @include flex();
      margin-top:14px;
      margin-bottom:31px;
      a{
        width:296px;
        height:167px;
      }
    }
    .banner{
      margin-bottom:50px;
    }
    .product-box{
      background-color:$colorJ;
      padding:30px 0 50px;
      h2{
        font-size:$fontF;
        height:21px;
        line-height:21px;
        color:$colorB;
        margin-bottom:20px;
      }
      .wrapper{
        display:flex;
        .banner-left{
          margin-right:16px;
          img{
            width:224px;
            height:619px;
          }
        }
        .list-box{
          .list{
            @include flex();
            width:986px;
            margin-bottom:14px;
            &:last-child{
              margin-bottom:0;
            }
            .item{
              width:236px;
              height:302px;
              background-color:$colorG;
              text-align:center;
              span{
                display:inline-block;
                width:67px;
                height:24px;
                font-size:14px;
                line-height:24px;
                color:$colorG;
                &.new-pro{
                  background-color:#7ECF68;
                }
                &.kill-pro{
                  background-color:#E82626;
                }
              }
              .item-img{
                img{
                  width:100%;
                  height:195px;
                }
              }
              .item-info{
                h3{
                  font-size:$fontJ;
                  color:$colorB;
                  line-height:$fontJ;
                  font-weight:bold;
                }
                p{
                  color:$colorD;
                  line-height:13px;
                  margin:6px auto 13px;
                }
                .price{
                  color:#F20A0A;
                  font-size:$fontJ;
                  font-weight:bold;
                  cursor:pointer;
                  &:after{
                    @include bgImg(22px,22px,'/imgs/icon-cart-hover.png');
                    content:' ';
                    margin-left:5px;
                    vertical-align: middle;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
</style>