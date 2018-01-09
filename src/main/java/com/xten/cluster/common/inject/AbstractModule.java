package com.xten.cluster.common.inject;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.name.Names;

import java.util.Objects;
import java.util.Properties;


/**
 * A support class for {@link Module}s which reduces repetition and results in
 * a more readable configuration. Simply extend this class, implement {@link
 * #configure()}, and call the inherited methods which mirror those found in
 * {@link Binder}. For example:
 * <pre>
 * public class MyModule extends AbstractModule {
 *   protected void configure() {
 *     bind(Service.class).to(ServiceImpl.class).in(Singleton.class);
 *     bind(CreditCardPaymentService.class);
 *     bind(PaymentService.class).to(CreditCardPaymentService.class);
 *     bindConstant().annotatedWith(Names.named("port")).to(8080);
 *   }
 * }
 * </pre>
 *
 * @author crazybob@google.com (Bob Lee)
 */
public abstract class AbstractModule implements Module {

    Binder binder;

    @Override
    public final synchronized void configure(Binder builder) {
        if (this.binder != null) {
            throw new IllegalStateException("Re-entry is not allowed.");
        }
        this.binder = Objects.requireNonNull(builder, "builder");
        try {
            configure();
        } finally {
            this.binder = null;
        }
    }

    /**
     * Configures a {@link Binder} via the exposed methods.
     */
    protected abstract void configure();

    /**
     * Gets direct access to the underlying {@code Binder}.
     */
    protected Binder binder() {
        return binder;
    }

    /**
     * @see Binder#bind(Class)
     */
    protected <T> AnnotatedBindingBuilder<T> bind(Class<T> clazz) {
        return binder.bind(clazz);
    }

    /**
     * 绑定Properties
     * @param properties
     */
    protected void bindProperties(Properties properties) {
        Names.bindProperties(binder,properties);
    }

}
