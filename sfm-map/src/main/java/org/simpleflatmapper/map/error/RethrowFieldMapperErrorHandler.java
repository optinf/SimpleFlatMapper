package org.simpleflatmapper.map.error;

import org.simpleflatmapper.map.FieldMapperErrorHandler;
import org.simpleflatmapper.map.MappingException;
import org.simpleflatmapper.util.ErrorHelper;

public final class RethrowFieldMapperErrorHandler implements FieldMapperErrorHandler<Object> {

	public static RethrowFieldMapperErrorHandler INSTANCE = new RethrowFieldMapperErrorHandler();

	private RethrowFieldMapperErrorHandler() {
	}

	@Override
	public void errorMappingField(final Object name, final Object source, final Object target,
			final Exception error) throws MappingException {
        ErrorHelper.rethrow(error);
	}

}
