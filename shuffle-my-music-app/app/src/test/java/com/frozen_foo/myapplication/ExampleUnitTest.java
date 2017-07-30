package com.frozen_foo.myapplication;

import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testJar() {

		String content = "5>>Start\r\n" +
				"1\r\n" +
				"2\r\n" +
				"3\r\n" +
				"4\r\n" +
				"5\r\n" +
				"<<End";
		InputStream inputStream = new ByteArrayInputStream(content.getBytes());
		String[] strings = new ShuffleMyMusicService().randomIndexEntries(inputStream, 2);
		assert 2 == strings.length;
    }
}