<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
    <ResultBoard v-if="$store.state.pk.loser !== 'none'" />
</template>
<script>
import PlayGround from "@/components/PlayGround.vue"
import MatchGround from "@/components/MatchGround.vue"
import ResultBoard from "@/components/ResultBoard.vue"
import { onMounted, onUnmounted } from "vue";
import { useStore } from "vuex";
import { ElNotification } from 'element-plus'

export default {
    components: {
        PlayGround,
        MatchGround,
        ResultBoard,
    },

    setup() {
        const store = useStore();
        // const socketUrl = `wss://app4306.acapp.acwing.com.cn/websocket/${store.state.user.token}/`
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`
        let scoket = null;
        store.commit("updateLoser", "none");
        onMounted(() => {

            store.commit("updateOpponent", {
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
            })
            scoket = new WebSocket(socketUrl);
            scoket.onopen = () => {
                console.log("connected!");
                store.commit("updateSocket", scoket);

            }
            scoket.onmessage = msg => {
                const data = JSON.parse(msg.data);

                if (data.event === "start-matching") {//表示匹配成功
                    store.commit("updateOpponent", {
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    });

                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 200);
                    userNotice();

                    store.commit("updateGamemap", data.game);

                } else if (data.event === "move") {
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);
                }
                else if (data.event === "result") {
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    if (data.loser === "all" || data.loser === "A") {
                        snake0.status = "die";
                    } if (data.loser === "all" || data.loser === "B") {
                        snake1.status = "die";
                    }
                    store.commit("updateLoser", data.loser);

                }

                console.log(data);
            }

            scoket.onclose = () => {
                console.log("disconnected!");
            }
        });
        onUnmounted(() => {
            scoket.close();
            store.commit("updateStatus", "matching");
        });

        const userNotice = () => {
            let player_camp = (store.state.pk.a_id === parseInt(store.state.user.id)) ? "蓝" : "红";
            let text_color = "";
            if (player_camp === '蓝') text_color = "#0066FF";
            else if (player_camp === '红') text_color = "#E63F00";
            console.log(store.state.pk.a_id);
            console.log(store.state.user.id);
            ElNotification({
                title: '游戏开始',
                message: ("hi", { style: 'color: ' + text_color }, '你出生在' + player_camp + "色方"),
                duration: 5000,
            })
        }

    }


}
</script>
<style scoped></style>