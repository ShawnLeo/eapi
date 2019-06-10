package com.meimeitech.generator.tools.swagger.cmd;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import io.swagger.codegen.*;
import io.swagger.codegen.utils.ImplementationVersion;
import io.swagger.util.Json;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * @author paul
 * @description
 * @date 2019/4/23
 */
public class CustomDefaultGenerator extends DefaultGenerator {

    /**
     * 修改判断是否生成支持文件
     */
    @Override
    protected void configureGeneratorProperties() {
        // allows generating only models by specifying a CSV of models to generate, or empty for all
        // NOTE: Boolean.TRUE is required below rather than `true` because of JVM boxing constraints and type inference.
        isGenerateApis = System.getProperty(CodegenConstants.APIS) != null ? Boolean.TRUE : getGeneratorPropertyDefaultSwitch(CodegenConstants.APIS, null);
        isGenerateModels = System.getProperty(CodegenConstants.MODELS) != null ? Boolean.TRUE : getGeneratorPropertyDefaultSwitch(CodegenConstants.MODELS, null);

        Map<String, Object> map = config.additionalProperties();
        Object useSwaggerAnnotation = map.get("isGenerateSupportingFiles");
        if (useSwaggerAnnotation != null) {
            isGenerateSupportingFiles = true;
        } else {
            isGenerateSupportingFiles = false;
        }


        if (isGenerateApis == null && isGenerateModels == null) {
            // no specifics are set, generate everything
            isGenerateApis = isGenerateModels = true;
        } else {
            if(isGenerateApis == null) {
                isGenerateApis = false;
            }
            if(isGenerateModels == null) {
                isGenerateModels = false;
            }
        }
        // model/api tests and documentation options rely on parent generate options (api or model) and no other options.
        // They default to true in all scenarios and can only be marked false explicitly
        isGenerateModelTests = System.getProperty(CodegenConstants.MODEL_TESTS) != null ? Boolean.valueOf(System.getProperty(CodegenConstants.MODEL_TESTS)) : getGeneratorPropertyDefaultSwitch(CodegenConstants.MODEL_TESTS, true);
        isGenerateModelDocumentation = System.getProperty(CodegenConstants.MODEL_DOCS) != null ? Boolean.valueOf(System.getProperty(CodegenConstants.MODEL_DOCS)) : getGeneratorPropertyDefaultSwitch(CodegenConstants.MODEL_DOCS, true);
        isGenerateApiTests = System.getProperty(CodegenConstants.API_TESTS) != null ? Boolean.valueOf(System.getProperty(CodegenConstants.API_TESTS)) : getGeneratorPropertyDefaultSwitch(CodegenConstants.API_TESTS, true);
        isGenerateApiDocumentation = System.getProperty(CodegenConstants.API_DOCS) != null ? Boolean.valueOf(System.getProperty(CodegenConstants.API_DOCS)) : getGeneratorPropertyDefaultSwitch(CodegenConstants.API_DOCS, true);


        // Additional properties added for tests to exclude references in project related files
        config.additionalProperties().put(CodegenConstants.GENERATE_API_TESTS, isGenerateApiTests);
        config.additionalProperties().put(CodegenConstants.GENERATE_MODEL_TESTS, isGenerateModelTests);

        config.additionalProperties().put(CodegenConstants.GENERATE_API_DOCS, isGenerateApiDocumentation);
        config.additionalProperties().put(CodegenConstants.GENERATE_MODEL_DOCS, isGenerateModelDocumentation);

        config.additionalProperties().put(CodegenConstants.GENERATE_APIS, isGenerateApis);
        config.additionalProperties().put(CodegenConstants.GENERATE_MODELS, isGenerateModels);

        if(!isGenerateApiTests && !isGenerateModelTests) {
            config.additionalProperties().put(CodegenConstants.EXCLUDE_TESTS, true);
        }
        if (System.getProperty("debugSwagger") != null) {
            Json.prettyPrint(swagger);
        }
        config.processOpts();
        config.preprocessSwagger(swagger);
        config.additionalProperties().put("generatorVersion", ImplementationVersion.read());
        config.additionalProperties().put("generatedDate", DateTime.now().toString());
        config.additionalProperties().put("generatedYear", String.valueOf(DateTime.now().getYear()));
        config.additionalProperties().put("generatorClass", config.getClass().getName());
        config.additionalProperties().put("inputSpec", config.getInputSpec());
        if (swagger.getVendorExtensions() != null) {
            config.vendorExtensions().putAll(swagger.getVendorExtensions());
        }

        contextPath = config.escapeText(swagger.getBasePath() == null ? "" : swagger.getBasePath());
        basePath = config.escapeText(getHost());
        basePathWithoutHost = config.escapeText(swagger.getBasePath());
        this.isGenerateSwaggerMetadata = getAdditionalPropertyDefaultSwitch("isGenerateSwaggerMetadata",Boolean.FALSE);
    }
    private Boolean getAdditionalPropertyDefaultSwitch(final String key, final Boolean defaultValue) {
        Object result = null;
        if (this.config.additionalProperties().containsKey(key)) {
            result = this.config.additionalProperties().get(key);
        }
        if (result != null && result instanceof String) {
            return Boolean.valueOf((String) result);
        }
        return defaultValue;
    }

    private String getHost() {
        StringBuilder hostBuilder = new StringBuilder();
        hostBuilder.append(getScheme());
        hostBuilder.append("://");
        if (!StringUtils.isEmpty(swagger.getHost())) {
            hostBuilder.append(swagger.getHost());
        } else {
            hostBuilder.append("localhost");
            LOGGER.warn("'host' not defined in the spec. Default to 'localhost'.");
        }
        if (!StringUtils.isEmpty(swagger.getBasePath()) && !swagger.getBasePath().equals("/")) {
            hostBuilder.append(swagger.getBasePath());
        }
        return hostBuilder.toString();
    }

    /**
     * 支持扩展目录 先从templateDir中查找(支持文件目录或者classpath文件),再从embeddedLibTemplateFile查找
     * @param config
     * @param templateFile
     * @return
     */
    @Override
    public String getFullTemplateFile(CodegenConfig config, String templateFile) {
        //1st the code will check if there's a <template folder>/libraries/<library> folder containing the file
        //2nd it will check for the file in the specified <template folder> folder
        //3rd it will check if there's an <embedded template>/libraries/<library> folder containing the file
        //4th and last it will assume the file is in <embedded template> folder.

        //check the supplied template library folder for the file
        final String library = config.getLibrary();
        if (StringUtils.isNotEmpty(library)) {
            //look for the file in the library subfolder of the supplied template
            final String libTemplateFile = buildLibraryFilePath(config.templateDir(), library, templateFile);
            if (new File(libTemplateFile).exists() || existClassPath(libTemplateFile)) {
                return libTemplateFile;
            }
        }

        //check the supplied template main folder for the file
        final String template = config.templateDir() + File.separator + templateFile;
        if (new File(template).exists() || existClassPath(template)) {
            return template;
        }

        //try the embedded template library folder next
        if (StringUtils.isNotEmpty(library)) {
            final String embeddedLibTemplateFile = buildLibraryFilePath(config.embeddedTemplateDir(), library, templateFile);
            if (embeddedTemplateExists(embeddedLibTemplateFile)) {
                // Fall back to the template file embedded/packaged in the JAR file library folder...
                return embeddedLibTemplateFile;
            }
        }

        // Fall back to the template file embedded/packaged in the JAR file...
        return config.embeddedTemplateDir() + File.separator + templateFile;
    }

    private String buildLibraryFilePath(String dir, String library, String file) {
        return dir + File.separator + "libraries" + File.separator + library + File.separator + file;
    }

    private boolean existClassPath(String name){
        try {
            URL resource = this.getClass().getClassLoader().getResource((getCPResourcePath(name)));

            if (resource != null) {
                return true;
            }

            String file = resource.getFile();
            if (new File(file).exists()){
                return true;
            }
        }catch (Exception e){
        }
        return false;
    }


    /**
     * 调整目录创建of.mkdirs();放到是否存在支持列表判断后面,以防止生成多余的目录
     * @param files
     * @param bundle
     */
    @Override
    protected void generateSupportingFiles(List<File> files, Map<String, Object> bundle) {
        if (!isGenerateSupportingFiles) {
            return;
        }
        Set<String> supportingFilesToGenerate = null;
        String supportingFiles = System.getProperty(CodegenConstants.SUPPORTING_FILES);
        if (supportingFiles != null && !supportingFiles.isEmpty()) {
            supportingFilesToGenerate = new HashSet<String>(Arrays.asList(supportingFiles.split(",")));
        }

        for (SupportingFile support : config.supportingFiles()) {
            try {
                String outputFolder = config.outputFolder();
                if (StringUtils.isNotEmpty(support.folder)) {
                    outputFolder += File.separator + support.folder;
                }

                boolean shouldGenerate = true;
                if (supportingFilesToGenerate != null && !supportingFilesToGenerate.isEmpty()) {
                    shouldGenerate = supportingFilesToGenerate.contains(support.destinationFilename);
                }
                if (!shouldGenerate) {
                    continue;
                }

                File of = new File(outputFolder);
                if (!of.isDirectory()) {
                    of.mkdirs();
                }
                String outputFilename = outputFolder + File.separator + support.destinationFilename.replace('/', File.separatorChar);
                if (!config.shouldOverwrite(outputFilename)) {
                    LOGGER.info("Skipped overwriting " + outputFilename);
                    continue;
                }
                String templateFile;
                if (support instanceof GlobalSupportingFile) {
                    templateFile = config.getCommonTemplateDir() + File.separator + support.templateFile;
                } else {
                    templateFile = getFullTemplateFile(config, support.templateFile);
                }


                if (ignoreProcessor.allowsFile(new File(outputFilename))) {
                    if (templateFile.endsWith("mustache")) {
                        String template = readTemplate(templateFile);
                        Mustache.Compiler compiler = Mustache.compiler();
                        compiler = config.processCompiler(compiler);
                        Template tmpl = compiler
                                .withLoader(new Mustache.TemplateLoader() {
                                    @Override
                                    public Reader getTemplate(String name) {
                                        return getTemplateReader(getFullTemplateFile(config, name + ".mustache"));
                                    }
                                })
                                .defaultValue("")
                                .compile(template);

                        writeToFile(outputFilename, tmpl.execute(bundle));
                        files.add(new File(outputFilename));
                    } else {
                        InputStream in = null;

                        try {
                            in = new FileInputStream(templateFile);
                        } catch (Exception e) {
                            // continue
                        }
                        if (in == null) {
                            in = this.getClass().getClassLoader().getResourceAsStream(getCPResourcePath(templateFile));
                        }
                        File outputFile = new File(outputFilename);
                        OutputStream out = new FileOutputStream(outputFile, false);
                        if (in != null) {
                            LOGGER.info("writing file " + outputFile);
                            IOUtils.copy(in, out);
                            out.close();
                        } else {
                            LOGGER.error("can't open " + templateFile + " for input");
                        }
                        files.add(outputFile);
                    }
                } else {
                    LOGGER.info("Skipped generation of " + outputFilename + " due to rule in .swagger-codegen-ignore");
                }
            } catch (Exception e) {
                throw new RuntimeException("Could not generate supporting file '" + support + "'", e);
            }
        }

        // Consider .swagger-codegen-ignore a supporting file
        // Output .swagger-codegen-ignore if it doesn't exist and wasn't explicitly created by a generator
        final String swaggerCodegenIgnore = ".swagger-codegen-ignore";
        String ignoreFileNameTarget = config.outputFolder() + File.separator + swaggerCodegenIgnore;
        File ignoreFile = new File(ignoreFileNameTarget);
        if (isGenerateSwaggerMetadata && !ignoreFile.exists()) {
            String ignoreFileNameSource = File.separator + config.getCommonTemplateDir() + File.separator + swaggerCodegenIgnore;
            String ignoreFileContents = readResourceContents(ignoreFileNameSource);
            try {
                writeToFile(ignoreFileNameTarget, ignoreFileContents);
            } catch (IOException e) {
                throw new RuntimeException("Could not generate supporting file '" + swaggerCodegenIgnore + "'", e);
            }
            files.add(ignoreFile);
        }

        if(isGenerateSwaggerMetadata) {
            final String swaggerVersionMetadata = config.outputFolder() + File.separator + ".swagger-codegen" + File.separator + "VERSION";
            File swaggerVersionMetadataFile = new File(swaggerVersionMetadata);
            try {
                writeToFile(swaggerVersionMetadata, ImplementationVersion.read());
                files.add(swaggerVersionMetadataFile);
            } catch (IOException e) {
                throw new RuntimeException("Could not generate supporting file '" + swaggerVersionMetadata + "'", e);
            }
        }

        /*
         * The following code adds default LICENSE (Apache-2.0) for all generators
         * To use license other than Apache2.0, update the following file:
         *   modules/swagger-codegen/src/main/resources/_common/LICENSE
         *
        final String apache2License = "LICENSE";
        String licenseFileNameTarget = config.outputFolder() + File.separator + apache2License;
        File licenseFile = new File(licenseFileNameTarget);
        String licenseFileNameSource = File.separator + config.getCommonTemplateDir() + File.separator + apache2License;
        String licenseFileContents = readResourceContents(licenseFileNameSource);
        try {
            writeToFile(licenseFileNameTarget, licenseFileContents);
        } catch (IOException e) {
            throw new RuntimeException("Could not generate LICENSE file '" + apache2License + "'", e);
        }
        files.add(licenseFile);
         */

    }




}
