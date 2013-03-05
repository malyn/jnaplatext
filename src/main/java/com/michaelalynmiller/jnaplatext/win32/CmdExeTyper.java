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
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.WPARAM;

/**
 * Sends strings of text to the Windows cmd.exe process.
 *
 * @author      Michael Alyn Miller <malyn@strangeGizmo.com>
 * @version     1.0.0
 */
public class CmdExeTyper {
    /** Window handle for the cmd.exe process. */
    private HWND hwnd;

    /**
     * Constructs a new CmdExeTyper that targets the cmd.exe process
     * identified by the given process id.
     *
     * @param processId Process id of the target cmd.exe process.
     */
    public CmdExeTyper(final int processId) throws Exception {
        /* Get the list of cmd.exe's windows. */
        List<HWND> cmdExeWindowList
            = ProcessUtils.getProcessWindows(processId);

        /* Fatal error if we didn't get any windows for cmd.exe or if
         * cmd.exe has more than one window. */
        if (cmdExeWindowList == null || cmdExeWindowList.size() != 1) {
            throw new Exception("Couldn't find cmd.exe's window.");
        }

        /* We're now ready to send characters to the window. */
        this.hwnd = cmdExeWindowList.get(0);
    }

    /**
     * Constructs a new CmdExeTyper that targets the given cmd.exe
     * window.
     *
     * @param cmdExeHwnd Window handle for cmd.exe's window.
     */
    public CmdExeTyper(final HWND cmdExeHwnd) {
        this.hwnd = cmdExeHwnd;
    }

    /**
     * Writes text to cmd.exe.
     *
     * @param text The text to write to cmd.exe.
     */
    public final void write(final String text) {
        for (char c : text.toCharArray()) {
            /* Send the character to cmd.exe. */
            User32.INSTANCE.PostMessage(
                this.hwnd, WinUser.WM_CHAR,
                new WPARAM(c), new LPARAM(0));

            /* Normally cmd.exe won't need a WM_KEYUP, but if this is a
             * repeated series of characters (33, aaa, etc.) then
             * cmd.exe will ignore every character other than the first
             * one unless it gets a WM_KEYUP after each WM_CHAR. */
            short vkey = User32.INSTANCE.VkKeyScan(c);
            int oemScan = User32.INSTANCE.MapVirtualKey(
                vkey & 0xff, 0); /* MAPVK_VK_TO_VSC */
            User32.INSTANCE.PostMessage(
                this.hwnd, WinUser.WM_KEYUP,
                new WPARAM(vkey & 0xff),
                new LPARAM(0 | (oemScan << 16) | (3 << 31)));
        }
    }
}
