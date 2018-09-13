<template>
  <div class="project-interface">
    <Form ref="formInline" inline class="project-interface-form">
      <FormItem>
        <!--<Button type="primary" @click="handleSubmit('formInline')">Signin</Button>-->
        <Button type="primary" icon="edit" @click="newInterface">新建接口</Button>
      </FormItem>
      <FormItem prop="search">
        <Input type="text" placeholder="搜索"/>
      </FormItem>
    </Form>
    <div class="clearfix"></div>
    <Form ref="formInline" inline class="project-interface-form form-in-table" v-show="showEditMenus">
      <Button @click="deleteInterface">删除</Button>
      <Button>设置状态</Button>
      <Button>设置标签</Button>
    </Form>
    <Table  stripe ref="selection" :columns="columns" :loading="loading"
      :data="data" @on-selection-change="onCelectionChange"></Table>
    <Modal v-model="addInterfaceModal" title="新建接口" width="700">
        <Form ref="interfaceItem" :model="interfaceItem" :label-width=80 :rules="ruleValidate">
            <FormItem label="接口名称" prop="name">
                <i-input v-model="interfaceItem.name" placeholder="最多20个中文或者40个英文字符"></i-input>
            </FormItem>
            <FormItem label="接口路径" prop="method"  style="width: 30%;float:left;">
              <Select v-model="interfaceItem.method" require="true">
                  <Option value="get">GET</Option>
                  <Option value="post">POST</Option>
                  <Option value="put">PUT</Option>
                  <Option value="delete">DELETE</Option>
              </Select>
            </FormItem>
            <FormItem prop="path" style="width:70%;float:left;" :label-width=-1>
                <i-input v-model="interfaceItem.path" placeholder="例如：/api/get/{id}"></i-input>
            </FormItem>
            <div class="clearfix"></div>
            <FormItem label="标签" style="width:50%;float:left;" prop="tagIds">
              <Select v-model="interfaceItem.tagIds" multiple require="true">
                  <Option v-for="(tag, index) in tags" :value="tag.id" :key="index">{{tag.name}}</Option>
              </Select>
            </FormItem>
            <FormItem label="代码映射" style="width:50%;float:left;" prop="operationId">
              <i-input v-model="interfaceItem.operationId" placeholder="例如：findByUserId"></i-input>
            </FormItem>
            <div class="clearfix"></div>
            <FormItem label="摘要" prop="summary">
                <i-input v-model="interfaceItem.summary" placeholder="最多20个中文或者40个英文字符"></i-input>
            </FormItem>
            <FormItem label="描述" prop="description">
                <i-input v-model="interfaceItem.description" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="描述"></i-input>
            </FormItem>
            <FormItem label="接口状态" prop="deprecated">
                <i-switch v-model="interfaceItem.deprecated" size="large">
                    <span slot="open">On</span>
                    <span slot="close">Off</span>
                </i-switch>
                <span style="color:#FF0000">*关闭表示接口已废弃</span>
            </FormItem>
        </Form>
        <div slot="footer">
          <Button type="text" size="large" @click="reset">重置</Button>
          <Button type="primary" size="large" @click="addInterface">确定</Button>
        </div>
    </Modal>
  </div>
</template>

<script type="text/ecmascript-6">
import { createInterface, getInterfaceList, getTagList, deleteInterfaceInBatch, checkInterfaceExists } from '../../../utils/const';
import {getStore} from '../../../utils/storage';
export default {
  data () {
    const validateNameExists = (rule, value, callback) => {
      checkInterfaceExists(this.interfaceItem, (response) => {
        if (response.header.code === '0') {
          if (response.body) {
            callback(new Error('接口已存在'));
          } else {
            callback();
          }
        } else {
          callback(new Error(response.header.message));
        }
      });
    };
    return {
      addInterfaceModal: false,
      interfaceItem: {
        name: '',
        method: 'get',
        path: '',
        tagIds: [],
        status: 100,
        operationId: '',
        summary: '',
        description: '',
        deprecated: true,
        projectId: ''
      },
      loading: false,
      showEditMenus: false,
      columns: [
        {
          type: 'selection',
          width: 58,
          align: 'center'
        },
        {
          title: '名称',
          key: 'name',
          render: (h, params) => {
            return h('a', {
              on: {
                click: () => {
                  this.$router.push({path: '/project/interface/edit', query: {id: params.row.id}});
                }
              }
            }, params.row.name);
          }
        },
        {
          title: '方法',
          width: 80,
          render: (h, params) => {
            let color = 'green';
            switch (params.row.method) {
              case 'get':
                color = 'blue';
              break;
              case 'put':
                color = 'yellow';
              break;
              case 'post':
                color = 'green';
              break;
              case 'delete':
                color = 'red';
              break;
            }
            return h('Tag', {
              props: {
                color: color
              }
            }, params.row.method);
          },
          key: 'method'
        },
        {
          title: '路径',
          key: 'path'
        },
        {
          title: '标签',
          key: 'tag',
          sortable: true,
          render: (h, params) => {
            let tags = [];
            params.row.tags.forEach((tag) => {
              tags.push(h('Tag', tag.name));
            });
            return tags;
          }
        },
        {
            title: '状态',
            key: 'status',
            sortable: true,
            width: 90,
            render: (h, params) => {
              let color = 'blue';
              let label = '未开始';
              switch (params.row.status) {
                case 100:
                  color = 'blue';
                  label = '未开始';
                break;
                case 200:
                  color = 'yellow';
                  label = '开发中';
                break;
                case 300:
                  color = 'red';
                  label = '测试中';
                break;
                case 400:
                  color = 'green';
                  label = '已完成';
                break;
              }
              return h('Tag', {
                props: {
                  type: 'border',
                  color: color
                }
              }, label);
            }
        },
        {
            title: '创建人',
            width: 80,
            key: 'creater'
        },
        {
            title: '创建时间',
            width: 100,
            render: (h, params) => {
              return h('span', params.row.createTime.substring(0, 10));
            }
        }
      ],
      data: [],
      selection: [],
      tags: [],
      ruleValidate: {
        name: [
          { required: true, message: '请输入接口名称', trigger: 'blur' }
        ],
        path: [
          { required: true, message: '请输入接口路径', trigger: 'blur' },
          { validator: validateNameExists, trigger: 'blur' }
        ],
        method: [
          { required: true, message: '请输入接口路径', trigger: 'blur' },
          { validator: validateNameExists, trigger: 'change' }
        ]
      }
    };
  },
  computed: {
    state() {
      return this.$store.state.app;
    }
  },
  methods: {
    init() {
      this.getInterfaceList();
      this.getTagList();
    },
    getInterfaceList: function() {
      this.loading = true;
      let projectId = this.state.projectId || getStore('projectId');
      this.interfaceItem.projectId = projectId;
      getInterfaceList({projectId: projectId}, (response) => {
        if (response.header.code === '0') {
          this.data = response.body;
        } else {
          this.$Message.error(response.header.message);
        }
      });
      this.loading = false;
    },
    getTagList: async function() {
      let projectId = this.state.projectId || getStore('projectId');
      await getTagList({projectId: projectId}, (response) => {
        if (response.header.code === '0') {
          this.tags = response.body;
        } else {
          this.$Message.error(response.header.message);
        }
      });
    },
    reset() {
      this.interfaceItem = {
        name: '',
        method: 'get',
        path: '',
        tagIds: [],
        status: 100,
        operationId: '',
        summary: '',
        description: '',
        deprecated: true,
        projectId: this.state.projectId || getStore('projectId')
      };
    },
    addInterface: function() {
      this.$refs['interfaceItem'].validate(async (valid) => {
          if (valid) {
            await createInterface(this.interfaceItem, (response) => {
              if (response.header.code === '0') {
                this.init();
                this.reset();
              } else {
                this.$Message.error(response.header.message);
              }
            });
            this.addInterfaceModal = false;
          }
      });
    },
    onCelectionChange(selection) {
      this.showEditMenus = selection.length !== 0;
      this.selection = selection;
    },
    deleteInterface() {
      deleteInterfaceInBatch(this.selection, (response) => {
        if (response.header.code === '0') {
          this.$Message.success('删除成功！');
          this.showEditMenus = false;
          this.init();
        } else {
          this.$Message.error(response.header.message);
        }
      });
    },
    newInterface() {
      this.addInterfaceModal = true;
    }
  },
  mounted() {
    this.init();
  }
};
</script>

<style lang="less">
  .project-interface{
    .project-interface-form{
      margin-top: 15px;
      float: right;
      .ivu-form-item{
        margin-bottom: 0px
      }
    }
    .form-in-table{
      width: 901px;
      background: #f8f8f9;
      position: absolute;
      z-index: 2;
      height: 39px;
      line-height: 35px;
      left: 60px;
      top: 127px;
    }
    .path-input .ivu-form-item-content{
        margin-left: 0px!important;
    }
  }
</style>
