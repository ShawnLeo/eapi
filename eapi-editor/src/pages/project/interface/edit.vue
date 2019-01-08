<template>
  <div class="project-interface-edit">
    <Form ref="interfaceItem" :model="interfaceItem" :label-width=90 :rules="ruleValidate">
      <Spin size="large" fix v-if="spinShow"></Spin>
      <div style="width:90%;margin:0 auto;padding-top:30px;">
        <Row class="interface-detail">
          <i-col span="13" style="border-right: 1px solid #dddee1;padding-right: 30px;">
            <FormItem prop="name" :label-width=-1 >
              <i-input v-model="interfaceItem.name" placeholder="Enter something..." class="login-input"></i-input>
            </FormItem>
            <FormItem label="标签" style="margin-top:20px;">
              <Select v-model="interfaceItem.tagIds" multiple require="true">
                <Option v-for="(tag, index) in tags" :value="tag.id" :key="index">{{tag.name}}</Option>
              </Select>
            </FormItem>
            <FormItem label="代码映射" style="margin-top:20px;">
              <i-input v-model="interfaceItem.operationId" placeholder="Enter something..."></i-input>
            </FormItem>
            <FormItem label="描述">
              <i-input v-model="interfaceItem.description" type="textarea" :autosize="{minRows: 2,maxRows: 5}"
                       placeholder="描述"></i-input>
            </FormItem>
          </i-col>
          <i-col span="11">
            <FormItem label="接口状态" style="margin-top:20px;">
              <Select v-model="interfaceItem.status" require="true">
                <Option v-for="(item, index) in status" :value="item.value" :key="index">{{item.label}}</Option>
              </Select>
            </FormItem>
            <FormItem label="创建人">
              <i-input v-model="interfaceItem.createrUserName" placeholder="Enter something..." disabled></i-input>
            </FormItem>
          </i-col>
        </Row>
      </div>
      <div class="request-tabs" style="width:90%;margin:0 auto;">
        <Tabs value="name1">
          <TabPane label="请求信息" name="name1" style="margin-top:30px;">
            <request :interfaceItem="interfaceItem"></request>
          </TabPane>
          <TabPane label="响应数据" name="name2">
            <response :interfaceItem="interfaceItem"></response>
          </TabPane>
          <TabPane label="操作日志" name="name3">开发中...</TabPane>
        </Tabs>
      </div>
      <FormItem>
        <Button type="primary" size="large" @click="updateInterface">保存</Button>
      </FormItem>
    </Form>
  </div>
</template>

<script type="text/ecmascript-6">

  import request from './request.vue';
  import response from './response.vue';
  import {reverse} from '../../../utils/utils';
  import {getTagList, getInterfaceById, updateInterface, checkInterfaceExists} from '../../../utils/interface';
  import {getStore} from '../../../utils/storage';

  export default {
    data() {
      const validateNameExists = (rule, value, callback) => {
        checkInterfaceExists(this.interfaceItem, (response) => {
          if (response.header.code === '0') {
            if (response.body) {
              callback(new Error('接口已存在'));
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
        interfaceItem: {},
        interfaceItemTemp: {},
        tags: [],
				status: [
					{
						value: 100,
						label: '未开始'
					},
					{
						value: 200,
						label: '开发中'
					},
					{
						value: 300,
						label: '测试中'
					},
					{
						value: 400,
						label: '已完成'
					},
					{
						value: 500,
						label: '已废弃'
					}
				],
        ruleValidate: {
          name: [
            {required: true, message: '请输入接口名称', trigger: 'blur'}
          ],
          path: [
            {required: true, message: '请输入接口路径', trigger: 'blur'},
            {validator: validateNameExists, trigger: 'blur'}
          ],
          method: [
            {required: true, message: '请输入接口路径', trigger: 'blur'},
            {validator: validateNameExists, trigger: 'change'}
          ]
        }
      };
    },
    methods: {
      init() {
        this.getTagList();
        this.getInterfaceById();
      },
      getInterfaceById() {
        getInterfaceById({
          id: this.$route.query.id
        }, (response) => {
          if (response.header.code === '0') {
            this.interfaceItem = response.body;
            if (this.interfaceItem.body) {
              reverse(this.interfaceItem.body);
            }
            if (this.interfaceItem.responseBody) {
              reverse(this.interfaceItem.responseBody);
            }
            // this.getTagIds();
          } else {
            this.$Message.error(response.header.message);
          }
        });
      },
//    getTagIds() {
//      this.interfaceItem.tagIds = [];
//      this.interfaceItem.tags.forEach((tag) => {
//        this.interfaceItem.tagIds.push(tag.id);
//      });
//    },
      getTagList: async function () {
        let projectId = this.state.projectId || getStore('projectId');
        await getTagList({projectId: projectId}, (response) => {
          if (response.header.code === '0') {
            this.tags = response.body;
          } else {
            this.$Message.error(response.header.message);
          }
        });
      },
      updateInterface() {
        // 判断该值是否发生变化
        this.$refs['interfaceItem'].validate(async (valid) => {
          if (valid) {
            this.spinShow = true;
            updateInterface(this.interfaceItem, (response) => {
              if (response.header.code === '0') {
                this.$Message.success('更新成功！');
                this.init();
              } else {
                this.$Message.error(response.header.message);
              }
              this.spinShow = false;
            });
          }
        });
      }
    },
    computed: {
      state() {
        return this.$store.state.app;
      }
    },
    components: {
      request,
      response
    },
    mounted() {
      this.init();
    }
  };
</script>

<style lang="less">
  .project-interface-edit {
    .interface-detail {
      .ivu-input {
        border: none;
      }
      .ivu-select-selection {
        border: none;
      }
      .ivu-icon-arrow-down-b:before {
        content: '';
      }
      .login-input {
        .ivu-input {
          font-size: 24px;
          color: #1188FC;
          margin-left: 30px;
        }
      }
    }
    .ivu-tabs {
      overflow-y: visible;
      .ivu-tabs-tabpane {
        width: 99.99%
      }
    }
  }
</style>
