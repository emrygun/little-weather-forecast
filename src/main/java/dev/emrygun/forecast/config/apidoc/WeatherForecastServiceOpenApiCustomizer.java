package dev.emrygun.forecast.config.apidoc;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;

@Configuration
public class WeatherForecastServiceOpenApiCustomizer implements OpenApiCustomizer {

    @Override
    public void customise(OpenAPI openApi) {
        openApi.getComponents().addSchemas("ProblemDetail", createSchemaWithDescription(ProblemDetail.class, "Problem Detail"));
    }

    private Schema<?> createSchemaWithDescription(Class<?> className, String description) {
        ResolvedSchema resolvedSchema = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(className).resolveAsRef(false));
        return resolvedSchema.schema.description(description);
    }
}
