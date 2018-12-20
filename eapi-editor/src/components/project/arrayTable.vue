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
  import { deleteDataModelInBatch } from '../../utils/interface';
  import expandRow from './hashTable.vue';
  import {getStore} from '../../utils/storage';
  export default {
      props: {
          rows: Array,
          level: Number,
          name: String,
          showIcon: Boolean
      },
      data() {
        return {
          columns10: [{
            type: 'expand',
            width: -1,
            className: 'expand-td',
            render: (h, params) => {
              return h(expandRow, {
                props: {
                  rows: params.row.children,
                  level: this.level + 1,
                  showIcon: true,
                  isArrayItem: params.row.dataType === 'array'
                },
                on: {
                  childrenChange: (children) => {
                    if (JSON.stringify(children) !== JSON.stringify(this.rows[params.index].children)) {
                      this.rows[params.index].children = children;
                    }
                  }
                }
              });
            }
          },
            {
              title: '元素类型',
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
                      value: params.row.dataType
                    },
                    on: {
                      'on-change': (value) => {
                        this.rows[params.index].dataType = value;
                        this.rows[params.index]._expanded = false;
                        this.rows[params.index].children = [];
                        if (value === 'object') {
//                          this.rows[params.index].children.push({
//                            name: '',
//                            description: '',
//                            dataType: 'string',
//                            example: '',
//														required: false,
//                            children: [],
//                            _expanded: false
//                          });
//                          setTimeout(() => {
//                            this.rows[params.index]._expanded = true;
//                          }, 100);
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
//                          this.rows[params.index].children.push({
//                            name: '',
//                            description: '',
//                            dataType: 'string',
//                            example: '',
//														required: false,
//                            children: [],
//                            _expanded: false
//                          });
//                          setTimeout(() => {
//                            this.rows[params.index]._expanded = true;
//                          }, 100);
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
