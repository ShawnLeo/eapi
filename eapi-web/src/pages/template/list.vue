<template>
  <div class="project-list main-content">
    <Card class="">
      <Tabs v-model="tabSelect">
        <TabPane label="后端模板" name="pm">
          <div class="project-box">
            <Card  class="project-item">
              <div style="text-align:center" @click="goInterface()">
                <!--<h3>Spring Boot</h3>-->
                <div class="avatar">
                  <div class="logo spring-boot"/>
                </div>
                <br>
                <h3>Spring Boot</h3>
              </div>
            </Card>

            <Card  class="project-item">
              <div style="text-align:center" @click="goInterface()">
                <!--<h3>Spring Boot</h3>-->
                <div class="avatar">
                  <div class="logo mybatis-logo"/>
                </div>
                <br>
                <h3>MyBatis</h3>
              </div>
            </Card>
          </div>
        </TabPane>

        <TabPane label="前端模板" name="um" style="padding: 10px 20px">

          <div class="project-box">

            <Card  class="project-item" >
              <div style="text-align:center" @click="goInterface()">
                <!--<h3>Spring Boot</h3>-->
                <div class="avatar">
                  <div class="logo vue-logo"/>
                </div>
                <br>
                <h3>Vue</h3>
              </div>
            </Card>
          </div>

        </TabPane>

      </Tabs>
    </Card>

    <Modal v-model="addProjectModal"
      title="新建项目" width="700">
      <Form ref="projectFormItem" :model="formItem" :label-width=120 :rules="ruleValidate" >
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
      <Form ref="groupFormItem" :model="group" :label-width=120 :rules="groupRuleValidate" >
        <FormItem label="项目组名称" prop="name">
          <i-input v-model="group.name" placeholder="项目名称"></i-input>
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
  import { getProjectList, checkProjectExists, createProject, deleteProjectById, groupAdd, groupList, getCurrUserRole, groupGet } from '../../utils/interface';
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
				activeName: 'all',
        loading: true,
        addProjectModal: false,
				addGroupModal: false,
        projects: [],
				groups: [],
        groupId: this.$route.query.groupId ? this.$route.query.groupId :'all',
				currUserRole: '', // 当前用户在项目组中角色
				group: {
					name: '',
					description: ''
				},
        selectGroup: {},
        formItem: {
          title: '',
          description: ''
        },
        ruleValidate: {
          title: [
            { required: true, message: '请输入项目名称', trigger: 'blur' },
            { validator: validateTitleExists, trigger: 'blur' }
          ],
          description: [
            { required: true, message: '请输入描述', trigger: 'blur' }
          ]
        },
				tabSelect: 'pm',
				groupRuleValidate: {
          name: [
            { required: true, message: '请输入项目组名称', trigger: 'blur' }
//            { validator: validateTitleExists, trigger: 'blur' }
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
        this.getGroupList();
      },
      getProjectList: function() {
        this.loading = true;
        // 获取该项目组下项目列表
        getProjectList({groupId: this.groupId}, (response) => {
          this.projects = response.body;
          this.loading = false;
        });
      },
      getCurrUserRole: function (groupId) {
				// 获取当前用户在该项目中的角色
				getCurrUserRole({groupId: groupId}, (response) => {
					this.currUserRole = response.body;
				});
			},
      getGroupList: async function() {
        this.loading = true;
        await groupList((response) => {
          this.groups = response.body;
					// this.$nextTick(() => { this.$refs.groupMenus.updateActiveName(); });
          this.loading = false;
        });
      },
      goInterface() {
        this.$router.push({path: '/template/edit'});
      },
      reset() {
        this.formItem = {
          title: '',
          description: ''
        };
        this.group = {
					name: '',
          description: ''
				};
      },
      addProject: function() {
				this.formItem.groupId = this.groupId;
        this.$refs['projectFormItem'].validate(async (valid) => {
          if (valid) {
            await createProject(this.formItem, (response) => {
							this.getProjectList();
              this.reset();
            });
            this.addProjectModal = false;
          }
        });
      },
      newProject() {
        this.addProjectModal = true;
      },
			addGroup() {
				this.$refs['groupFormItem'].validate(async (valid) => {
					if (valid) {
						await groupAdd(this.group, (response) => {
								this.init();
								this.reset();
						});
						this.addGroupModal = false;
					}
				});
			},
			selectMenu(name) {
				this.currUserRole = '';
				this.tabSelect = 'pm';
				this.groupId = name;
				this.getProjectList();
				if (name !== 'all') {
					this.getCurrUserRole(name);
        }
			},
			quitGroup() {
        window.location.reload();
			}
    },
		watch: {
			groupId: {
				handler: function (val, oldVal) {
					if (val === 'all') {
						return;
          }
					groupGet({id: this.groupId}, (resp) => {
						this.selectGroup = resp.body;
					});
				}
			}
		},
    mounted() {
      this.init();
    }
  };
</script>

<style lang="less">
  .project-list{
    min-height: 600px;
    .ivu-card-extra{
      z-index: 999;
    }
    .project-box{
      display: flex;
      display: -webkit-flex;
      flex-wrap:wrap;
      .project-item{
        width:260px;
        margin: 15px;
        cursor: pointer;
        background: url(../../assets/img/project_item.png) no-repeat;
        background-size:100% 100%;
        .avatar{
          padding: 20px;
          border-bottom: 1px solid #dcdee2;
          .project-avatar {
            width: 60px;
            height: 60px;
            border-radius: 30px;
            font-size: 24px;
            padding: 10px;

          }
          .logo{
            width: 180px;
            height: 60px;
            margin: 0 auto;
          }
          .spring-boot{
            background: url(../../assets/img/spring-logo.svg) no-repeat;
            background-size: contain;
          }
          .mybatis-logo{
            background: url(../../assets/img/mybatis-logo.png) no-repeat;
            background-size: contain;
          }
          .vue-logo{
            background: url(../../assets/img/vue-logo.png) no-repeat center;
            background-size: contain;
          }
        }
      }
    }
    .no-project{
      text-align: center;
      line-height: 120px;
    }
    .ivu-tabs-nav {
      height: 60px;
      line-height: 40px;
    }
  }


</style>
