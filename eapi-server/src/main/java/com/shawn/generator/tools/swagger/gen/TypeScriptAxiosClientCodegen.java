package com.shawn.generator.tools.swagger.gen;

import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.CodegenParameter;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.languages.AbstractTypeScriptClientCodegen;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

import java.io.File;

/**
 * @author paul
 * @description
 * @date 2019/4/26
 */
public class TypeScriptAxiosClientCodegen extends AbstractTypeScriptClientCodegen {

    public TypeScriptAxiosClientCodegen() {
        super();
        modelTemplateFiles.put("model.mustache", ".ts");
        apiTemplateFiles.put("api.mustache", ".ts");
        typeMapping.put("Date", "Date");
        apiPackage = "api";
        modelPackage = "model";
        outputFolder = "generated-code/typescript-axios";
        embeddedTemplateDir = templateDir = "typescript-axios";
   }

    @Override
    public String getName() {
        return "typescript-axios";
    }


    @Override
    public void processOpts() {
        super.processOpts();
        supportingFiles.add(new SupportingFile("models.mustache", modelPackage().replace('.', File.separatorChar), "models.ts"));
        supportingFiles.add(new SupportingFile("apis.mustache", apiPackage().replace('.', File.separatorChar), "Api.ts"));
        supportingFiles.add(new SupportingFile("resultModel.mustache", modelPackage().replace('.', File.separatorChar), "Result.ts"));
    }

    @Override
    public String getHelp() {
        return "Generates a TypeScript axios client library.";
    }

    @Override
    public String getSwaggerType(Property p) {
        String swaggerType = super.getSwaggerType(p);
        if (p instanceof StringProperty) {
            StringProperty sp = (StringProperty) p;
            if (sp.getEnum() != null) {
                return swaggerType;
            }
        }
        if (isLanguagePrimitive(swaggerType) || isLanguageGenericType(swaggerType)) {
            return swaggerType;
        }
        return addModelPrefix(swaggerType);
    }

    private String addModelPrefix(String swaggerType) {
        String type = null;
        if (typeMapping.containsKey(swaggerType)) {
            type = typeMapping.get(swaggerType);
        } else {
            type = swaggerType;
        }

        if (!isLanguagePrimitive(type) && !isLanguageGenericType(type)) {
            type = "models." + swaggerType;
        }
        return type;
    }

    private boolean isLanguagePrimitive(String type) {
        return languageSpecificPrimitives.contains(type);
    }

    private boolean isLanguageGenericType(String type) {
        for (String genericType : languageGenericTypes) {
            if (type.startsWith(genericType + "<")) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void postProcessParameter(CodegenParameter parameter) {
        super.postProcessParameter(parameter);

        if (!parameter.isEnum) {
            parameter.dataType = addModelPrefix(parameter.dataType);
        }
    }
    @Override
    protected void addAdditionPropertiesToCodeGenModel(CodegenModel codegenModel, ModelImpl swaggerModel) {
        codegenModel.additionalPropertiesType = getSwaggerType(swaggerModel.getAdditionalProperties());
        addImport(codegenModel, codegenModel.additionalPropertiesType);
    }

}
