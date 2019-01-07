<template>
  <div class="request">
    <FormItem label="方法路径" prop="method" style="width: 30%;float:left;">
      <Select v-model="interfaceItem.method" require="true">
        <Option value="get">GET</Option>
        <Option value="post">POST</Option>
        <Option value="put">PUT</Option>
        <Option value="delete">DELETE</Option>
      </Select>
      <!--<i-input v-model="interfaceItem.path" @keyup.native="setPathParams" placeholder="例如：/api/get/{id}" style="width: 80%;float:left;"></i-input>-->
    </FormItem>
    <FormItem prop="path" style="width:70%;float:left;" :label-width=-1>
      <i-input v-model="interfaceItem.path" @keyup.native="setPathParams" placeholder="例如：/api/get/{id}"></i-input>
    </FormItem>
    <div class="clearfix"></div>
    <FormItem>
      <span slot="label">
        Path参数
        <Tooltip content="定义在方法路径中。例如：/user/{id}" placement="right">
            <Icon type="ios-help-circle-outline" />
        </Tooltip>
      </span>
      <Table :columns="pathParamsColumns" :data="interfaceItem.pathParams"></Table>
    </FormItem>
    <FormItem label="Header参数">
      <Table :columns="headersColumns" :data="interfaceItem.headers"></Table>
    </FormItem>
    <FormItem>
      <span slot="label">
        Query参数
        <Tooltip content="Key-Value格式提交。例如：a=1&b=2" placement="right">
            <Icon type="ios-help-circle-outline" />
        </Tooltip>
      </span>
      <Table :columns="querysColumns" :data="interfaceItem.querys"></Table>
    </FormItem>
    <FormItem v-if="showRequsts">
      <span slot="label">
        请求数据
        <Tooltip content="body和formData不可同时选择" placement="right">
            <Icon type="ios-help-circle-outline" />
        </Tooltip>
      </span>
      <RadioGroup v-model="interfaceItem.requestType" @on-change="requestTypeChange">
        <Radio label="body">
          <span>
            body
            <Tooltip content="body不可添加文件类型数据" placement="bottom">
                <Icon type="ios-help-circle-outline" />
            </Tooltip>
          </span>
        </Radio>
        <Radio label="formData">
          <span>
            formData
            <Tooltip content="formData(表单提交)可以添加文件类型数据" placement="bottom">
                <Icon type="ios-help-circle-outline" />
            </Tooltip>
          </span>
        </Radio>
      </RadioGroup>

      <Table :columns="bodyColumns" :data="interfaceItem.body" v-if="interfaceItem.requestType === 'body'"></Table>
      <Table :columns="formDatasColumns" :data="interfaceItem.formDatas" v-if="interfaceItem.requestType === 'formData'"></Table>
      <!--<hash-table v-if="interfaceItem.requestType === 'body'"></hash-table>-->

    </FormItem>
    <!--<FormItem label="样例数据" v-if="requestType === 'body'">-->
      <!--<aceEditor v-model="content" lang="json" height="400" width="100%"></aceEditor>-->
    <!--</FormItem>-->
  </div>
</template>

<script type="text/ecmascript-6">
  import hashTable from '../../../components/project/hashTable.vue';
  import { deleteRequestInBatch } from '../../../utils/interface';
  import { pathParam } from '../../../utils/utils';
  import aceEditor from 'vue2-ace-editor';
  import {getStore} from '../../../utils/storage';
  import 'brace/theme/chrome';
  import 'brace/mode/json';
  export default {
    name: 'request',
    props: {
      interfaceItem: Object
    },
    data() {
      return {
        content: JSON.stringify({
          'type': 'object',
          'properties': {
            'first_name': {
              'type': 'string'
            },
            'last_name': {
              'type': 'string'
            },
            'full_name': {
              'type': 'string',
              'template': '{{fname}} {{lname}}',
              'watch': {
                'fname': 'first_name',
                'lname': 'last_name'
              }
            }
          }
        }, null, 2),
        requestType: '',
        showRequsts: false,
        pathParamsColumns: [{
          title: '名称',
          render: (h, params) => {
            return h('span', params.row.dataModel.name);
          }
        }, {
          title: '类型',
          key: 'dataType',
          render: (h, params) => {
            let options = [];
            options.push(h('Option', { props: { value: 'string' } }, 'string'));
            options.push(h('Option', { props: { value: 'boolean' } }, 'boolean'));
            options.push(h('Option', { props: { value: 'integer' } }, 'integer'));
            return h('div', [
              h('Select', {
                attrs: {
                  value: params.row.dataModel.dataType
                },
                on: {
                  'on-change': (value) => {
                    this.interfaceItem.pathParams[params.index].dataModel.dataType = value;
                    return value;
                  }
                }
              }, options)
            ]);
          }
        }, {
          title: '描述',
          render: (h, params) => {
            return h('i-input', {
              props: {
                placeholder: '请添加描述',
                value: params.row.dataModel.description
              },
              on: {
                'on-blur': (event) => {
                  this.interfaceItem.pathParams[params.index].dataModel.description = event.target.value;
                }
              }
            }, params.row.dataModel.description);
          }
        }],
        headersColumns: [{
          title: '名称',
          key: 'name',
          render: (h, params) => {
            return h('i-input', {
              props: {
                placeholder: '请添加名称',
                value: params.row.dataModel.name
              },
              on: {
                'on-blur': (event) => {
                  this.interfaceItem.headers[params.index].dataModel.name = event.target.value;
                  console.log(this.interfaceItem.headers);
                }
              }
            });
          }
        }, {
          title: '类型',
          key: 'dataType',
          render: (h, params) => {
            let options = [];
            options.push(h('Option', { props: { value: 'string' } }, 'string'));
            options.push(h('Option', { props: { value: 'boolean' } }, 'boolean'));
            options.push(h('Option', { props: { value: 'integer' } }, 'integer'));
            return h('div', [
              h('Select', {
                attrs: {
                  value: params.row.dataModel.dataType
                },
                on: {
                  'on-change': (value) => {
                    this.interfaceItem.headers[params.index].dataModel.dataType = value;
                    return value;
                  }
                }
              }, options)
            ]);
          }
        }, {
            title: '值',
            key: 'value',
            render: (h, params) => {
              return h('i-input', {
                props: {
                  placeholder: '请添加值',
                  value: params.row.dataModel.example
                },
                on: {
                  'on-blur': (event) => {
                    this.interfaceItem.headers[params.index].dataModel.example = event.target.value;
                  }
                }
              });
            }
          },
          {
            title: '描述',
            key: 'description',
            render: (h, params) => {
              return h('i-input', {
                props: {
                  placeholder: '请添加描述',
                  value: params.row.dataModel.description
                },
                on: {
                  'on-blur': (event) => {
                    this.interfaceItem.headers[params.index].dataModel.description = event.target.value;
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
                      this.interfaceItem.headers.push({
                        paramIn: 'header',
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
											this.interfaceItem.headers.splice(params.index, 1);
//                      if (params.row.id) {
//                        this.deleteRequestInfo([params.row], () => {
//                          this.interfaceItem.headers.splice(params.index, 1);
//                        });
//                      } else {
//                        this.interfaceItem.headers.splice(params.index, 1);
//                      }
                    }
                  }
                })
              ]);
            }
          }
        ],
        querysColumns: [{
          title: '名称',
          key: 'name',
          render: (h, params) => {
            return h('div', [
              h('i-input', {
                attrs: {
                  placeholder: '名称',
                  value: params.row.dataModel.name
                },
                on: {
                  'on-blur': (event) => {
                    this.interfaceItem.querys[params.index].dataModel.name = event.target.value;
                  }
                }
              })
            ]);
          }
        }, {
          title: '类型',
          key: 'dataType',
          render: (h, params) => {
            let options = [];
            options.push(h('Option', { props: { value: 'string' } }, 'string'));
            options.push(h('Option', { props: { value: 'boolean' } }, 'boolean'));
            options.push(h('Option', { props: { value: 'integer' } }, 'integer'));
            options.push(h('Option', { props: { value: 'file' } }, 'file'));
            return h('div', [
              h('Select', {
                attrs: {
                  value: params.row.dataModel.dataType
                },
                on: {
                  'on-change': (value) => {
                    this.interfaceItem.querys[params.index].dataModel.dataType = value;
                    return value;
                  }
                }
              }, options)
            ]);
          }
        }, {
            title: '值',
            key: 'value',
            render: (h, params) => {
              return h('i-input', {
                props: {
                  placeholder: '请添加值',
                  value: params.row.dataModel.example
                },
                on: {
                  'on-blur': (event) => {
                    this.interfaceItem.querys[params.index].dataModel.example = event.target.value;
                  }
                }
              });
            }
          },
          {
            title: '描述',
            key: 'description',
            render: (h, params) => {
              return h('i-input', {
                props: {
                  placeholder: '请添加描述',
                  value: params.row.dataModel.description
                },
                on: {
                  'on-blur': (event) => {
                    this.interfaceItem.querys[params.index].dataModel.description = event.target.value;
                  }
                }
              });
            }
          },
          {
            title: '必需',
            key: 'required',
            render: (h, params) => {
              return h('div', [
                h('i-switch', {
                  attrs: {
                    id: 'edit-name-' + params.index,
                    value: params.row.dataModel.required
                  },
                  on: {
                    'on-change': (value) => {
                      this.interfaceItem.querys[params.index].required = value;
                      this.interfaceItem.querys[params.index].dataModel.required = value;
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
              // let self = this;
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
                      this.interfaceItem.querys.push({
                        paramIn: 'query',
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
											this.interfaceItem.querys.splice(params.index, 1);
//                      if (params.row.id) {
//                        this.deleteRequestInfo([params.row], () => {
//                          this.interfaceItem.querys.splice(params.index, 1);
//                        });
//                      } else {
//                        this.interfaceItem.querys.splice(params.index, 1);
//                      }
                    }
                  }
                })
              ]);
            }
          }
        ],
        formDatasColumns: [{
          title: '名称',
          render: (h, params) => {
            return h('div', [
              h('i-input', {
                attrs: {
                  id: 'edit-form-data-name-' + params.index,
                  placeholder: '名称',
                  value: params.row.dataModel.name
                },
                on: {
                  'on-blur': (event) => {
                    this.interfaceItem.formDatas[params.index].dataModel.name = event.target.value;
                  }
                }
              })
            ]);
          }
        }, {
          title: '类型',
          render: (h, params) => {
            let options = [];
            options.push(h('Option', { props: { value: 'string' } }, 'string'));
            options.push(h('Option', { props: { value: 'boolean' } }, 'boolean'));
            options.push(h('Option', { props: { value: 'integer' } }, 'integer'));
            options.push(h('Option', { props: { value: 'file' } }, 'file'));
            return h('div', [
              h('Select', {
                attrs: {
                  id: 'edit-form-data-type-' + params.index,
                  value: params.row.dataModel.dataType
                },
                on: {
                  'on-change': (value) => {
                    this.interfaceItem.formDatas[params.index].dataModel.dataType = value;
                    return value;
                  }
                }
              }, options)
            ]);
          }
        }, {
            title: '值',
            key: 'value',
            render: (h, params) => {
              return h('i-input', {
                props: {
                  placeholder: '请添加值',
                  value: params.row.dataModel.example
                },
                on: {
                  'on-blur': (event) => {
                    this.interfaceItem.formDatas[params.index].dataModel.example = event.target.value;
                  }
                }
              });
            }
          },
          {
            title: '描述',
            key: 'description',
            render: (h, params) => {
              return h('i-input', {
                props: {
                  placeholder: '请添加描述',
                  value: params.row.dataModel.description
                },
                on: {
                  'on-blur': (event) => {
                    this.interfaceItem.formDatas[params.index].dataModel.description = event.target.value;
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
              // let self = this;
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
                      this.interfaceItem.formDatas.push({
                        paramIn: 'formData',
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
											this.interfaceItem.formDatas.splice(params.index, 1);
//                      if (params.row.id) {
//                        this.deleteRequestInfo([params.row], () => {
//                          this.interfaceItem.formDatas.splice(params.index, 1);
//                        });
//                      } else {
//                        this.interfaceItem.formDatas.splice(params.index, 1);
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
//                   console.log('最外层组件的children：' + JSON.stringify(children));
                  if (JSON.stringify(children) !== JSON.stringify(this.interfaceItem.body[params.index].dataModel.children)) {
                    this.interfaceItem.body[params.index].dataModel.children = children;
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
                      this.interfaceItem.body[params.index].dataModel.name = event.target.value;
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
                    value: params.row.dataModel.dataType,
                    filterable: true
                  },
                  on: {
                    'on-change': (value) => {
                      this.interfaceItem.body[params.index].dataModel.dataType = value;
                      this.interfaceItem.body[params.index]._expanded = false;
                      this.interfaceItem.body[params.index].dataModel.children = [];
                      if (value === 'object') {
                        this.interfaceItem.body[params.index].dataModel.children.push({
                          name: '',
                          description: '',
                          dataType: 'string',
                          example: '',
                          children: [],
													required: false,
                          _expanded: false
                        });
                        this.interfaceItem.body[params.index]._expanded = true;
                      } else if (value === 'array') {
                        this.interfaceItem.body[params.index].dataModel.children.push({
                          name: '',
                          description: '',
                          dataType: 'string',
                          example: '',
                          children: [],
													required: false,
                          _expanded: false
                        });
                        this.interfaceItem.body[params.index]._expanded = true;
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
                    value: params.row.dataModel.description,
                    placeholder: '描述'
                  },
                  on: {
                    'on-blur': (event) => {
                      this.interfaceItem.body[params.index].dataModel.description = event.target.value;
                    }
                  }
                })
              ]);
            }
          },
          {
            title: '必需',
            key: 'required',
            render: (h, params) => {
              return h('div', [
                h('i-switch', {
                  attrs: {
                    id: 'edit-name-' + params.index,
                    value: params.row.dataModel.required
                  },
                  on: {
                    'on-change': (value) => {
                      this.interfaceItem.querys[params.index].required = value;
                      this.interfaceItem.querys[params.index].dataModel.required = value;
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
                    value: params.row.dataModel.example,
                    placeholder: '默认值'
                  },
                  on: {
                    'on-blur': (event) => {
                      this.interfaceItem.body[params.index].dataModel.example = event.target.value;
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
                      this.interfaceItem.body.push({
                          paramIn: 'body',
                          interfaceId: this.interfaceItem.id,
                          dataModel: {
                            name: '',
                            description: '',
                            dataType: 'string',
                            example: '',
														required: false,
                            children: [],
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
											this.interfaceItem.body.splice(params.index, 1);
//                      if (this.interfaceItem.body[params.index].dataModel.id) {
//                        this.deleteRequestInfo([params.row], () => {
//                          this.interfaceItem.body.splice(params.index, 1);
//                        });
//                        this.deleteDataModel([params.row], params.index);
//                      } else {
//                        this.interfaceItem.body.splice(params.index, 1);
//                      }
                    }
                  }
                })
              ]);
            }
          }
          ]
      };
    },
    components: {
      aceEditor,
      hashTable
    },
    methods: {
      init() {
        setTimeout(() => {
					this.requestType = this.interfaceItem.requestType;
          if (this.interfaceItem.pathParams.length === 0) {
            this.setPathParams();
          }
        }, 500);
      },
//			changeMethod(value) {
//				this.$Modal.confirm({
//					title: '切换确认',
//					content: '<p>您确定要切换吗？</p><p>切换请求数据类型将会导致现有数据丢失！，确定切换成 “' + value + '” 类型吗？</p>',
//					onOk: () => {
////						this.requestType = this.interfaceItem.requestType;
//						this.$Message.info('Clicked ok');
//					},
//					onCancel: () => {
////						this.interfaceItem.requestType = this.requestType;
//						this.$Message.info('Clicked cancel');
//					}
//				});
//      	// 请求数据是否展示
//      },
      requestTypeChange(value) {
        this.$Modal.confirm({
          title: '切换确认',
          content: '<p>您确定要切换吗？</p><p>切换请求数据类型将会导致现有数据丢失！，确定切换成 “' + value + '” 类型吗？</p>',
          onOk: () => {
						if (value === 'body') {
							this.interfaceItem.formDatas = [];
            } else if (value === 'formData') {
							this.interfaceItem.body = [];
            }
						this.requestType = this.interfaceItem.requestType;
          },
          onCancel: () => {
						this.interfaceItem.requestType = this.requestType;
          }
        });
      },
      deleteRequestInfo(row, callback) {
        deleteRequestInBatch(row, (response) => {
          if (response.header.code === '0') {
            this.$Message.success('删除成功！');
            callback();
          } else {
            this.$Message.error(response.header.message);
          }
        });
      },
      setPathParams() {
        // 删掉旧的
        this.pathParamsDel(this.interfaceItem.pathParams);
        this.interfaceItem.pathParams = [];
        // 添加新的
        let pathParams = pathParam(this.interfaceItem.path);
        pathParams.forEach((item, index) => {
          this.interfaceItem.pathParams.push({
            paramIn: 'path',
            interfaceId: this.interfaceItem.id,
            dataModel: {
              name: item,
              dataType: 'string',
              type: 'unit',
              description: '',
              required: true,
              example: ''
            }
          });
        });
      },
      pathParamsDel (pathParams) {
        let deleteParams = [];
        pathParams.forEach((item) => {
          if (item.id) {
            deleteParams.push(item);
          }
        });
//        if (deleteParams.length > 0) {
//          this.deleteRequestInfo(deleteParams);
//        }
      }
    },
    mounted() {
      this.init();
    },
    watch: {
      'interfaceItem.method'(val, oldVal) {
				if (val.toLowerCase() === 'get') {
					this.interfaceItem.formDatas = [];
					this.interfaceItem.body = [];
					this.showRequsts = false;
				} else {
					this.showRequsts = true;
				}
      }
    }
  };
</script>

<style lang="less">
  .request{
    .ivu-table{
      overflow: inherit;
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
      .ivu-icon-ios-arrow-forward:before{
        content: '';
      }
      .ivu-table-wrapper, .ivu-page{
        margin-top: -32px;
      }
      .expand-row{
        margin: 5px;
      }
    }
  }
</style>
