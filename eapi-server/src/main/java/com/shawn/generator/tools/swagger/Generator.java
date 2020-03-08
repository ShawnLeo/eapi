package com.shawn.generator.tools.swagger;

import com.shawn.generator.tools.swagger.cmd.SwaggerCodegen;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

/**
 * @author paul
 * @description
 * @date 2019/4/17
 */
@Builder
public class Generator {

    private String swaggerJson;
    private String output;
    private String apiPackage;
    private String modelPackage;
    private String lang;
    private String library;
    private  Boolean generateSupportingFiles;
    private String groupId;
    private String artifactId;

    public void generatorController() {
        StringBuilder sb = new StringBuilder();

        sb.append("generate -i ").append(swaggerJson).append(" -l ").append(lang);

        if (StringUtils.isNoneEmpty(apiPackage)) {
            sb.append(" --api-package=").append(apiPackage);
        }
        if (StringUtils.isNoneEmpty(modelPackage)) {
            sb.append(" --model-package=").append(modelPackage);
        }
        if (StringUtils.isNoneEmpty(library)) {
             sb.append(" --library ").append(library);
        }
        if (StringUtils.isNoneEmpty(output)) {
            sb.append(" -o ").append(output);
        }
        if (StringUtils.isNoneEmpty(groupId)) {
            sb.append(" --group-id ").append(groupId);
        }
        if (StringUtils.isNoneEmpty(artifactId)) {
            sb.append(" --artifact-id ").append(artifactId);
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
