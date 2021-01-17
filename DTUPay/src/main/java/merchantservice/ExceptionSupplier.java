package merchantservice;

import java.util.function.Supplier;

public interface ExceptionSupplier<F extends Exception> extends Supplier<F> {
    public F get(String errorMessage);
}
