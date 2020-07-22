<template>
	<div class="group">
		<i-col span="24"  v-if="currUserRole === '1' || currUserRole === '2'">
			<div class="wrapper-content">
				<h2 class="title-border">项目组信息</h2>
			</div>
			<div class="wrapper-content box">
				<Form :model="group" :label-width="100">
					<FormItem label="项目组名称">
						<Input type="text" v-model="group.name"/>
					</FormItem>
					<FormItem label="项目组描述">
						<Input type="textarea" :autosize="{minRows: 2,maxRows: 5}" v-model="group.description"/>
					</FormItem>
					<FormItem>
						<Button @click="updateGroup" type="success" style="float: right">修改</Button>
					</FormItem>
				</Form>
			</div>
		</i-col>
		<i-col span="24"  v-if="currUserRole !== '1'">
			<div class="wrapper-content">
				<h2 class="title-border">退出项目组</h2>
			</div>
			<Form :model="group" :label-width="100">
				<FormItem :label-width=-1>
					<Button type="error" long @click="quitGroup">退出此项目组</Button>
				</FormItem>
			</Form>
		</i-col>

		<i-col span="24"  v-if="currUserRole === '1'">
			<div class="wrapper-content">
				<h2 class="title-border">删除项目组</h2>
			</div>
			<div class="wrapper-content box danger-box">
				<Form :model="group" :label-width="100">
					<p>此操作不可逆，将删除<b>{{group.name}}</b>, 及以下所有项目、用户，且<b>不可恢复</b>！</p>
					<br>
					<p>请输入项目组名称以进行确认:</p><br>
					<Input v-model="title" placeholder="项目组名称"/>
					<br>
					<br>
					<FormItem :label-width=-1>
						<Button type="error" long v-if="showDeleteButton" @click="handleRemove">我已了解风险，删除此项目组</Button>
						<Button type="error" long disabled v-else>我已了解风险，删除此项目组</Button>
					</FormItem>
				</Form>
			</div>
		</i-col>
	</div>
</template>

<script type="text/ecmascript-6">
	import {groupUpdate, groupDelete, groupUserQuit} from '../../utils/interface';

	export default {
		props: {
			groupId: String,
			currUserRole: String,
			group: Object
		},
		data() {
			return {
				title: '',
//				group: {},
				showDeleteButton: false
			};
		},
		methods: {
//			init() {
//				groupGet({id: this.groupId}, (resp) => {
//					this.group = resp.body;
//				});
//			},
			handleRemove() {
				groupDelete({id: this.groupId}, (response) => {
					this.$Message.success('删除成功！');
					window.location.reload();
				});
			},
			updateGroup() {
				groupUpdate(this.group, () => {
					this.$Message.success('修改成功!');
					this.$emit('update');
				});
			},
			quitGroup() {
				this.$Modal.confirm({
					title: '确认退出？',
					content: '<p>确认退出 <b>[' + this.group.name + ']</b> 吗？</p>',
					onOk: () => {
						groupUserQuit({groupId: this.groupId}, (response) => {
							this.$Message.success('退出成功!');
							this.$emit('quit');
						});
					}
				});
			}
		},
		watch: {
//			groupId: {
//				handler: function (val, oldVal) {
//					this.init();
//				}
//			},
			title: function (newTitle, oldTitle) {
				if (newTitle === this.group.name) {
					this.showDeleteButton = true;
				} else {
					this.showDeleteButton = false;
				}
			}
		},
		mounted() {
		}
	};
</script>

<style>
</style>