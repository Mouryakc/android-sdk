package com.ibm.watson.developer_cloud.android.myapplication;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by blakeball on 10/3/16.
 */

@RunWith(AndroidJUnit4.class)
public class GalleryHelperTest {

  private Instrumentation.ActivityResult result;

  @Rule
  public ActivityTestRule<MainActivity> activityTestRule =
      new ActivityTestRule<>(MainActivity.class);

  @Before
  public void setupImageUri() {
    Resources resources = InstrumentationRegistry.getTargetContext().getResources();
    Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
        resources.getResourcePackageName(R.mipmap.ic_launcher) + '/' +
        resources.getResourceTypeName(R.mipmap.ic_launcher) + '/' +
        resources.getResourceEntryName(R.mipmap.ic_launcher));

    Intent resultData = new Intent();
    resultData.setData(imageUri);
    result = new Instrumentation.ActivityResult(
        Activity.RESULT_OK, resultData);
  }

  @Test
  public void testSelectedImageIsSet() {

    Intents.init();

    Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_PICK),
        hasData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
    intending(expectedIntent).respondWith(result);

    Espresso.onView(withId(R.id.gallery_button)).perform(click());
    intended(expectedIntent);


    //intending(hasComponent(String.valueOf(hasAction(MediaStore.ACTION_IMAGE_CAPTURE))));
    Intents.release();
  }
}
