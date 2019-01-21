<template>
  <div class="project-list">

    <!--<Card class="fr main-content">-->
      <!--<i-input v-model="value13" style="width: 80%;">-->
        <!--<Select v-model="select3" slot="prepend" style="width: 80px">-->
          <!--<Option value="group">项目组</Option>-->
          <!--<Option value="project">项目</Option>-->
        <!--</Select>-->
        <!--<Button slot="append" icon="ios-search"></Button>-->
      <!--</i-input>-->
    <!--</Card>-->

    <Card class="fr recent-project main-content right-content">
      <p slot="title">
        <Icon type="md-apps" />
        最近使用的项目
      </p>

      <div class="project-box">
        <Card style="width:240px;margin: 15px;cursor: pointer;" v-for="(project, index) in projects" :key="index">
          <div style="text-align:center" @click="goInterface(project.id)">
            <Avatar class="project-avatar" size="large" >{{project.title.substring(0, 1)}}</Avatar>
            <br>
            <br>
            <h4>{{project.title}}</h4>
          </div>
        </Card>
        <Card style="width:240px;margin: 15px;cursor: pointer;">
          <div style="text-align:center;"  @click="$router.push({path: '/project/list'})">
            <Avatar style="background: #f0faff;color: #2d8cf0;" class="project-avatar" size="large" >ALL</Avatar>
            <br>
            <br>
            <h4 style="color: #2d8cf0;">查看所有项目</h4>
            <!--<Icon type="md-add" size="100"></Icon>-->
          </div>
        </Card>
      </div>
      <Spin size="large" fix v-if="loading"></Spin>
      <!--<Tabs value="name1">-->
        <!--<TabPane label="最近使用的项目" name="name2">-->
          <!---->
        <!--</TabPane>-->
      <!--</Tabs>-->
    </Card>

    <Card class="fr unprocessed-application main-content ">
      <p slot="title">
        <Icon type="ios-alarm" />
        未处理的申请
      </p>
      <Table :columns="columns2" :data="data2" style="margin-bottom: 60px"></Table>
      <!--<Tabs value="name1">-->
        <!--<TabPane label="未处理的申请" name="name2">-->
          <!--<Table :columns="columns2" :data="data2" style="margin-bottom: 60px"></Table>-->
        <!--</TabPane>-->
      <!--</Tabs>-->
    </Card>


  </div>
</template>

<script type="text/ecmascript-6">
  import { getProjectList } from '../../utils/interface';
  export default {
    data () {
      return {
        loading: true,
        projects: [],
				value13: '',
				select3: 'group',
				columns2: [
					{
						renderHeader: (h, params) => {
							return [
								h('Icon', {props: {type: 'md-person'}}),
								h('span', ' 用户名')
							];
						},
						key: 'name',
						render: (h, params) => {
							return h('div', [
								h('Avatar', {
									props: {
										size: 'small'
									},
									style: {
										marginRight: '5px'
									}
								}, params.row.name.substring(0, 1)),
								h('span', params.row.name + ' ')
							]);
						}
					},
					{
						title: '申请加入的项目组',
						key: 'groupName'
					},
					{
						title: '申请时间',
						key: 'date'
					},
					{
						title: '申请理由',
						key: 'reason'
					},
					{
						title: '操作',
						width: 140,
						render: (h, params) => {
//						<Icon type="md-checkmark" />
							return h('div', [
								h('Button', {
									props: {
										type: 'primary',
										size: 'small'
									},
									style: {
										marginRight: '5px',
										display: params.row.roleType === 1 ? 'none': ''
									},
									on: {
										click: () => {
//											this.show(params.row.minUnit, params.row.maxUnit, params.row.paySupportType);
										}
									}
								}, '通过'),
								h('Button', {
									props: {
										type: 'error',
										size: 'small'
									},
									style: {
										marginRight: '5px'
									},
									on: {
										click: () => {
//											this.show(params.row.minUnit, params.row.maxUnit, params.row.paySupportType);
										}
									}
								}, '拒绝')
							]);
						}
					}
				],
				data2: [
					{
						name: '464328772@qq.com',
						groupName: '美美理财',
						date: '2016-10-03',
						reason: '项目开发'
					},
					{
						name: '17611221187',
						groupName: '美美消费',
						date: '2016-10-03',
						reason: '看看'
					}
				]
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

<style lang="less">
  .main-content{
    .recent-project{
      min-height: 170px;
      .project-box{
        display: flex;
        display: -webkit-flex;
        flex-wrap:wrap;
      }
      .ivu-card-head{
        padding: 25px 16px;
      }
    }
    .unprocessed-application{
      margin-top: 0px;
      .ivu-card-head{
        padding: 25px 16px;
      }
    }
  }
  .ivu-tabs-nav {
    height: 60px;
    line-height: 40px;
  }
  .project-avatar {
    width: 60px;
    height: 60px;
    border-radius: 30px;
    font-size: 24px;
    padding: 10px;
  }
</style>
