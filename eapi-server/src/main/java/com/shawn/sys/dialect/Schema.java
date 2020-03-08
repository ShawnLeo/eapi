package com.shawn.sys.dialect;

public abstract class Schema {

	public static final String SEQ_SUFFIX = "_SEQ";

	/**
	 * 表名
	 */
	public interface Tables {
		/**
		 * 表命名空间
		 */
		String NS = "SYS_";
		/**
		 * 菜单
		 */
		String MENU = NS + "MENU";
		/**
		 * 权限
		 */
		String RESOURCE = NS + "RESOURCE";
		/**
		 * 用户
		 */
		String USER = NS + "USER";

		/**
		 * 用户注册临时表
		 */
		String REG_USER = NS + "REG_USER";

		/**
		 * 重置密码临时表
		 */
		String RESET_USER = NS + "RESET_USER";

		/**
		 * 角色
		 */
		String ROLE = NS + "ROLE";
		/**
		 * 组织机构
		 */
		String ORGAN = NS + "ORGAN";
		/**
		 * 用户角色
		 */
		String USERROLE = NS + "USER_ROLE";

		/**
		 * 用户角色
		 */
		String ROLERESOURCE = NS + "ROLE_RESOURCE";
		/**
		 * 按钮级操作
		 */
		String ACTION = NS + "ACTION";
		/**
		 * 系统日志
		 */
		String SYSLOG = NS + "LOG";
		/**
		 * 系统数据字典
		 */
		String WORDBOOK = NS + "WORDBOOK";
		/**
		 * 行政区划表
		 */
		String DISTRICT = NS + "DISTRICT";
		/**
		 * 银行信息
		 */
		String BANKINFO = NS + "BANKINFO";
		/**
		 * 用户扩展属性
		 */
		String USEREXTEN = "CRD_CR_USER_EXTEND";
		/**
		 * 行业信息
		 */
		String INDUSTRY = NS + "INDUSTRY";
		/**
		 * 用户认证
		 */
		String USERAUTH = NS + "USER_AUTH";

	}

	/**
	 * 列名
	 */
	public static interface Columns {
		/**
		 * USERROLE.ROLEID
		 */
		String USERROLE_ROLEID = "ROLEID";

		String ROLERESOURCE_ROLEID = "ROLEID";

		String ROLERESOURCE_RESOURCEID = "RESOURCEID";

		/**
		 * USERROLE.USERID
		 */
		String USERROLE_USERID = "USERID";
		/**
		 * RESOURCE.ROLEID
		 */
		String RESOURCE_ROLEID = "ROLEID";
		/**
		 * MENU.LEVEL
		 */
		String MENU_LEVEL = "NODE_LEVEL";
		/**
		 * ACTION.LEVEL
		 */
		String ACTION_LEVEL = "NODE_LEVEL";
		/**
		 * ORGAN.LEVEL
		 */
		String ORGAN_LEVEL = "NODE_LEVEL";
		/**
		 * WORDBOOK.LEVEL
		 */
		String WORDBOOK_LEVEL = "NODE_LEVEL";
	}

}
