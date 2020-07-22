<template>
  <div class="search-list main-content">
    <Card class="fl left-content">
      <Menu ref="groupMenus" theme="light" :active-name="activeName" width="152px" @on-select="selectMenu">
        <Menu-Item name="all">项目组</Menu-Item>
        <Menu-Item name="MEME2C_PROJECT">项目</Menu-Item>
      </Menu>
    </Card>

    <Card class="fr right-content">
      <Tabs v-model="tabSelect">
        <TabPane label="搜索结果(5)" name="result" style="padding: 10px 20px">
          <ul class="search-ul">
            <li>
              <h1>美美理财</h1>
              <p><b>简介：</b> 美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财</p>
              <p><b>创建时间：</b>2018-08-04 <Button type="primary" class="add-button">加入项目组</Button></p>
            </li>
            <li>
              <h1>美美理财</h1>
              <p><b>简介：</b>美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财</p>
              <p><b>创建时间：</b>2018-08-04 <Button type="primary" class="add-button">加入项目组</Button></p>
            </li>
            <li>
              <h1>美美理财</h1>
              <p><b>简介：</b> 美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财</p>
              <p><b>创建时间：</b>2018-08-04 <Button type="primary" class="add-button">加入项目组</Button></p>
            </li>
            <li>
              <h1>美美理财</h1>
              <p><b>简介：</b> 美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财</p>
              <p><b>创建时间：</b>2018-08-04 <Button type="primary" class="add-button">加入项目组</Button></p>
            </li>
            <li>
              <h1>美美理财</h1>
              <p><b>简介：</b> 美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财美美理财</p>
              <p><b>创建时间：</b>2018-08-04 <Button type="primary" class="add-button">加入项目组</Button></p>
            </li>
          </ul>
          <Page :total="100" show-sizer  style="float:right;margin-top:30px;"/>
        </TabPane>
      </Tabs>
    </Card>

  </div>
</template>

<script type="text/ecmascript-6">
  import { getProjectList, checkProjectExists, createProject, deleteProjectById, groupAdd, groupList, getCurrUserRole, groupGet } from '../../utils/interface';
  import group from '../../components/project/group.vue';
  import member from '../../components/project/member.vue';
  import permission from '../../components/project/permission.vue';
  export default {
		components: {},
    data () {
      return {
				activeName: 'all',
        loading: true,
				tabSelect: 'result'
      };
    },
    methods: {
      init() {
      },
			selectMenu(name) {
			},
			quitGroup() {
			}
    },
		watch: {
			groupId: {
				handler: function (val, oldVal) {}
			}
		},
    mounted() {
      this.init();
    }
  };
</script>

<style lang="less">
  .search-list{
    .search-ul{
      li{
        padding: 20px 0px ;
        border-bottom: 1px solid #dcdee2;
        h1{
          /*color: #1188FC;*/
        }
        p{
          line-height: 30px;
          .add-button{
            float: right;
          }
        }
      }
    }
    .ivu-tabs-nav {
      height: 60px;
      line-height: 40px;
    }
  }


</style>
