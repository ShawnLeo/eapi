<template>
	<div class="member">
		<i-col span="24" v-if="currUserRole === '1' || currUserRole === '2'">
			<div class="wrapper-content clearfix">
				<h2 class="title-border">添加成员</h2>
			</div>
			<div class="wrapper-content box">
				<Form ref="formItem" :model="formItem" :label-width="80" :rules="ruleValidate">
					<FormItem label="用户" prop="users">
						<Select v-model="formItem.users" multiple filterable remote :remote-method="remoteUser" :loading="loading2"
								placeholder="请输入用户名">
							<Option v-for="(option, index) in options" :value="option.value" :key="index">{{option.label}}</Option>
						</Select>
					</FormItem>

					<FormItem label="角色" prop="role">
						<Select v-model="formItem.role">
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
		</i-col>

		<i-col span="24">
			<div class="wrapper-content">
				<h2 class="title-border">「{{group.name}}」成员</h2>
			</div>

			<Table :columns="columns1" :data="data1" style="margin-bottom: 60px"></Table>
		</i-col>
	</div>
</template>

<script type="text/ecmascript-6">
	import {
		groupUserQuery,
		groupUserList,
		groupUserAdd,
		groupUserQuit,
		groupTransfer,
		groupUserUpdate
	} from '../../utils/interface';

	export default {
		props: {
			groupId: String,
			currUserRole: String,
			group: Object
		},
		data() {
			return {
				formItem: {
					groupId: this.groupId,
					users: [],
					role: ''
				},
//				group: {},
				options: [],
				loading2: false,
				columns1: [
					{
						renderHeader: (h, params) => {
							return [
								h('Icon', {props: {type: 'md-person'}}),
								h('span', ' 用户名')
							];
						},
//						align: 'center',
						key: 'userName',
						render: (h, params) => {
							let renderH = [
								h('Avatar', {
									props: {
										size: 'small'
									},
									style: {
										marginRight: '5px'
									}
								}, params.row.userName.substring(0, 1)),
								h('span', params.row.userName + ' ')
							];
							if (params.row.userId === this.$store.state.app.user.id) {
								renderH.push(h('Tag', {
									props: {
										color: 'success',
										type: 'border'
									}
								}, '你'));
							}
							return h('div', renderH);
						}
					},
					{
						title: '角色',
						key: 'role',
						render: (h, params) => {
							let role = '';
							let disable = (this.currUserRole === '3' || this.currUserRole === '4');
							switch (params.row.role) {
								case '1':
									role = '创建者';
									disable = true;
									break;
								case '2':
									role = '管理员';
									break;
								case '3':
									role = '开发者';
									break;
								case '4':
									role = '观察者';
									break;
							}
							return h('Dropdown', {
								props: {
									trigger: 'click'
								},
								on: {
									'on-click': (name) => {
										if (params.row.role === name) {
											return;
										}
										groupUserUpdate({groupUserId: params.row.id, role: name}, (response) => {
											this.init();
											this.$emit('change');
										});
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
									h('DropdownItem', {props: {name: '2'}}, '管理者'),
									h('DropdownItem', {props: {name: '3'}}, '开发者'),
									h('DropdownItem', {props: {name: '4'}}, '观察者')
								])
							]);
						}
					},
					{
						title: '加入时间',
						key: 'joinTime',
						render: (h, params) => {
							return h('span', params.row.joinTime.substring(0, 10));
						}
					},
					{
						title: '操作',
						width: 120,
						render: (h, params) => {
							// 该条记录是创建者用户，按钮都不显示
							if (params.row.role === '1') {
								return null;
							}

							if (this.currUserRole === '1') { // 当前用户是创建者 （非创建者用户显示： ‘转交’和‘删除’ 按钮）
								return h('div', [
									h('Button', {
										props: {
											type: 'primary',
											size: 'small'
										},
										style: {
											marginRight: '5px',
											display: params.row.role === '1' ? 'none' : ''
										},
										on: {
											click: () => {
												this.$Modal.confirm({
													title: '确认转交？',
													content: '<p>确认将项目组转交给' + params.row.userName + ' 么？</p>',
													onOk: () => {
														groupTransfer({groupId: this.groupId, transferId: params.row.userId}, (response) => {
															this.init();
															this.$emit('change');
														});
													}
												});
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
												this.$Modal.confirm({
													title: '确认删除？',
													content: '<p>确认将用户' + params.row.userName + ' 从项目组中删除么？</p>',
													onOk: () => {
														groupUserQuit({groupId: this.groupId, userId: params.row.userId}, (response) => {
															this.init();
														});
													},
													onCancel: () => {
														this.$Message.success("取消删除！");
													}
												});

//											this.show(params.row.minUnit, params.row.maxUnit, params.row.paySupportType);
											}
										}
									}, '删除')
								]);
							} else if (this.$store.state.app.user.id === params.row.userId) { // 不是创建者 但 是当前用户 （当前用户显示‘退出’按钮）
								return h('div', [
									h('Button', {
										props: {
											type: 'primary',
											size: 'small'
										},
										style: {
											marginRight: '5px',
											display: params.row.role === '1' ? 'none' : ''
										},
										on: {
											click: () => {
												let _this = this;
												this.$Modal.confirm({
													title: '确认退出？',
													content: '<p>确认退出 <b>[' + this.group.name + ']</b> 吗？</p>',
													onOk: () => {
														groupUserQuit({groupId: this.groupId}, (response) => {
															this.$emit('quit');
														});
													}
												});
//											this.show(params.row.minUnit, params.row.maxUnit, params.row.paySupportType);
											}
										}
									}, '退出')]);
							}
							return null;
						}
					}
				],
				data1: [],
				ruleValidate: {
					users: [
						{required: true, type: 'array', message: '请选择用户', trigger: 'blur'}
					],
					role: [
						{required: true, message: '请选择角色', trigger: 'blur'},
					]
				}
			};
		},
		methods: {
			init() {
				groupUserList({groupId: this.groupId}, (resp) => {
					this.data1 = resp.body;
				});
			},
			remoteUser: async function (query) {
				this.options = [];
				if (!query) {
					return;
				}
				this.loading2 = true;
				await  groupUserQuery({username: query, groupId: this.groupId}, (resp) => {
					this.loading2 = false;
					resp.body.forEach(item => {
						this.options.push({value: item.userName, label: item.userName});
					});
				});
			},
			addMember() {
				let _this = this;
				this.$refs['formItem'].validate(async (valid) => {
					if (valid) {
						groupUserAdd(this.formItem, function (resp) {
							_this.init();
							_this.$Message.success('添加成功！');
							_this.formItem.users = [];
							_this.formItem.role = '';
						});
					}
				});
			}
		},
		watch: {
			groupId: {
				handler: function (val, oldVal) {
					this.formItem.groupId = val;
					this.init();
				}
			}
		},
		mounted() {
			this.init();
		}
	};
</script>

<style>
</style>