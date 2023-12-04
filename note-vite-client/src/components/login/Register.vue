<template>
    <div>
        <el-card class="box-card">
            <template #header>
                <div class="my-header">
                    <h2 class="titleClass">注册</h2>
                    <div>    
                        <el-text class="mx-1" type="info">已有账号？</el-text>
                        <el-button type="primary" text class="goRegisterBtn" @click="emits('changeStep',1)">
                            前往登录
                        </el-button>
                    </div>
                </div>
            </template>

            <el-form
                ref="registerRuleForm"
                label-position="top"
                label-width="100px"
                :model="registerFormValue"
                style="max-width: 460px;"
                :rules="registerFormRules"
            >
                <el-form-item label="邮箱" prop="email">
                    <el-input placeholder="请输入邮箱" size="large" v-model="registerFormValue.email">
                        <template #prefix>
                            <el-icon class="el-input__icon" :size="20"><Message /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>

                <el-form-item label="验证码" prop="code">
                    <el-col :span="12">
                        <el-input placeholder="请输入验证码" size="large" v-model="registerFormValue.code">
                        </el-input>
                    </el-col>
                    <el-col :span="1"></el-col>
                    <el-col :span="11">
                        <el-button style="color: #F74800;width: 100%;font-weight: bold;" size="large" color="#FFCCA9" type="success" :disabled="btnCountDown.disabled" @click="getEmailCode(registerRuleForm)">{{ btnCountDown.text }}</el-button>
                    </el-col>
                </el-form-item> 

                <el-form-item prop="trim">
                    <el-checkbox v-model="registerFormValue.trim" style="color: #000;">同意本公司的</el-checkbox>
                    <el-button type="primary" text class="goRegisterBtn">
                        《条款与协议》
                    </el-button>
                </el-form-item> 

                <el-form-item>
                    <el-button style="width: 100%;" size="large" color="#F74800" type="success" :disabled="registerBtnDisabled" @click="registerBtn(registerRuleForm)">注册</el-button>
                </el-form-item>
            </el-form> 
        </el-card>
    </div>
</template>

<script setup>

    import {ref} from 'vue';
    import {Message} from '@element-plus/icons-vue';  //图标
    import { noteBaseRequest } from "@/request/note_request";

    // 自定义事件
    const emits = defineEmits(['changeStep']);

    const emailVckey = ref('');  //验证码查询的关键词

    const registerBtnDisabled = ref(false);

    // 邮箱判断
    const emailInput = (rule,value, callback) => {
        if(!value){
            return callback(new Error('请输入邮箱号码'))
        }else if(!(/^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/.test(value))){
            return callback(new Error('请输入正确邮箱格式'))
        }else{
            callback()
        }
    };

    // 显示注册框
    const registerVisible = ref(true);
    // 注册表单值
    const registerFormValue = ref({
        email: '',
        code: '',
        trim: false,
    });
    // 注册form
    const registerRuleForm = ref(null);
    // 注册表单验证规则
    const registerFormRules = {
        email:{
            required: true,
            trigger: 'blur',
            validator:emailInput
        },
        code:{
            required: true, 
            message: '请输入验证码', 
            trigger: 'blur'
        },
        trim:{
            message: '请认真阅读本公司的条款与协议', 
            trigger: 'change',
            validator:(rule,value) => {
                return value  
            }
        }   
    };

    // 注册按钮
    const registerBtn = (formEl) => {
        if (!formEl) return
        formEl.validate( async (valid) => {
            console.log(valid);
            if (valid) {
                console.log('点击注册表单验证成功')

                //是否获取验证码
                if(emailVckey.value === '' || emailVckey === null){
                    ElMessage({
                        message: '请先获取验证码',
                        type: 'warning',
                    });
                    return false;
                };

                //判断接受验证码的邮箱是否和注册的邮箱一致
                const vc_email = emailVckey.value.split(":")[1];  //接受验证码的邮箱
                const email = registerFormValue.value.email;   //注册账号的邮箱
                if(email !== vc_email){
                    ElMessage({
                        message: '邮箱账号已发生改变，请重新获取',
                        type: 'error',
                    });
                    return false;
                };

                registerBtnDisabled.value = true;  //禁用注册按钮

                const { data: responseData } = await noteBaseRequest.post(
                    '/user/register/email',
                    {
                        email,
                        vc:registerFormValue.value.code,
                        vcKey: emailVckey.value
                    }
                ).catch(() => {
                    ElMessage({
                        message: '注册请求失败',
                        type: 'error',
                    })
                    setTimeout(() => {
                        registerBtnDisabled.value = false;  //解除禁用注册按钮
                    })
                    throw '注册请求失败';
                })

                console.log('注册 ==>',responseData);
                if(responseData.success){
                    ElMessage({
                        message: responseData.message,
                        type: 'success'
                    })
                    emits('changeStep',1)
                }else{
                    ElMessage({
                        message: responseData.message,
                        type: 'error'
                    })
                    setTimeout(() => {
                        registerBtnDisabled.value = false;  //解除禁用注册按钮
                    })
                }
            } else {
                console.log('error submit!')
                return false
            }
        })

    };

    // 获取验证码
    const btnCountDown = ref({
        text:'获取验证码',
        time:60,
        disabled:false,  //是否禁用按钮
        clock: null
    });
    // 按钮倒计时
    const buttonCountDown = () => {
        btnCountDown.value.disabled = true;
        btnCountDown.value.clock = setInterval(() => {
            if(btnCountDown.value.time === 1){
                resetButtonCountDownStatus();
            }else{
                
                btnCountDown.value.time--;
                btnCountDown.value.text = btnCountDown.value.time + 's重新获取';   
            }
        },1000)
    };
    // 重置获取验证码的状态
    const resetButtonCountDownStatus = () => {
        clearInterval(btnCountDown.value.clock);
        btnCountDown.value.disabled = false;
        btnCountDown.value.time = 60;
        btnCountDown.value.text = '获取验证码';   
    };

    //获取验证码按钮 -------- 获取验证码之前先验证邮箱 ----验证表单的某一项
    const getEmailCode = (formEl) => {
        if (!formEl) return
        // 验证表单的某一项
        formEl.validateField('email',async (valid) => {
            if (valid) {  //只有邮箱验证通过，才执行倒计时
                buttonCountDown();

                const { data: responseData } = await noteBaseRequest.get(
                    '/mail/register/vc',
                    {
                        params: {
                            email: registerFormValue.value.email,
                        } 
                    }
                ).catch(() => {
                    ElMessage({
                        message: '发送验证码失败',
                        type: 'error',
                    })
                    return false;
                })

                console.log('邮箱注册码',responseData);
                let mseeageType;
                if(responseData.success){
                    mseeageType = 'success';
                    emailVckey.value = responseData.data;
                }else{
                    mseeageType = 'error';
                }
                ElMessage({
                    message: responseData.message,
                    type: mseeageType
                })
            }
            //  else {
            //     console.log('error submit!')
            //     return false
            // }
        })
    }

    // 重置表单以及重置获取验证码的状态
    const resetForm = () => {
        registerRuleForm.value.resetFields();
        resetButtonCountDownStatus();
    };
	// 暴露方法  -父组件调用此子组件方法
	defineExpose({
		resetForm
    })

    
</script>

<style lang="less" scoped>
    .my-header {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
    }
    .titleClass{
        margin: 0;
    }
    .goRegisterBtn{
        padding:0px;
        margin-top: -2px;
        background-color: #fff !important;
    }
</style>