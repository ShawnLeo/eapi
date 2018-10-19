<template>
  <div class="project">
    <Card class="fr right-content clearfix">
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
          <!--<Menu-Item name="/project/regular">-->
            <!--{{$t('subMenus.BusinessApplication')}}-->
          <!--</Menu-Item>-->
          <Menu-Item name="/project/settings">
            管理
          </Menu-Item>
        </Menu>
        <div class="tab-bar">
          <Button type="info" icon="ios-play" class="fr sub-menu-button" @click="publish">发布</Button>
          <Button type="success" icon="ios-eye" class="fr sub-menu-button" @click="preview">预览</Button>
          <!--<Dropdown class="fr sub-menu-button">-->
            <!--<Button icon="forward" type="ghost">-->
              <!--导出-->
              <!--<Icon type="arrow-down-b"></Icon>-->
            <!--</Button>-->
            <!--<DropdownMenu slot="list">-->
              <!--<DropdownItem>swagger文档</DropdownItem>-->
            <!--</DropdownMenu>-->
          <!--</Dropdown>-->
        </div>
        <div class="clearfix"></div>
      </div>
      <!--<Tabs :value="activeTabName" @on-click="onTabClick">-->
          <!--<TabPane label="接口" name="/project/interface"></TabPane>-->
          <!--<TabPane label="数据模型" name="/project/datamodel"></TabPane>-->
          <!--<TabPane label="标签管理" name="/project/tags"></TabPane>-->
          <!--&lt;!&ndash; <TabPane label="规则函数" name="/project/regular"></TabPane> &ndash;&gt;-->
          <!--<TabPane label="设置" name="/project/settings"></TabPane>-->
      <!--</Tabs>-->
      <router-view/>
    </Card>
  </div>
</template>

<script type="text/ecmascript-6">
  import {getStore} from '../../utils/storage';
  import { publishProject } from '../../utils/const';
export default {
  data () {
    return {
      activeTabName: this.$route.path
    };
  },
  methods: {
    selectMenu(name) {
      this.$router.push({path: name});
    },
    preview() {
      let projectId = getStore('projectId');
//      let href = this.$router.resolve('/swagger');
      window.open('#/swagger?projectId=' + projectId, '_blank');
    },
    publish() {
      let projectId = getStore('projectId');
      publishProject({id: projectId}, (response) => {
        if (response.header.code === '0') {
          this.$Message.success(response.body);
        } else {
          this.$Message.error(response.header.message);
        }
        this.loading = false;
      });
    }
  }
};
</script>

<style lang="less">
  .project {
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
