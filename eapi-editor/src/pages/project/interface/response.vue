<template>
  <div class="response">
    <FormItem label="响应头">
      <Table :columns="headersColumns" :data="interfaceItem.responseHeader"></Table>
    </FormItem>

    <FormItem label="响应数据">
      <Table :columns="bodyColumns" :data="interfaceItem.responseBody"></Table>
    </FormItem>
  </div>
</template>

<script type="text/ecmascript-6">
  import hashTable from '../../../components/project/hashTable.vue';
  import { deleteResponseInBatch } from '../../../utils/interface';
  import {getStore} from '../../../utils/storage';
  export default {
    name: 'response',
    components: {
      hashTable
    },
    props: {
      interfaceItem: Object
    },
    data() {
      return {
        headersColumns: [{
          title: '名称',
          // key: 'name',
          render: (h, params) => {
            let _this = this;
            return h('i-input', {
              props: {
                placeholder: '请添加名称',
                value: params.row.dataModel.name
              },
              on: {
                'on-blur': (event) => {
                  _this.interfaceItem.responseHeader[params.index].dataModel.name = event.target.value;
                  // console.log(_this.interfaceItem.headers);
                }
              }
            });
          }
        },
//          {
//            title: '值',
//            // key: 'description',
//            render: (h, params) => {
//              return h('i-input', {
//                props: {
//                  placeholder: '请添加值',
//                  value: params.row.dataModel.example
//                },
//                on: {
//                  'on-blur': (event) => {
//                    this.interfaceItem.responseHeader[params.index].dataModel.example = event.target.value;
//                  }
//                }
//              });
//            }
//          },
          {
            title: '描述',
            // key: 'description',
            render: (h, params) => {
              return h('i-input', {
                props: {
                  placeholder: '请添加描述',
                  value: params.row.dataModel.description
                },
                on: {
                  'on-blur': (event) => {
                    this.interfaceItem.responseHeader[params.index].dataModel.description = event.target.value;
                  }
                }
              });
            }
          },
          {
            title: '添加',
            key: 'action',
            width: 60,
            align: 'center',
            renderHeader: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {
//                    type: 'ghost',
                    shape: 'circle',
                    size: 'small',
                    icon: 'md-add'
                  },
                  style: {
                    //                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.interfaceItem.responseHeader.push({
                        responseIn: 'header',
                        interfaceId: this.interfaceItem.id,
                        dataModel: {
                          name: '',
                          dataType: 'string',
                          type: 'unit',
                          description: '',
                          required: true,
                          example: ''
                        }
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
//                    type: 'ghost',
                    shape: 'circle',
                    size: 'small',
                    icon: 'md-remove'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
											this.interfaceItem.responseHeader.splice(params.index, 1);
//                      if (params.row.id) {
//                        this.deleteRequestInfo([params.row], () => {
//                          this.interfaceItem.responseHeader.splice(params.index, 1);
//                        });
//                      } else {
//                        this.interfaceItem.responseHeader.splice(params.index, 1);
//                      }
                    }
                  }
                })
              ]);
            }
          }
        ],
        bodyColumns: [{
          type: 'expand',
          width: -1,
//              className: 'expand-td',
          render: (h, params) => {
            return h(hashTable, {
              props: {
                rows: params.row.dataModel.children,
                name: this.name,
                level: this.level + 1,
                showIcon: true,
                isArrayItem: params.row.dataModel.dataType === 'array'
              },
              on: {
                childrenChange: (children) => {
                  // console.log('第' + this.level + '层组件的children：' + JSON.stringify(children));
                  if (JSON.stringify(children) !== JSON.stringify(this.interfaceItem.responseBody[params.index].dataModel.children)) {
                    this.interfaceItem.responseBody[params.index].dataModel.children = children;
                  }
                }
              }
            });
          }
        },
          {
            title: '名称',
            render: (h, params) => {
              return h('div', [
                h('i-input', {
                  attrs: {
                    id: 'edit-name-' + params.index,
                    placeholder: '名称',
                    value: params.row.dataModel.name
                  },
                  on: {
                    'on-blur': (event) => {
                      this.interfaceItem.responseBody[params.index].dataModel.name = event.target.value;
                    }
                  }
                })
              ]);
            }
          },
          {
            title: '类型',
            render: (h, params) => {
              let systemDataModel = JSON.parse(getStore('systemDataModel'));
              let customDataModel = JSON.parse(getStore('customDataModel'));
              let options = [];
              systemDataModel.forEach(datamodel => {
                options.push(h('Option', {
                  props: {
                    value: datamodel.dataType
                  }
                }, datamodel.dataType));
              });
              customDataModel.forEach(datamodel => {
                options.push(h('Option', {
                  props: {
                    value: datamodel.name
                  }
                }, datamodel.name));
              });
              return h('div', [
                h('Select', {
                  attrs: {
                    id: 'edit-name-' + params.index,
                    value: params.row.dataModel.dataType
                  },
                  on: {
                    'on-change': (value) => {
                      this.interfaceItem.responseBody[params.index].dataModel.dataType = value;
                      this.interfaceItem.responseBody[params.index]._expanded = false;
                      this.interfaceItem.responseBody[params.index].dataModel.children = [];
                      if (value === 'object') {
                        this.interfaceItem.responseBody[params.index].dataModel.children.push({
                          name: '',
                          description: '',
                          dataType: 'string',
                          example: '',
                          children: [],
													required: false,
                          _expanded: false
                        });
                        this.interfaceItem.responseBody[params.index]._expanded = true;
                      } else if (value === 'array') {
                        this.interfaceItem.responseBody[params.index].dataModel.children.push({
                          name: '',
                          description: '',
                          dataType: 'string',
                          example: '',
                          children: [],
													required: false,
                          _expanded: false
                        });
                        this.interfaceItem.responseBody[params.index]._expanded = true;
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
                      this.interfaceItem.responseBody[params.index].description = event.target.value;
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
                    id: 'edit-name-' + params.index
                    // value: params.row.dataModel.name
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
                    value: params.row.dataModel.example,
                    placeholder: '默认值'
                  },
                  on: {
                    'on-blur': (event) => {
                      this.interfaceItem.responseBody[params.index].dataModel.example = event.target.value;
                    }
                  }
                })
              ]);
            }
          },
          {
            title: '添加',
            key: 'action',
            width: 60,
            align: 'center',
            renderHeader: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {
//                    type: 'ghost',
                    shape: 'circle',
                    size: 'small',
                    icon: 'md-add'
                  },
                  style: {
                    //                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.interfaceItem.responseBody.push({
                        responseIn: 'schema',
                        interfaceId: this.interfaceItem.id,
                        dataModel: {
                          name: '',
                          description: '',
                          dataType: 'string',
                          example: '',
                          children: [],
													required: false,
                          _expanded: false
                        }
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
//                    type: 'ghost',
                    shape: 'circle',
                    size: 'small',
                    icon: 'md-remove'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      if (this.interfaceItem.responseBody.length <= 1) {
                        this.$Message.warning('请至少保留一条响应数据');
                        return;
                      }

											this.interfaceItem.responseBody.splice(params.index, 1);
//                      if (this.interfaceItem.responseBody[params.index].dataModel.id) {
//                        this.deleteRequestInfo([params.row], () => {
//                          this.interfaceItem.responseBody.splice(params.index, 1);
//                        });
////                        this.deleteDataModel([params.row], params.index);
//                      } else {
//                        this.interfaceItem.responseBody.splice(params.index, 1);
//                      }
                    }
                  }
                })
              ]);
            }
          }]
      };
    },
    methods: {
      requestTypeChange(value) {},
      deleteRequestInfo(row, callback) {
        deleteResponseInBatch(row, (response) => {
          if (response.header.code === '0') {
            this.$Message.success('删除成功！');
            callback();
          } else {
            this.$Message.error(response.header.message);
          }
        });
      }
    }
  };
</script>

<style lang="less">
  .response{
    .ivu-table{
      overflow: inherit;
      .ivu-table-overflowX {
        overflow-x: inherit;
      }
      td.ivu-table-expanded-cell{
        padding: 10px
      }
      .ivu-icon-ios-arrow-right:before{
        content: '';
      }
      .ivu-icon-ios-arrow-forward:before{
        content: '';
      }
      .ivu-table-wrapper, .ivu-page{
        margin-top: -20px;
      }
      .expand-row{
        margin: 5px;
      }
    }
  }
</style>
