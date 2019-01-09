export default {
  'zh-CN': {
    currentLang: '简体中文',
    responseError: {
      code400: '请求信息有误',
      code401: '权限不足',
      code404: '数据不存在',
      code405: '错误的请求类型',
      code500: '服务器开小差了，请稍后再试',
      code503: '系统维护，请稍后再试',
      code600: '数据处理错误',
      netError: '网络异常, 请检查网络稍后再试'
    },
    menus: {
      index: '首页',
      projects: '项目管理',
			codegen: '代码生成'
    },
    switchLangTitle: '切换语言',
		login: {
			title: '登录',
			exitLoginL: '退出登录',
			username: '账号',
			enterUserName: '请输入账号',
			password: '密码',
			enterPassword: '请输入登录密码',
			btnName: '登录',
			btnLoading: '登录中...',
			registerNow: '立即注册',
			noAccount: '还没账号？',
			forgetPass: '忘记密码',
			btnText2: '注册',
			passErrorL: '密码长度6-32个字符',
			loginSuccess: '登录成功',
			kaptStart: '请按住滑块，拖动到最右边',
			kaptYes: '验证通过',
			kaptError1: '哎呀，出错了，点击',
			kaptRefresh: '刷新',
			kaptError2: '再来一次',
			kaptError3: '网络不给力，请',
			kaptRefresh2: '立即刷新',
			kaptEr4: '请进行滑块验证'
		},
		register: {
			title: '注册',
			email: '邮箱',
			phone: '手机号',
			enterEmail: '请输入邮箱',
			emailError: '请输入正确的邮箱',
			enterPhone: '请输入手机号',
			password: '密码',
			enterPassword: '请输入密码',
			rePassword: '确认密码',
			enterRePassword: '请再次输入密码',
			passErrorL2: '两次输入密码不一致',
			passErrorL3: '密码长度8-32个字符',
			passErrorL4: '必须包含大小写字母和数字',
			readError: '请阅读协议',
			read: '我已阅读并同意',
			protocolName: '服务协议',
			privacyName: '隐私条款',
			btnName: '注册',
			btnLoading: '注册中...',
			regSuccess: '注册成功',
			smsTxt: '短信验证码'
		},
		enterEmail: {
			title: ' 忘记密码',
			email: '注册邮箱号',
			enterEmail: '请输入您注册时的邮箱号',
			btnName: '下一步',
			btnLoading: '发送邮件中...'
		},
		registerValidEmail: {
			title: '邮箱验证',
			contentBefore: '已向您的注册邮箱',
			contentAfter: '发送了一封验证邮件，请按照邮件中的提示完成您的注册。',
			resendEmail: '重新发送邮件',
			tipsTit: '如果您未收到邮件，请进行以下操作：',
			tips01: '1、确保邮件地址是否正确',
			tips02: '2、请尝试在垃圾邮件或其他文件夹中查找',
			tips03: '3、设置邮件地址白名单',
			tips04: '4、检查邮件客户端是否收发正常',
			btnName: '查看邮件'
		},
		activeSuccess: {
			title: '账号激活成功',
			failTitle: '账号激活失败',
			content: '您的账户已经激活，请登录开始您的交易。',
			contentFail: '您的账户激活失败！',
			loginNow: '立即登录'
		},
		resetPassEmail: {
			title: '重置登录密码',
			contentBefore: '已向您的注册邮箱',
			contentAfter: '发送了一封验证邮件，请按照邮件中的提示完成您的注册。',
			resendEmail: '重新发送邮件',
			resendEmailSuccess: '重新发送成功!',
			tipsTit: '如果您未收到邮件，请进行以下操作：',
			tips01: '1、确保邮件地址是否正确',
			tips02: '2、请尝试在垃圾邮件或其他文件夹中查找',
			tips03: '3、设置邮件地址白名单',
			tips04: '4、检查邮件客户端是否收发正常',
			btnName: '查看邮件'
		},
		resetPass: {
			title: '重置密码',
			newPass: '新密码',
			originPass: '原密码',
			enterNewPass: '请输入新密码',
			enterOriginPass: '请输入原密码',
			rePassword: '确认密码',
			enterPassword: '请再次输入密码',
			btnName: '重置',
			btnLoading: '重置中...',
			psdEL: '两次输入密码不一致',
			resetSuccess: '重置密码成功'
		}
  },
  'zh-TW': {
    currentLang: '繁體中文',
    responseError: {
      code400: '請求信息有誤',
      code404: '數據不存在',
      code405: '錯誤的請求類型',
      code500: '服務器開小差了，請稍後再試',
      code503: '系統維護，請稍後再試',
      code600: '數據處理錯誤',
      netError: '網絡異常, 請檢查網絡稍後再試'
    },
    menus: {
      index: '首頁',
      projects: '項目管理'
    },
    switchLangTitle: '切換語言'
  },
  'en-US': {
    currentLang: 'English',
    responseError: {
      code400: 'Wrong request information',
      code404: 'Data does not exist',
      code405: 'Bad request type',
      code500: 'The server is open, please try again later',
      code503: 'System maintenance, please try again later',
      code600: 'Data processing error',
      netError: 'The network is abnormal. Please check the network and try again later'
    },
    menus: {
      index: 'INDEX',
      projects: 'PROJECTS'
    },
    switchLangTitle: 'Switch Lang'
  }
};
