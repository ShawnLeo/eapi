package com.meimeitech.generator.tools.swagger;

import com.meimeitech.generator.tools.swagger.cmd.SwaggerCodegen;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

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
    private String targetProject;
    private String apiPackage;
    private String modelPackage;
    private String lang;
    private String library;
    private  Boolean generateSupportingFiles;

    public void generatorController() {
        StringBuilder sb = new StringBuilder();

        sb.append("generate -i ").append(swaggerJson).append(" -l ").append(lang);

//                .append(" --library  ").append(library)
        if (StringUtils.isNoneEmpty(apiPackage)) {
            sb.append(" --api-package=").append(apiPackage);
        }
        if (StringUtils.isNoneEmpty(modelPackage)) {
            sb.append(" --model-package=").append(modelPackage);
        }
        if (StringUtils.isNoneEmpty(library)) {
             sb.append(" --library ").append(library);
        }
        if (StringUtils.isNoneEmpty(targetProject)) {
            sb.append(" -o ").append(targetProject);
        }

        sb.append("  --additional-properties hideGenerationTimestamp=true,useBeanValidation=false");

        if (generateSupportingFiles) {
            sb.append(",isGenerateSupportingFiles=true");
        }
//        String string = "generate -i " + swaggerJson+" "+
//                "--library spring-boot -l " + lang +" "+
//                "--api-package="+targetPackage+".gen.swagger.controller --model-package="+targetPackage+".model " +
//                "-o "+targetProject+" --additional-properties useBeanValidation=false,hideGenerationTimestamp=true";
        SwaggerCodegen.main(sb.toString().split("\\s+"));
    }

//    public void generatorFeignClient(){
//        String string = "generate -i " + swaggerJson+" "+
//                "--library spring-cloud -l " + lang +" "+
//                "--api-package="+targetPackage+".gen.swagger.client --model-package="+targetPackage+".model " +
//                "-o "+targetProject+" --additional-properties useBeanValidation=false,hideGenerationTimestamp=true,isOpenFeign=true";
//        SwaggerCodegen.main(string.split("\\s+"));
//    }
//
//    public void generatorAxiosClient(){
//        String string = "generate -i " + swaggerJson+" "+
//                "-l typescript-axios " +
//                "-o "+targetProject+" --additional-properties isGenerateSupportingFiles=" + generateSupportingFiles;
//        SwaggerCodegen.main(string.split("\\s+"));
//    }
}
