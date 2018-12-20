package com.meimeitech.sys.util;

/**
 * Created by wanglu-jf on 17/9/8.
 */

import org.hibernate.validator.HibernateValidatorFactory;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.spi.scripting.ScriptEvaluator;
import org.hibernate.validator.spi.scripting.ScriptEvaluatorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Validation;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import static org.hibernate.validator.internal.util.logging.Messages.MESSAGES;

public class PropertyScriptAssertValidator implements ConstraintValidator<PropertyScriptAssert, Object> {

    private static final Log log = LoggerFactory.make(MethodHandles.lookup());

    private String script;
    private String languageName;
    private String alias;
    private String property;
    private String message;

    public void initialize(PropertyScriptAssert constraintAnnotation) {
        validateParameters( constraintAnnotation );

        this.script = constraintAnnotation.script();
        this.languageName = constraintAnnotation.lang();
        this.alias = constraintAnnotation.alias();
        this.property = constraintAnnotation.property();
        this.message = constraintAnnotation.message();
    }

    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        Object evaluationResult;
        ScriptEvaluator scriptEvaluator;

        HibernateValidatorFactory validatorFactory = (HibernateValidatorFactory) Validation.buildDefaultValidatorFactory();
        ScriptEvaluatorFactory evaluatorFactory = validatorFactory.getScriptEvaluatorFactory();
        scriptEvaluator = evaluatorFactory.getScriptEvaluatorByLanguageName( languageName );

//            evaluationResult = scriptEvaluator.evaluate(script, value, alias);
//            Map<String, Object> map = StringUtils.objParseMap(value);
//            map.put("alias",alias);
//            ScriptAssertValidator
            Map<String, Object> bindings = new HashMap();
            bindings.put(alias, value);
            evaluationResult = scriptEvaluator.evaluate(script, bindings);

        if ( evaluationResult == null ) {
            throw log.getScriptMustReturnTrueOrFalseException( script );
        }
        if ( !( evaluationResult instanceof Boolean ) ) {
            throw log.getScriptMustReturnTrueOrFalseException(
                    script,
                    evaluationResult,
                    evaluationResult.getClass().getCanonicalName()
            );
        }

        if(Boolean.FALSE.equals(evaluationResult)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(property)
                    .addConstraintViolation();
        }

        return Boolean.TRUE.equals( evaluationResult );
    }

    private void validateParameters(PropertyScriptAssert constraintAnnotation) {
        Contracts.assertNotEmpty( constraintAnnotation.script(), MESSAGES.parameterMustNotBeEmpty( "script" ) );
        Contracts.assertNotEmpty( constraintAnnotation.lang(), MESSAGES.parameterMustNotBeEmpty( "lang" ) );
        Contracts.assertNotEmpty( constraintAnnotation.alias(), MESSAGES.parameterMustNotBeEmpty( "alias" ) );
        Contracts.assertNotEmpty( constraintAnnotation.property(), MESSAGES.parameterMustNotBeEmpty( "property" ) );
        Contracts.assertNotEmpty( constraintAnnotation.message(), MESSAGES.parameterMustNotBeEmpty( "message" ) );
    }
}
