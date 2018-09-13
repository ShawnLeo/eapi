<template>
  <div class="components-project-hashTable">
    <Icon type="arrow-up-b" style="position:relative;left:180px;top:-6px; z-index:2;color:#dddee1;" v-show="showIcon"></Icon>
    <Table :columns="columns10" :data="rows" class="datamodel-table"></Table>
  </div>
</template>
<script>
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
          }
        ]
      };
    },
    methods: {
      init() {}
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
