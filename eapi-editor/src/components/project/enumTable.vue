<template>
    <div class="components-project-hashTable">
      <Icon type="arrow-up-b" style="position:relative;left:180px;top:-6px; z-index:2;color:#dddee1;" v-show="showIcon"></Icon>
      <Table :columns="columns10" :data="rows" class="datamodel-table"></Table>
      <!-- <Form ref="formInline" inline>
        <Button>导入数据模型</Button>
        <Button>JSON导入</Button>
        <Button>在线接口导入</Button>
      </Form> -->
    </div>
</template>
<script>
  import { deleteDataModelInBatch } from '../../utils/const';
  export default {
      props: {
          rows: Array,
          level: Number,
          showIcon: Boolean
      },
      data() {
        return {
          columns10: [
            {
              title: '值',
              key: 'name',
              render: (h, params) => {
                return h('div', [
                  h('i-input', {
                    attrs: {
                      id: 'edit-name-' + params.index,
                      placeholder: '名称',
                      value: params.row.name
                    },
                    on: {
                      'on-blur': (event) => {
                        this.rows[params.index].name = event.target.value;
                      }
                    }
                  })
                ]);
              }
            },
            {
              title: '类型',
              key: 'dataType',
              render: (h, params) => {
                return h('div', [
                  h('Select', {
                    attrs: {
                      id: 'edit-name-' + params.index,
                      value: params.row.dataType
                    },
                    on: {
                      'on-change': (value) => {
                        this.rows[params.index].dataType = value;
                        if (value === 'object') {
                          this.rows[params.index].children.push({
                            name: '',
                            description: '',
                            dataType: 'string',
                            example: '',
														required: false,
                            children: [],
                            _expanded: false
                          });
                          this.rows[params.index]._expanded = true;
                        } else {
                          this.rows[params.index].children = [];
                          this.rows[params.index]._expanded = false;
                        }
                        return value;
                      }
                    }
                  },
                  [ h('Option', {
                        props: {
                            value: 'array'
                        }
                    }, 'array'),
                    h('Option', {
                        props: {
                            value: 'boolean'
                        }
                    }, 'boolean'),
                    h('Option', {
                        props: {
                            value: 'file'
                        }
                    }, 'file'),
                    h('Option', {
                        props: {
                            value: 'string'
                        }
                    }, 'string'),
                    h('Option', {
                        props: {
                            value: 'object'
                        }
                    }, 'object')
                  ])
                ]);
              }
            },
            {
              title: '键（可选）',
              key: 'address',
              render: (h, params) => {
                return h('div', [
                  h('i-input', {
                    attrs: {
                      id: 'edit-describe-' + params.index,
                      value: params.row.description,
                      placeholder: '描述'
                    }
                  })
                ]);
              }
            },
            {
              title: '描述',
              key: 'address',
              render: (h, params) => {
                return h('div', [
                  h('i-switch', {
                    attrs: {
                      id: 'edit-name-' + params.index
                      // value: params.row.name
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
													required: false,
                          children: [],
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
          // this.expand();
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
.datamodel-table{
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
    .ivu-table-wrapper, .ivu-page{
      margin-top: -20px;
    }
    .expand-row{
        margin: 5px;
    }
    // .expand-td{
    //   width: -1px;
    // }
  }
}
</style>
