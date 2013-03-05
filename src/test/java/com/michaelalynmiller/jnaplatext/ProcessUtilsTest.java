/*
 * Copyright (c) 2013, Michael Alyn Miller <malyn@strangeGizmo.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice unmodified, this list of conditions, and the following
 *    disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of Michael Alyn Miller nor the names of the
 *    contributors to this software may be used to endorse or promote
 *    products derived from this software without specific prior written
 *    permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

package com.michaelalynmiller.jnaplatext.win32;

/* Java imports. */
import java.util.List;

/* JNA imports. */
import com.sun.jna.platform.win32.WinDef.HWND;

/* JNA platform extension imports. */
import com.michaelalynmiller.jnaplatext.win32.ProcessUtils;

/* JUnit imports. */
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for ProcessUtils.
 */
public class ProcessUtilsTest extends Assert {
    /**
     * Basic getProcessList test.
     */
	@Test
    public void testGetProcessList() throws Exception {
        List<ProcessUtils.ProcessInfo> processList
            = ProcessUtils.getProcessList();
    }

    /**
     * Basic getProcessAncestors test.
     */
	@Test
    public void testGetProcessAncestors() throws Exception {
        List<ProcessUtils.ProcessInfo> processAncestors
            = ProcessUtils.getProcessAncestors();
    }

    /**
     * Basic getProcessAncestors test.
     */
	@Test
    public void testGetProcessAncestorsGivenProcessList() throws Exception {
        List<ProcessUtils.ProcessInfo> processList
            = ProcessUtils.getProcessList();
        List<ProcessUtils.ProcessInfo> processAncestors
            = ProcessUtils.getProcessAncestors(processList);
    }

    /**
     * Basic getProcessWindows test.
     */
	@Test
    public void testGetProcessWindows() throws Exception {
        List<HWND> processWindows
            = ProcessUtils.getProcessWindows(
                    Kernel32.INSTANCE.GetCurrentProcessId());
    }
}
