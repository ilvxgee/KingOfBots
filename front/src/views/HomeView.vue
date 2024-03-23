<template>
    <ContentField>首页</ContentField>
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
<style scoped></style>