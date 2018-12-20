<template>
  <div class="project-datamodel-edit">
    <Form :model="formItem" ref="datamodelForm"  :label-width="80" style="margin-top: 30px;" :rules="ruleValidate">
      <Spin size="large" fix v-if="spinShow"></Spin>
      <FormItem label="模型名称" style="width: 60%;" prop="name">
        <i-input v-model="formItem.name" placeholder="最多20个中文或者40个英文字符"></i-input>
      </FormItem>
      <FormItem label="描述" style="width: 60%;">
        <i-input v-model="formItem.description" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="描述"></i-input>
      </FormItem>
      <FormItem label="类别">
        <RadioGroup v-model="formItem.dataType" type="button" size="small" @on-change="dataTypeChange">
           <Radio v-for="(item, index) in systemDataModel" :label="item.dataType" :key="index"></Radio>
       </RadioGroup>
      </FormItem>
      <FormItem label="属性">
        <!--<hash-table :rows="formItem.children" :level="1" @childrenChange="childrenChange"></hash-table>-->
        <hash-table :rows="formItem.children" :name="formItem.name" :level="1" v-if="formItem.dataType === 'object'" @childrenChange="childrenChange"></hash-table>
        <array-table :rows="formItem.children" :name="formItem.name" :level="1" v-if="formItem.dataType === 'array'"></array-table>
        <enum-table :rows="formItem.children" :level="1" v-if="formItem.dataType === 'enum'"></enum-table>
        <string-table :rows="formItem.children" :level="1" v-if="formItem.dataType === 'string'"></string-table>
        <number-table :rows="formItem.children" :level="1" v-if="formItem.dataType === 'integer'"></number-table>
        <boolean-table :rows="formItem.children" :level="1" v-if="formItem.dataType === 'boolean'"></boolean-table>
        <file-table :rows="formItem.children" :level="1" v-if="formItem.dataType === 'file'"></file-table>
        <!-- <div style="float:right;"><Button>取消</Button> <Button type="primary">保存</Button></div> -->
      </FormItem>
      <!--<FormItem label="样例数据">-->
        <!--<ace-editor v-model="content" lang="json"  height="400" width="100%; border: 1px solid #e9eaec;"></ace-editor>-->
      <!--</FormItem>-->
      <FormItem>
        <Button type="primary" size="large" @click="update">保存</Button>
      </FormItem>
    </Form>
  </div>
</template>

<script type="text/ecmascript-6">
import hashTable from '../../../components/project/hashTable.vue';
import enumTable from '../../../components/project/enumTable.vue';
import arrayTable from '../../../components/project/arrayTable.vue';
import stringTable from '../../../components/project/stringTable.vue';
import numberTable from '../../../components/project/numberTable.vue';
import booleanTable from '../../../components/project/booleanTable.vue';
import fileTable from '../../../components/project/fileTable.vue';
import {
  getDataModelList, updateDataModel, getDataModelById, deleteDataModelInBatch, checkExists
} from '../../../utils/interface';
import { reverse } from '../../../utils/utils';
import aceEditor from 'vue2-ace-editor';
import 'brace/theme/chrome';
import 'brace/mode/json';
export default {
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
      spinShow: false,
      typeBak: '',
      systemDataModel: [],
      content: '',
      formItem: {
        name: '',
        dataType: 'object',
        description: '',
				required: false,
        children: []
      },
      ruleValidate: {
        name: [
          { required: true, message: '请输入模型名称', trigger: 'blur' },
          { validator: validateNameExists, trigger: 'blur' }
        ]
      }
    };
  },
  components: {
    hashTable,
    enumTable,
    arrayTable,
    stringTable,
    numberTable,
    booleanTable,
    fileTable,
    aceEditor
  },
  methods: {
    init() {
      this.getDataModelById();
      this.getDataModelList();
    },
    dataTypeChange(val) {
			this.$Modal.confirm({
				title: '确认切换？',
				content: '<p>切换类型将导致现有数据丢失！</p><p>你确认要切换成“' + val + '”吗？</p>',
				onOk: () => {
					this.typeBak = this.formItem.dataType;
					this.formItem.children = [];
					this.formItem.children.push({
						name: '',
						description: '',
						dataType: 'string',
						example: '',
						children: [],
						required: false,
						_expanded: false
					});
				},
				onCancel: () => {
					this.formItem.dataType = this.typeBak;
				}
			});
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
    getDataModelById() {
      getDataModelById({
        id: this.$route.query.id
      }, (response) => {
        if (response.header.code === '0') {
          this.formItem = response.body;
          this.typeBak = this.formItem.dataType;
          reverse(this.formItem.children); //
        } else {
          this.$Message.error(response.header.message);
        }
      });
    },
    getDataModelList: async function() { // 系统默认数据结构
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
    joint(content, children) { // 迭代拼接 样例数据
      children.forEach(item => {
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
//    reverse(children) { // 迭代处理 反显数据处理
//      children.forEach(item => {
//        if (item.dataType === 'object' || item.dataType === 'array') {
//          item._expanded = true;
//        }
//        if (item.dataType === 'array') {
//          item.isArrayItem = true;
//        }
//        if (item.children && item.children.length > 0) {
//          this.reverse(item.children);
//        }
//      });
//    },
    childrenChange(children) {
      if (JSON.stringify(this.formItem.children) !== JSON.stringify(children)) {
        this.formItem.children = children;
      }
//      let content = {};
//      this.joint(content, children);
//      this.content = JSON.stringify(content, null, 2);
    },
    update() {
      this.spinShow = true;
      this.$refs['datamodelForm'].validate(async (valid) => {
          if (valid) {
            await updateDataModel(this.formItem, (response) => {
              if (response.header.code === '0') {
                this.$Message.success('修改成功！');
                this.init();
              } else {
                this.$Message.error(response.header.message);
              }
              this.spinShow = false;
            });
          } else {
            this.spinShow = false;
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
</style>
