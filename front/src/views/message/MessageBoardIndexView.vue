<template>
    <InfoContent style="width:700px">
        <div class=" message-board">
            <el-card>
                <template #header>
                    <div class="card-header">
                        <span>留言板</span>
                    </div>
                </template>
                <el-scrollbar height="400px">
                    <div v-for="(message, index) in messages" :key="index" class="message">
                        <p>{{ message.username }}说:{{ message.comment }}</p>
                        <el-divider />
                    </div>
                </el-scrollbar>
            </el-card>
            <div style="margin-bottom:25px;"></div>
            <div class="message-input">
                <el-input v-model="newMessage" placeholder="请输入留言" style="margin-bottom:25px;"></el-input>
                <el-button type="primary" style="float:right" @click="addMessage">发布留言</el-button>
            </div>
        </div>

    </InfoContent>
</template>

<script>
import { ref } from 'vue';
import $ from 'jquery'
import { useStore } from "vuex";
import InfoContent from "@/components/InfoContent";
export default {
    components: {
        InfoContent,
    },
    setup() {
        const store = useStore();
        const newMessage = ref('');
        const messages = ref([]);

        const addMessage = () => {
            // if (newMessage.value.trim() !== '') {
            //     messages.value.push(newMessage.value);
            //     newMessage.value = '';
            // }

            $.ajax({
                url: "http://127.0.0.1:3000/api/comment/addComment/",
                type: "post",
                data: {
                    user_id: store.state.user.id,
                    comment: newMessage.value,
                    username: store.state.user.username,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    newMessage.value = "";
                    refresh_comment();
                    console.log(resp);
                },
                error(resp) {
                    console.log(resp);
                }
            });
        };

        const refresh_comment = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/api/comment/getCommentList/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    messages.value = resp;
                    console.log(resp);
                },
                error(resp) {
                    console.log(resp);
                }
            });


        };
        refresh_comment();
        return {
            newMessage,
            messages,
            refresh_comment,
            addMessage,
        }
    }
}

</script>

<style scoped>
.message-board {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
}

.message-input {
    margin-bottom: 20px;
}

.message-list .message {
    border: 1px solid #ccc;
    border-radius: 5px;
    padding: 10px;
    margin-bottom: 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
</style>
