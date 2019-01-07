<template>
    <div class="components-project-hashtable">
      <Icon type="md-arrow-dropup" style="position:relative;left:25%;top:-20px;font-size: 24px; z-index:2;color:#dddee1;" v-show="showIcon"></Icon>
      <Table :columns="columns10" :data="rows" class="datamodel-table"></Table>
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
          rows: Array,
          name: String,
          level: Number,
          showIcon: Boolean,
          isArrayItem: Boolean
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
          }],
          columns10: [{
              type: 'expand',
              width: -1,
//              className: 'expand-td',
              render: (h, params) => {
                return h(params.row.dataType === 'array'? expandArrayRow : expandRow, {
                  props: {
                    rows: params.row.children,
                    name: this.name,
                    level: this.level + 1,
                    showIcon: true,
                    isArrayItem: params.row.dataType === 'array'
                  },
                  on: {
                    childrenChange: (children) => {
                      // console.log('第' + this.level + '层组件的children：' + JSON.stringify(children));
                      if (JSON.stringify(children) !== JSON.stringify(this.rows[params.index].children)) {
                        this.rows[params.index].children = children;
                      }
                    }
                  }
                });
              }
            },
            {
              title: '名称',
              key: 'name',
              render: (h, params) => {
                return h('div', [
                  h('i-input', {
                    attrs: {
                      id: 'edit-name-' + params.index,
                      placeholder: '名称',
//                      disabled: this.isArrayItem,
                      value: params.row.name
                    },
                    on: {
                      'on-blur': (event) => {
                        this.rows[params.index].name = event.target.value;
                      }
                    }
//                    class: ['ivu-form-item-error']
                  })
                ]);
              }
            },
            {
              title: '类型',
              key: 'dataType',
              render: (h, params) => {
                let systemDataModel = JSON.parse(getStore('systemDataModel'));
                let customDataModel = JSON.parse(getStore('customDataModel'));
                let options = [];
                systemDataModel.forEach(datamodel => {
                  if (!(datamodel.dataType === 'array' && this.isArrayItem)) {
                    options.push(h('Option', {
                      props: {
                        value: datamodel.dataType
                      }
                    }, datamodel.dataType));
                  }
                });
                customDataModel.forEach(datamodel => {
                  if (datamodel.name !== this.name) {
                    options.push(h('Option', {
                      props: {
                        value: datamodel.name
                      }
                    }, datamodel.name));
                  }
                });
                return h('div', [
                  h('Select', {
                    attrs: {
                      id: 'edit-name-' + params.index,
                      value: params.row.dataType,
											filterable: true
                    },
                    on: {
                      'on-change': (value) => {
                        this.rows[params.index].dataType = value;
                        this.rows[params.index]._expanded = false;
                        this.rows[params.index].children = [];
                        if (value === 'object') {
                          this.rows[params.index].children.push({
                            name: '',
                            description: '',
                            dataType: 'string',
                            example: '',
                            children: [],
														required: false,
                            _expanded: false
                          });
                          this.rows[params.index]._expanded = true;
                        } else if (value === 'array') {
                          this.rows[params.index].children.push({
                            name: '',
                            description: '',
                            dataType: 'string',
                            example: '',
                            children: [],
														required: false,
                            _expanded: false
                          });
                          this.rows[params.index]._expanded = true;
                        }
                        return value;
                      }
                    }
                  }, options)
                ]);
              }
            },
            {
              title: '描述',
              key: 'description',
              render: (h, params) => {
                return h('div', [
                  h('i-input', {
                    attrs: {
                      id: 'edit-describe-' + params.index,
                      value: params.row.description,
                      placeholder: '描述'
                    },
                    on: {
                      'on-blur': (event) => {
                        this.rows[params.index].description = event.target.value;
                      }
                    }
                  })
                ]);
              }
            },
            {
              title: '必需',
              key: 'address',
              render: (h, params) => {
                return h('div', [
                  h('i-switch', {
                    attrs: {
                      id: 'edit-name-' + params.index,
                      value: params.row.required
                    },
                    on: {
                      'on-change': (value) => {
                        this.rows[params.index].required = value;
                      }
                    }
                  })
                ]);
              }
            },
            {
              title: '默认值',
              key: 'example',
              render: (h, params) => {
                return h('div', [
                  h('i-input', {
                    attrs: {
                      id: 'edit-name-' + params.index,
                      value: params.row.example,
                      placeholder: '默认值'
                    },
                    on: {
                      'on-blur': (event) => {
                        this.rows[params.index].example = event.target.value;
                      }
                    }
                  })
                ]);
              }
            },
//            {
//              title: '生成规则',
//              key: 'address',
//              render: (h, params) => {
//                return h('div', [
//                  h('i-input', {
//                    attrs: {
//                      id: 'edit-name-' + params.index,
//                      // value: params.row.name,
//                      placeholder: '生成规则'
//                    }
//                  })
//                ]);
//              }
//            },
            {
              title: '添加',
              key: 'action',
              width: 60,
              align: 'center',
              renderHeader: (h, params) => {
                let self = this;
                return h('div', [
                  h('Button', {
                    props: {
//                      type: 'ghost',
                      shape: 'circle',
                      size: 'small',
                      icon: 'md-add'
                    },
                    style: {
                      //                    marginRight: '5px'
                    },
                    on: {
                      click: () => {
                        self.rows.push({
                          name: '',
                          description: '',
                          dataType: 'string',
                          example: '',
                          children: [],
													required: false,
                          _expanded: false
                        });
                      }
                    }
                  })
                ]);
              },
              render: (h, params) => {
                return h('div', [
                  h('Button', {
                    props: {
//                      type: 'ghost',
                      shape: 'circle',
                      size: 'small',
                      icon: 'md-remove'
                    },
                    style: {
                      marginRight: '5px'
                    },
                    on: {
                      click: () => {
												this.rows.splice(params.index, 1);
//                        if (params.row.id) {
//                          this.deleteDataModel([params.row], params.index);
//                        } else {
//                          this.rows.splice(params.index, 1);
//                        }
                      }
                    }
                  })
                ]);
              }
            }
          ]
        };
      },
      methods: {
        init() {
          if (this.isArrayItem) {
            this.columns10.splice(-1);
          }
        },
        deleteDataModel(row, index) {
          deleteDataModelInBatch(row, (response) => {
            if (response.header.code === '0') {
              this.$Message.success('删除成功！');
              this.rows.splice(index, 1);
            } else {
              this.$Message.error(response.header.message);
            }
          });
        },
        searchDatamodel() {
					this.filterCustomDataModel = this.customDataModel.filter(item => item.name.toLowerCase().indexOf(this.searchModel.toLowerCase()) > -1);
        },
				asyncOKImportFromDatamodel() {
					let datamodels = this.rows;
					if (datamodels.length === 1 && !datamodels[0].name) {
						datamodels.splice(0, 1);
					}
					this.selectionDatamodel.forEach(datamodel => {
						datamodel.children.forEach(child => {
							child.id = null;
						});
						datamodels = datamodels.concat(datamodel.children);
					});
					reverse(datamodels);
					this.$emit('childrenChange', datamodels);
				},
				asyncOKImportFromJSON() {
					let datamodels = this.rows;
					if (datamodels.length === 1 && !datamodels[0].name) {
						datamodels.splice(0, 1);
					}

					if (!this.jsonContent) {
						return;
					}
					coverJsonToDatamodel(JSON.parse(this.jsonContent), datamodels);
					reverse(datamodels);
					this.$emit('childrenChange', datamodels);
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
