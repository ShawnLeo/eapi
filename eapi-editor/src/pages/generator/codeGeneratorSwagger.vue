<template>
    <div class="search">
        <br>
        <Card style="min-height: 400px">
            <Row>
                <Button @click="init" type="primary" icon="md-refresh" style="margin-left: 5px;">刷新</Button>
            </Row>
            <Row>
                <br>
                <Table
                        border
                        :columns="columnDef"
                        :data="projects"
                ></Table>
            </Row>

        </Card>
    </div>
</template>

<script>
    import {getProjectList, generatorSwaggerGen, generatorSwaggerDownload} from '../../utils/interface';
    import {setStore, getStore} from "../../utils/storage";
    import {Message} from 'iview';
    export default {
        name: "codeGeneratorSwagger",
        data () {
            return {
                projects: [],
                columnDef: [

                    {
                        type: "index",
                        width: 60,
                        align: "center"
                    },
                    {
                        title: "id",
                        key: "id"
                    },
                    {
                        title: "title",
                        key: "title"
                    },
                    {
                        title: "操作",
                        key: "action",
                        align: "center",
                        render: (h, params) => {
                            return h("div", [
                                h(
                                    "Button",
                                    {
                                        props: {
                                            type: "primary",
                                            size: "small",
                                            icon: "ios-create-outline"
                                        },
                                        style: {
                                            marginRight: "5px"
                                        },
                                        on: {
                                            click: () => {
                                                this.gen(params.row);
                                            }
                                        }
                                    },
                                    "生成"
                                ),

                            ]);
                        }
                    }
                ]
            };
        },

        methods: {
            download (data) {
                if (!data) {
                    return;
                }
                let url = window.URL.createObjectURL(new Blob([data]));
                let link = document.createElement('a');
                link.style.display = 'none';
                link.href = url;
                link.setAttribute('download', 'SwaggerCodeGen.zip');
                document.body.appendChild(link);
                link.click();
            },
            setProjects(data){
                this.projects = data;
            },
            gen(row){
                const db = getStore("generator_db");
                if (!db) {
                    Message.error("请配置数据库信息");
                } else {
                    let model = JSON.parse(db);
                    let request = {};
                    request.targetPackage = model.targetPackage;
                    request.targetProject = row.id;
                    let download = this.download;
                    generatorSwaggerGen(request, function (data) {
                        if (data.header.code == '0') {
                            Message.success("生成成功");
                            generatorSwaggerDownload({uuid: data.body}, (response) => {
                                download(response);
                            });
                        } else {
                            Message.error("生成失败");
                        }
                    });
                }
            },
            init() {
                let setProjects = this.setProjects;
                getProjectList({groupId: 'all'}, (response) => {
                    if (response.header.code == '0') {
                        setProjects(response.body);
                        Message.success("数据获取成功");
                    } else {
                        Message.error("数据获取失败");
                    }
                });
            }
        },
        mounted(){
            this.init();
        }
    };

</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>

