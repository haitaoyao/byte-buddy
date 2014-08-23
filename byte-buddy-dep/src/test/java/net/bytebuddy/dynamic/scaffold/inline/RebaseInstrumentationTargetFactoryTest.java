package net.bytebuddy.dynamic.scaffold.inline;

import net.bytebuddy.dynamic.scaffold.BridgeMethodResolver;
import net.bytebuddy.instrumentation.Instrumentation;
import net.bytebuddy.instrumentation.method.MethodDescription;
import net.bytebuddy.instrumentation.method.MethodList;
import net.bytebuddy.instrumentation.method.MethodLookupEngine;
import net.bytebuddy.instrumentation.type.TypeDescription;
import net.bytebuddy.utility.MockitoRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RebaseInstrumentationTargetFactoryTest {

    @Rule
    public TestRule mockitoRule = new MockitoRule(this);

    @Mock
    private BridgeMethodResolver.Factory bridgeMethodResolverFactory;
    @Mock
    private MethodFlatteningResolver methodFlatteningResolver;
    @Mock
    private MethodLookupEngine.Finding finding;
    @Mock
    private TypeDescription instrumentedType, superType;

    private Instrumentation.Target.Factory factory;

    @Before
    public void setUp() throws Exception {
        when(finding.getInvokableMethods()).thenReturn(new MethodList.Empty());
        when(finding.getInvokableDefaultMethods()).thenReturn(Collections.<TypeDescription, Set<MethodDescription>>emptyMap());
        when(finding.getTypeDescription()).thenReturn(instrumentedType);
        when(instrumentedType.getSupertype()).thenReturn(superType);
        when(superType.getDeclaredMethods()).thenReturn(new MethodList.Empty());
        factory = new RebaseInstrumentationTarget.Factory(bridgeMethodResolverFactory, methodFlatteningResolver);
    }

    @Test
    public void testReturnsRebaseInstrumentationTarget() throws Exception {
        assertThat(factory.make(finding) instanceof RebaseInstrumentationTarget, is(true));
    }

    @Test
    public void testHashCodeEquals() throws Exception {
        assertThat(factory.hashCode(), is(new RebaseInstrumentationTarget.Factory(bridgeMethodResolverFactory, methodFlatteningResolver).hashCode()));
        assertThat(factory, is((Instrumentation.Target.Factory) new RebaseInstrumentationTarget.Factory(bridgeMethodResolverFactory, methodFlatteningResolver)));
        BridgeMethodResolver.Factory otherFactory = mock(BridgeMethodResolver.Factory.class);
        assertThat(factory.hashCode(), not(is(new RebaseInstrumentationTarget.Factory(otherFactory, methodFlatteningResolver).hashCode())));
        assertThat(factory, not(is((Instrumentation.Target.Factory) new RebaseInstrumentationTarget.Factory(otherFactory, methodFlatteningResolver))));
    }
}