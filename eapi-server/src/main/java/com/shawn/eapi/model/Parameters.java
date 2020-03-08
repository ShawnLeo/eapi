package com.shawn.eapi.model;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.shawn.eapi.consts.ParamInConsts;
import com.shawn.eapi.entity.DataModel;
import com.shawn.eapi.entity.RequestInfo;
import io.swagger.models.ArrayModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.RefModel;
import io.swagger.models.parameters.*;
import io.swagger.models.properties.*;
import org.apache.commons.lang3.StringUtils;

import static com.shawn.eapi.consts.DataModelType.UNIT_TYPE;

import java.util.Date;
import java.util.Map;

public class Parameters {

    private static final Map<String, Function<String, ? extends Parameter>> typeFactory
            = ImmutableMap.<String, Function<String, ? extends Parameter>>builder()
            .put(ParamInConsts.path.name(), newInstanceOf(PathParameter.class))
            .put(ParamInConsts.query.name(), newInstanceOf(QueryParameter.class))
            .put(ParamInConsts.body.name(), newInstanceOf(BodyParameter.class))
            .put(ParamInConsts.formData.name(), newInstanceOf(FormParameter.class))
            .put(ParamInConsts.header.name(), newInstanceOf(HeaderParameter.class))
            .build();

    public static Parameter parameter(RequestInfo requestInfo) {
        String paramInType = requestInfo.getParamIn();

        Function<String, ? extends Parameter> function = typeFactory.get(paramInType);

        Parameter parameter = function.apply(paramInType);

        if (parameter instanceof BodyParameter) {
            Property property = Properties.mapProperty(requestInfo.getDataModel());
            Model model = PropertyBuilder.toModel(property);
            if (property instanceof ObjectProperty) {
                model.setProperties(((ObjectProperty) property).getProperties());
            }
            ((BodyParameter) parameter).setSchema(model);
        } else {

            String type = requestInfo.getDataModel().getDataType();

            if(type.equals("long")){
                ((AbstractSerializableParameter) parameter).setType("integer");
                ((AbstractSerializableParameter) parameter).setFormat("int64");
            } else if(type.equals("date")) {
                ((AbstractSerializableParameter) parameter).setType("string");
                ((AbstractSerializableParameter) parameter).setFormat("date");
            } else if(type.equals("float")) {
                ((AbstractSerializableParameter) parameter).setType("number");
                ((AbstractSerializableParameter) parameter).setFormat("float");
            } else if(type.equals("double")) {
                ((AbstractSerializableParameter) parameter).setType("number");
                ((AbstractSerializableParameter) parameter).setFormat("double");
            } else {
                ((AbstractSerializableParameter) parameter).setType(requestInfo.getDataModel().getDataType());
            }
        }

        parameter.setIn(requestInfo.getParamIn());
        parameter.setName(requestInfo.getDataModel().getName());
        parameter.setRequired(requestInfo.getDataModel().isRequired());
        parameter.setDescription(requestInfo.getDataModel().getDescription());

        return parameter;
    }

    public static RequestInfo parameter(Parameter parameter) {

        Function<String, ? extends Parameter> function = typeFactory.get(parameter.getIn());

        RequestInfo requestInfo = new RequestInfo();
//        requestInfo.setCreater(creater);
        requestInfo.setCreateTime(new Date());
        requestInfo.setParamIn(parameter.getIn());

        DataModel dataModel = new DataModel();
        dataModel.setCreateTime(new Date());
//        dataModel.setCreater(creater);
        dataModel.setName(parameter.getName());
        dataModel.setDescription(parameter.getDescription());
        dataModel.setRequired(parameter.getRequired());
        dataModel.setType(UNIT_TYPE);
//        dataModel.setProjectId();
        if (parameter instanceof BodyParameter) {
            BodyParameter bodyParameter = (BodyParameter) parameter;
            Model model = bodyParameter.getSchema();

            if (model instanceof ModelImpl) {
                ModelImpl modelImpl = (ModelImpl) model;
                String type = modelImpl.getType();
                String format = modelImpl.getFormat();

                if (StringUtils.isEmpty(format)) {
                    dataModel.setDataType(type);
                } else if (type.equals("integer") && format.equals("int64")) {
                    dataModel.setDataType("long");
                } else if (type.equals("string") && format.equals("date")) {
                    dataModel.setDataType("date");
                } else if (type.equals("number") && format.equals("float")) {
                    dataModel.setDataType("float");
                } else if (type.equals("number") && format.equals("double")) {
                    dataModel.setDataType("double");
                } else {
                    dataModel.setDataType(type);
                }
            }

            if (model instanceof ArrayModel) {
                ArrayModel arrayModel = (ArrayModel) model;
                dataModel.setDataType(arrayModel.getType());

                Property propertie = arrayModel.getItems();
                Properties.mapPropertie(propertie, dataModel);
            }

            if(model instanceof RefModel) {
                RefModel refModel = (RefModel) model;
                dataModel.setDataType(refModel.getSimpleRef());
            }

            Map<String, Property> properties = model.getProperties();
            Properties.mapProperties(properties, dataModel);

        } else {
            AbstractSerializableParameter abstractSerializableParameter =  (AbstractSerializableParameter) parameter;

            String type =  abstractSerializableParameter.getType();
            String format =  abstractSerializableParameter.getFormat();

            if (StringUtils.isEmpty(format)) {
                dataModel.setDataType(abstractSerializableParameter.getType());
            } else if(type.equals("integer") && format.equals("int64")){
                dataModel.setDataType("long");
            } else if(type.equals("string") && format.equals("date")) {
                dataModel.setDataType("date");
            } else if(type.equals("number") && format.equals("float")) {
                dataModel.setDataType("float");
            } else if(type.equals("number") && format.equals("double")) {
                dataModel.setDataType("double");
            } else {
                dataModel.setDataType(abstractSerializableParameter.getType());
            }

        }
        requestInfo.setDataModel(dataModel);

        return requestInfo;
    }


    private static <T extends AbstractParameter> Function<String, T> newInstanceOf(final Class<T> clazz) {
        return input -> {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                //This is bad! should never come here
                throw new IllegalStateException(e);
            }
        };
    }

}
