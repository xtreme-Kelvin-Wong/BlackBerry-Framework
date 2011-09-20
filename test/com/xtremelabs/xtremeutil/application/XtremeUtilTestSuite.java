package com.xtremelabs.xtremeutil.application;

import com.xtremelabs.xtremeutil.application.descriptor.AppWorldDataTest;
import com.xtremelabs.xtremeutil.application.descriptor.JadParserTest;
import com.xtremelabs.xtremeutil.device.api.persistence.PersistenceTest;
import com.xtremelabs.xtremeutil.device.api.runtimestore.RuntimeSingletonFactoryTestSuite;
import com.xtremelabs.xtremeutil.util.datetime.DateUtilsTest;
import com.xtremelabs.xtremeutil.util.lang.AggregateEnumerationTest;
import com.xtremelabs.xtremeutil.util.lang.ArrayUtilsTest;
import com.xtremelabs.xtremeutil.util.lang.CoerceTest;
import com.xtremelabs.xtremeutil.util.lang.StackTest;
import com.xtremelabs.xtremeutil.util.string.XStringUtilitiesTest;
import rimunit.TestSuite;


public class XtremeUtilTestSuite extends TestSuite {
    protected Class[] suite() {
        return new Class[]{
                ArrayUtilsTest.TestContainsFunction.class,

                DateUtilsTest.TestCreateDate.class,
                DateUtilsTest.TestFormatNoPunctuationUtc.class,
                DateUtilsTest.TestFormatRange.class,
                DateUtilsTest.TestFormatTime.class,
                DateUtilsTest.TestCreateDate.class,
                DateUtilsTest.TestDifferentDay.class,

                StackTest.TestPop.class,
                StackTest.TestPush.class,

                XStringUtilitiesTest.TestChunkDelimitersDisabled.class,
                XStringUtilitiesTest.TestChunkShouldHandleDelimiterAtBothEnds.class,
                XStringUtilitiesTest.TestChunkShouldHandleMultiCharacterDelimiters.class,
                XStringUtilitiesTest.TestRemoveWhitespace.class,
                XStringUtilitiesTest.TestCollapseWhitespace.class,
                XStringUtilitiesTest.TestIndexOfAny.class,
                XStringUtilitiesTest.TestLongestRun.class,
                XStringUtilitiesTest.TestRemoveChars.class,
                XStringUtilitiesTest.TestReturnsOriginalStringIfLessThan16Chars.class,
                XStringUtilitiesTest.TestWordWrapNullStringReturnsEmptyString.class,
                XStringUtilitiesTest.TestIsWhitespace.class,
                XStringUtilitiesTest.TestIsLetter.class,
                XStringUtilitiesTest.TestSplit.class,
                XStringUtilitiesTest.TestWordBoundaryPredicates.class,
                XStringUtilitiesTest.TestValidateEmailAddressSyntax.class,

                CoerceTest.TestBooleanCoercion.class,
                CoerceTest.TestSafeUnbox.class,
                CoerceTest.TestCoercePrimitiveToString.class,
                CoerceTest.TestCoerceObjectToString.class,
                CoerceTest.TestCoerceToClassWithDefaultValue.class,
                CoerceTest.TestCoerceToClassWithDefaultInstantiation.class,

                RuntimeSingletonFactoryTestSuite.class,

                JadParserTest.TestIsFromAppWorld.class,
                AppWorldDataTest.TestInitialization.class,
                AggregateEnumerationTest.class,

                PersistenceTest.LoadNonExistantKeyTest.class,
                PersistenceTest.LoadKeyTest.class,
                PersistenceTest.DeleteKeyTest.class
        };
    }
}
