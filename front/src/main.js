import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

createApp(App).use(store).use(router).use(ElementPlus).mount('#app')
// app.vue写在script里面  main.js写在app挂在完之后

// 解决ERROR ResizeObserver loop completed with undelivered notifications.

//报错解决：ResizeObserver loop completed with undelivered notifications
const debounce = (fn, delay) => {

    let timer = null;

    return function () {

        let context = this;

        let args = arguments;

        clearTimeout(timer);

        timer = setTimeout(function () {

            fn.apply(context, args);

        }, delay);

    }

}

// 解决ERROR ResizeObserver loop completed with undelivered notifications.

//问题的

const _ResizeObserver = window.ResizeObserver;

window.ResizeObserver = class ResizeObserver extends _ResizeObserver {

    constructor(callback) {

        callback = debounce(callback, 16);

        super(callback);

    }

}