<template>
    <div class="container">
        <div class="common-layout">
            <el-container>
                <el-aside width="200px">
                    <div style="height: 4%;"></div>
                    <el-card style="max-width: 600px" class="common-layout">
                        <img :src="$store.state.user.photo" style="width: 100%" />
                    </el-card>
                </el-aside>
                <el-main>
                    <el-card style="max-width: 800px">
                        <template #header>
                            <div class="card-header">
                                <span>我的bot</span>
                                <span style="float: right;"><el-button type="primary"
                                        @click="dialogTableVisible = true">创建Bot</el-button></span>
                            </div>
                        </template>
                        <el-table :data="bots" height="350" style="width: 100%">
                            <el-table-column prop="title" label="名称" width="180" />
                            <el-table-column prop="createtime" label="创建时间" width="300" />
                            <el-table-column label="操作">
                                <template #default="scope">
                                    <el-button size="small" type="success"
                                        @click="botsbefore = scope.row; startEdit(scope.row)">修改</el-button>
                                </template>
                            </el-table-column>
                            <el-table-column>
                                <template #default="scope">
                                    <el-button size="small" type="danger"
                                        @click="remove_bot(scope.row.id)">删除</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-card>
                </el-main>
            </el-container>
        </div>
    </div>


    <el-dialog v-model="dialogTableVisible" title="创建Bot" width="800">
        <el-form label-position="top">
            <el-form-item label="名称">
                <el-input v-model="botadd.title" placeholder="请输入名称"></el-input>
            </el-form-item>
            <el-form-item label="简介">
                <el-input v-model="botadd.description" placeholder="请输入简介"></el-input>
            </el-form-item>
            <span>代码</span>

            <!-- <el-input v-model="botadd.content" :autosize="{ minRows: 10 }" type="textarea"
                    placeholder="请输入代码"></el-input> -->
            <VAceEditor v-model:value="botadd.content" @init="editorInit" lang="c_cpp" :theme="aceConfig.theme"
                style="height: 300px" :options="aceConfig.options" class="ace-editor" />
        </el-form>
        <el-divider />
        <template #footer>
            <div class="dialog-footer">
                <el-button id="cancelButton_1" @click="dialogTableVisible = false">取消</el-button>
                <el-button type="primary" @click="add_bot()">
                    创建
                </el-button>
            </div>
        </template>
    </el-dialog>

    <el-dialog v-model="dialogTableVisible_update" title="修改Bot" width="800">
        <el-form label-position="top">
            <el-form-item label="名称">
                <el-input v-model="botsbefore.title" placeholder="请输入名称"></el-input>
            </el-form-item>
            <el-form-item label="简介">
                <el-input v-model="botsbefore.description" placeholder="请输入简介"></el-input>
            </el-form-item>
            <span>代码</span>
            <!-- <el-input v-model="botsbefore.content" :autosize="{ minRows: 10 }" type="textarea"
                    placeholder="请输入代码"></el-input> -->
            <VAceEditor v-model:value="botsbefore.content" @init="editorInit" lang="c_cpp" :theme="aceConfig.theme"
                style="height: 300px" :options="aceConfig.options" class="ace-editor" />
        </el-form>
        <el-divider />
        <template #footer>
            <div class="dialog-footer">
                <el-button id="cancelButton_2" @click="handleCancelEditAndRefresh">取消</el-button>
                <el-button type="primary" @click="update_bot(botsbefore)">
                    保存修改
                </el-button>
            </div>
        </template>
    </el-dialog>

</template>
<script>

import { ref, reactive, nextTick } from 'vue'
import $ from 'jquery'
import { useStore } from "vuex";
import { ElMessage } from 'element-plus'
import { VAceEditor } from 'vue3-ace-editor';
// 加了这个【import "ace-builds/webpack-resolver";】可能会报错
//（若报错 则需要安装node.js的一个包 就是主题）
// 命令：npm install --save-dev file-loader
import "ace-builds/webpack-resolver";
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/theme-chrome';
import 'ace-builds/src-noconflict/ext-language_tools';

export default {

    components: {
        VAceEditor,
    },

    setup() {

        const aceConfig = reactive({
            theme: 'chrome', //主题
            arr: [
                /*所有主题*/
                "ambiance",
                "chaos",
                "chrome",
                "clouds",
                "clouds_midnight",
                "cobalt",
                "crimson_editor",
                "dawn",
                "dracula",
                "dreamweaver",
                "eclipse",
                "github",
                "gob",
                "gruvbox",
                "idle_fingers",
                "iplastic",
                "katzenmilch",
                "kr_theme",
                "kuroir",
                "merbivore",
                "merbivore_soft",
                "monokai",
                "mono_industrial",
                "pastel_on_dark",
                "solarized_dark",
                "solarized_light",
                "sqlserver",
                "terminal",
                "textmate",
                "tomorrow",
                "tomorrow_night",
                "tomorrow_night_blue",
                "tomorrow_night_bright",
                "tomorrow_night_eighties",
                "twilight",
                "vibrant_ink",
                "xcode",
            ],
            readOnly: false, //是否只读
            options: {
                enableBasicAutocompletion: true,
                enableSnippets: true,
                enableLiveAutocompletion: true,
                tabSize: 2,
                showPrintMargin: false,
                fontSize: 16
            }
        });



        const dialogTableVisible = ref(false);
        const dialogTableVisible_update = ref(false);

        const store = useStore();
        let bots = ref([]);
        const botadd = reactive({
            title: "",
            description: "",
            content: "",
            error_message: "",
        })

        //修改前
        let botsbefore = ref([]);


        const handleCancelEditAndRefresh = async () => {
            await cancelEdit(); // 等待取消编辑操作完成
            refresh_bots(); // 取消编辑完成后执行刷新操作
        };






        let backupData = reactive({}); // 新增一个备份数据的变量

        const editBot = reactive({
            id: "",
            title: "",
            description: "",
            content: "",
            error_message: "",
        });

        const startEdit = (bot) => {
            // 备份修改前的数据
            backupData = Object.assign({}, bot);
            // 将当前机器人的数据复制到编辑表单中
            Object.assign(editBot, bot);
            // 显示编辑对话框
            dialogTableVisible_update.value = true;
        };

        const cancelEdit = () => {
            // 将数据还原为修改前的状态
            Object.assign(botsbefore, backupData);
            // 关闭编辑对话框
            nextTick(() => {
                dialogTableVisible_update.value = false;
            });
        };


        const refresh_bots = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/api/user/bot/getList/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                },
                error(resp) {
                    console.log(resp);
                }
            });

        }

        refresh_bots();

        const add_bot = () => {
            botadd.error_message = "";
            $.ajax({
                url: "http://127.0.0.1:3000/api/user/bot/add/",
                type: "post",
                data: {
                    title: botadd.title,
                    description: botadd.description,
                    content: botadd.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        botadd.title = "";
                        botadd.description = "";
                        botadd.content = "";
                        $("#cancelButton_1").trigger('click');
                        refresh_bots();
                    } else {
                        // dialogTableVisible = true;
                        botadd.error_message = resp.error_message;
                        ElMessage({
                            message: resp.error_message,
                            type: 'error'
                        })
                    }
                    console.log(resp);
                },
                error(resp) {
                    console.log(resp);
                }
            })

        }


        const remove_bot = (id) => {
            $.ajax({
                url: "http://127.0.0.1:3000/api/user/bot/remove/",
                type: "post",
                data: {
                    bot_id: id,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        console.log(resp);
                        refresh_bots();
                    }
                },
                error(resp) {
                    console.log(resp);
                }
            });

        }



        const update_bot = (row) => {
            botadd.error_message = "";
            $.ajax({
                url: "http://127.0.0.1:3000/api/user/bot/update/",
                type: "post",
                data: {
                    bot_id: row.id,
                    title: row.title,
                    description: row.description,
                    content: row.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        $("#cancelButton_2").trigger('click');
                        refresh_bots();
                    } else {
                        botadd.error_message = resp.error_message;
                        ElMessage({
                            message: resp.error_message,
                            type: 'error'
                        })
                    }
                    console.log(resp);
                },
                error(resp) {
                    console.log(resp);
                }
            })

        }


        return {
            bots,
            dialogTableVisible,
            dialogTableVisible_update,
            botadd,
            add_bot,
            remove_bot,
            update_bot,
            refresh_bots,
            botsbefore,
            cancelEdit,
            startEdit,
            handleCancelEditAndRefresh,
            aceConfig,




        }

    }
}
</script>
<style scoped>
.container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 70vh;
}
</style>