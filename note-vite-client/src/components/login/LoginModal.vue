<template>
    <div>
        <el-dialog v-model="showLoginModal" :show-close="true" width="450" :close-on-press-escape="false"
            :close-on-click-modal="false" style="padding: 0;">

            <!-- <el-icon size="32" color="#fff" class="dialog-close" @click="changeLoginModalShowStatus(false)"> -->
            <el-icon v-if="loginModalStep != 3" size="32" color="#fff" class="dialog-close" @click="dialogClose()">
                <Close />
            </el-icon>

            <!-- vue.js内置组件动画效果 -->
            <Transition name="bounce" mode="out-in">
                <component ref="childRef" :is="showLoginModelCard" @changeStep="changeLoginModelStep" @childCloseDialog="dialogClose"/>
            </Transition>
            
            <!-- 登录对话框 -->
                <!-- <Login v-if="loginModalStep === 1"></Login> -->
                <!-- 注册对话框 -->
                <!-- <Register v-else-if="loginModalStep === 2"></Register> -->
                <!-- 注册成功对话框 -->
                <!-- <successRegister v-else></successRegister> -->
        </el-dialog>
    </div>
</template>

<script setup>

    import { computed, ref } from 'vue';
    import { Bell, Message, Lock, Close } from '@element-plus/icons-vue';  //图标
    // import Login from './Login.vue';
    import Login from '@/components/login/Login.vue';
    import Register from '@/components/login/Register.vue';
    import successRegister from '@/components/login/SuccessRegister.vue'

    import {storeToRefs} from 'pinia';
    import { userLoginModalStore } from '@/stores/LoginModalStore.js';

    // 登录模态框共享资源的对象
    const loginModalStore = userLoginModalStore();

    // 是否显示登录对话框
    const {showLoginModal} = storeToRefs(loginModalStore);
    // const showLoginModal = ref(false);
    // 改变登录模态框显示的状态
    const {changeLoginModalShowStatus} = loginModalStore;  //方法

    // 登录模态框显示的内容（1：登录，2：注册，3：注册成功）
    const loginModalStep = ref(1);

    // 登录模态框显示的卡片
    const showLoginModelCard = computed(() => {
        switch (loginModalStep.value) {
            case 1:
                return Login;
            case 2:
                return Register;
            case 3:
                return successRegister;
        }
    });

    // 改变登录模态框显示的卡片
    const changeLoginModelStep = step => {
        loginModalStep.value = step;
    };

    // 关闭模态框  --调用子组件方法重置表单
    const childRef = ref();
    const dialogClose = () => {
        childRef.value.resetForm();
        changeLoginModalShowStatus(false);
        loginModalStep.value = 1;
    };

</script>

<style lang="less" scoped>
    // 登录模态框padding设置
	/deep/.el-dialog__body {
        padding: 0;
    }
    // 登录模态框卡片边框设置
    /deep/.el-card__header,
    /deep/.el-card {
        border: none;
    }
    // 登录模态框卡片阴影设置
    /deep/.el-card.is-always-shadow {
        box-shadow: none;
    }

    /deep/.el-dialog__header {
        //model头部隐藏
        display: none;
    }
    // 关闭模态框图标
    .dialog-close {
        position: absolute;
        top: -80px;
        right: -100px;
        cursor: pointer;
    }


    // vue.js内置组件动画效果
    .bounce-enter-active {
        animation: bounce-in 0.5s;
    }
    .bounce-leave-active {
        animation: bounce-in 0.5s reverse;
    }
    @keyframes bounce-in {
        0% {
            transform: scale(0);
        }

        50% {
            transform: scale(1.25);
        }

        100% {
            transform: scale(1);
        }
    }

</style>