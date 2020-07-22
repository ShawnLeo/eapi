<template>
  <div class="search  main-content">
    <Card>
      <Row class="operation" :gutter="10">
        <!--<Button @click="add" type="primary" icon="md-add">添加子节点</Button>-->
        <!--<Button @click="addRoot" icon="md-add">添加一级节点</Button>-->
        <!--<Button @click="delAll" icon="md-trash">批量删除</Button>-->
        <i-col span="6">
          <Button icon="ios-arrow-back" @click="close" style="margin-right: 5px">返回</Button>
          <Button icon="md-refresh" @click="getParentList" type="primary">刷新</Button>
        </i-col>
        <!--<i-switch v-model="strict" size="large" style="margin-left:5px">-->
        <!--<span slot="open">级联</span>-->
        <!--<span slot="close">单选</span>-->
        <!--</i-switch>-->
        <i-col span="18" style="text-align: right;">
          <Button @click="submitEdit" :loading="submitLoading" type="primary"
                  icon="ios-create-outline" style="margin-right:5px">修改并保存
          </Button>
          <Button @click="handleReset">重置</Button>
        </i-col>

      </Row>
      <Row type="flex" justify="start" class="code-row-bg" :gutter="10">
        <i-col span="6">
          <Alert show-icon>
            当前选择编辑：
            <span class="select-title">{{editTitle}}</span>
            <a class="select-clear" v-if="form.data" @click="cancelEdit">取消选择</a>
          </Alert>
          <Input v-model="searchKey" suffix="ios-search" @on-change="search" placeholder="输入节点名搜索"
                 clearable/>
          <div class="custom-tree-bar" :style="{maxHeight: maxHeight}">
            <Tree ref="tree" :data="data" :render="renderContent" :load-data="loadData"
                  @on-check-change="changeSelect" :check-strictly="!strict"></Tree>
            <!--show-checkbox-->
          </div>
          <Spin size="large" fix v-if="loading"></Spin>
        </i-col>
        <i-col span="18">
          <Form ref="form" :model="form" :label-width="85" :rules="formValidate">
            <monaco-java :value="fileValue"></monaco-java>
          </Form>
        </i-col>
      </Row>
    </Card>

    <Modal :title="modalTitle" v-model="modalVisible" :mask-closable="false" :width="500">
      <monaco-java></monaco-java>
      <div slot="footer">
        <Button type="text" @click="cancelAdd">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="submitAdd">提交</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
  import {templateFile} from "../../utils/interface";

  import monacoJava from "../../components/monaco/monaco-java";
  import ICol from "iview/src/components/grid/col";

  export default {
    name: "custom-tree",
    components: {ICol, monacoJava},
    data() {
      return {
        loading: false, // 树加载状态
        maxHeight: "800px",
        strict: true,
        loadingEdit: false, // 编辑上级树加载状态
        modalVisible: false, // 添加显示
        selectList: [], // 多选数据
        selectCount: 0, // 多选计数
        showParent: false, // 显示上级标识
        modalTitle: "", // 添加标题
        editTitle: "", // 编辑节点名称
        searchKey: "", // 搜索树
        form: {
          // 编辑对象初始化数据
          id: "",
          parentId: "",
          parentTitle: "",
          sortOrder: 0,
          status: 0
        },
        formAdd: {
          // 添加对象初始化数据
          title: ""
        },
        formValidate: {
          // 表单验证规则
          title: [{required: true, message: "名称不能为空", trigger: "blur"}]
        },
        submitLoading: false,
        data: [],
        dataEdit: [],
        fileValue: ''
      };
    },
    methods: {
      init() {
        // 初始化一级节点
        this.getParentList();
        // 初始化一级节点为编辑上级节点使用
        this.getParentListEdit();
      },
      close() {
        this.$router.go(-1);
      },
      renderContent(h, {root, node, data}) {
        // 自定义render函数 这里主要通过数据中type字段判断 演示使用 当然你可以自定义业务逻辑
        if (data.type == "1") {
          return h(
              "span",
              {
                style: {
                  display: "inline-block",
                  cursor: "pointer"
                },
                on: {
                  click: () => {
                    this.selectTree(data);
                  }
                }
              },
              [
                h("span", [
                  h("Icon", {
                    props: {
                      type: "md-folder",
                      size: "16"
                    },
                    style: {
                      "margin-right": "8px",
                      "margin-bottom": "3px"
                    }
                  }),
                  h(
                      "span",
                      {
                        class: {
                          "ivu-tree-title": true,
                          "ivu-tree-title-selected": data.data === this.form.data
                        }
                      },
                      data.title
                  )
                ])
              ]
          );
        } else if (data.type == "2") {
          return h(
              "span",
              {
                style: {
                  display: "inline-block",
                  cursor: "pointer"
                },
                on: {
                  click: () => {
                    this.selectTree(data);
                  }
                }
              },
              [
                h("span", [
                  h("img", {
                    attrs: {
                      src: require("@/assets/img/icon/handlebars_icon.png"),
                      alt: "..."
                    },
                    style: {
                      cursor: "pointer",
                      width: "17px",
                      height: "17px",
                      "margin-right": "5px"
                    }
                  }),
                  h(
                      "span",
                      {
                        class: {
                          "ivu-tree-title": true,
                          "ivu-tree-title-selected": data.data == this.form.data
                        }
                      },
                      data.title
                  )
                ])
              ]
          );
        }
      },
      getParentList() {
        // this.loading = true;
        // this.getRequest("一级数据请求路径，如/tree/getByParentId/0").then(res => {
        //   this.loading = false;
        //   if (res.success == true) {
        //     res.result.forEach(function(e) {
        //       if (e.isParent) {
        //         e.loading = false;
        //         e.children = [];
        //       }
        //     });
        //     this.data = res.result;
        //   }
        // });
        // 模拟请求成功
        this.data = [{
          "children": [{
            "children": [{
              "data": "meimeitechSpring/libraries/spring-mvc/pom.mustache",
              "title": "pom.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-mvc/webApplication.mustache",
              "title": "webApplication.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-mvc/api_test.mustache",
              "title": "api_test.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-mvc/README.mustache",
              "title": "README.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-mvc/swaggerUiConfiguration.mustache",
              "title": "swaggerUiConfiguration.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-mvc/webMvcConfiguration.mustache",
              "title": "webMvcConfiguration.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-mvc/RFC3339DateFormat.mustache",
              "title": "RFC3339DateFormat.mustache",
              "type": 2
            }],
            "data": "meimeitechSpring/libraries/spring-mvc",
            "title": "spring-mvc",
            "type": 1
          }, {
            "children": [{
              "data": "meimeitechSpring/libraries/spring-cloud/formParams.mustache",
              "title": "formParams.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-cloud/apiKeyRequestInterceptor.mustache",
              "title": "apiKeyRequestInterceptor.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-cloud/pom.mustache",
              "title": "pom.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-cloud/apiClient.mustache",
              "title": "apiClient.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-cloud/README.mustache",
              "title": "README.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-cloud/api.mustache",
              "title": "api.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-cloud/clientConfiguration.mustache",
              "title": "clientConfiguration.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-cloud/jacksonConfiguration.mustache",
              "title": "jacksonConfiguration.mustache",
              "type": 2
            }],
            "data": "meimeitechSpring/libraries/spring-cloud",
            "title": "spring-cloud",
            "type": 1
          }, {
            "children": [{
              "data": "meimeitechSpring/libraries/spring-boot/headerParamsService.mustache",
              "title": "headerParamsService.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-boot/apiService.mustache",
              "title": "apiService.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-boot/apiController.mustache",
              "title": "apiController.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-boot/pom.mustache",
              "title": "pom.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-boot/pathParamsService.mustache",
              "title": "pathParamsService.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-boot/api_test.mustache",
              "title": "api_test.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-boot/swagger2SpringBoot.mustache",
              "title": "swagger2SpringBoot.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-boot/bodyParamsService.mustache",
              "title": "bodyParamsService.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-boot/homeController.mustache",
              "title": "homeController.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-boot/formParamsService.mustache",
              "title": "formParamsService.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-boot/README.mustache",
              "title": "README.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-boot/baseController.mustache",
              "title": "baseController.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-boot/RFC3339DateFormat.mustache",
              "title": "RFC3339DateFormat.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-boot/jacksonConfiguration.mustache",
              "title": "jacksonConfiguration.mustache",
              "type": 2
            }, {
              "data": "meimeitechSpring/libraries/spring-boot/queryParamsService.mustache",
              "title": "queryParamsService.mustache",
              "type": 2
            }],
            "data": "meimeitechSpring/libraries/spring-boot",
            "title": "spring-boot",
            "type": 1
          }],
          "data": "meimeitechSpring/libraries",
          "title": "libraries",
          "type": 1
        }, {
          "children": [{
            "data": "meimeitechSpring/project/build.properties",
            "title": "build.properties",
            "type": 2
          }, {
            "data": "meimeitechSpring/project/plugins.sbt",
            "title": "plugins.sbt",
            "type": 2
          }],
          "data": "meimeitechSpring/project",
          "title": "project",
          "type": 1
        }, {
          "data": "meimeitechSpring/apiException.mustache",
          "title": "apiException.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/beanValidationQueryParams.mustache",
          "title": "beanValidationQueryParams.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/xmlAnnotation.mustache",
          "title": "xmlAnnotation.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/formParams.mustache",
          "title": "formParams.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/model.mustache",
          "title": "model.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/apiController.mustache",
          "title": "apiController.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/bodyParams.mustache",
          "title": "bodyParams.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/optionalDataType.mustache",
          "title": "optionalDataType.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/generatedAnnotation.mustache",
          "title": "generatedAnnotation.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/swaggerDocumentationConfig.mustache",
          "title": "swaggerDocumentationConfig.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/customInstantDeserializer.mustache",
          "title": "customInstantDeserializer.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/implicitHeader.mustache",
          "title": "implicitHeader.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/pojo.mustache",
          "title": "pojo.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/headerParams.mustache",
          "title": "headerParams.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/apiOriginFilter.mustache",
          "title": "apiOriginFilter.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/apiResponseMessage.mustache",
          "title": "apiResponseMessage.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/apiDelegate.mustache",
          "title": "apiDelegate.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/pathParams.mustache",
          "title": "pathParams.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/queryParams.mustache",
          "title": "queryParams.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/enumOuterClass.mustache",
          "title": "enumOuterClass.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/returnTypes.mustache",
          "title": "returnTypes.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/exampleReturnTypes.mustache",
          "title": "exampleReturnTypes.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/api.mustache",
          "title": "api.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/beanValidationPathParams.mustache",
          "title": "beanValidationPathParams.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/notFoundException.mustache",
          "title": "notFoundException.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/beanValidationCore.mustache",
          "title": "beanValidationCore.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/enumClass.mustache",
          "title": "enumClass.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/typeInfoAnnotation.mustache",
          "title": "typeInfoAnnotation.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/application.properties",
          "title": "application.properties",
          "type": 2
        }, {
          "data": "meimeitechSpring/beanValidation.mustache",
          "title": "beanValidation.mustache",
          "type": 2
        }, {
          "data": "meimeitechSpring/application.mustache",
          "title": "application.mustache",
          "type": 2
        }];
      },
      getParentListEdit() {
        // this.loadingEdit = true;
        // this.getRequest("/tree/getByParentId/0").then(res => {
        //   this.loadingEdit = false;
        //   if (res.success == true) {
        //     res.result.forEach(function(e) {
        //       if (e.isParent) {
        //         e.loading = false;
        //         e.children = [];
        //       }
        //     });
        //     // 头部加入一级
        //     let first = {
        //       id: "0",
        //       title: "一级节点"
        //     };
        //     res.result.unshift(first);
        //     this.dataEdit = res.result;
        //   }
        // });
        // 模拟请求成功
        this.dataEdit = [
          {
            title: "自定义图标",
            id: "1",
            type: "1",
            icon: "logo-apple",
            color: "",
            parentId: "0",
            parentTitle: "一级节点",
            status: 0,
            loading: false,
            children: [
              {
                title: "带颜色的自定义图标",
                id: "2",
                type: "1",
                icon: "logo-android",
                color: "#97c03d",
                parentId: "1",
                status: 0,
                parentTitle: "自定义图标"
              }
            ]
          },
          {
            title: "自定义图片",
            id: "4",
            type: "2",
            parentId: "0",
            parentTitle: "一级节点",
            status: 0
          },
          {
            title: "自定义iView组件",
            id: "5",
            type: "3",
            word: "E",
            color: "orange",
            parentId: "0",
            parentTitle: "一级节点",
            status: -1,
            loading: false,
            children: [
              {
                title: "自定义iView组件",
                id: "2",
                type: "3",
                word: "字",
                color: "#c601c6",
                parentId: "1",
                status: 0,
                parentTitle: "自定义iView组件"
              }
            ]
          }
        ];
      },
      loadData(item, callback) {
        // 异步加载树子节点数据
        // this.getRequest("请求路径，如/tree/getByParentId/" + item.id).then(res => {
        //   if (res.success == true) {
        //     res.result.forEach(function(e) {
        //       if (e.isParent) {
        //         e.loading = false;
        //         e.children = [];
        //       }
        //     });
        //     callback(res.result);
        //   }
        // });
      },
      search() {
        // 搜索树
        if (this.searchKey) {
          // 模拟请求
          // this.loading = true;
          // this.getRequest("搜索请求路径", { title: this.searchKey }).then(res => {
          //   this.loading = false;
          //   if (res.success == true) {
          //     this.data = res.result;
          //   }
          // });
          // 模拟请求成功
          this.data = [
            {
              title: "这里需要请求后台接口",
              id: "1",
              parentId: "0",
              parentTitle: "一级节点",
              status: 0
            },
            {
              title: "所以这里是假数据",
              id: "4",
              parentId: "0",
              parentTitle: "一级节点",
              status: 0
            },
            {
              title: "我是被禁用的节点",
              id: "5",
              parentId: "0",
              parentTitle: "一级节点",
              status: -1
            }
          ];
        } else {
          // 为空重新加载
          this.getParentList();
        }
      },
      selectTree(v) {
        if (v && v.data != this.form.data) {
          // 转换null为""
          for (let attr in v) {
            if (v[attr] == null) {
              v[attr] = "";
            }
          }
          let str = JSON.stringify(v);
          let data = JSON.parse(str);
          this.form = data;
          this.editTitle = data.title;
          this.getFile(this.form);
        } else {
          this.cancelEdit();
        }
      },
      cancelEdit() {
        let data = this.$refs.tree.getSelectedNodes()[0];
        if (data) {
          data.selected = false;
        }
        this.$refs.form.resetFields();
        this.form.data = "";
        delete this.form.data;
        this.editTitle = "";
      },
      getFile(data) {
        if (data.type === 1) {
          return;
        }

        templateFile({"name": data.data}, (resp) => {
          this.fileValue = 'aaa';

          let reader = new FileReader();
          reader.readAsText(resp, 'utf-8');
          reader.onload = (e) => {
            // console.info();
            this.fileValue = e.target.result;
          }
        });
        // if (v.length > 0) {
        //   // 转换null为""
        //   for (let attr in v[0]) {
        //     if (v[0][attr] == null) {
        //       v[0][attr] = "";
        //     }
        //   }
        //   let str = JSON.stringify(v[0]);
        //   let data = JSON.parse(str);
        //   this.form.parentId = data.id;
        //   this.form.parentTitle = data.title;
        // }
      },
      cancelAdd() {
        this.modalVisible = false;
      },
      handleReset() {
        this.$refs.form.resetFields();
        this.form.status = 0;
      },
      submitEdit() {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (!this.form.id) {
              this.$Message.warning("请先点击选择要修改的节点");
              return;
            }
            this.submitLoading = true;
            // 避免传入null字符串
            // this.postRequest("请求路径，如/tree/edit", this.form).then(res => {
            //   this.submitLoading = false;
            //   if (res.success == true) {
            //     this.$Message.success("编辑成功");
            //     this.init();
            //     this.modalVisible = false;
            //   }
            // });
            // 模拟成功
            this.submitLoading = false;
            this.$Message.success("编辑成功");
            this.modalVisible = false;
          }
        });
      },
      submitAdd() {
        this.$refs.formAdd.validate(valid => {
          if (valid) {
            this.submitLoading = true;
            // this.postRequest("请求路径，如/tree/add", this.formAdd).then(res => {
            //   this.submitLoading = false;
            //   if (res.success == true) {
            //     this.$Message.success("添加成功");
            //     this.init();
            //     this.modalVisible = false;
            //   }
            // });
            // 模拟成功
            this.submitLoading = false;
            this.$Message.success("添加成功");
            this.modalVisible = false;
          }
        });
      },
      add() {
        if (this.form.id == "" || this.form.id == null) {
          this.$Message.warning("请先点击选择一个节点");
          return;
        }
        this.modalTitle = "添加子节点";
        this.showParent = true;
        this.formAdd = {
          parentId: this.form.id,
          sortOrder: 0,
          status: 0
        };
        this.modalVisible = true;
      },
      addRoot() {
        this.modalTitle = "添加一级节点";
        this.showParent = false;
        this.formAdd = {
          parentId: 0,
          sortOrder: 0,
          status: 0
        };
        this.modalVisible = true;
      },
      changeSelect(v) {
        this.selectCount = v.length;
        this.selectList = v;
      },
      delAll() {
        if (this.selectCount <= 0) {
          this.$Message.warning("您还未勾选要删除的数据");
          return;
        }
        this.$Modal.confirm({
          title: "确认删除",
          content: "您确认要删除所选的 " + this.selectCount + " 条数据?",
          onOk: () => {
            let ids = "";
            this.selectList.forEach(function (e) {
              ids += e.id + ",";
            });
            ids = ids.substring(0, ids.length - 1);
            // this.deleteRequest("请求路径，如/tree/delByIds/" + ids).then(res => {
            //   if (res.success == true) {
            //     this.$Message.success("删除成功");
            //     this.selectList = [];
            //     this.selectCount = 0;
            //     this.cancelEdit();
            //     this.init();
            //   }
            // });
            // 模拟成功
            this.$Message.success("删除成功");
            this.selectList = [];
            this.selectCount = 0;
            this.cancelEdit();
          }
        });
      }
    },
    mounted() {
      // 计算高度
      let height = document.documentElement.clientHeight;
      this.maxHeight = Number(height - 287) + "px";
      this.init();
    }
  };
</script>

<style lang="less">
  .search {
    .operation {
      margin-bottom: 2vh;
    }

    .select-count {
      font-size: 13px;
      font-weight: 600;
      color: #40a9ff;
    }

    .select-title {
      font-size: 12px;
      font-weight: 600;
      color: #40a9ff;
    }

    .select-clear {
      margin-left: 10px;
    }

    .page {
      margin-top: 2vh;
    }

    .drop-down {
      font-size: 13px;
      margin-left: 5px;
    }
  }

  .custom-tree-bar {
    overflow: auto;
    margin-top: 5px;
  }

  .custom-tree-bar::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }

  .custom-tree-bar::-webkit-scrollbar-thumb {
    border-radius: 4px;
    -webkit-box-shadow: inset 0 0 2px #d1d1d1;
    background: #e4e4e4;
  }

  .ivu-tree .ivu-tree-empty {
    margin-top: 10px;
    font-size: 12px;
  }
</style>