package com.shawn.eapi.model;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shawn.eapi.entity.DataModel;
import io.swagger.models.properties.*;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Functions.forMap;
import static com.google.common.base.Strings.nullToEmpty;
import static com.shawn.eapi.consts.DataModelType.UNIT_TYPE;

public class Properties {

    private static final Map<String, Function<String, ? extends Property>> typeFactory
            = ImmutableMap.<String, Function<String, ? extends Property>>builder()
            .put("int", newInstanceOf(IntegerProperty.class))
            .put("integer", newInstanceOf(IntegerProperty.class))
            .put("long", newInstanceOf(LongProperty.class))
            .put("float", newInstanceOf(FloatProperty.class))
            .put("double", newInstanceOf(DoubleProperty.class))
            .put("string", newInstanceOf(StringProperty.class))
            .put("boolean", newInstanceOf(BooleanProperty.class))
            .put("date", newInstanceOf(DateProperty.class))
            .put("date-time", newInstanceOf(DateTimeProperty.class))
            .put("bigdecimal", newInstanceOf(DecimalProperty.class))
            .put("biginteger", newInstanceOf(BaseIntegerProperty.class))
            .put("uuid", newInstanceOf(UUIDProperty.class))
            .put("object", newInstanceOf(ObjectProperty.class))
            .put("byte", bytePropertyFactory())
            .put("__file", filePropertyFactory())
            .build();

    private Properties() {
        throw new UnsupportedOperationException();
    }

    public static Property property(final String typeName) {
        String safeTypeName = nullToEmpty(typeName);
        Function<String, Function<String, ? extends Property>> propertyLookup
                = forMap(typeFactory, voidOrRef(safeTypeName));
        return propertyLookup.apply(safeTypeName.toLowerCase()).apply(safeTypeName);
    }

    public static Property property(DataModel dataModel) {
        String safeTypeName = nullToEmpty(dataModel.getDataType());

        if (safeTypeName.equals("array")) {
            return new ArrayProperty(itemTypeProperty(dataModel.getChildren().get(0)));
        }

        Function<String, Function<String, ? extends Property>> propertyLookup
                = forMap(typeFactory, voidOrRef(safeTypeName));
        return propertyLookup.apply(safeTypeName.toLowerCase()).apply(safeTypeName);
    }

    private static <T extends Property> Function<String, T> newInstanceOf(final Class<T> clazz) {
        return input -> {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                //This is bad! should never come here
                throw new IllegalStateException(e);
            }
        };
    }

    private static Function<String, ? extends Property> voidOrRef(final String typeName) {
        return (Function<String, Property>) input -> {
            if (typeName.equalsIgnoreCase("void")) {
                return null;
            }
            return new RefProperty(typeName);
        };
    }

    private static Function<String, ? extends Property> bytePropertyFactory() {
        return (Function<String, Property>) input -> {
            StringProperty byteArray = new StringProperty();
            byteArray.setFormat("byte");
            return byteArray;
        };
    }

    private static Function<String, ? extends Property> filePropertyFactory() {
        return (Function<String, Property>) input -> new FileProperty();
    }

    private static Property itemTypeProperty(DataModel dataModel) {
        if (dataModel.getDataType().equals("array")) {
            return new ArrayProperty(itemTypeProperty(dataModel.getChildren().get(0)));
        }
        return property(dataModel.getDataType());
    }

    public static Property mapProperty(DataModel source) {

        Property property = modelRefToProperty(source);
        if (property instanceof ArrayProperty && source.getChildren() != null
                && source.getChildren().size() > 0) {
            ArrayProperty arrayProperty = (ArrayProperty) property;
            arrayProperty.setItems(mapProperty(source.getChildren().get(0)));
//            maybeAddAllowableValues(arrayProperty.getItems(), source.getA llowableValues());
        }
//        if (property instanceof AbstractNumericProperty) {
//            AbstractNumericProperty numericProperty = (AbstractNumericProperty) property;
//            AllowableValues allowableValues = source.getAllowableValues();
//            if (allowableValues instanceof AllowableRangeValues) {
//                AllowableRangeValues range = (AllowableRangeValues) allowableValues;
//                numericProperty.maximum(safeBigDecimal(range.getMax()));
//                numericProperty.exclusiveMaximum(range.getExclusiveMax());
//                numericProperty.minimum(safeBigDecimal(range.getMin()));
//                numericProperty.exclusiveMinimum(range.getExclusiveMin());
//            }
//        }
//        if (property instanceof StringProperty) {
//            StringProperty stringProperty = (StringProperty) property;
//            AllowableValues allowableValues = source.getAllowableValues();
//            if (allowableValues instanceof AllowableRangeValues) {
//                AllowableRangeValues range = (AllowableRangeValues) allowableValues;
//                stringProperty.maxLength(safeInteger(range.getMax()));
//                stringProperty.minLength(safeInteger(range.getMin()));
//            }
//            if (source.getPattern() != null) {
//                stringProperty.setPattern(source.getPattern());
//            }
//        }
        if (property instanceof ObjectProperty) {

            Map<String, Property> mappedProperties = new LinkedHashMap<>();
            ObjectProperty objectProperty = (ObjectProperty) property;
            source.getChildren().forEach(propertie -> { // 迭代
                mappedProperties.put(propertie.getName(), mapProperty(propertie));
            });

            objectProperty.setProperties(mappedProperties);
        }

        if (property != null) {
            property.setDescription(source.getDescription());
            property.setName(source.getName());
            property.setRequired(source.isRequired());
//            property.setReadOnly(source.isReadOnly());
            if (StringUtils.isNotEmpty(source.getExample())) {
                property.setExample((Object) source.getExample());
            }

        }
        return property;
    }

    static Property modelRefToProperty(DataModel dataModel) {
        if (dataModel.getDataType() == null) {
            return null;
        }
        Property responseProperty;
        if ("array".equalsIgnoreCase(dataModel.getDataType())) {
            responseProperty = Properties.property(dataModel);
        } else {
            responseProperty = Properties.property(dataModel.getDataType());
        }

//        maybeAddAllowableValues(responseProperty, modelRef.getAllowableValues());

        return responseProperty;
    }


    public static void mapProperties(Map<String, Property> properties, DataModel parent) {

        if (properties == null) {
            return;
        }
        List<DataModel> children = Lists.newArrayList();

        for (Map.Entry<String, Property> _entry : properties.entrySet()) {
            DataModel dataModel = new DataModel();
            Property property = _entry.getValue();
            dataModel.setName(_entry.getKey());
            dataModel.setType(UNIT_TYPE);
            dataModel.setParent(parent);
            dataModel.setDescription(property.getDescription());
            dataModel.setRequired(property.getRequired());

            if (property instanceof LongProperty) {
                dataModel.setDataType("long");
            } else if (property instanceof DateProperty) {
                dataModel.setDataType("date");
            } else if (property instanceof FloatProperty) {
                dataModel.setDataType("float");
            } else if (property instanceof DoubleProperty) {
                dataModel.setDataType("double");
            } else if (property instanceof DecimalProperty
                    && property.getType().equals("number")) {
                dataModel.setDataType("bigdecimal");
            } else {
                dataModel.setDataType(property.getType());
            }

            if (property.getExample() != null) {
                dataModel.setExample(property.getExample().toString());
            }

            if (property instanceof RefProperty) {
                RefProperty refProperty = (RefProperty) property;
                dataModel.setDataType(refProperty.getSimpleRef());
            }

            if (property instanceof ObjectProperty) {
                ObjectProperty objectProperty = (ObjectProperty) property;

                Map<String, Property> mappedProperties = objectProperty.getProperties();

//                for (Map.Entry<String, Property> __entity : mappedProperties.entrySet()) {
//                    Property property1 = __entity.getValue();
//                }

                mapProperties(mappedProperties, dataModel);

//                for (Map.Entry<String, Property> __entity : mappedProperties.entrySet()) {
//                    Property property1 = __entity.getValue();
//                }

//                Map<String, Property> mappedProperties = new LinkedHashMap<>();
//
//                source.getChildren().forEach(propertie -> { // 迭代
//                    mappedProperties.put(propertie.getName(), mapProperty(propertie));
//                });
//
//                objectProperty.setProperties(mappedProperties);
            }

            if (property instanceof ArrayProperty) {
                ArrayProperty arrayProperty = (ArrayProperty) property;
                mapPropertie(arrayProperty, dataModel);
//                arrayProperty.getItems()
//                arrayProperty.setItems(mapProperty(source.getChildren().get(0)));
//            maybeAddAllowableValues(arrayProperty.getItems(), source.getA llowableValues());
            }

            if (dataModel.getParent() != null && dataModel.getParent().getType().equals("array")) {
                dataModel.setName(null);
            }

            children.add(dataModel);

        }
        parent.setChildren(children);
    }

    public static void mapPropertie(Property property, DataModel parent) {
        if (property instanceof ArrayProperty) {
            ArrayProperty arrayProperty = (ArrayProperty) property;
            Map<String, Property> arrayPropertys = Maps.newHashMap();
            arrayPropertys.put(arrayProperty.getItems().getName(), arrayProperty.getItems());
            mapProperties(arrayPropertys, parent);
        } else {
            Map<String, Property> arrayPropertys = Maps.newHashMap();
            arrayPropertys.put(property.getName(), property);
            mapProperties(arrayPropertys, parent);
        }
    }
}
