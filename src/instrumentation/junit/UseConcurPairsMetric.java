package instrumentation.junit;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Use the ConcurPairs parallel metric.
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface UseConcurPairsMetric {
	double threshold() default 1.0;
	boolean optional() default false;
	int window() default 10;
}
