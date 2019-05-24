package com.meimeitech.generator.tools.swagger;

import com.meimeitech.generator.tools.swagger.cmd.SwaggerCodegen;
import lombok.Builder;

/**
 * @author paul
 * @description
 * @date 2019/4/17
 */
@Builder
public class Generator {
    /**
     *  http://127.0.0.1:7050/v2/api-docs/402835816a34a2d3016a34a482760002
     *  file:///F:/tools/mybatis-generator/src/test/resources/user.json
     */
    private String swaggerJson;
    private String targetPackage;
    private String targetProject;

    public void generatorController(){
        String string = "generate -i" + swaggerJson+" "+
                "--library spring-boot -l PaulSpring " +
                "--api-package="+targetPackage+".gen.swagger.api --model-package="+targetPackage+".gen.swagger.model " +
                "-o "+targetProject+" --additional-properties useBeanValidation=false,hideGenerationTimestamp=true";
        SwaggerCodegen.main(string.split("\\s+"));
    }

    public void generatorFeignClient(){
        String string = "generate -i" + swaggerJson+" "+
                "--library spring-cloud -l PaulSpring " +
                "--api-package="+targetPackage+".gen.swagger.client --model-package="+targetPackage+".gen.swagger.model " +
                "-o "+targetProject+" --additional-properties useBeanValidation=false,hideGenerationTimestamp=true,isOpenFeign=true";
        SwaggerCodegen.main(string.split("\\s+"));
    }

    public void generatorAxiosClient(){
        String string = "generate -i" + swaggerJson+" "+
                "-l typescript-axios " +
                "-o "+targetProject+" --additional-properties isGenerateSupportingFiles=true";
        SwaggerCodegen.main(string.split("\\s+"));
    }
}
