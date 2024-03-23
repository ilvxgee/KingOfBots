<template>
    <div class="top" />
    <ContentField v-if="!$store.state.user.pulling_info">
        <el-form label-width="auto" style="max-width: 600px">
            <el-form-item label="用户名">
                <el-input v-model="username" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item label="密码">
                <el-input type="password" show-password v-model="password" placeholder="请输入密码" />
            </el-form-item>
            <div class="error-message" style="height: 12px;  line-height: 0.1;text-align: center;">{{ error_message }}
            </div>
            <div class="container">
                <el-button type="primary" @click.prevent="login" style="width: 200px; ">登录</el-button>
            </div>
        </el-form>


    </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue"
import { ref } from 'vue'
import { useStore } from "vuex"
import router from '@/router/index'

export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        let username = ref('');
        let password = ref('');
        let error_message = ref('');
        // let show_content = ref(false);

        const jwt_token = localStorage.getItem("jwt_token");
        //jwt_token为真时，说明正在获取信息，需要设置pulling_info为true
        if (jwt_token) {
            store.commit("updatePullingInfo", true);
            store.commit("updateToken", jwt_token);
            store.dispatch("getinfo", {
                success() {
                    router.push({ name: "home" })
                    store.commit("updatePullingInfo", false)
                },
                error() {
                    store.commit("updatePullingInfo", false)
                }
            })
        } else {
            store.commit("updatePullingInfo", false)
        }

        const login = () => {
            error_message.value = "";

            store.dispatch("login", {
                username: username.value,
                password: password.value,

                success(resp) {

                    store.dispatch("getinfo", {
                        success() {
                            router.push({ name: "home" });
                            console.log(store.state.user);
                        }
                    })

                    console.log(resp);
                },
                error() {
                    error_message.value = '用户名或密码错误';
                }

            })

        }


        return {
            username,
            password,
            error_message,
            login,
            // show_content


        }
    }
}
</script>

<style scoped>
.top {
    margin-top: 10%;
}

.container {
    display: flex;
    justify-content: center;
    align-items: center;
}

.error-message {
    color: red;
}
</style>