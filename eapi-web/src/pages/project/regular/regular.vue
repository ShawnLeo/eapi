<template>
  <div class="project-regular">
    <Form ref="formInline" inline class="project-regular-form">
      <FormItem>
        <!--<Button type="primary" @click="handleSubmit('formInline')">Signin</Button>-->
        <Button type="primary" icon="md-add">新建规则</Button>
      </FormItem>
      <FormItem prop="search">
        <Input type="text" placeholder="搜索"/>
      </FormItem>
    </Form>
    <div class="clearfix"></div>
    <Form ref="formInline" inline class="project-regular-form form-in-table" v-show="showEditMenus">
      <Button>删除</Button>
      <Button>设置状态</Button>
      <Button>设置标签</Button>
    </Form>
    <Table stripe ref="selection" :columns="columns" :data="data" @on-selection-change="onCelectionChange"></Table>
  </div>
</template>

<script type="text/ecmascript-6">
export default {
  data () {
    return {
      showEditMenus: false,
      columns: [
        {
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
          key: 'describe'
        },
        {
          title: '分组',
          key: 'group'
        },
        {
            title: '创建人',
            key: 'creater'
        },
        {
            title: '创建时间',
            key: 'createTime'
        }
      ],
      data: [
          {
              name: 'bool',
              describe: '随机布尔值',
              group: '系统默认',
              creater: 'shawn',
              createTime: '2018-04-23'
          },
          {
              name: 'chinese',
              describe: '随机中文字符串',
              group: '系统默认',
              creater: 'shawn',
              createTime: '2018-04-23'
          },
          {
              name: 'id',
              describe: '唯一标识',
              group: '系统默认',
              creater: 'shawn',
              createTime: '2018-04-23'
          },
          {
              name: 'loop',
              describe: '指定层数的数据',
              group: '系统默认',
              creater: 'shawn',
              createTime: '2018-04-23'
          },
          {
              name: 'email',
              describe: '随机邮箱地址',
              group: '系统默认',
              creater: 'shawn',
              createTime: '2018-04-23'
          },
          {
              name: 'null',
              describe: 'null值',
              group: '系统默认',
              creater: 'shawn',
              createTime: '2018-04-23'
          },
          {
              name: 'num',
              describe: '随机数字',
              group: '系统默认',
              status: 400,
              creater: 'shawn',
              createTime: '2018-04-23'
          },
          {
              name: 'repeat',
              describe: '指定元素个数的数组',
              group: '系统默认',
              status: 400,
              creater: 'shawn',
              createTime: '2018-04-23'
          },
          {
              name: 'str',
              describe: '随机字符串',
              group: '系统默认',
              status: 400,
              creater: 'shawn',
              createTime: '2018-04-23'
          },
          {
              name: 'url',
              describe: '随机URL地址',
              group: '系统默认',
              status: 400,
              creater: 'shawn',
              createTime: '2018-04-23'
          },
          {
              name: 'var',
              describe: '随机类型的值',
              group: '系统默认',
              status: 400,
              creater: 'shawn',
              createTime: '2018-04-23'
          }
      ]
    };
  },
  methods: {
    onCelectionChange(selection) {
      this.showEditMenus = selection.length !== 0;
      // console.log(selection);
    }
  }
};
</script>

<style lang="less">
  .project-regular{
    .project-regular-form{
      float: right;
      .ivu-form-item{
        margin-bottom: 0px
      }
    }
    .form-in-table{
      width: 890px;
      background: #ffffff;
      position: absolute;
      z-index: 2;
      left: 75px;
      top: 124px
    }
  }
</style>
