<template>
  <div class="project-list main-content">
    <!--<Card class="fl left-content">-->
      <!--<a href="#" slot="extra" class="add-group" @click="addGroupModal = true"><Icon type="logo-googleplus" /> 添加</a>-->
      <!--<Menu ref="groupMenus" theme="light" :active-name="groupId" width="152px" @on-select="selectMenu">-->
        <!--<MenuGroup title="项目组">-->
          <!--<Menu-Item name="all">全部项目组</Menu-Item>-->
          <!--<Menu-Item :name="group.id" v-for="(group, index) in groups" :key="index">{{group.name}} </Menu-Item>-->
        <!--</MenuGroup>-->
      <!--</Menu>-->
    <!--</Card>-->

    <!--<Card class="fr right-content">-->
    <Card class="">
      <Tabs v-model="tabSelect">
        <TabPane label="项目管理" name="pm">
          <Spin size="large" fix v-if="loading"></Spin>
          <div class="project-box">
            <Card  class="project-item" v-for="(project, index) in projects" :key="index">
              <div style="text-align:center" @click="goInterface(project.id)">
                <div class="avatar">
                <Avatar class="project-avatar" size="large" >{{project.title.substring(0, 1)}}</Avatar>
                </div>
                <br>
                <h3>{{project.title}}</h3>
              </div>
            </Card>
            <!--<Card style="width:220px;margin: 15px;text-align: center;cursor: pointer;" v-if="groupId !=='all'">-->
            <Card style="width:220px;margin: 15px;text-align: center;cursor: pointer;">
              <div style="text-align:center;line-height: 10;" @click="newProject">
                <Icon type="md-add" size="100"></Icon>
              </div>
            </Card>
          </div>
          <div class="no-project" v-if="projects.length === 0 && groupId ==='all'">
            暂无可用项目
          </div>
        </TabPane>

        <!--<TabPane label="成员管理" name="um" style="padding: 10px 20px" v-if="groupId !== 'all'">-->
          <!--<member :groupId="groupId" :currUserRole="currUserRole" :group="selectGroup" v-on:change="getCurrUserRole(groupId)" v-on:quit="quitGroup()"></member>-->
        <!--</TabPane>-->

        <!--<TabPane label="设置" name="gs" style="padding: 10px 20px" v-if="groupId !== 'all'">-->
          <!--<group :groupId="groupId" :currUserRole="currUserRole" :group="selectGroup" v-on:update="getGroupList()" v-on:quit="quitGroup()"></group>-->
        <!--</TabPane>-->

        <!--&lt;!&ndash;创建者 和 管理员 才显示&ndash;&gt;-->
        <!--<TabPane label="权限控制" name="ac" style="padding: 10px 20px" v-if="groupId !== 'all' && (currUserRole === '1' || currUserRole === '2')">-->
          <!--<permission :groupId="groupId" :group="selectGroup"></permission>-->
        <!--</TabPane>-->
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
					this.$nextTick(() => { this.$refs.groupMenus.updateActiveName(); });
          this.loading = false;
        });
      },
      goInterface(id) {
        // 项目
        this.$store.dispatch('projectId', id);
        // 项目组
        this.$router.push({path: '/project/interface'});
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
