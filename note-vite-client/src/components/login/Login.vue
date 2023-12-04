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
                    <el-checkbox v-model="loginFormValue.trim" style="color: #000;">同意本公司的</el-checkbox>
                    <el-button type="primary" text class="goRegisterBtn">
                        《条款与协议》
                    </el-button>
                </el-form-item> 

                <el-form-item>
                    <el-button style="width: 100%;" size="large" color="#F74800" type="success" @click="loginBtn(loginRuleForm)">登录</el-button>

                    <el-button style="width: 100%;" text>忘记密码</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script setup>
    import {ref, onBeforeUnmount} from 'vue';
    import { Bell, Message, Lock } from '@element-plus/icons-vue';  //图标

    // 自定义事件
    const emits = defineEmits(['changeStep']);

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
    const loginBtn = (formEl) => {
        if (!formEl) return
        formEl.validate((valid) => {
            console.log(valid);
            if (valid) {
                console.log('submit!')
            } else {
                console.log('error submit!')
                return false
            }
        }); 
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
</style>