<template>
    <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false" :router="true">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <el-menu-item>KingOfBots</el-menu-item>
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/pk/">对战</el-menu-item>
        <el-menu-item index="/record/">对局列表</el-menu-item>
        <el-menu-item index="/ranklist/">排行榜</el-menu-item>

        <div class="flex-grow" />
        <el-sub-menu index="6" class="el-sub-menu" v-if="$store.state.user.is_login">
            <template #title>{{ $store.state.user.username }}</template>
            <el-menu-item index="/user/bot/">我的bot</el-menu-item>
            <el-menu-item index="" @click="logout">退出</el-menu-item>
        </el-sub-menu>
        <template v-else-if="!$store.state.user.is_login && !$store.state.user.pulling_info">
            <el-menu-item index="/user/account/login/">登录</el-menu-item>
            <el-menu-item index="/user/account/register/">注册</el-menu-item>
        </template>

        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </el-menu>
</template>

<script>
import { useStore } from 'vuex';
export default {
    //由于刷新会导致pulling_info初始化为true，但是我们在注册页面并没有获取用户信息，所以需要设置为flase
    setup() {
        const store = useStore();
        store.commit("updatePullingInfo", false);   // 第二句
        const logout = () => {
            store.dispatch("logout");
        }

        return {
            logout,
        }
    }
}
</script>

<style scoped>
.flex-grow {
    flex-grow: 1;
}
</style>