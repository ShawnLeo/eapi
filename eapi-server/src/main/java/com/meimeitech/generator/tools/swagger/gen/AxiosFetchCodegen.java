package com.meimeitech.generator.tools.swagger.gen;

import io.swagger.codegen.*;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.Parameter;


/**
 *
 */
public class AxiosFetchCodegen extends DefaultCodegen implements CodegenConfig {


    public AxiosFetchCodegen() {
        super();
        templateDir = "axios-fetch";
        apiTemplateFiles.put("api.mustache", ".js");
    }

    @Override
    public void processOpts() {
        super.processOpts();
    }

    @Override
    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    @Override
    public String getName() {
        return "axios-fetch";
    }

    @Override
    public String getHelp() {
        return "Generates a axios client library.";
    }


    @Override
    public String toApiName(String name) {
        if (name.length() == 0) {
            return "Default";
        }
        name = sanitizeName(name);
        return camelize(name, true);
    }

    @Override
    public void preprocessSwagger(Swagger swagger) {

        if (swagger == null || swagger.getPaths() == null){
            return;
        }
//        swagger.setVendorExtension("x-context", "/catmax");
        for (String pathname : swagger.getPaths().keySet()) {
            Path path = swagger.getPath(pathname);
            if (path.getOperations() == null){
                continue;
            }
            for (Operation operation : path.getOperations()) {
                boolean hasFormParameters = false;
                boolean hasBodyParameters = false;
                for (Parameter parameter : operation.getParameters()) {
                    if (parameter instanceof FormParameter) {
                        hasFormParameters = true;
                    }
                    if (parameter instanceof BodyParameter) {
                        hasBodyParameters = true;
                    }
                }
                if (hasBodyParameters || hasFormParameters){
                    String defaultContentType = hasFormParameters ? "application/x-www-form-urlencoded" : "application/json";
                    String contentType =  operation.getConsumes() == null || operation.getConsumes().isEmpty() ? defaultContentType : operation.getConsumes().get(0);
                    operation.setVendorExtension("x-contentType", contentType);
                }
//                String accepts = getAccept(operation);
//                operation.setVendorExtension("x-accepts", accepts);
            }
        }

    }

}
