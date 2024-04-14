<template>
    <div class="container">
        <ContentField>

            <el-card shadow="always">
                <h3>KingOfBots 是什么？<small>网站功能</small></h3>
                <p>KingOfBots 是在线的程序对抗平台，AI 可以根据已有的游戏规则进行比赛一决胜负。同时支持人机，人人对战</p>
                <el-divider />
                <h3>我不知道怎么用 KingOfBots 啊？<small>使用方法</small></h3>
                <ol>
                    <li>注册 KingOfBots 账户</li>
                    <li>
                        点击
                        <el-link type="primary" href="https://github.com/ilvxgee/KingOfBots" target="_blank"
                            class="card-link">游戏规则</el-link>
                        查看Snake游戏规则,按照游戏说明写自己的 AI 程序
                    </li>
                    <!--      <li>找一个你想参加的游戏，按照游戏说明写自己的 AI 程序（目前只有一个游戏）</li>-->
                    <li>点击右上角账户名，进入我的 Bot 页面，创建 Bot 并上传程序</li>
                    <li>回到对战页面，点击开始匹配，与他人或其他 Bot 开始对战</li>
                </ol>
            </el-card>
            <p>
                欲查规则？点击
                <el-link type="primary" href="https://github.com/ilvxgee/KingOfBots" target="_blank"
                    class="card-link">游戏规则</el-link>
                进入游戏一览。
            </p>
            <p>
                参加比赛？点击对战进入小组一览。
            </p>
            <p>
                提交程序？点击顶栏按钮管理你的 Bot。
            </p>
        </ContentField>
    </div>
</template>
<script>
import ContentField from "@/components/ContentField.vue"
import router from "@/router";
import { useStore } from "vuex";
export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        const jwt_token = localStorage.getItem("jwt_token");
        if (jwt_token) {
            store.commit("updateToken", jwt_token);
            store.dispatch("getinfo", {
                success() {
                    router.push({ name: "home" });
                    store.commit("updatePullingInfo", false);
                },
                error() {
                    store.commit("updatePullingInfo", false);
                }
            })
        } else {
            store.commit("updatePullingInfo", false);
        }
    }
}
</script>
<style scoped>
.container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 80vh;
}
</style>