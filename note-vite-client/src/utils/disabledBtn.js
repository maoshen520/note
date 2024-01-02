
/**
 * 
 * @param {*} btnDisabled 按钮 disabled 属性绑定的数据源
 * @param {*} isDisabled  是否是禁用按钮
 * @param {*} isDelay 取消禁用按钮是否需要延迟
 * @param {*} time  延迟的时间
 */

export const disabledBtn = (btnDisabled, isDisabled, isDelay = false, time = 0) => {
    if(isDisabled){
        btnDisabled.value = isDisabled;  //禁用按钮
    }else {
        if(isDelay){
            setTimeout(() => {
                btnDisabled.value = isDisabled;  //禁用按钮
            }, time * 1000)
        }else {
            btnDisabled.value = isDisabled;  //取消禁用按钮
        }
    }
}