<template>
  <div class="project-datamodel">
    <Form ref="formInline" inline class="project-datamodel-form">
      <FormItem>
        <!--<Button type="primary" @click="handleSubmit('formInline')">Signin</Button>-->
        <Button type="primary" icon="md-add" @click="newDatamodel">新建模型</Button>
      </FormItem>
      <FormItem prop="search">
        <Input type="text" placeholder="名称搜索" v-model="searchModel" @on-keyup="searchData"/>
      </FormItem>
    </Form>
    <div class="clearfix"></div>
    <Form ref="formInline" inline class="project-datamodel-form form-in-table" v-show="showEditMenus">
      <Button size="small" @click="deleteDataModel">删除</Button>
      <!--<Button>设置状态</Button>-->
      <!--<Button>设置标签</Button>-->
    </Form>
    <Table :loading="loading" stripe ref="selection" :columns="columns" :data="filterDatamodels" @on-selection-change="onCelectionChange"></Table>
    <add-data-model :addDataModelModal="addDataModelModal" @closeModal="closeModal"></add-data-model>
  </div>
</template>

<script type="text/ecmascript-6">
  import addDataModel from '../../../components/project/addDataModel.vue';
  import {
    getDataModelList,
    deleteDataModelInBatch,
    getCustomDataModelList
  } from '../../../utils/interface';
  import {setStore, getStore} from '../../../utils/storage';
  export default {
    name: 'datamodel',
    data() {
      return {
        loading: false,
        spinShow: false,
        showEditMenus: false,
        addDataModelModal: false,
        columns: [{
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
                    this.$router.push({path: '/project/datamodel/edit', query: {id: params.row.id}});
                  }
                }
              }, params.row.name);
            }
          },
          {
            title: '描述',
            key: 'description'
          },
          {
            title: '分组',
            key: 'group'
          },
          {
            title: '创建人',
            key: 'createrUserName'
          },
          {
            title: '创建时间',
            key: 'createTime',
            render: (h, params) => {
              return h('span', params.row.createTime.substring(0, 10));
            }
          }
        ],
        selection: [],
				filterDatamodels: [],
				datamodels: [],
				searchModel: ''
      };
    },
    components: {
      addDataModel
    },
    computed: {
			projectId() {
        return this.$store.state.app.projectId || getStore('projectId');
      },
			state() {
        return this.$store.state.app;
      }
    },
    methods: {
      init() {
				this.getDataModelList();
      },
      getDataModelList: async function () {
				this.loading = true;
        await getCustomDataModelList({projectId: this.projectId}, (response) => {
          this.datamodels = response.body;
          this.filterDatamodels = response.body;
					this.$store.dispatch('customDataModel', response.body);
          this.loading = false;
        });
      },
      deleteDataModel() {
				this.$Modal.confirm({
					title: '确认删除？',
					content: '<p>删除数据不可恢复！</p><p>确认要删除吗？</p>',
					onOk: () => {
						deleteDataModelInBatch(this.selection, (response) => {
              this.$Message.success('删除成功！');
              this.showEditMenus = false;
              this.init();
						});
					},
					onCancel: () => {
						this.$Message.success("取消删除！");
					}
				});
      },
      onCelectionChange(selection) {
        this.showEditMenus = selection.length !== 0;
        this.selection = selection;
      },
      newDatamodel() {
        this.addDataModelModal = true;
      },
      closeModal () {
        this.init();
        this.addDataModelModal = false;
      },
			searchData() {
				this.filterDatamodels = this.datamodels.filter(item => item.name.toLowerCase().indexOf(this.searchModel.toLowerCase()) > -1);
			}
    },
    mounted() {
      this.init();
    }
  };
</script>

<style lang="less">
  .project-datamodel {
    .project-datamodel-form {
      margin-top: 15px;
      float: right;
      .ivu-form-item {
        margin-bottom: 0;
      }
    }
    .form-in-table {
      width: 901px;
      background: #f8f8f9;
      position: absolute;
      z-index: 2;
      height: 39px;
      line-height: 35px;
      left: 60px;
      top: 127px;
    }
    .ivu-table-wrapper,.ivu-page{
      margin-top: 15px;
    }
  }
</style>
