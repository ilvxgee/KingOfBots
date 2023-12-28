<template>
    <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false" :router="true">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <el-menu-item>KingOfBots</el-menu-item>
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/pk/">对战</el-menu-item>
        <el-menu-item index="/record/">对局列表</el-menu-item>
        <el-menu-item index="/ranklist/">排行榜</el-menu-item>

        <div class="flex-grow" />
        <el-sub-menu index="6" class="el-sub-menu">
            <template #title>ilvxgee</template>
            <el-menu-item index="/user/bot/">我的bot</el-menu-item>
            <el-menu-item index="#">退出</el-menu-item>
        </el-sub-menu>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

    </el-menu>
</template>
<script>

</script>
<style scoped>
.flex-grow {
    flex-grow: 1;
}
</style>