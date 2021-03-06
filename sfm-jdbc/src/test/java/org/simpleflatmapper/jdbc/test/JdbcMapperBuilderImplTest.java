package org.simpleflatmapper.jdbc.test;

import org.junit.Test;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperBuilder;
import org.simpleflatmapper.map.FieldMapper;
import org.simpleflatmapper.map.MappingContext;
import org.simpleflatmapper.map.MappingException;
import org.simpleflatmapper.reflect.BiInstantiator;
import org.simpleflatmapper.reflect.InstantiatorDefinition;
import org.simpleflatmapper.reflect.Parameter;
import org.simpleflatmapper.reflect.ReflectionService;
import org.simpleflatmapper.test.beans.DbObject;
import org.simpleflatmapper.reflect.asm.AsmFactory;
import org.simpleflatmapper.reflect.InstantiatorFactory;
import org.simpleflatmapper.util.BiFunction;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class JdbcMapperBuilderImplTest {

	@Test
	public void testAddFieldMapper() {
		
		JdbcMapperBuilder<DbObject> builder = JdbcMapperFactoryHelper.asm().newBuilder(DbObject.class);
		
		JdbcMapper<DbObject> mapper = builder.addMapper(new FieldMapper<ResultSet, DbObject>() {
			
			@Override
			public void mapTo(ResultSet source, DbObject target, MappingContext<? super ResultSet> mappingContext) throws MappingException {
				target.setId(33);
			}
		}).mapper();
		
		assertEquals(33, mapper.map(null).getId());
	}

	
	@Test
	public void testAsmFailureOnmapper() {

		JdbcMapperBuilder<DbObject> builder = JdbcMapperFactoryHelper.noFailOnAsm().reflectionService(new ReflectionService(newAsmFactoryFailsOnmapper())).newBuilder(DbObject.class);

		final JdbcMapper<DbObject> mapper = builder.mapper();
		assertTrue(mapper.getClass().getSimpleName().equals("StaticJdbcSetRowMapper"));
	}

	public AsmFactory newAsmFactoryFailsOnmapper() {
		return new AsmFactory(Thread.currentThread().getContextClassLoader()) {
			@Override
			public Class<?> createClass(String className, byte[] bytes, ClassLoader declaringClassLoader) {
				throw new IllegalArgumentException("Invalid class");
			}
		};
	}

	@Test
    public void testAsmFailureOnJdbcMapperFailOnAsm() {
		JdbcMapperBuilder<DbObject> builder = JdbcMapperFactoryHelper.asm().reflectionService(new ReflectionService(newAsmFactoryFailsOnmapper())).newBuilder(DbObject.class);

        try {
            builder.mapper();
            fail();
        } catch(IllegalArgumentException e) {
            // expected
        }
    }
	
	@Test
	public void testAsmFailureOnInstantiator() {
		JdbcMapperBuilder<DbObject> builder = JdbcMapperFactoryHelper.noFailOnAsm().reflectionService(new ReflectionService(null) {
			@Override
			public InstantiatorFactory getInstantiatorFactory() {
				return new InstantiatorFactory(null) {

					@Override
					public <S1, S2, T> BiInstantiator<S1, S2, T> getBiInstantiator(Type target, Class<?> s1, Class<?> s2, List<InstantiatorDefinition> constructors, Map<Parameter, BiFunction<? super S1, ? super S2, ?>> injections, boolean useAsmIfEnabled) throws SecurityException {
						throw new UnsupportedOperationException();
					}
				};
			}
			
		}).newBuilder(DbObject.class);
		
		try {
			builder.mapper();
			fail("Expected exception");
		} catch(UnsupportedOperationException e) {
		}
	}


}
