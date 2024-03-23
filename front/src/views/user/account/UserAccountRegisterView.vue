<template>
    <div class="top" />
    <ContentField>
        <el-form label-width="auto" style="max-width: 600px">
            <el-form-item label="用户名">
                <el-input v-model="username" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item label="密码">
                <el-input type="password" show-password v-model="password" placeholder="请输入密码" />
            </el-form-item>
            <el-form-item label="确认密码">
                <el-input type="password" show-password v-model="confirmedPassword" placeholder="请再次输入密码" />
            </el-form-item>
            <div class="error-message" style="height: 12px;  line-height: 0.1;text-align: center;">{{ error_message }}
            </div>
            <div class="container">
                <el-button type="primary" @click.prevent="register" style="width: 200px; ">登录</el-button>
            </div>
        </el-form>
    </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue"
import { ref } from "vue";
import $ from 'jquery'
import router from "@/router";

export default {
    components: {
        ContentField
    },
    setup() {
        let username = ref('');
        let password = ref('');
        let confirmedPassword = ref('');
        let error_message = ref('');


        const register = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/register/",
                type: "post",
                data: {
                    username: username.value,
                    password: password.value,
                    confirmedPassword: confirmedPassword.value,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        router.push({ name: "user_account_login" })
                    } else {
                        error_message.value = resp.error_message;
                    }
                    console.log(resp);
                },


            });

        }

        return {
            username,
            password,
            confirmedPassword,
            error_message,
            register
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
