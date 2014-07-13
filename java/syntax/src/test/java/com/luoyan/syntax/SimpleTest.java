package com.luoyan.syntax;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimpleTest {
	//
	@Mock
	private A m_a ; 
	
	//@Mock
	@InjectMocks
	private Simple m_Simple;// = new Simple();
	
	private void refreshMockBehavior() {
		//given(m_Simple.add(anyInt(), anyInt())).willReturn(3);

		//given(m_a.add(anyInt(), anyInt())).willReturn(2);
		given(m_a.add(anyInt(), anyInt())).willReturn(3);
	}
    private void initMock() {
        //MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(this);
        refreshMockBehavior();
    }
    @Before
    public void setUp() throws Exception {
        //initData();
        initMock();
    }
	@Test
	public void testAdd() {
		//Simple a = new Simple();
		//refreshMockBehavior();
		//m_a = mock(A.class);
		//m_Simple = mock(Simple.class);
		System.out.println(m_Simple);
		System.out.println(new A());
		System.out.println(m_a);
		System.out.println(m_Simple.m_a);
		assertEquals(3, m_Simple.add(1, 2));
		assertEquals(3, m_Simple.add(4, 2));
	}
}
