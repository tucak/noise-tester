package instrumentation.junit;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
/**
 * Log the accumulated amount of tasks for each metric into the file specified by <tt>file</tt>.
 *
 */
public @interface ParallelMetricLog {
	String file();
}
