<template>
	<div class="member">
		<div class="wrapper-content">
			<h2 class="title-border">添加成员</h2>
		</div>
		<div class="wrapper-content box">
			<Form :model="formItem" :label-width="80">
				<FormItem label="用户">
					<Select v-model="users" multiple filterable remote :remote-method="remoteUser" :loading="loading2">
						<Option v-for="(option, index) in options" :value="option.value" :key="index">{{option.label}}</Option>
					</Select>
				</FormItem>

				<FormItem label="角色">
					<Select v-model="formItem.select">
						<Option value="2">管理员</Option>
						<Option value="3">开发者</Option>
						<Option value="4">观察者</Option>
					</Select>
				</FormItem>

				<FormItem>
					<Button @click="addMember" type="success" style="float: right">添加成员</Button>
				</FormItem>
			</Form>
		</div>

		<div class="wrapper-content">
			<h2 class="title-border">「默认组」成员</h2>
		</div>

		<Table :columns="columns1" :data="data1" style="margin-bottom: 60px"></Table>

	</div>
</template>

<script type="text/ecmascript-6">
	export default {
		data () {
			return {
				formItem: {
					title: '',
					description: ''
				},
				list: ['Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California', 'Colorado', 'Connecticut', 'Delaware', 'Florida', 'Georgia', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana', 'Maine', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi', 'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New hampshire', 'New jersey', 'New mexico', 'New york', 'North carolina', 'North dakota', 'Ohio', 'Oklahoma', 'Oregon', 'Pennsylvania', 'Rhode island', 'South carolina', 'South dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont', 'Virginia', 'Washington', 'West virginia', 'Wisconsin', 'Wyoming'],
				options: [{value: 'Alabama', label: 'Alabama'}, {value: 'Alaska', label: 'Alaska'}, {value: 'Arizona', label: 'Arizona'}, {value: 'Arkansas', label: 'Arkansas'}],
				loading2: false,
				users: [],
				columns1: [
					{
//						title: '用户名',
//						width: 420,
						renderHeader: (h, params) => {
							return [
								h('Icon', {props: {type: 'md-person'}}),
								h('span', ' 用户名')
							];
						},
//						align: 'center',
						key: 'name',
						render: (h, params) => {
							let renderH = [
								h('Avatar', {
									props: {
										size: 'small'
									},
									style: {
										marginRight: '5px'
									}
								}, params.row.name.substring(0, 1)),
								h('span', params.row.name + ' ')
							];
							if (params.row.roleType === 1) {
								renderH.push(h('Tag', {
									props: {
										color: 'success',
										type: 'border'
									}
								}, '你'))
							}
							return h('div', renderH);
						}
					},
					{
						title: '角色',
						key: 'role',
						render: (h, params) => {
							let role = '';
							let disable = false;
							switch (params.row.roleType) {
								case 1:
									role = '创建者';
									disable = true;
									break;
								case 2:
									role = '管理员';
									break;
								case 3:
									role = '开发者';
									break;
								case 4:
									role = '观察者';
									break;
							}
							return h('Dropdown', {
								props: {
									trigger: 'click'
								},
								on: {
									'on-click': (name) => {
										console.log(name);
										if (params.row.roleType === name) {
											return;
										}
//										this.changStatus(params.row.id, name);
									}
								}
							}, [
								h('Button', {
									props: {
										type: 'info',
										disabled: disable,
										size: "small",
										ghost: true
									}
								}, role),
								h('DropdownMenu', {slot: 'list'}, [
									h('DropdownItem', {props: {name: 2}}, '管理者'),
									h('DropdownItem', {props: {name: 3}}, '开发者'),
									h('DropdownItem', {props: {name: 4}}, '观察者')
								])
							]);
						}
					},
					{
						title: '加入时间',
						key: 'date'
					},
					{
						title: '操作',
						width: 120,
						render: (h, params) => {
							if (params.row.roleType === 1) {
								return null;
							}
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
								}, '转交'),
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
								}, '删除')
							]);
						}
					}
				],
				data1: [
					{
						name: '464328772@qq.com',
						roleType: 1,
						date: '2016-10-03'
					},
					{
						name: '17611221187',
						roleType: 2,
						date: '2016-10-03'
					}
				]
			}
		},
		methods: {
			remoteUser(query) {
				if (query !== '') {
					this.loading2 = true;
					setTimeout(() => {
						this.loading2 = false;
						const list = this.list.map(item => {
							return {
								value: item,
								label: item
							};
						});
						this.options = list.filter(item => item.label.toLowerCase().indexOf(query.toLowerCase()) > -1);
					}, 200);
				} else {
					this.options = [{value: 'Alabama', label: 'Alabama'}, {value: 'Alaska', label: 'Alaska'}, {value: 'Arizona', label: 'Arizona'}, {value: 'Arkansas', label: 'Arkansas'}];
				}
			},
			addMember() {

			}
		}
	};
</script>

<style>
</style>