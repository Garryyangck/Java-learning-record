const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,

    devServer: {
        // 解决页面弹出红色报错遮罩层
        client: {
            overlay: false
        }
    }
})
