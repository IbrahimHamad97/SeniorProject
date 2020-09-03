package com.cmps312.courseregistration;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cmps312.courseregistration.db.DatabaseSystem;
import com.cmps312.courseregistration.model.Student;
import com.google.firebase.FirebaseApp;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class DbInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.cmps312.courseregistration", appContext.getPackageName());
    }

    @Test
    public void getStudent() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        DatabaseSystem db = new DatabaseSystem(appContext);

        Student std = db.getStudent("201308047");

        assertNotNull(std);
    }
}
