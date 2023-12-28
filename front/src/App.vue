<template>
  <NavBar />
  <router-view />
</template>

<script>
import NavBar from "@/components/NavBar.vue"
//export default 相当于创建一个vue实例
//setup 是将里面的变量函数中返回的对象会暴露给模板和组件实例
export default {
  components: {
    NavBar,
  }

}
</script>

<style>
/* .container{
  display: flex;
} */
body {
  background-image: url("@/assets/images/background.jpg");
  background-size: cover;
  margin: 0;
  padding: 0
}
</style>
