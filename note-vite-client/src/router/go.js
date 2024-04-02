import router from "@/router/index";

// 路径跳转
/**
 * 
 * @param {*} path 路由地址，路由地址对象
 * @param {*} callback 路由跳转地址后，需要执行得函数
 * @param {*} isRouter 是否采用路由跳转
 * @param {*} isNew 是否采用新标签页（isRouter = false）
 */
export const toHref = (path = '/', callback = () => {
}, isRouter = true, isNew = false) => {
    if(isRouter){
        //采用路由跳转
        router.push(path).then(() => {
            callback();
        })
    }else{
        //采用window 跳转地址
        // 判断跳转得方式（是否是新标签页）
        const target = isNew ? '_blank' : '_self';
        //跳转地址
        window.open(path, target)
    }
}