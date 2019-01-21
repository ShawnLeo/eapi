<template>
  <div class="project-list main-content">
    <Card class="fl left-content">
      <a href="#" slot="extra" class="add-group" @click="addGroupModal = true"><Icon type="md-add" /></a>
      <Menu ref="coinMenus" theme="light" active-name="DEFAULT_PROJECT" width="152px" @on-select="selectMenu">
        <MenuGroup title="项目组">
          <Menu-Item name="DEFAULT_PROJECT">默认组 </Menu-Item>
          <Menu-Item name="MEME2C_PROJECT">美美理财组</Menu-Item>
          <Menu-Item name="CONSUMPTION_PROJECT">美美消费组</Menu-Item>
          <!--<Menu-Item v-for="(baseCoin, index) in baseCoinNoList" :name="'BUY_' + baseCoin.coinNo" :key="index">-->
            <!--{{baseCoin.coinNo}}-->
          <!--</Menu-Item>-->
        </MenuGroup>
        <!--<MenuGroup :title="$t('tradeFloor.wantSell')">-->
          <!--<Menu-Item v-for="(baseCoin, index) in baseCoinNoList" :name="'SELL_' + baseCoin.coinNo" :key="index">-->
            <!--{{baseCoin.coinNo}}-->
          <!--</Menu-Item>-->
        <!--</MenuGroup>-->
      </Menu>
    </Card>

    <Card class="fr right-content">
      <Tabs value="name1">
        <TabPane label="项目管理" name="name1">
          <Spin size="large" fix v-if="loading"></Spin>
          <div class="project-box">
            <Card style="width:220px;margin: 15px;;cursor: pointer;" v-for="(project, index) in projects" :key="index">
              <div style="text-align:center" @click="goInterface(project.id)">
                <Avatar class="project-avatar" size="large" >{{project.title.substring(0, 1)}}</Avatar>
                <br>
                <br>
                <h3>{{project.title}}</h3>
              </div>
            </Card>
            <Card style="width:220px;margin: 15px;text-align: center;cursor: pointer;">
              <div style="text-align:center" @click="newProject">
                <Icon type="md-add" size="100"></Icon>
              </div>
            </Card>
          </div>
        </TabPane>
        <TabPane label="成员管理" name="name2" style="padding: 10px 20px">
          <member></member>
        </TabPane>

        <TabPane label="权限控制" name="name3" style="padding: 10px 20px">
          <permission></permission>
        </TabPane>

        <TabPane label="设置" name="name4" style="padding: 10px 20px">
          <group></group>
        </TabPane>
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

    <Modal v-model="addGroupModal"
           title="新建项目组" width="700">
      <Form ref="tagFormItem" :model="group" :label-width=120 :rules="ruleValidate" >
        <FormItem label="项目组名称" prop="title">
          <i-input v-model="group.title" placeholder="项目名称"></i-input>
        </FormItem>
        <FormItem label="项目组描述" prop="description">
          <i-input v-model="group.description" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="最多20个中文或者40个英文字符"></i-input>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" size="large" @click="addGroupModal = false">取消</Button>
        <Button type="primary" size="large" @click="addGroup">确定</Button>
      </div>
    </Modal>
  </div>
</template>

<script type="text/ecmascript-6">
  import { getProjectList, checkProjectExists, createProject, deleteProjectById } from '../../utils/interface';
  import group from '../../components/project/group.vue';
  import member from '../../components/project/member.vue';
  import permission from '../../components/project/permission.vue';
  export default {
		components: {
			group,
			member,
			permission
		},
    data () {
      const validateTitleExists = (rule, value, callback) => {
        checkProjectExists(this.formItem, (response) => {
          if (response.header.code === '0') {
            if (response.body) {
              callback(new Error('项目已存在！'));
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
				addGroupModal: false,
        projects: [],
				group: {
					title: '',
					description: ''
				},
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
          this.projects = response.body;
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
			addGroup() {

      },
			selectMenu() {

      }
    },
    mounted() {
      this.init();
    }
  };
</script>

<style lang="less">
  .project-list{
    .ivu-card-extra{
      z-index: 999;
    }
    .project-box{
      display: flex;
      display: -webkit-flex;
      flex-wrap:wrap;
      .project-avatar {
        width: 60px;
        height: 60px;
        border-radius: 30px;
        font-size: 24px;
        padding: 10px;
      }
    }
    .ivu-tabs-nav {
      height: 60px;
      line-height: 40px;
    }
  }


</style>
