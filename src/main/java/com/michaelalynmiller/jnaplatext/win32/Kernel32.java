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

/* JNA imports. */
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

/**
 * Provides JNA access to Win32 functions in KERNEL32.DLL.
 *
 * @author      Michael Alyn Miller <malyn@strangeGizmo.com>
 * @version     1.0.0
 */
public interface Kernel32
    extends StdCallLibrary,
            com.sun.jna.platform.win32.Kernel32 {

    /** Instance of KERNEL32.DLL for use in accessing native functions. */
    Kernel32 INSTANCE = (Kernel32) Native.loadLibrary(
            "kernel32", Kernel32.class, W32APIOptions.DEFAULT_OPTIONS);

    /**
     * Retrieves the full name of the executable image for the specified
     * process.
     *
     * @param hProcess Handle to the process.
     * @param dwFlags Type of path format to return.
     * @param lpExeName On output, the path to the executable image.
     * @param lpdwSize On input, the size of lpExeName.  On success, the
     *  number of characters written to the buffer.
     * @return true if the function succeeds, false otherwise.
     */
    boolean QueryFullProcessImageName(
            HANDLE hProcess, DWORD dwFlags,
            char[] lpExeName, IntByReference lpdwSize);
}

