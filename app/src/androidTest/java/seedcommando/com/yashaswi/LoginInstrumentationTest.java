package seedcommando.com.yashaswi;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by commando4 on 2/20/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginInstrumentationTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void typeOperandsAndPerformAddOperation() {
        performOperation( "emp", "sa");
    }

    private void performOperation( String operandOne,
                                  String operandTwo) {
        // Type the two operands in the EditText fields
        onView(withId(R.id.editText_username)).perform(typeText(operandOne),
                closeSoftKeyboard());
        onView(withId(R.id.editText_password)).perform(typeText(operandTwo),
                closeSoftKeyboard());

        // Click on a given operation button
        onView(withId(R.id.button_login)).perform(click());

        // Check the expected test is displayed in the Ui
        //onView(withId(R.id.operation_result_text_view)).check(matches(withText(expectedResult)));
        onView(withId(R.id.button_mark_Attendance)).perform(click());
        onView(withId(R.id.in_button)).perform(click());
        onView(withId(R.id.remark_txt_reason)).perform(typeText("hellow"),closeSoftKeyboard());
        onView(withId(R.id.submit)).perform(click());
    }

}
