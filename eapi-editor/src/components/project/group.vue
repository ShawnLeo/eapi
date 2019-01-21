<template>
	<div class="group">
		<div class="wrapper-content">
			<h2 class="title-border">项目组信息</h2>
		</div>
		<div class="wrapper-content box">
			<Form :model="formItem" :label-width="100">
				<FormItem label="项目组名称">
					<Input type="text" v-model="formItem.passwd"/>
				</FormItem>
				<FormItem label="项目组描述">
					<Input type="textarea" :autosize="{minRows: 2,maxRows: 5}" v-model="formItem.passwd"/>
				</FormItem>
				<FormItem>
					<Button @click="updateGroup" type="success" style="float: right">修改</Button>
				</FormItem>
			</Form>
		</div>

		<i-col span="24">
			<div class="wrapper-content">
				<h2 class="title-border">退出项目组</h2>
			</div>
				<Form :model="formItem" :label-width="100">
					<FormItem :label-width=-1>
						<Button type="error" long>退出此项目组</Button>
					</FormItem>
				</Form>
		</i-col>

		<i-col span="24">
			<div class="wrapper-content">
				<h2 class="title-border">删除项目组</h2>
			</div>
			<div class="wrapper-content box danger-box">
				<Form :model="formItem" :label-width="100">
					<p>此操作不可逆，将删除<b>默认项目组</b>, 及以下所有项目，且<b>不可恢复</b>！</p>
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
	export default {
		data () {
			return {
				title: '',
				formItem: {
					title: '',
					description: ''
				},
				showDeleteButton: false
			};
		},
		methods: {
			handleRemove() {},
			updateGroup() {}
		}
	};
</script>

<style>
</style>