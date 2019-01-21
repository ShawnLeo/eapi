<template>
	<div class="permission">
		<div class="wrapper-content">
			<h2 class="title-border">权限控制</h2>
		</div>
		<div class="wrapper-content box">
			<Form :model="formItem" :label-width="140">
				<FormItem label="用户申请验证方式：" style="float: left">
					<RadioGroup v-model="formItem.permission">
						<Radio label="public">需管理员验证</Radio>
						<Radio label="private">自动通过为</Radio>
					</RadioGroup>
				</FormItem>

				<FormItem label="" style="width: 500px;float: right" :label-width="-1">
					<Select v-model="formItem.select">
						<Option value="2">管理员</Option>
						<Option value="3">开发者</Option>
						<Option value="4">观察者</Option>
					</Select>
				</FormItem>
				<div class="clearfix"></div>

				<FormItem>
					<Button @click="updatePremission" type="success" style="float: right">修改</Button>
				</FormItem>
			</Form>
		</div>

		<div class="wrapper-content">
			<h2 class="title-border">申请列表</h2>
		</div>

		<Table :columns="columns2" :data="data2" style="margin-bottom: 60px"></Table>

	</div>
</template>

<script type="text/ecmascript-6">
	export default {
		data () {
			return {
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
						title: '申请时间',
						key: 'date'
					},
					{
						title: '申请理由',
						key: 'reason'
					},
					{
						title: '操作',
						width: 120,
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
						date: '2016-10-03',
						reason: '项目开发'
					},
					{
						name: '17611221187',
						date: '2016-10-03',
						reason: '看看'
					}
				],
				formItem: {
					title: '',
					description: ''
				}
			};
		},
		methods: {
			updatePremission() {}
		}
	};
</script>

<style>
</style>