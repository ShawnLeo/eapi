<template>
  <div class="project-list">
    <Card class="fr right-content">
      <Tabs value="name1">
        <TabPane label="项目管理" name="name1">
          <Spin size="large" fix v-if="loading"></Spin>
          <div style="display: flex">
            <Card style="width:240px;margin: 15px;" v-for="(project, index) in projects" :key="index">
              <a href="#" slot="extra">
                <Icon type="ios-trash-outline" @click.native="handleRemove(project.id)"></Icon>
              </a>
              <div style="text-align:center" @click="goInterface(project.id)">
                <img width="80" height="80" style="border-radius:  50%;" src="../../assets/logo.png">
                <h3>{{project.title}}</h3>
              </div>
            </Card>
            <Card style="width:240px;margin: 15px;text-align: center">
              <div style="text-align:center" @click="newProject">
                <!--<Icon type="ios-plus-empty" size="100"></Icon>-->
                <Icon type="ios-add" size="100" />
              </div>
            </Card>
          </div>
        </TabPane>
        <TabPane label="团队管理" name="name2">开发中</TabPane>
      </Tabs>
    </Card>

    <Modal v-model="addProjectModal"
      title="新建项目" width="700">
      <Form ref="tagFormItem" :model="formItem" :label-width=120 :rules="ruleValidate" >
        <FormItem label="项目名称" prop="title">
          <i-input v-model="formItem.title" placeholder="项目名称"></i-input>
        </FormItem>
        <FormItem label="项目描述" prop="description">
          <i-input v-model="formItem.description" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="最多20个中文或者40个英文字符"></i-input>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" size="large" @click="reset">重置</Button>
        <Button type="primary" size="large" @click="addProject">确定</Button>
      </div>
    </Modal>
  </div>
</template>

<script type="text/ecmascript-6">
  import { getProjectList, checkProjectExists, createProject, deleteProjectById } from '../../utils/const';
  export default {
    data () {
      const validateTitleExists = (rule, value, callback) => {
        checkProjectExists(this.formItem, (response) => {
          if (response.header.code === '0') {
            if (response.body) {
              callback(new Error('标签已存在'));
            } else {
              callback();
            }
          } else {
            callback(new Error(response.header.message));
          }
        });
      };
      return {
        loading: true,
        addProjectModal: false,
        projects: [],
        formItem: {
          title: '',
          description: ''
        },
        ruleValidate: {
          title: [
            { required: true, message: '请输入标签名称', trigger: 'blur' },
            { validator: validateTitleExists, trigger: 'blur' }
          ],
          description: [
            { required: true, message: '请输入描述', trigger: 'blur' }
          ]
        }
      };
    },
    methods: {
      init() {
        this.getProjectList();
      },
      getProjectList: async function() {
        this.loading = true;
        await getProjectList((response) => {
          if (response.header.code === '0') {
            this.projects = response.body;
          } else {
            this.$Message.error(response.header.message);
          }
          this.loading = false;
        });
      },
      goInterface(id) {
        this.$store.dispatch('projectId', id);
        this.$router.push({path: '/project/interface'});
      },
      reset() {
        this.formItem = {
          title: '',
          description: ''
        };
      },
      addProject: function() {
        this.$refs['tagFormItem'].validate(async (valid) => {
          if (valid) {
            await createProject(this.formItem, (response) => {
              if (response.header.code === '0') {
                this.init();
                this.reset();
              } else {
                this.$Message.error(response.header.message);
              }
            });
            this.addProjectModal = false;
          }
        });
      },
      newProject() {
        this.addProjectModal = true;
      },
      handleRemove(id) {
        this.$Modal.confirm({
          title: '删除项目',
          content: '<p>将删除项目下所有内容且不可恢复！</p><p>你确认要删除码？</p>',
          onOk: () => {
            deleteProjectById({id: id}, (response) => {
              if (response.header.code === '0') {
                this.$Message.success('删除成功！');
                this.showEditMenus = false;
                this.init();
              } else {
                this.$Message.error(response.header.message);
              }
            });
          },
          onCancel: () => {
            this.$Message.info('取消删除！');
          }
        });
      }
    },
    mounted() {
      this.init();
    }
  };
</script>

<style>
</style>
