<template>
  <div class="project">

    <div class="title">
      <h2><Icon type="ios-bookmarks-outline" />  {{state.project.title}}</h2>
      <div></div>
    </div>

    <Card class="right-content clearfix">
      <div class="sub-menu">
        <Menu ref="mainNav" mode="horizontal" theme="light" :active-name="$route.meta.subMenuActive" class="fl"
              @on-select="selectMenu">
          <Menu-Item name="/project/interface">
            接口
          </Menu-Item>
          <Menu-Item name="/project/datamodel">
            数据模型
          </Menu-Item>
          <Menu-Item name="/project/tags">
            标签管理
          </Menu-Item>
          <Menu-Item name="/project/settings">
            管理
          </Menu-Item>
        </Menu>
        <div class="tab-bar">
          <Button type="info" icon="ios-play" class="fr sub-menu-button" @click="publish">发布</Button>
          <Button type="success" icon="ios-eye" class="fr sub-menu-button" @click="preview">预览</Button>
        </div>
        <div class="clearfix"></div>
      </div>
      <router-view></router-view>
    </Card>
  </div>
</template>

<script type="text/ecmascript-6">
  import {getStore} from '../../utils/storage';
  import { publishProject, getProjectById, getCustomDataModelList, getDataModelList } from '../../utils/interface';
export default {
  data () {
    return {
      activeTabName: this.$route.path
    };
  },
	computed: {
		state() {
			return this.$store.state.app;
		},
		projectId(){
			return this.$store.state.app.projectId
    }
	},
  methods: {
    selectMenu(name) {
      this.$router.push({path: name});
    },
    preview() {
      window.open('#/swagger?projectId=' + this.projectId, '_blank');
    },
    publish() {
      publishProject({id: this.projectId}, (response) => {this.$Message.success(response.body);});
    },
		getProjectById: async function() {
			await getProjectById({id: this.projectId}, (response) => {this.$store.dispatch('project', response.body);});
		},
		getSystemDataModelList: function () {
			getDataModelList({type: 'system'}, (response) => {this.$store.dispatch('systemDataModel', response.body);});
		},
		getDataModelList: function () {
			getCustomDataModelList({projectId: this.projectId}, (response) => {this.$store.dispatch('customDataModel', response.body);});
		}
  },
	mounted() {
		if (!this.$store.state.app.projectId) {
			this.$store.dispatch('projectId', getStore('projectId'));
		}
		this.getProjectById();
		this.getSystemDataModelList(); // 系统默认数据结构
		this.getDataModelList(); // 自定义数据结构
	}
};
</script>

<style lang="less">
  .project {
    .title{
      margin-bottom: 15px;
    }
    .right-content{
      .sub-menu{
        border-bottom: 1px solid #e9eaec;
        .ivu-menu {
          &:after {
            background: none;
          }
        }
        .tab-bar {
          float: right;
          /*width: 50%;*/
          position: relative;
          top: 16px;
          .sub-menu-button{
            margin-left: 15px;
          }
        }
      }
    }
  }
</style>
