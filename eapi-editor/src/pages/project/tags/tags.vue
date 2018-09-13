<template>
  <div class="project-tags">
    <Form ref="formInline" inline class="project-tags-form">
      <FormItem>
        <!--<Button type="primary" @click="handleSubmit('formInline')">Signin</Button>-->
        <Button type="primary" icon="edit" @click="newTag">新建标签</Button>
      </FormItem>
      <FormItem prop="search">
        <Input type="text" placeholder="搜索"/>
      </FormItem>
    </Form>
    <div class="clearfix"></div>
    <Form ref="formInline" inline class="project-tags-form form-in-table" v-show="showEditMenus">
      <Button @click="deleteTags">删除</Button>
    </Form>
    <Table :loading="loading" stripe ref="selection" :columns="columns" :data="data" @on-selection-change="onCelectionChange"></Table>
    <Modal
        v-model="addTagModal"
        title="新建标签组" width="700">
        <Form ref="tagFormItem" :model="formItem" :label-width=120 :rules="ruleValidate" >
            <FormItem label="标签名称" prop="name">
                <i-input v-model="formItem.name" placeholder="例如：user"></i-input>
            </FormItem>
            <FormItem label="描述" prop="description">
                <i-input v-model="formItem.description" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="最多20个中文或者40个英文字符"></i-input>
            </FormItem>
            <!-- <FormItem label="外部文档描述">
                <i-input v-model="formItem.input" placeholder="最多20个中文或者40个英文字符"></i-input>
            </FormItem>
            <FormItem label="外部文档地址">
                <i-input v-model="formItem.input" placeholder="例如：http://editor.swagger.io/"></i-input>
            </FormItem> -->
        </Form>
        <div slot="footer">
          <Button type="text" size="large" @click="reset">重置</Button>
          <Button type="primary" size="large" @click="addTag">确定</Button>
        </div>
    </Modal>
  </div>
</template>

<script type="text/ecmascript-6">
import { getTagList, createTag, deleteTagInBatch, checkTagExists } from '../../../utils/const';
import {getStore} from '../../../utils/storage';
export default {
  data () {
    const validateNameExists = (rule, value, callback) => {
      checkTagExists(this.formItem, (response) => {
        if (response.header.code === '0') {
          if (response.body) {
            callback(new Error('标签已存在'));
          } else {
            callback();
          }
        } else {
          callback(new Error(response.header.message));
        }
      });
    };
    return {
      addTagModal: false,
      loading: false,
      formItem: {
        name: '',
        description: '',
        projectId: ''
      },
      showEditMenus: false,
      columns: [
        {
          type: 'selection',
          width: 58,
          align: 'center'
        },
        {
          title: '名称',
          key: 'name'
        },
        {
          title: '描述',
          key: 'description'
        },
        {
            title: '创建人',
            key: 'creater'
        },
        {
            title: '创建时间',
            render: (h, params) => {
              return h('span', params.row.createTime.substring(0, 10));
            }
        }
      ],
      data: [],
      selection: [],
      ruleValidate: {
        name: [
          { required: true, message: '请输入标签名称', trigger: 'blur' },
          { validator: validateNameExists, trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入描述', trigger: 'blur' }
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
      this.getTagList();
    },
    getTagList: async function() {
      this.loading = true;
      let projectId = this.state.projectId || getStore('projectId');
      this.formItem.projectId = projectId;
      await getTagList({projectId: projectId}, (response) => {
        if (response.header.code === '0') {
          this.data = response.body;
        } else {
          this.$Message.error(response.header.message);
        }
        this.loading = false;
      });
    },
    onCelectionChange(selection) {
      this.showEditMenus = selection.length !== 0;
      this.selection = selection;
    },
    reset() {
      this.formItem = {
        name: '',
        description: '',
        projectId: this.state.projectId || getStore('projectId')
      };
    },
    addTag: function() {
      this.$refs['tagFormItem'].validate(async (valid) => {
          if (valid) {
            await createTag(this.formItem, (response) => {
              if (response.header.code === '0') {
                this.init();
                this.reset();
              } else {
                this.$Message.error(response.header.message);
              }
            });
            this.addTagModal = false;
          }
      });
    },
    newTag() {
      this.addTagModal = true;
    },
    deleteTags() {
      deleteTagInBatch(this.selection, (response) => {
        if (response.header.code === '0') {
          this.$Message.success('删除成功！');
          this.showEditMenus = false;
          this.init();
        } else {
          this.$Message.error(response.header.message);
        }
      });
    }
  },
  mounted() {
    this.init();
  }
};
</script>

<style lang="less">
  .project-tags{
    .project-tags-form{
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
  }
</style>
