<template>
  <Modal
    v-model="addDataModelModal"
    :closable="false"
    title="新建数据模型" width="1000"
    :mask-closable="false">
    <a class="ivu-modal-close" @click="closeModal"><i class="ivu-icon ivu-icon-ios-close-empty"></i></a>
    <div class="project-datamodel-add">
      <Form :model="formItem" ref="datamodelForm" :label-width=80 style="margin-top: 30px;" :rules="ruleValidate">
        <FormItem label="模型名称" style="width: 60%;" prop="name">
          <i-input v-model="formItem.name" placeholder="最多20个中文或者40个英文字符"></i-input>
        </FormItem>
        <FormItem label="描述" style="width: 60%;">
          <i-input v-model="formItem.description" type="textarea" :autosize="{minRows: 2,maxRows: 5}"
                   placeholder="描述"></i-input>
        </FormItem>
        <FormItem label="类别">
          <RadioGroup v-model="formItem.dataType" type="button" size="small" @on-change="dataTypeChange">
            <Radio v-for="(item, index) in systemDataModel" :label="item.dataType" :key="index"></Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="属性">
          <hash-table :rows="formItem.children" :name="formItem.name" :level="1" v-if="formItem.dataType === 'object'"></hash-table>
          <array-table :rows="formItem.children" :name="formItem.name" :level="1" v-if="formItem.dataType === 'array'"></array-table>
          <enum-table :rows="formItem.children" :level="1" v-if="formItem.dataType === 'enum'"></enum-table>
          <string-table :rows="formItem.children" :level="1" v-if="formItem.dataType === 'string'"></string-table>
          <number-table :rows="formItem.children" :level="1" v-if="formItem.dataType === 'integer'"></number-table>
          <boolean-table :rows="formItem.children" :level="1" v-if="formItem.dataType === 'boolean'"></boolean-table>
          <file-table :rows="formItem.children" :level="1" v-if="formItem.dataType === 'file'"></file-table>
        </FormItem>
        <!--<FormItem>-->
          <!--<Button type="primary" size="large" @click="create">保存</Button>-->
        <!--</FormItem>-->
      </Form>
    </div>
    <div slot="footer">
      <Button type="text" size="large" @click="closeModal">取消</Button>
      <Button type="primary" size="large" @click="create">保存</Button>
    </div>
  </Modal>
</template>

<script type="text/ecmascript-6">
  import hashTable from './hashTable.vue';
  import enumTable from './enumTable.vue';
  import arrayTable from './arrayTable.vue';
  import stringTable from './stringTable.vue';
  import numberTable from './numberTable.vue';
  import booleanTable from './booleanTable.vue';
  import fileTable from './fileTable.vue';
  import {
    getDataModelList, createDataModel, deleteDataModelInBatch, checkExists
  } from '../../utils/interface';
  import {getStore} from '../../utils/storage';
  export default {
    name: 'add-data-model',
    data() {
      const validateNameExists = (rule, value, callback) => {
        checkExists(this.formItem, (response) => {
          if (response.header.code === '0') {
            if (response.body) {
              callback(new Error('数据模型已存在'));
            } else {
              callback();
            }
          } else {
            callback(new Error(response.header.message));
          }
        });
      };

      return {
        systemDataModel: [],
				typeBak: 'object',
        formItem: {
          name: '',
          dataType: 'object',
          projectId: '',
          description: '',
          children: [{
            name: '',
            description: '',
            dataType: 'string',
            example: '',
						required: false,
            children: [],
            _expanded: false
          }]
        },
        ruleValidate: {
          name: [
            {required: true, message: '请输入模型名称', trigger: 'blur'},
            { validator: validateNameExists, trigger: 'blur' }
          ]
        }
      };
    },
    props: {
      addDataModelModal: {
        type: Boolean,
        default: false
      }
    },
    computed: {
      state() {
        return this.$store.state.app;
      }
    },
    components: {
      hashTable,
      enumTable,
      arrayTable,
      stringTable,
      numberTable,
      booleanTable,
      fileTable
    },
    methods: {
      init() {
        this.formItem.projectId = this.state.projectId || getStore('projectId');
        this.getDataModelList();
      },
      getDataModelList: async function () {
        await getDataModelList({
          type: 'system'
        }, (response) => {
          if (response.header.code === '0') {
            this.systemDataModel = response.body;
          } else {
            this.$Message.error(response.header.message);
          }
        });
      },
      joint(content, children) {
        children.forEach(item => {
//          console.log(item);
          if (item.children && item.children.length > 0) {
            if (item.name) {
              content[item.name] = {};
              this.joint(content[item.name], item.children);
            }
          } else {
            content[item.name] = item.example;
          }
        });
      },
      childrenChange(children) {
//        console.log('最外层：' + JSON.stringify(children));
        if (JSON.stringify(this.formItem.children) !== JSON.stringify(children)) {
          this.formItem.children = children;
        }
//        // this.formItem.children = children;
//        let content = {};
//        this.joint(content, children);
//        this.content = JSON.stringify(content, null, 2);
      },
      dataTypeChange(val) {
//        this.deleteDataModel(this.formItem.children, () => {
				this.$Modal.confirm({
					title: '确认切换？',
					content: '<p>切换类型将导致现有数据丢失！</p><p>你确认要切换成“' + val + '”码？</p>',
					onOk: () => {
						this.typeBak = this.formItem.dataType;
						this.formItem.children = [];
						this.formItem.children.push({
							name: '',
							description: '',
							dataType: 'string',
							example: '',
							required: false,
							children: [],
							_expanded: false
						});
					},
					onCancel: () => {
						this.formItem.dataType = this.typeBak;
					}
				});
//        });
      },
      deleteDataModel(datas, callback) {
        let deleteDatas = [];
        for (let i = 0; i < datas.length; i++) {
          if (datas[i].id) {
            deleteDatas.push(datas[i]);
          }
        }
        if (deleteDatas.length > 0) {
          deleteDataModelInBatch(deleteDatas, (response) => {
            if (response.header.code === '0') {
              callback();
            } else {
              this.$Message.error(response.header.message);
            }
          });
        } else {
          callback();
        }
      },
      create() {
        this.$refs['datamodelForm'].validate(async (valid) => {
          if (valid) {
            await createDataModel(this.formItem, (response) => {
              if (response.header.code === '0') {
                this.$Message.success('添加成功');
                this.closeModal();
              } else {
                this.$Message.error(response.header.message);
              }
            });
          } else {
            this.$Message.error('Fail!');
          }
        });
      },
      closeModal () {
        this.formItem = {
            name: '',
            dataType: 'object',
            projectId: this.state.projectId || getStore('projectId'),
            description: '',
            children: [{
              name: '',
              description: '',
              dataType: 'string',
              example: '',
							required: false,
              children: [],
              _expanded: false
          }]
        };
				this.typeBak = 'object';
        this.$emit('closeModal');
      }
    },
    mounted() {
      this.init();
    }
  };
</script>

<style lang="less">
</style>
