<template>
  <div class="project-tags">
    <Form ref="formInline" inline class="project-tags-form">
      <FormItem>
        <!--<Button type="primary" @click="handleSubmit('formInline')">Signin</Button>-->
        <Button type="primary" icon="md-add" @click="newTag">新建标签</Button>
      </FormItem>
      <FormItem prop="search">
        <Input type="text" placeholder="名称搜索" v-model="searchModel" @on-keyup="searchData"/>
      </FormItem>
    </Form>
    <div class="clearfix"></div>
    <Form ref="formInline" inline class="project-tags-form form-in-table" v-show="showEditMenus">
      <Button size="small" @click="deleteTags">删除</Button>
    </Form>
    <Table :loading="loading" stripe ref="selection" :columns="columns" :data="filterTags" @on-selection-change="onCelectionChange"></Table>
    <Modal
        v-model="addTagModal"
        title="新建标签组" width="700" :mask-closable=false>
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
import { getTagList, createTag, deleteTagInBatch, checkTagExists } from '../../../utils/interface';
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
					sortable: true,
          key: 'name'
        },
        {
          title: '描述',
          key: 'description'
        },
        {
					title: '创建人',
					key: 'createrUserName'
        },
        {
					title: '创建时间',
					sortable: true,
					render: (h, params) => {
						return h('span', params.row.createTime.substring(0, 10));
					}
        }
      ],
			tags: [],
			filterTags: [],
			searchModel: '',
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
          this.tags = response.body;
          this.filterTags = response.body;
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
			this.$Modal.confirm({
				title: '确认删除？',
				content: '<p>删除数据不可恢复！</p><p>确认要删除吗？</p>',
				onOk: () => {
					deleteTagInBatch(this.selection, (response) => {
						if (response.header.code === '0') {
							this.$Message.success('删除成功！');
							this.showEditMenus = false;
							this.init();
						} else {
							this.$Message.error(response.header.message);
						}
					});
				},
				onCancel: () => {
					this.$Message.success("取消删除！");
				}
			});
    },
		searchData() {
			this.filterTags = this.tags.filter(item => item.name.toLowerCase().indexOf(this.searchModel.toLowerCase()) > -1);
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
      width: 93%;
      background: #f8f8f9;
      position: absolute;
      z-index: 2;
      height: 38px;
      line-height: 35px;
      left: 60px;
      top: 127px;
    }
    .ivu-table-wrapper,.ivu-page{
      margin-top: 15px;
    }
  }
</style>
