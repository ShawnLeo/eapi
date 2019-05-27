<template>
    <div class="components-project-hashtable">
      <Form ref="formInline" inline class="bottom-btn">
        <Button type="dashed" size="small" @click="importFromDatamodel = true">从数据模型导入</Button>&nbsp;
        <Button type="dashed" size="small" @click="importFromJSON = true">从JSON导入</Button>
      </Form>

      <Modal v-model="importFromDatamodel"
             title="从数据模型导入"
             @on-ok="asyncOKImportFromDatamodel"
             width="720">
        <Input type="text" v-model="searchModel" placeholder="搜索" @on-keyup="searchDatamodel" style="width: 300px;float: right;margin-bottom: 20px;"/>
        <div class="clearfix" ></div>
        <Table ref="selection" :columns="customDataModelColumns" :data="filterCustomDataModel" @on-selection-change="onCelectionChange"></Table>
      </Modal>

      <Modal v-model="importFromJSON"
             title="从JSON导入"
             @on-ok="asyncOKImportFromJSON"
             width="720">
        <ace-editor v-model="jsonContent" lang="json"  height="250" width="100%; border: 1px solid #e9eaec;"></ace-editor>
      </Modal>
    </div>
</template>
<script>
  import expandRow from './hashTable.vue';
  import expandArrayRow from './arrayTable.vue';
  import { deleteDataModelInBatch } from '../../utils/interface';
	import { reverse, coverJsonToDatamodel } from '../../utils/utils';
  import {getStore} from '../../utils/storage';
	import aceEditor from 'vue2-ace-editor';
	import 'brace/theme/chrome';
	import 'brace/mode/json';
  export default {
      props: {
      },
      components: {
        aceEditor
      },
      data() {
        return {
					jsonContent: '',
					importFromDatamodel: false,
					importFromJSON: false,
					searchModel: '',
					selectionDatamodel: [],
          filterCustomDataModel: JSON.parse(getStore('customDataModel')),
					customDataModel: JSON.parse(getStore('customDataModel')),
					customDataModelColumns: [{
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
          }]
        };
      },
      methods: {
        init() {
        },
        searchDatamodel() {
					this.filterCustomDataModel = this.customDataModel.filter(item => item.name.toLowerCase().indexOf(this.searchModel.toLowerCase()) > -1);
        },
				asyncOKImportFromDatamodel() {
                    let datamodels = [];
					this.selectionDatamodel.forEach(datamodel => {
						datamodel.children.forEach(child => {
							child.id = null;
						});
						datamodels = datamodels.concat(datamodel.children);
					});
					reverse(datamodels);

					this.$emit('childrenChange', JSON.parse(JSON.stringify(datamodels)));
				},
				asyncOKImportFromJSON() {
                    let datamodels = [];
					if (!this.jsonContent) {
						return;
					}
					coverJsonToDatamodel(JSON.parse(this.jsonContent), datamodels);
					reverse(datamodels);
					this.$emit('childrenChange', JSON.parse(JSON.stringify(datamodels)));
				},
				onCelectionChange(selection) {
					this.selectionDatamodel = selection;
        }
      },
      watch: {
				rows: {
          handler: function (val, oldVal) {
            this.$emit('childrenChange', val);
          },
          deep: true
				}
			},
      mounted() {
        this.init();
      }
  };
</script>
<style lang="less">
  .components-project-hashtable{
    background: #f8f8f9;
    border: 1px solid #dcdee2;
    box-shadow: 2px 2px 4px #dcdee2;
    .datamodel-table{
      &.ivu-table-wrapper{
        overflow: visible;
        border: none;
      }
      .ivu-table{
        overflow: inherit;
        border: none;
        .ivu-table-overflowX {
          overflow-x: inherit;
        }
        td.ivu-table-expanded-cell{
          padding: 10px;
          background: none;
        }
        .ivu-icon-ios-arrow-right:before{
          content: '';
        }

        .ivu-table-wrapper, .ivu-page{
          margin-top: -32px;
        }
        .expand-row{
          margin: 5px;
        }
        &:after{
          width: 0px;
        }
        // .expand-td{
        //   width: -1px;
        // }
      }
    }
    .bottom-btn{
      padding-left: 5px;
    }
  }


</style>
