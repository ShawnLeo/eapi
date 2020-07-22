<template>
  <div class="components-project-hashtable">
    <Icon type="md-arrow-dropup"
          style="position:relative;left:25%;top:-20px;font-size: 24px; z-index:2;color:#dddee1;"
          v-show="showIcon"></Icon>
    <Table :columns="columns10" :data="rows" class="datamodel-table"></Table>
    <Form ref="formInline" inline class="bottom-btn">
      <Button type="dashed" size="small" @click="importFromDatamodel = true">已有模型导入</Button>&nbsp;
      <Button type="dashed" size="small" @click="importFromJSON = true">JSON导入</Button>
      &nbsp;<a>|</a> &nbsp;
      <Button type="dashed" size="small" @click="exportJSONModleShow">导出JSON</Button>
    </Form>

    <Modal v-model="importFromDatamodel"
           title="从数据模型导入"
           @on-ok="asyncOKImportFromDatamodel"
           width="720">
      <Input type="text" v-model="searchModel" placeholder="搜索" @on-keyup="searchDatamodel"
             style="width: 300px;float: right;margin-bottom: 20px;"/>
      <div class="clearfix"></div>
      <Table ref="selection" :columns="customDataModelColumns" :data="filterCustomDataModel"
             @on-selection-change="onCelectionChange"></Table>
    </Modal>

    <Modal v-model="importFromJSON"
           title="从JSON导入"
           @on-ok="asyncOKImportFromJSON"
           width="720">
      <Tabs :value="importTab" @on-click="(name) => {this.importTab = name}">
        <TabPane label="K-V对象" name="object">
          <Alert show-icon>
            <!--key将作为名称，value作为默认值。-->
            选择导入K-V 类型：
            <RadioGroup v-model="kvType">
              <Radio label="1">
                K(name)-V(example)
                <Tooltip content='例如：{"id":"1001", "name":"shawn"}' placement="top">
                  <Icon type="ios-help-circle-outline"/>
                </Tooltip>
              </Radio>
              <Radio label="2">
                K(name)-V(type)
                <Tooltip content='例如：{"id":"integer", "name":"string"}' placement="top">
                  <Icon type="ios-help-circle-outline"/>
                </Tooltip>
              </Radio>
              <Radio label="3">
                K(name)-V(description)
                <Tooltip content='例如：{"id":"用户ID", "name":"名称"}' placement="left">
                  <Icon type="ios-help-circle-outline"/>
                </Tooltip>
              </Radio>
            </RadioGroup>
          </Alert>


          <ace-editor v-model="jsonContent" lang="json" height="250"
                      width="100%; border: 1px solid #e9eaec;"></ace-editor>
        </TabPane>
        <TabPane label="模型数组" name="array">
          <Alert show-icon>
            模型数组可以在 <b>代码生成 -> Mybatis生成 -> 表名 -> 复制</b>。
            <template slot="desc">
              例如：[{"name":"id","description":"主键", "type": "integer"}]
            </template>
          </Alert>
          <ace-editor v-model="modelJsonContent" lang="json" height="250"
                      width="100%; border: 1px solid #e9eaec;"></ace-editor>
        </TabPane>
      </Tabs>

    </Modal>

    <Modal v-model="exportFromJSON"
           title="导出JSON"
           okText="复制JSON"
           @on-ok="asyncOKExportJSON"
           width="720">
      <Tabs :value="importTab" @on-click="(name) => {this.importTab = name}">
        <TabPane label="K-V对象" name="object">
          <Alert show-icon>
            <!--key将作为名称，value作为默认值。-->
            仅导出基础类型数据
            <br><br>选择导入K-V 类型：
            <RadioGroup v-model="kvType" @on-change="changeKvType">
              <Radio label="1">
                K(name)-V(example)
                <Tooltip content='例如：{"id":"1001", "name":"shawn"}' placement="top">
                  <Icon type="ios-help-circle-outline"/>
                </Tooltip>
              </Radio>
              <Radio label="2">
                K(name)-V(type)
                <Tooltip content='例如：{"id":"integer", "name":"string"}' placement="top">
                  <Icon type="ios-help-circle-outline"/>
                </Tooltip>
              </Radio>
              <Radio label="3">
                K(name)-V(description)
                <Tooltip content='例如：{"id":"用户ID", "name":"名称"}' placement="left">
                  <Icon type="ios-help-circle-outline"/>
                </Tooltip>
              </Radio>
            </RadioGroup>
          </Alert>

          <ace-editor v-model="exportJsonContent" lang="json" height="250"
                      width="100%; border: 1px solid #e9eaec;"></ace-editor>
        </TabPane>
        <TabPane label="模型数组" name="array">
          <Alert show-icon>
            模型数组格式为：name + description + type
            <template slot="desc">例如:[{"name":"id","description":"主键", "type": "integer"}]
            </template>
          </Alert>
          <ace-editor v-model="exportArrayJsonContent" lang="json" height="250"
                      width="100%; border: 1px solid #e9eaec;"></ace-editor>
        </TabPane>
      </Tabs>

    </Modal>
  </div>
</template>
<script>
  import expandRow from './hashTable.vue';
  import expandArrayRow from './arrayTable.vue';
  import {deleteDataModelInBatch} from '../../utils/interface';
  import {reverse, coverJsonToSimpleDatamodel, coverArrayJsonToDatamodel} from '../../utils/utils';
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
        kvType: "1",
        jsonContent: '',
        modelJsonContent: '',
        exportJsonContent: '',
        exportArrayJsonContent: '',
        importTab: 'object',
        importFromDatamodel: false,
        importFromJSON: false,
        exportFromJSON: false,
        searchModel: '',
        selectionDatamodel: [],
        systemDataModel: JSON.parse(getStore('systemDataModel')),
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
            return h(params.row.dataType === 'array' ? expandArrayRow : expandRow, {
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
                  if (JSON.stringify(children) !== JSON.stringify(
                      this.rows[params.index].children)) {
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

              let systemDataModel = this.systemDataModel;
              let customDataModel = this.customDataModel;
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
                // if (datamodel.name !== this.name) {
                options.push(h('Option', {
                  props: {
                    value: datamodel.name
                  }
                }, datamodel.name));
                // }
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
        this.filterCustomDataModel = this.customDataModel.filter(
            item => item.name.toLowerCase().indexOf(this.searchModel.toLowerCase()) > -1);
      },
      asyncOKImportFromDatamodel() {
        let datamodels = this.rows;
        if (datamodels.length === 1 && !datamodels[0].name) {
          datamodels.splice(0, 1);
        }
        this.selectionDatamodel.forEach(datamodel => {
          datamodel.children.forEach(child => {
            child.id = null;
            this.resetId(child.children);
          });
          datamodels = datamodels.concat(datamodel.children);
        });
        reverse(datamodels);
        this.$emit('childrenChange', datamodels);
      },
      resetId(datamodels) {
        if (!datamodels || datamodels.length === 0) {
          return;
        }
        datamodels.forEach(datamodel => {
          datamodel.id = null;
          this.resetId(datamodel.children);
        });
      },
      asyncOKImportFromJSON() {
        let datamodels = this.getAllSpliceFirstNullItemDatamodels();

        if (this.importTab === 'object') {
          if (!this.jsonContent) {
            return;
          }
          coverJsonToSimpleDatamodel(JSON.parse(this.jsonContent), datamodels, this.kvType);
        } else {
          if (!this.modelJsonContent) {
            return;
          }
          coverArrayJsonToDatamodel(JSON.parse(this.modelJsonContent), datamodels);
        }
        reverse(datamodels);
        this.$emit('childrenChange', datamodels);
      },
      onCelectionChange(selection) {
        this.selectionDatamodel = selection;
      },
      exportJSONModleShow() {
        let datamodels = this.rows;
        // coverDatamodelToJson
        let exportJson = {};
        let exportArrayJson = [];

        datamodels.forEach(datamodel => {
          if (this.isSystemDataModel(datamodel.dataType)) {
            switch (this.kvType) {
              case "1":
                exportJson[datamodel.name] = datamodel.example ? datamodel.example : "";
                break;
              case "2":
                exportJson[datamodel.name] = datamodel.dataType ? datamodel.dataType : "";
                break;
              case "3":
                exportJson[datamodel.name] = datamodel.description ? datamodel.description : "";
                break;
              default:
                exportJson[datamodel.name] = datamodel.example ? datamodel.example : "";
                break;
            }
            exportArrayJson.push({
              "name": datamodel.name,
              "description": datamodel.description ? datamodel.description : "",
              "type": datamodel.dataType ? datamodel.dataType : ""
            });
          }
        });

        this.exportJsonContent = JSON.stringify(exportJson);
        this.exportArrayJsonContent = JSON.stringify(exportArrayJson);
        this.exportFromJSON = true;
      },
      changeKvType(kvType) {
        console.log(kvType);
        this.kvType = kvType;
        this.exportJSONModleShow();
      },
      isSystemDataModel(dataType) {
        let flag = false;
        this.systemDataModel.forEach(item => {
          if (item.dataType === dataType) {
            flag = true;
          }
        });
        return flag;
      },
      getAllSpliceFirstNullItemDatamodels() {
        let datamodels = this.rows;
        if (datamodels.length === 1 && !datamodels[0].name) {
          datamodels.splice(0, 1);
        }
        return datamodels;
      },
      asyncOKExportJSON() {
        let copyText = this.exportArrayJsonContent;

        if (this.importTab === 'object') {
          copyText = this.exportJsonContent;
        }
        let _this = this;
        this.$copyText(copyText).then(
            () => {
              _this.$Message.success("复制成功");
            }, () => {
              _this.$Message.warning("复制失败，请手动复制");
            }
        );
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
//        this.init();
    }
  };
</script>
<style lang="less">
  .components-project-hashtable {
    background: #f8f8f9;
    border: 1px solid #dcdee2;
    box-shadow: 2px 2px 4px #dcdee2;

    .datamodel-table {
      &.ivu-table-wrapper {
        overflow: visible;
        border: none;
      }

      .ivu-table {
        overflow: inherit;
        border: none;

        .ivu-table-overflowX {
          overflow-x: inherit;
        }

        td.ivu-table-expanded-cell {
          padding: 10px;
          background: none;
        }

        .ivu-icon-ios-arrow-right:before {
          content: '';
        }

        .ivu-table-wrapper, .ivu-page {
          margin-top: -32px;
        }

        .expand-row {
          margin: 5px;
        }

        &:after {
          width: 0px;
        }
      }
    }

    .bottom-btn {
      padding-left: 5px;
    }
  }
</style>
