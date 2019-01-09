<template>
  <div class="project-list">
    <Card class="fr right-content">
      <Tabs value="name1">
        <TabPane label=" 最近使用项目" name="name1">
          <Spin size="large" fix v-if="loading"></Spin>
          <div style="display: flex">
            <Card style="width:240px;margin: 15px;" v-for="(project, index) in projects" :key="index">
              <div style="text-align:center" @click="goInterface(project.id)">
                <img width="80" height="80" style="border-radius:  50%;" src="../../assets/img/logo2.png">
                <h3>{{project.title}}</h3>
              </div>
            </Card>
          </div>
        </TabPane>
        <!--<TabPane label="团队管理" name="name2">开发中</TabPane>-->
      </Tabs>
    </Card>
  </div>
</template>

<script type="text/ecmascript-6">
  import { getProjectList } from '../../utils/interface';
  export default {
    data () {
      return {
        loading: true,
        projects: []
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
      }
    },
    mounted() {
      this.init();
    }
  };
</script>

<style>
</style>
