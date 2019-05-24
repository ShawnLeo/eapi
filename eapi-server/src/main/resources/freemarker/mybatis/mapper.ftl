package ${package};

import org.apache.ibatis.annotations.Mapper;
import ${tableClass.fullClassName};

/**
* 通用 Mapper 代码生成器
*
* @author mapper-generator
*/
@Mapper
public interface ${tableClass.shortClassName}${mapperSuffix} extends ${baseMapper!"tk.mybatis.mapper.common.Mapper"}<${tableClass.shortClassName}> {

}




