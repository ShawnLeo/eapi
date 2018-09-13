package com.meimeitech.eapi.dialect;

public abstract class Schema {

	public static final String SEQ_SUFFIX = "_SEQ";

	/**
	 * 表名
	 */
	public interface Tables {

		/**
		 * 表命名空间
		 */
		String NS = "EAPI_";

		/**
		 * 项目组
		 */
		String PROJECT_GROUP = NS + "PROJECT_GROUP";

		/**
		 * 项目
		 */
		String PROJECT = NS + "PROJECT";

		/**
		 * 接口
		 */
		String INTERFACE = NS + "INTERFACE";

		/**
		 * 接口标签
		 */
		String INTERFACE_TAG = NS + "INTERFACE_TAG";

		/**
		 * 数据模型
		 */
		String DATAMODEL = NS + "DATAMODEL";

		/**
		 * 标签
		 */
		String TAG = NS + "TAG";

		/**
		 * 请求数据信息
		 */
		String REQUEST_INFO = NS + "REQUEST_INFO";


		/**
		 * 响应数据信息
		 */
		String RESPONSE_INFO = NS + "RESPONSE_INFO";
	}

	/**
	 * 列名
	 */
	public interface Columns {

		/**
		 * 接口标签 - 接口Id
		 */
		String INTERFACE_ID = "INTERFACE_ID";

		/**
		 * 接口标签 - 标签Id
		 */
		String TAG_ID = "TAG_ID";
	}

}
