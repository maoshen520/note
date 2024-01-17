<template>
    <div>
        <el-card class="box-card">
            <template #header>
                <div class="my-header">
                    <h2 class="titleClass">登录</h2>
                    
                    <div>    
                        <el-text class="mx-1" type="info">暂无账号？</el-text>
                        <el-button type="primary" text class="goRegisterBtn" @click="emits('changeStep',2)">
                            前往注册
                        </el-button>
                    </div>
                </div>
            </template>

            <el-form
                ref="loginRuleForm"
                label-position="top"
                label-width="100px"
                :model="loginFormValue"
                style="max-width: 460px;"
                :rules="loginFormRules"
            >
                <el-form-item label="邮箱" prop="email">
                    <el-input placeholder="请输入邮箱" size="large" v-model="loginFormValue.email">
                        <template #prefix>
                            <el-icon class="el-input__icon" :size="20"><Message /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>

                <el-form-item label="密码" prop="password">
                    <el-input placeholder="请输入密码" size="large" type="password" v-model="loginFormValue.password">
                        <template #prefix>
                            <el-icon class="el-input__icon" :size="20"><Lock /></el-icon>
                        </template>
                    </el-input>
                </el-form-item> 

                <el-form-item prop="trim">
                    <el-checkbox v-model="loginFormValue.trim">同意本公司的</el-checkbox>
                    <el-button type="primary" text class="goRegisterBtn">
                        《条款与协议》
                    </el-button>
                </el-form-item> 

                <el-form-item>
                    <el-button style="width: 100%;" size="large" color="#F74800" type="success" :disabled="loginBtnDisabled" @click="loginBtn(loginRuleForm)">登录</el-button>

                    <el-button style="width: 100%;" text>忘记密码</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script setup>
    import {ref, onBeforeUnmount} from 'vue';
    import { Bell, Message, Lock } from '@element-plus/icons-vue';  //图标
    import { noteBaseRequest } from "@/request/note_request";
    import { useUserStore } from "@/stores/userStore.js"
    import {disabledBtn} from "@/utils/disabledBtn.js";
    import serverRequest from "@/request";
    import userApi from '@/request/api/userApi';

    // 自定义事件
    const emits = defineEmits(['changeStep','childCloseDialog']);

    //是否禁用登录按钮
    const loginBtnDisabled = ref(false);  

    // 用户共享的资源对象
    const userStore = useUserStore();
    const {setUserInfo} = userStore;

    // 显示登录框
    const loginVisible = ref(true);
    // 登录表单值
    const loginFormValue = ref({
        email: '',
        password: '',
        trim: false,
    });
    // 登录表单form
    const loginRuleForm = ref(null)
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
    // 登录表单验证规则
    const loginFormRules = {
        email:{
            required: true,
            trigger: 'blur',
            validator:emailInput
        },
        password:{
            required: true, 
            message: '请输入密码', 
            trigger: 'blur'
        },
        trim:{
            message: '请认真阅读本公司的条款与协议', 
            trigger: 'change',
            validator:(rule,value) => {
                return value  
            }
        }
        // [
        //     {
        //         required: true, 
        //         message: '请输入邮箱号码', 
        //         trigger: 'blur'
        //     },
        //     {
        //         message: '请输入正确邮箱格式', 
        //         trigger: 'blur',
        //         validator:(rule,value) =>{
        //             return 
        //         }
        //     }
        // ],    
    };

    // 登录按钮
    const loginBtn = async (formEl) => {
        if (!formEl) return

        await formEl.validate((valid) => {
            if (!valid) throw "表单验证失败";
        }); 

        //禁用登录按钮
        disabledBtn(loginBtnDisabled, true);

        // 获取请求api
        let API = {...userApi.loginEmailPassword};

        // 封装请求体中（data）的参数
        API.data = {
            email: loginFormValue.value.email,
            password: loginFormValue.value.password
        }

        // 发送请求
        await serverRequest(API).then(responseData => {
        
            if(!responseData) return;

            emits('childCloseDialog');

            const user = responseData.data.user;
            setUserInfo(
                responseData.data.userToken,
                user.id,
                user.email,
                user.nickname,
                user.headPic,
                user.level,
                user.time
            );
        })
        // .catch(error => {

        // })
        //解除禁用登录按钮
        disabledBtn(loginBtnDisabled, false, true, 2); 


        // formEl.validate( async (valid) => {
        //     if (valid) {
        //         // loginBtnDisabled.value = true;  
        //         //禁用登录按钮
        //         disabledBtn(loginBtnDisabled, true);
        //         const { data: responseData } = await noteBaseRequest.post(
        //             '/user/login/email/password',
        //             {
        //                 email: loginFormValue.value.email,
        //                 password: loginFormValue.value.password
        //             }
        //         ).catch(() => {
        //             ElMessage({
        //                 message: '发送登录请求失败',
        //                 type: 'error',
        //             })
        //             // setTimeout(() => {
        //             //     loginBtnDisabled.value = false;  //解除禁用登录按钮
        //             // },2000)
        //             disabledBtn(loginBtnDisabled, false, true, 2);
        //             throw '发送登录请求失败'
        //         })
        //         console.log(responseData)

        //         if(responseData.success){
        //             // localStorage.setItem('userToken',responseData.data.userToken);
        //             emits('childCloseDialog');
        //             const user = responseData.data.user;
        //             setUserInfo(
        //                 responseData.data.userToken,
        //                 user.id,
        //                 user.email,
        //                 user.nickname,
        //                 user.headPic,
        //                 user.level,
        //                 user.time
        //             );
        //             ElMessage({
        //                 message: responseData.message,
        //                 type: 'success'
        //             })
        //         }else{
        //             ElMessage({
        //                 message: responseData.message,
        //                 type: 'error'
        //             })
        //         }
        //         // setTimeout(() => {
        //         //     loginBtnDisabled.value = false;  //解除禁用登录按钮
        //         // },2000)
        //         disabledBtn(loginBtnDisabled, false, true, 2);
        //     } else {
        //         return false
        //     }
        // }); 
    };

    // 重置表单
    const resetForm = () => {
        loginRuleForm.value.resetFields();
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

    /deep/.el-checkbox__input.is-checked+.el-checkbox__label{
        color:#000;
    }
</style>