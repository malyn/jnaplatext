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
import com.sun.jna.WString;
import com.sun.jna.platform.win32.BaseTSD.LONG_PTR;
import com.sun.jna.platform.win32.WinDef.WORD;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

/**
 * Provides JNA access to Win32 functions in USER32.DLL.
 *
 * @author      Michael Alyn Miller <malyn@strangeGizmo.com>
 * @version     1.0.0
 */
public interface User32
    extends StdCallLibrary,
            com.sun.jna.platform.win32.User32 {

    /** Instance of USER32.DLL for use in accessing native functions. */
    User32 INSTANCE = (User32) Native.loadLibrary(
            "user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

    /**
     * An atom.
     */
    public static class ATOM extends WORD {
        /**
         * Instantiates a new ATOM.
         */
        public ATOM() {
            this(0);
        }

        /**
         * Instantiates a new ATOM with a given value.
         *
         * @param value the value
         */
        public ATOM(final long value) {
            super(value);
        }
    }

    /**
     * A pointer to any type.
     */
    public static class LPVOID extends LONG_PTR {
        /**
         * Instantiates a new LPVOID.
         */
        public LPVOID() {
            this(0);
        }

        /**
         * Instantiates a new LPVOID with the given value.
         *
         * @param value the value
         */
        public LPVOID(final long value) {
            super(value);
        }
    }


    /**
     * Creates an overlapped, pop-up, or child window with an extended
     * window style; otherwise, this function is identical to the
     * CreateWindow function.
     *
     * @param dwExStyle The extended window style of the window being
     *  created
     * @param lpClassName A string or class atom created by a previous
     *  call to RegisterClass or RegisterClassEx.
     * @param lpWindowName The window name.
     * @param dwStyle The style of the window being created.
     * @param x The initial horizontal position of the window.
     * @param y The initial vertical position of the window.
     * @param nWidth The width, in device units, of the window.
     * @param nHeight The height, in device units, of the window.
     * @param hWndParent A handle to the parent or owner window of the
     *  window being created.
     * @param hMenu A handle to a menu, or specifies a child-window
     *  identifier, depending on the window style.
     * @param hInstance A handle to the instance of the module to be
     *  associated with the window
     * @param lpParam Pointer to a value to be passed to the window
     *  through the CREATESTRUCT structure (lpCreateParams member)
     *  pointed to by the lParam param of the WM_CREATE message.
     * @return The handle to the new window on success; null otherwise.
     */
    HWND CreateWindowEx(int dwExStyle, WString lpClassName,
        String lpWindowName, int dwStyle, int x, int y, int nWidth,
        int nHeight, HWND hWndParent, HMENU hMenu, HINSTANCE hInstance,
        LPVOID lpParam);

    /**
     * Calls the default window procedure to provide default processing
     * for any window messages that an application does not process.
     *
     * @param hWnd A handle to the window procedure that received the
     *  message.
     * @param Msg The message.
     * @param wParam Additional message information.
     * @param lParam Additional message information.
     * @return The result of the message processing and depends on the
     *  message.
     */
    LRESULT DefWindowProc(HWND hWnd, int Msg, WPARAM wParam, LPARAM lParam);

    /**
     * Destroys the specified window.
     *
     * @param hWnd A handle to the window to be destroyed.
     * @return true if the function succeeds, false otherwise.
     */
    boolean DestroyWindow(HWND hWnd);

    /**
     * Translates (maps) a virtual-key code into a scan code or
     * character value, or translates a scan code into a virtual-key
     * code.
     *
     * @param uCode The virtual key code or scan code for a key.
     * @param uMapType The translation to be performed.
     * @return The return value is either a scan code, a virtual-key
     *  code, or a character value, depending on the value of uCode and
     *  uMapType. If there is no translation, the return value is zero.
     */
    int MapVirtualKey(int uCode, int uMapType);

    /**
     * Registers a window class for subsequent use in calls to the
     * CreateWindow or CreateWindowEx function.
     *
     * @param lpwcx A pointer to a WNDCLASSEX structure.
     * @return The atom that uniquely identifies the class on success;
     *  zero otherwise.
     */
    ATOM RegisterClassEx(WinUser.WNDCLASSEX lpwcx);

    /**
     * Sends the specified message to a window or windows.
     *
     * @param hWnd A handle to the window whose window procedure will
     *  receive the message.
     * @param Msg The message to be sent.
     * @param wParam Additional message-specific information.
     * @param lParam Additional message-specific information.
     * @return The result of the message processing; it depends on the
     *  message sent.
     */
    LRESULT SendMessage(HWND hWnd, int Msg, WPARAM wParam, LPARAM lParam);

    /**
     * Unregisters a window class.
     *
     * @param lpClassName A class name or atom.
     * @param hInstance A handle to the instance of the module that
     *  created the class
     * @return true if the function succeeds, false otherwise.
     */
    boolean UnregisterClass(WString lpClassName, HINSTANCE hInstance);

    /**
     * Translates a character to the corresponding virtual-key code and
     * shift state for the current keyboard.
     *
     * @param ch The character to be translated into a virtual-key code.
     * @return On success, the low-order byte contains the virtual-key
     *  code and the high-order byte contains the shift state; -1 on
     *  failure.
     */
    short VkKeyScan(char ch);
}
