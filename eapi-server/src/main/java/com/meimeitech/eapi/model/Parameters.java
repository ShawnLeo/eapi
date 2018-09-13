package com.meimeitech.eapi.model;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.meimeitech.eapi.consts.ParamInConsts;
import com.meimeitech.eapi.entity.RequestInfo;
import io.swagger.models.Model;
import io.swagger.models.parameters.*;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;

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
            ((AbstractSerializableParameter) parameter).setType(requestInfo.getDataModel().getDataType());
        }

        parameter.setIn(requestInfo.getParamIn());
        parameter.setName(requestInfo.getDataModel().getName());
        parameter.setRequired(requestInfo.getDataModel().isRequired());
        parameter.setDescription(requestInfo.getDataModel().getDescription());

        return parameter;
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
