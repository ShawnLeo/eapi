package com.shawn.generator.tools.swagger.gen;

import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.languages.SpringCodegen;
import io.swagger.models.Model;

import java.io.File;
import java.util.Map;

/**
 * @author paul
 */
public class MeimeitechSpringCodegen extends SpringCodegen {

    public String servicePackage;

    public MeimeitechSpringCodegen() {
        super();
        templateDir = "meimeitechSpring";
        embeddedTemplateDir = "spring";
    }

    @Override
    public String getName() {
        return "meimeitechSpring";
    }

    @Override
    public void processOpts() {
        super.processOpts();

        this.servicePackage = additionalProperties.get(CodegenConstants.INVOKER_PACKAGE) + ".service";
        additionalProperties.put("servicePackage", this.servicePackage);

        writePropertyBack(USE_BEANVALIDATION, useBeanValidation);
        writePropertyBack(USE_OPTIONAL, useOptional);
        if ("spring-boot".equals(library)) {
            apiTemplateFiles.remove("api.mustache");
            apiTemplateFiles.remove("apiController.mustache");
            apiTemplateFiles.put("apiController.mustache", "Controller.java");

            apiTemplateFiles.put("apiService.mustache", "Service.java");
            supportingFiles.add(new SupportingFile("baseController.mustache",
                    (sourceFolder + File.separator + apiPackage).replace(".", java.io.File.separator), "BaseController.java"));
        }
    }

    @Override
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);
        model.imports.remove("ApiModelProperty");
        model.imports.remove("ApiModel");
    }

    @Override
    public CodegenModel fromModel(String name, Model model, Map<String, Model> allDefinitions) {
        CodegenModel codegenModel = super.fromModel(name, model, allDefinitions);
        codegenModel.imports.remove("ApiModel");
        return codegenModel;
    }

    public String apiPackage(String templateName) {
        if (templateName.contains("Service")) {
            return sourceFolder + "/" + servicePackage.replace('.', '/');
        } else {
            return sourceFolder + "/" + apiPackage().replace('.', '/');
        }
    }
    @Override
    public String apiFilename(String templateName, String tag) {
        String suffix = apiTemplateFiles().get(templateName);
        return apiFileFolder(templateName) + File.separator + toApiFilename(tag) + suffix;
    }

    public String apiFileFolder(String templateName) {
        return outputFolder + "/" + apiPackage(templateName).replace('.', '/');
    }

    @Override
    public String toApiName(String name) {
        if (name.length() == 0) {
            return "Default";
        }
        name = sanitizeName(name);
        return camelize(name);
    }

}
