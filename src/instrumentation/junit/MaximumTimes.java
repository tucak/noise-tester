/**
 * 
 */
package instrumentation.junit;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target(METHOD)
/**
 * Always stop testing if the test has already run <tt>amount</tt> times.
 *
 */
public @interface MaximumTimes {
	int amount();
}
