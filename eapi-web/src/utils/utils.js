/** ***** 返回路径参数 ***** **/
    // TODO 这个规则还有待完善
export const pathParam = (path) => {
      let leftArray = path.split('/{');
      let params = [];
      leftArray.forEach((item, index) => {
        if (leftArray[index + 1]) {
          let rightArray = leftArray[index + 1].split('}');
          if (rightArray.length > 1) {
            params.push(rightArray[0]);
          }
        }
      });

      return params;
    };

export const reverse = (children) => { // 迭代处理 反显数据处理
  children.forEach(item => {
    if ((item.dataType === 'object' || item.dataType === 'array') ||
        (item.dataModel && (item.dataModel.dataType === 'object'
            || item.dataModel.dataType === 'array'))) {
      item._expanded = true;
    }
    if (item.dataType === 'array' || (item.dataModel && item.dataModel.dataType
        === 'array')) {
      item.isArrayItem = true;
    }
    if (item.children && item.children.length > 0) {
      reverse(item.children);
    }
    if (item.dataModel && item.dataModel.children
        && item.dataModel.children.length > 0) {
      reverse(item.dataModel.children);
    }
  });
};

export const coverJsonToDatamodel = (jsonObject, children) => {
  for (let key  in jsonObject) {
    let value = jsonObject[key];

    if (!jsonObject[key] || ((typeof value === 'string') && value.constructor
        === String)) {
      // String 类型的
      children.push({
        name: key,
        description: '',
        dataType: 'string',
        example: value,
        children: [],
        required: false,
        _expanded: false
      });
    } else if ((typeof value == 'number') && value.constructor == Number) {
      // String 数字类型的
      children.push({
        name: key,
        description: '',
        dataType: 'integer',
        example: value,
        children: [],
        required: false,
        _expanded: false
      });
    } else if ((typeof value == 'object') && value.constructor == Object) {
      let item = {
        name: key,
        description: '',
        dataType: 'object',
        example: '',
        children: [],
        required: false,
        _expanded: false
      };
      coverJsonToDatamodel(value, item.children);
      children.push(item);
    } else if ((typeof value == 'object') && value.constructor == Array) {
      let item = {
        name: key,
        description: '',
        dataType: 'array',
        example: '',
        children: [],
        required: false,
        _expanded: false
      };
      if (value.length > 0) {
        let _value = value.slice(0, 1);
        coverJsonToDatamodel(_value, item.children);
      }
      children.push(item);
    }
    // console.log(key,":",jsonObject[key]);
  }
};

export const coverJsonToSimpleDatamodel = (jsonObject, children, kvType) => {

  for (let key  in jsonObject) {
    let value = jsonObject[key];
    let item = {
      name: key,
      description: '',
      dataType: '',
      example: "",
      children: [],
      required: false,
      _expanded: false
    };
    switch (kvType) {
      case "1":
        item.example = value;
        break;
      case "2":
        item.dataType = value;
        break;
      case "3":
        item.description = value;
        break;
      default:
        item.example = value;
        break;
    }
    children.push(item);
  }
};

export const coverArrayJsonToDatamodel = (arrayJSON, children) => {
  arrayJSON.forEach(jsonObj => {
    let item = {
      name: jsonObj.name,
      description: jsonObj.description,
      dataType: jsonObj.type ? jsonObj.type : 'string',
      example: jsonObj.example,
      children: [],
      required: false,
      _expanded: false
    };
    children.push(item);
  });
};

export const camel = (str) => { // 下划线转驼峰
  return str.replace(/\_(\w)/g, function (all, letter) {
    return letter.toUpperCase();
  });
};

export const firstUpper = (str) => { // 首字母大写
  return str.replace(str[0], str[0].toUpperCase());
};

export const download = (data, filename) => {
  if (!data) {
    return;
  }
  let url = window.URL.createObjectURL(new Blob([data]));
  let link = document.createElement('a');
  link.style.display = 'none';
  link.href = url;
  link.setAttribute('download', filename);
  document.body.appendChild(link);
  link.click();
};
